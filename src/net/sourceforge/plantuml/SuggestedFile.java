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

public class SuggestedFile {

	private final FileFormat fileFormat;
	private final int initialCpt;
	private final File outputFile;

	private SuggestedFile(File outputFile, FileFormat fileFormat, int initialCpt) {
		if (outputFile.getName().endsWith(fileFormat.getFileSuffix())) {
			throw new IllegalArgumentException();
		}
		this.outputFile = outputFile;
		this.fileFormat = fileFormat;
		this.initialCpt = initialCpt;
	}

	public SuggestedFile withPreprocFormat() {
		return new SuggestedFile(outputFile, FileFormat.PREPROC, initialCpt);
	}

	@Override
	public String toString() {
		return outputFile.getAbsolutePath() + "[" + initialCpt + "]";
	}

	public static SuggestedFile fromOutputFile(File outputFile, FileFormat fileFormat) {
		return fromOutputFile(outputFile, fileFormat, 0);
	}

	public File getParentFile() {
		return outputFile.getParentFile();
	}

	public String getName() {
		return outputFile.getName();
	}

	public File getFile(int cpt) {
		final String newName = fileFormat.changeName(outputFile.getName(), initialCpt + cpt);
		return new File(outputFile.getParentFile(), newName);
	}

	public static SuggestedFile fromOutputFile(File outputFile, FileFormat fileFormat, int initialCpt) {
		return new SuggestedFile(outputFile, fileFormat, initialCpt);
	}

	public File getTmpFile() {
		return new File(getParentFile(), getName() + ".tmp");
	}

}
