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
package net.sourceforge.plantuml.graphic;

import java.awt.Color;

import net.sourceforge.plantuml.ugraphic.ColorMapper;

public class HtmlColorMiddle implements HtmlColor {

	private final HtmlColor c1;
	private final HtmlColor c2;

	public HtmlColorMiddle(HtmlColor c1, HtmlColor c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	public Color getMappedColor(ColorMapper colorMapper) {
		final Color cc1 = colorMapper.getMappedColor(c1);
		final Color cc2 = colorMapper.getMappedColor(c2);
		final int r1 = cc1.getRed();
		final int g1 = cc1.getGreen();
		final int b1 = cc1.getBlue();
		final int r2 = cc2.getRed();
		final int g2 = cc2.getGreen();
		final int b2 = cc2.getBlue();

		final int r = (r1 + r2) / 2;
		final int g = (g1 + g2) / 2;
		final int b = (b1 + b2) / 2;
		return new Color(r, g, b);
	}

}
