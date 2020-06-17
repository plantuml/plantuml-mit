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

import net.sourceforge.plantuml.svek.DotStringFactory;

public abstract class AbstractColorMapper implements ColorMapper {

	final public String toRGB(HColor hcolor) {
		if (hcolor == null) {
			return null;
		}
		final Color color = toColor(hcolor);
		return DotStringFactory.sharp000000(color.getRGB());
	}

	final public String toSvg(HColor hcolor) {
		if (hcolor == null) {
			return "none";
		}
		if (hcolor instanceof HColorBackground) {
			hcolor = ((HColorBackground) hcolor).getBack();
//			Thread.dumpStack();
//			System.exit(0);
//			return toHtml(result);
		}
		if (HColorUtils.isTransparent(hcolor)) {
			return "#00000000";
		}
		final Color color = toColor(hcolor);
		final int alpha = color.getAlpha();
		if (alpha == 255) {
			return toRGB(hcolor);
		}
		String s = "0" + Integer.toHexString(alpha).toUpperCase();
		s = s.substring(s.length() - 2);
		return toRGB(hcolor) + s;
	}

	private static String sharpAlpha(int color) {
		final int v = color & 0xFFFFFF;
		String s = "00000" + Integer.toHexString(v).toUpperCase();
		s = s.substring(s.length() - 6);
		final int alpha = (int) (((long) color) & 0x000000FF) << 24;
		final String s2 = "0" + Integer.toHexString(alpha).toUpperCase();
		return "#" + s + s2.substring(0, 2);
	}

}
