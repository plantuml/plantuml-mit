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

public enum ColorOrder {
	RGB, RBG, GRB, GBR, BRG, BGR;

	public Color getColor(Color color) {
		if (this == RGB) {
			return new Color(color.getRed(), color.getGreen(), color.getBlue());
		}
		if (this == RBG) {
			return new Color(color.getRed(), color.getBlue(), color.getGreen());
		}
		if (this == GRB) {
			return new Color(color.getGreen(), color.getRed(), color.getBlue());
		}
		if (this == GBR) {
			return new Color(color.getGreen(), color.getBlue(), color.getRed());
		}
		if (this == BRG) {
			return new Color(color.getBlue(), color.getRed(), color.getGreen());
		}
		if (this == BGR) {
			return new Color(color.getBlue(), color.getGreen(), color.getRed());
		}
		throw new IllegalStateException();
	}

	public Color getReverse(Color color) {
		color = this.getColor(color);
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

	public static ColorOrder fromString(String order) {
		try {
			return ColorOrder.valueOf(order.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

}
