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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AFile;
import net.sourceforge.plantuml.AFileRegular;
import net.sourceforge.plantuml.AFileZipEntry;
import net.sourceforge.plantuml.AParentFolder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class ImportedFiles {

	private final List<SFile> imported;
	private final AParentFolder currentDir;

	private ImportedFiles(List<SFile> imported, AParentFolder currentDir) {
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
		return new ImportedFiles(new ArrayList<SFile>(), newCurrentDir);
	}

	@Override
	public String toString() {
		return "ImportedFiles=" + imported + " currentDir=" + currentDir;
	}

	public AFile getAFile(String nameOrPath) throws IOException {
		// Log.info("ImportedFiles::getAFile nameOrPath = " + nameOrPath);
		// Log.info("ImportedFiles::getAFile currentDir = " + currentDir);
		final AParentFolder dir = currentDir;
		if (dir == null || isAbsolute(nameOrPath)) {
			return new AFileRegular(new SFile(nameOrPath).getCanonicalFile());
		}
		// final File filecurrent = SecurityUtils.File(dir.getAbsoluteFile(),
		// nameOrPath);
		final AFile filecurrent = dir.getAFile(nameOrPath);
		Log.info("ImportedFiles::getAFile filecurrent = " + filecurrent);
		if (filecurrent != null && filecurrent.isOk()) {
			return filecurrent;
		}
		for (SFile d : getPath()) {
			if (d.isDirectory()) {
				final SFile file = d.file(nameOrPath);
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

	public List<SFile> getPath() {
		final List<SFile> result = new ArrayList<SFile>(imported);
		result.addAll(includePath());
		result.addAll(SecurityUtils.getPath("java.class.path"));
		return result;
	}

	private List<SFile> includePath() {
		return SecurityUtils.getPath("plantuml.include.path");
	}

	private boolean isAbsolute(String nameOrPath) {
		final SFile f = new SFile(nameOrPath);
		return f.isAbsolute();
	}

	public void add(SFile file) {
		this.imported.add(file);
	}

	public AParentFolder getCurrentDir() {
		return currentDir;
	}

	public FileWithSuffix getFile(String filename, String suffix) throws IOException {
		final int idx = filename.indexOf('~');
		final AFile file;
		final String entry;
		if (idx == -1) {
			file = getAFile(filename);
			entry = null;
		} else {
			file = getAFile(filename.substring(0, idx));
			entry = filename.substring(idx + 1);
		}
		if (isAllowed(file) == false) {
			return FileWithSuffix.none();
		}
		return new FileWithSuffix(filename, suffix, file, entry);
	}

	private boolean isAllowed(AFile file) throws IOException {
		if (OptionFlags.ALLOW_INCLUDE) {
			return true;
		}
		if (file != null) {
			final SFile folder = file.getSystemFolder();
			// System.err.println("canonicalPath=" + path + " " + folder + " " +
			// INCLUDE_PATH);
			if (includePath().contains(folder)) {
				return true;
			}
		}
		return false;
	}

}
