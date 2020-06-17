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

import java.io.IOException;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class FileSystem {

	private final static FileSystem singleton = new FileSystem();

	private final ThreadLocal<SFile> currentDir = new ThreadLocal<SFile>();

	private FileSystem() {
		reset();
	}

	public static FileSystem getInstance() {
		return singleton;
	}

	public void setCurrentDir(SFile dir) {
		// if (dir == null) {
		// throw new IllegalArgumentException();
		// }
		if (dir != null) {
			Log.info("Setting current dir: " + dir.getAbsolutePath());
		}
		this.currentDir.set(dir);
	}

	public SFile getCurrentDir() {
		return this.currentDir.get();
	}

	public SFile getFile(String nameOrPath) throws IOException {
		if (isAbsolute(nameOrPath)) {
			return new SFile(nameOrPath).getCanonicalFile();
		}
		final SFile dir = currentDir.get();
		SFile filecurrent = null;
		if (dir != null) {
			filecurrent = dir.getAbsoluteFile().file(nameOrPath);
			if (filecurrent.exists()) {
				return filecurrent.getCanonicalFile();

			}
		}
		for (SFile d : SecurityUtils.getPath("plantuml.include.path")) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists()) {
				return file.getCanonicalFile();

			}
		}
		for (SFile d : SecurityUtils.getPath("java.class.path")) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists()) {
				return file.getCanonicalFile();
			}
		}
		if (dir == null) {
			assert filecurrent == null;
			return new SFile(nameOrPath).getCanonicalFile();
		}
		assert filecurrent != null;
		return filecurrent;
	}

	private boolean isAbsolute(String nameOrPath) {
		final SFile f = new SFile(nameOrPath);
		return f.isAbsolute();
	}

	public void reset() {
		setCurrentDir(new SFile("."));
	}

}
