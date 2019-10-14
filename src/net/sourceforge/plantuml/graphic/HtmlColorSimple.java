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

import net.sourceforge.plantuml.ugraphic.ColorChangerMonochrome;

public class HtmlColorSimple implements HtmlColor {

	private final Color color;
	private final boolean monochrome;

	@Override
	public int hashCode() {
		return color.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof HtmlColorSimple == false) {
			return false;
		}
		return this.color.equals(((HtmlColorSimple) other).color);
	}

	public HtmlColorSimple(Color c, boolean monochrome) {
		this.color = c;
		this.monochrome = monochrome;
	}

	public Color getColor999() {
		return color;
	}

	public HtmlColorSimple asMonochrome() {
		if (monochrome) {
			throw new IllegalStateException();
		}
		return new HtmlColorSimple(new ColorChangerMonochrome().getChangedColor(color), true);
	}

	public HtmlColorSimple opposite() {
		final Color mono = new ColorChangerMonochrome().getChangedColor(color);
		final int grayScale = 255 - mono.getGreen() > 127 ? 255 : 0;
		return new HtmlColorSimple(new Color(grayScale, grayScale, grayScale), true);
	}

	public double distance(HtmlColorSimple other) {
		final int diffRed = Math.abs(this.color.getRed() - other.color.getRed());
		final int diffGreen = Math.abs(this.color.getGreen() - other.color.getGreen());
		final int diffBlue = Math.abs(this.color.getBlue() - other.color.getBlue());
		return diffRed * .3 + diffGreen * .59 + diffBlue * .11;
	}

}
