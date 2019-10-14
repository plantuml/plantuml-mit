/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * Licensed under The MIT License (Massachusetts Institute of Technology License)
 * 
 * See http://opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;

@Deprecated
public class DirWatcher {

	final private File dir;
	final private Option option;
	final private String pattern;

	final private Map<File, FileWatcher> modifieds = new HashMap<File, FileWatcher>();

	public DirWatcher(File dir, Option option, String pattern) {
		this.dir = dir;
		this.option = option;
		this.pattern = pattern;
	}

	public List<GeneratedImage> buildCreatedFiles() throws IOException, InterruptedException {
		boolean error = false;
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();
		for (File f : dir.listFiles()) {
			if (error) {
				continue;
			}
			if (f.isFile() == false) {
				continue;
			}
			if (fileToProcess(f.getName()) == false) {
				continue;
			}
			final FileWatcher watcher = modifieds.get(f);

			if (watcher == null || watcher.hasChanged()) {
				final SourceFileReader sourceFileReader = new SourceFileReader(Defines.createWithFileName(f), f,
						option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
				final Set<File> files = FileWithSuffix.convert(sourceFileReader.getIncludedFiles());
				files.add(f);
				for (GeneratedImage g : sourceFileReader.getGeneratedImages()) {
					result.add(g);
					if (option.isFailfastOrFailfast2() && g.lineErrorRaw() != -1) {
						error = true;
					}
				}
				modifieds.put(f, new FileWatcher(files));
			}
		}
		Collections.sort(result);
		return Collections.unmodifiableList(result);
	}

	public File getErrorFile() throws IOException, InterruptedException {
		for (File f : dir.listFiles()) {
			if (f.isFile() == false) {
				continue;
			}
			if (fileToProcess(f.getName()) == false) {
				continue;
			}
			final FileWatcher watcher = modifieds.get(f);

			if (watcher == null || watcher.hasChanged()) {
				final SourceFileReader sourceFileReader = new SourceFileReader(Defines.createWithFileName(f), f,
						option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
				if (sourceFileReader.hasError()) {
					return f;
				}
			}
		}
		return null;
	}

	private boolean fileToProcess(String name) {
		return name.matches(pattern);
	}

	public final File getDir() {
		return dir;
	}

	// public void setPattern(String pattern) {
	// this.pattern = pattern;
	// }
}
