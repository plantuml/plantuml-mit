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
package net.sourceforge.plantuml.sprite;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.sourceforge.plantuml.version.Version;

public class RessourcesUtils {

	public static Set<String> getJarFile(String path, boolean folder) throws IOException {
		if (path.startsWith("/") || path.endsWith("/")) {
			throw new IllegalArgumentException();
		}
		final String protocol = getProtocol();
		if ("file".equals(protocol)) {
			final URL local = Version.class.getClassLoader().getResource(path);
			try {
				return listEntry(new File(local.toURI()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
				return null;
			}
		}
		if ("jar".equals(protocol)) {
			final String classFile = Version.class.getName().replace(".", "/") + ".class";
			final URL versionURL = Version.class.getClassLoader().getResource(classFile);
			final String jarPath = versionURL.getPath().substring(5, versionURL.getPath().indexOf("!"));
			if (folder) {
				return listFolders(new JarFile(URLDecoder.decode(jarPath, "UTF-8")), path + "/");
			} else {
				return listFiles(new JarFile(URLDecoder.decode(jarPath, "UTF-8")), path + "/");

			}
		}
		return Collections.<String> emptySet();
	}

	private static String getProtocol() {
		final URL resource = Version.class.getClassLoader().getResource("net/sourceforge/plantuml/version/logo.png");
		return resource.getProtocol();
	}

	private static Set<String> listFiles(JarFile jarFile, String path) {
		final Enumeration<JarEntry> entries = jarFile.entries();
		final Set<String> result = new TreeSet<String>();
		while (entries.hasMoreElements()) {
			final String name = entries.nextElement().getName();
			if (name.startsWith(path)) {
				result.add(name.substring(path.length()));
			}
		}
		return result;
	}

	private static Set<String> listFolders(JarFile jarFile, String path) {
		final Enumeration<JarEntry> entries = jarFile.entries();
		final Set<String> result = new TreeSet<String>();
		while (entries.hasMoreElements()) {
			final String name = entries.nextElement().getName();
			if (name.startsWith(path)) {
				final String folder = name.substring(path.length());
				final int x = folder.indexOf('/');
				if (x != -1) {
					result.add(folder.substring(0, x));
				}
			}
		}
		return result;
	}

	private static Set<String> listEntry(File dir) {
		final Set<String> result = new TreeSet<String>();
		for (String n : dir.list()) {
			result.add(n);
		}
		return result;
	}

}
