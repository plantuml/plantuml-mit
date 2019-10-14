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
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AFile;
import net.sourceforge.plantuml.AFileRegular;
import net.sourceforge.plantuml.AFileZipEntry;
import net.sourceforge.plantuml.AParentFolder;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;

public class ImportedFiles {

	private final List<File> imported;
	private final AParentFolder currentDir;

	private ImportedFiles(List<File> imported, AParentFolder currentDir) {
		this.imported = imported;
		this.currentDir = currentDir;
	}

	public ImportedFiles withCurrentDir(AParentFolder newCurrentDir) {
		if (newCurrentDir == null) {
			return this;
		}
		return new ImportedFiles(imported, newCurrentDir);
	}

	public static ImportedFiles createImportedFiles(AParentFolder newCurrentDir) {
		return new ImportedFiles(new ArrayList<File>(), newCurrentDir);
	}

	@Override
	public String toString() {
		return "ImportedFiles=" + imported + " currentDir=" + currentDir;
	}

	public AFile getAFile(String nameOrPath) throws IOException {
		Log.info("ImportedFiles::getAFile nameOrPath = " + nameOrPath);
		Log.info("ImportedFiles::getAFile currentDir = " + currentDir);
		final AParentFolder dir = currentDir;
		if (dir == null || isAbsolute(nameOrPath)) {
			return new AFileRegular(new File(nameOrPath).getCanonicalFile());
		}
		// final File filecurrent = new File(dir.getAbsoluteFile(), nameOrPath);
		final AFile filecurrent = dir.getAFile(nameOrPath);
		Log.info("ImportedFiles::getAFile filecurrent = " + filecurrent);
		if (filecurrent != null && filecurrent.isOk()) {
			return filecurrent;
		}
		for (File d : getPath()) {
			if (d.isDirectory()) {
				final File file = new File(d, nameOrPath);
				if (file.exists()) {
					return new AFileRegular(file.getCanonicalFile());
				}
			} else if (d.isFile()) {
				final AFileZipEntry zipEntry = new AFileZipEntry(d, nameOrPath);
				if (zipEntry.isOk()) {
					return zipEntry;
				}
			}
		}
		return filecurrent;
	}

	public List<File> getPath() {
		final List<File> result = new ArrayList<File>(imported);
		result.addAll(FileSystem.getPath("plantuml.include.path", true));
		result.addAll(FileSystem.getPath("java.class.path", true));
		return result;
	}

	private boolean isAbsolute(String nameOrPath) {
		final File f = new File(nameOrPath);
		return f.isAbsolute();
	}

	public void add(File file) {
		this.imported.add(file);
	}

	public AParentFolder getCurrentDir() {
		return currentDir;
	}

}
