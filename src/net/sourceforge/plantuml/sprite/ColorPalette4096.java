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
package net.sourceforge.plantuml.sprite;

import java.awt.Color;

import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

public class ColorPalette4096 {

	private static final String colorValue = "!#$%&*+-:;<=>?@^_~GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String getStringFor(Color dest) {
		return getStringFor(new HColorSimple(dest, false));
	}

	public String getStringFor(HColor dest) {
		int result = 0;
		double resultDist = Double.MAX_VALUE;
		for (int i = 0; i < 4096; i++) {
			final double dist = ((HColorSimple) dest).distance(getHtmlColorSimpleFor(i));
			if (dist < resultDist) {
				result = i;
				resultDist = dist;
			}
		}
		return encodeInt(result);
	}

	protected String encodeInt(int result) {
		final int v2 = result % 64;
		final int v1 = result / 64;
		assert v1 >= 0 && v1 <= 63 && v2 >= 0 && v2 <= 63;
		return "" + colorValue.charAt(v1) + colorValue.charAt(v2);
	}

	private HColorSimple getHtmlColorSimpleFor(int s) {
		final Color color = getColorFor(s);
		if (color == null) {
			throw new IllegalArgumentException();
		}
		return new HColorSimple(color, false);
	}

	public Color getColorFor(String s) {
		if (s.length() != 2) {
			throw new IllegalArgumentException();
		}
		final int v1 = colorValue.indexOf(s.charAt(0));
		if (v1 == -1) {
			return null;
		}
		final int v2 = colorValue.indexOf(s.charAt(1));
		if (v2 == -1) {
			return null;
		}
		final int code = v1 * 64 + v2;
		return getColorFor(code);
	}

	protected Color getColorFor(final int code) {
		final int blue = code % 16;
		final int green = (code / 16) % 16;
		final int red = (code / 256) % 16;
		return new Color(dup(red), dup(green), dup(blue));
	}

	private int dup(int v) {
		assert v >= 0 && v <= 15;
		return v * 16 + v;
	}

}
