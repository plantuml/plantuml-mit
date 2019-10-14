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

public class AParentFolderZip implements AParentFolder {

	private final File zipFile;
	private final String parent;

	@Override
	public String toString() {
		return "AParentFolderZip::" + zipFile + " " + parent;
	}

	public AParentFolderZip(File zipFile, String entry) {
		this.zipFile = zipFile;
		final int idx = entry.lastIndexOf('/');
		if (idx == -1) {
			parent = "";
		} else {
			parent = entry.substring(0, idx + 1);
		}
	}

	public AFile getAFile(String nameOrPath) throws IOException {
		return new AFileZipEntry(zipFile, merge(parent + nameOrPath));
	}

	String merge(String full) {
		// full = full.replaceFirst("\\.", "Z");
		while (true) {
			int len = full.length();
			full = full.replaceFirst("[^/]+/\\.\\./", "");
			if (full.length() == len) {
				return full;
			}
		}
	}

}
