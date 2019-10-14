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
package net.sourceforge.plantuml.swing;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import net.sourceforge.plantuml.GeneratedImage;

class SimpleLine2 implements Comparable<SimpleLine2> {

	private final File file;
	private final GeneratedImage generatedImage;
	private final Future<List<GeneratedImage>> future;

	public static SimpleLine2 fromFuture(File file, Future<List<GeneratedImage>> future) {
		return new SimpleLine2(file, null, future);
	}

	public static SimpleLine2 fromGeneratedImage(File file, GeneratedImage generatedImage) {
		return new SimpleLine2(file, generatedImage, null);
	}

	private SimpleLine2(File file, GeneratedImage generatedImage, Future<List<GeneratedImage>> future) {
		this.generatedImage = generatedImage;
		this.file = file;
		this.future = future;
	}

	public File getFile() {
		return file;
	}

	public boolean pendingAndFinished() {
		return generatedImage == null && future.isDone();
	}

	@Override
	public String toString() {
		if (generatedImage == null) {
			return file.getName() + " (...pending...)";
		}
		final StringBuilder sb = new StringBuilder(generatedImage.getPngFile().getName());
		sb.append(" ");
		sb.append(generatedImage.getDescription());
		return sb.toString();
	}

	public Future<List<GeneratedImage>> getFuture() {
		return future;
	}

	public int compareTo(SimpleLine2 other) {
		return toString().compareTo(other.toString());
	}

	public GeneratedImage getGeneratedImage() {
		return generatedImage;
	}

}
