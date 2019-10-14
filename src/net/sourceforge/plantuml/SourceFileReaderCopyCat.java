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
import java.util.List;

import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;

public class SourceFileReaderCopyCat extends SourceFileReaderAbstract implements ISourceFileReader {

	public SourceFileReaderCopyCat(Defines defines, final File file, File outputDirectory, List<String> config,
			String charset, FileFormatOption fileFormatOption) throws IOException {
		this.file = file;
		this.fileFormatOption = fileFormatOption;
		if (file.exists() == false) {
			throw new IllegalArgumentException();
		}
		final String path = file.getParentFile().getPath();
		// System.err.println("SourceFileReaderCopyCat::path=" + path);
		// System.err.println("SourceFileReaderCopyCat::outputDirectory=" + outputDirectory);
		this.outputDirectory = new File(outputDirectory, path).getAbsoluteFile();
		if (outputDirectory.exists() == false) {
			outputDirectory.mkdirs();
		}
		// System.err.println("SourceFileReaderCopyCat=" + this.outputDirectory.getPath() + " "
		// + this.outputDirectory.getAbsolutePath());
		builder = new BlockUmlBuilder(config, charset, defines, getReader(charset), file.getAbsoluteFile()
				.getParentFile(), FileWithSuffix.getFileName(file));
	}

	@Override
	protected SuggestedFile getSuggestedFile(BlockUml blockUml) {
		final String newName = blockUml.getFileOrDirname();
		SuggestedFile suggested = null;
		if (newName == null) {
			suggested = SuggestedFile.fromOutputFile(new File(outputDirectory, file.getName()),
					fileFormatOption.getFileFormat(), cpt++);
		} else {
			suggested = SuggestedFile.fromOutputFile(new File(outputDirectory, newName),
					fileFormatOption.getFileFormat(), cpt++);
		}
		// System.err.println("SourceFileReaderCopyCat::suggested=" + suggested);
		suggested.getParentFile().mkdirs();
		return suggested;
	}

}
