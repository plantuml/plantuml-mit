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
package net.sourceforge.plantuml.ugraphic.color;

import java.awt.Color;

public class ColorUtils {

	static int getGrayScale(Color color) {
		final int grayScale = (int) (color.getRed() * .3 + color.getGreen() * .59 + color.getBlue() * .11);
		return grayScale;
	}

	public static Color getGrayScaleColor(Color color) {
		final int grayScale = getGrayScale(color);
		return new Color(grayScale, grayScale, grayScale);
	}

	public static Color getGrayScaleColorReverse(Color color) {
		final int grayScale = 255 - getGrayScale(color);
		return new Color(grayScale, grayScale, grayScale);
	}

	/*
	 * This code is still experimental. If you can improve it, please go ahead :-)
	 * 
	 * Many thanks to Alexei Boronine for the idea.
	 * 
	 * Some pointer to help you: https://www.hsluv.org/
	 * https://www.kuon.ch/post/2020-03-08-hsluv/
	 * https://www.boronine.com/2012/03/26/Color-Spaces-for-Human-Beings/
	 * 
	 */
	public static synchronized Color getReversed(Color color) {
		final int red = color.getRed();
		final int green = color.getGreen();
		final int blue = color.getBlue();

		final double hsluv[] = HUSLColorConverter.rgbToHsluv(new double[] { red / 256.0, green / 256.0, blue / 256.0 });

		final double h = hsluv[0];
		final double s = hsluv[1];
		double l = hsluv[2];

		if (s > 40 && s < 60) {
			if (l > 50) {
				l -= 50;
			} else if (l < 50) {
				l += 50;
			}
		} else {
			l = 100 - l;
		}

		final double rgb[] = HUSLColorConverter.hsluvToRgb(new double[] { h, s, l });

		final int red2 = to255(rgb[0]);
		final int green2 = to255(rgb[1]);
		final int blue2 = to255(rgb[2]);

		return new Color(red2, green2, blue2);
	}

	private static int to255(final double value) {
		final int result = (int) (255 * value);
		if (result < 0) {
			return 0;
		}
		if (result > 255) {
			return 255;
		}
		return result;
	}

}
