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
package net.sourceforge.plantuml.png;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.UAntiAliasing;

public class PngFlashcoder {

	private final List<BufferedImage> flashcodes;

	public PngFlashcoder(List<BufferedImage> flashcodes) {
		this.flashcodes = flashcodes;
	}

	public BufferedImage processImage(BufferedImage im, Color background) {
		if (flashcodes != null) {
			im = addImage(im, background);
		}
		return im;

	}

	private BufferedImage addImage(BufferedImage im, Color background) {

		final double width = Math.max(im.getWidth(), getWidth(flashcodes));
		final double height = im.getHeight() + getHeight(flashcodes);

		final BufferedImage newIm = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();

		UAntiAliasing.ANTI_ALIASING_OFF.apply(g2d);
		g2d.setColor(background);
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		g2d.drawImage(im, null, 0, 0);
		int x = 0;
		for (BufferedImage f : flashcodes) {
			g2d.drawImage(f, null, x, (int) im.getHeight());
			x += f.getWidth();
		}
		g2d.dispose();
		return newIm;

	}

	public static int getHeight(List<BufferedImage> flashcodes) {
		int result = 0;
		for (BufferedImage im : flashcodes) {
			result = Math.max(result, im.getWidth());
		}
		return result;
	}

	public static int getWidth(List<BufferedImage> flashcodes) {
		int result = 0;
		for (BufferedImage im : flashcodes) {
			result += im.getWidth();
		}
		return result;
	}
}
