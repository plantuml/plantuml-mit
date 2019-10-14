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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class UImage implements UShape {

	private final BufferedImage image;
	private final String formula;

	public UImage(BufferedImage image) {
		this(image, null);
	}

	public UImage(BufferedImage image, String formula) {
		this.image = image;
		this.formula = formula;
	}

	public UImage scale(double scale) {
		return scale(scale, AffineTransformOp.TYPE_BILINEAR);
	}

	public UImage scaleNearestNeighbor(double scale) {
		return scale(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	}

	private UImage scale(double scale, final int type) {
		if (scale == 1) {
			return this;
		}
		final int w = (int) Math.round(image.getWidth() * scale);
		final int h = (int) Math.round(image.getHeight() * scale);
		final BufferedImage after = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		final AffineTransformOp scaleOp = new AffineTransformOp(at, type);
		return new UImage(scaleOp.filter(image, after), formula);
	}

	public final BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return image.getWidth() - 1;
	}

	public int getHeight() {
		return image.getHeight() - 1;
	}

	public final String getFormula() {
		return formula;
	}

	public UImage muteColor(Color newColor) {
		if (newColor == null) {
			return this;
		}
		int darkerRgb = getDarkerRgb();
		final BufferedImage copy = deepCopy2();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final int color = image.getRGB(i, j);
				final int rgb = getRgb(color);
				final int a = getA(color);
				if (a != 0 && rgb == darkerRgb) {
					copy.setRGB(i, j, newColor.getRGB() + a);
				}
			}
		}
		return new UImage(copy, formula);
	}

	public UImage muteTransparentColor(Color newColor) {
		if (newColor == null) {
			newColor = Color.WHITE;
		}
		final BufferedImage copy = deepCopy2();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final int color = image.getRGB(i, j);
				// final int rgb = getRgb(color);
				final int a = getA(color);
				if (a == 0) {
					copy.setRGB(i, j, newColor.getRGB());
				}
			}
		}
		return new UImage(copy, formula);
	}

	private int getDarkerRgb() {
		int darkerRgb = -1;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final int color = image.getRGB(i, j);
				// System.err.println("i="+i+" j="+j+" "+Integer.toHexString(color)+" "+isTransparent(color));
				final int rgb = getRgb(color);
				final int a = getA(color);
				if (a != mask_a__) {
					continue;
				}
				// if (isTransparent(color)) {
				// continue;
				// }
				final int grey = ColorChangerMonochrome.getGrayScale(rgb);
				if (darkerRgb == -1 || grey < ColorChangerMonochrome.getGrayScale(darkerRgb)) {
					darkerRgb = rgb;
				}
			}
		}
		return darkerRgb;
	}

	private static final int mask_a__ = 0xFF000000;
	private static final int mask_rgb = 0x00FFFFFF;

	private int getRgb(int color) {
		return color & mask_rgb;
	}

	private int getA(int color) {
		return color & mask_a__;
	}

	// private boolean isTransparent(int argb) {
	// if ((argb & mask) == mask) {
	// return false;
	// }
	// return true;
	// }

	// From https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	private static BufferedImage deepCopyOld(BufferedImage bi) {
		final ColorModel cm = bi.getColorModel();
		final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		final WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	private BufferedImage deepCopy2() {
		final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < this.image.getWidth(); i++) {
			for (int j = 0; j < this.image.getHeight(); j++) {
				result.setRGB(i, j, image.getRGB(i, j));
			}
		}
		return result;
	}

}
