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
package net.sourceforge.plantuml.security;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageIO {

	public static ImageOutputStream createImageOutputStream(OutputStream os) throws IOException {
		return javax.imageio.ImageIO.createImageOutputStream(os);
	}

	public static void write(RenderedImage image, String format, OutputStream os) throws IOException {
		javax.imageio.ImageIO.write(image, format, os);
	}

	public static void write(RenderedImage image, String format, java.io.File file) throws IOException {
		javax.imageio.ImageIO.write(image, format, file);
	}

	public static void write(RenderedImage image, String format, SFile file) throws IOException {
		javax.imageio.ImageIO.write(image, format, file.conv());
	}

	public static BufferedImage read(java.io.File file) throws IOException {
		return javax.imageio.ImageIO.read(file);
	}

	public static BufferedImage read(SFile file) throws IOException {
		return javax.imageio.ImageIO.read(file.conv());
	}

	public static BufferedImage read(InputStream is) throws IOException {
		return javax.imageio.ImageIO.read(is);
	}

	public static ImageInputStream createImageInputStream(java.io.File file) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(file);
	}

	public static ImageInputStream createImageInputStream(SFile file) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(file.conv());
	}

	public static ImageInputStream createImageInputStream(Object obj) throws IOException {
		if (obj instanceof SFile) {
			obj = ((SFile) obj).conv();
		}
		return javax.imageio.ImageIO.createImageInputStream(obj);
	}

	public static ImageInputStream createImageInputStream(InputStream is) throws IOException {
		return javax.imageio.ImageIO.createImageInputStream(is);
	}

	public static Iterator<ImageReader> getImageReaders(ImageInputStream iis) {
		return javax.imageio.ImageIO.getImageReaders(iis);
	}

	public static Iterator<ImageWriter> getImageWritersBySuffix(String string) {
		return javax.imageio.ImageIO.getImageWritersBySuffix(string);
	}

}
