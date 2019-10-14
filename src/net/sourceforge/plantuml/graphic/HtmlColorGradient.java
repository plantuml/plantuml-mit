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

public class HtmlColorGradient implements HtmlColor {

	private final HtmlColor color1;
	private final HtmlColor color2;
	private final char policy;

	public HtmlColorGradient(HtmlColor color1, HtmlColor color2, char policy) {
		if (color1 == null || color2 == null) {
			throw new IllegalArgumentException();
		}
		if (color1 instanceof HtmlColorGradient) {
			color1 = ((HtmlColorGradient) color1).color1;
		}
		if (color2 instanceof HtmlColorGradient) {
			color2 = ((HtmlColorGradient) color2).color2;
		}
		this.color1 = color1;
		this.color2 = color2;
		this.policy = policy;
	}

	public final HtmlColor getColor1() {
		return color1;
	}

	public final HtmlColor getColor2() {
		return color2;
	}

	public final Color getColor(ColorMapper mapper, double coeff) {
		if (coeff > 1 || coeff < 0) {
			throw new IllegalArgumentException("c=" + coeff);
		}
		final Color c1 = mapper.getMappedColor(color1);
		final Color c2 = mapper.getMappedColor(color2);
		final int vred = c2.getRed() - c1.getRed();
		final int vgreen = c2.getGreen() - c1.getGreen();
		final int vblue = c2.getBlue() - c1.getBlue();

		final int red = c1.getRed() + (int) (coeff * vred);
		final int green = c1.getGreen() + (int) (coeff * vgreen);
		final int blue = c1.getBlue() + (int) (coeff * vblue);

		return new Color(red, green, blue);

	}

	public final char getPolicy() {
		return policy;
	}

}
