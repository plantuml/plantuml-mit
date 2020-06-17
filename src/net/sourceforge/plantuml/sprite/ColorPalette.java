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

public class ColorPalette {

	private static final String colorValue = "!#$%&*+-:;<=>?@^_~GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public char getCharFor(Color dest) {
		return getCharFor(new HColorSimple(dest, false));
	}

	public char getCharFor(HColor dest) {
		char result = 0;
		double resultDist = Double.MAX_VALUE;
		for (int i = 0; i < colorValue.length(); i++) {
			final char c = colorValue.charAt(i);
			final double dist = ((HColorSimple) dest).distance(getHtmlColorSimpleFor(c));
			if (dist < resultDist) {
				result = c;
				resultDist = dist;
			}
		}
		assert result != 0;
		return result;
	}

	private HColorSimple getHtmlColorSimpleFor(char c) {
		final Color color = getColorFor(c);
		if (color == null) {
			throw new IllegalArgumentException();
		}
		return new HColorSimple(color, false);
	}

	public Color getColorFor(char c) {
		final int col = colorValue.indexOf(c);
		if (col == -1) {
			return null;
		}
		final int blue = (col % 4) * 85;
		final int green = ((col / 4) % 4) * 85;
		final int red = ((col / 16) % 4) * 85;
		return new Color(red, green, blue);
	}

}
