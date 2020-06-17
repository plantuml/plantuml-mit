/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.sourceforge.plantuml.preproc.FileWithSuffix;

public class DirWatcher2 {

	final private File dir;
	final private Option option;
	final private String pattern;

	final private Map<File, FileWatcher> modifieds = new ConcurrentHashMap<File, FileWatcher>();
	final private ExecutorService executorService;

	public DirWatcher2(File dir, Option option, String pattern) {
		this.dir = dir;
		this.option = option;
		this.pattern = pattern;
		final int nb = Option.defaultNbThreads();
		this.executorService = Executors.newFixedThreadPool(nb);

	}

	public Map<File, Future<List<GeneratedImage>>> buildCreatedFiles() throws IOException, InterruptedException {
		final Map<File, Future<List<GeneratedImage>>> result = new TreeMap<File, Future<List<GeneratedImage>>>();
		if (dir.listFiles() != null) {
			for (final File f : dir.listFiles()) {
				if (f.isFile() == false) {
					continue;
				}
				if (fileToProcess(f.getName()) == false) {
					continue;
				}
				final FileWatcher watcher = modifieds.get(f);

				if (watcher == null || watcher.hasChanged()) {
					final SourceFileReader sourceFileReader = new SourceFileReader(option.getDefaultDefines(f), f,
							option.getOutputDir(), option.getConfig(), option.getCharset(),
							option.getFileFormatOption());
					modifieds.put(f, new FileWatcher(Collections.singleton(f)));
					final Future<List<GeneratedImage>> value = executorService
							.submit(new Callable<List<GeneratedImage>>() {
								public List<GeneratedImage> call() throws Exception {
									try {
										final List<GeneratedImage> generatedImages = sourceFileReader
												.getGeneratedImages();
										final Set<File> files = FileWithSuffix
												.convert(sourceFileReader.getIncludedFiles());
										files.add(f);
										modifieds.put(f, new FileWatcher(files));
										return Collections.unmodifiableList(generatedImages);
									} catch (Exception e) {
										e.printStackTrace();
										return Collections.emptyList();
									}
								}
							});
					result.put(f, value);
				}
			}
		}
		return Collections.unmodifiableMap(result);
	}

	private boolean fileToProcess(String name) {
		return name.matches(pattern);
	}

	public final File getDir() {
		return dir;
	}

	public void cancel() {
		executorService.shutdownNow();
	}

	public void waitEnd() throws InterruptedException {
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	}

}
