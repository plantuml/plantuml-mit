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
import java.util.List;
import java.util.StringTokenizer;

public class FileSystem {

	private final static FileSystem singleton = new FileSystem();

	private final ThreadLocal<File> currentDir = new ThreadLocal<File>();

	private FileSystem() {
		reset();
	}

	public static FileSystem getInstance() {
		return singleton;
	}

	public void setCurrentDir(File dir) {
		// if (dir == null) {
		// throw new IllegalArgumentException();
		// }
		Log.info("Setting current dir: " + dir);
		this.currentDir.set(dir);
	}

	public File getCurrentDir() {
		return this.currentDir.get();
	}

	public File getFile(String nameOrPath) throws IOException {
		final File dir = currentDir.get();
		if (dir == null || isAbsolute(nameOrPath)) {
			return new File(nameOrPath).getCanonicalFile();
		}
		final File filecurrent = new File(dir.getAbsoluteFile(), nameOrPath);
		if (filecurrent.exists()) {
			return filecurrent.getCanonicalFile();
		}
		for (File d : getPath("plantuml.include.path", true)) {
			if (d.isDirectory()) {
				final File file = new File(d, nameOrPath);
				if (file.exists()) {
					return file.getCanonicalFile();
				}
			}
		}
		for (File d : getPath("java.class.path", true)) {
			if (d.isDirectory()) {
				final File file = new File(d, nameOrPath);
				if (file.exists()) {
					return file.getCanonicalFile();
				}
			}
		}
		return filecurrent;
	}

	public static List<File> getPath(String prop, boolean onlyDir) {
		final List<File> result = new ArrayList<File>();
		String paths = System.getProperty(prop);
		if (paths == null) {
			return result;
		}
		paths = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(paths);
		final StringTokenizer st = new StringTokenizer(paths, System.getProperty("path.separator"));
		while (st.hasMoreTokens()) {
			final File f = new File(st.nextToken());
			if (f.exists() && (onlyDir == false || f.isDirectory())) {
				result.add(f);
			}
		}
		return result;
	}

	private boolean isAbsolute(String nameOrPath) {
		final File f = new File(nameOrPath);
		return f.isAbsolute();
	}

	public void reset() {
		setCurrentDir(new File("."));
	}

}
