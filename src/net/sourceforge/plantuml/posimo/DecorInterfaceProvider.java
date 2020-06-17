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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class DecorInterfaceProvider implements Decor {

	private final double radius = 5;
	private final double radius2 = 9;
	private final LinkStyle style;

	// private final double distanceCircle = 16;

	public DecorInterfaceProvider(LinkStyle style) {
//		if (style != LinkStyle.__toremove_INTERFACE_PROVIDER && style != LinkStyle.__toremove_INTERFACE_USER) {
//			throw new IllegalArgumentException();
//		}
		this.style = style;
	}

	public void drawDecor(UGraphic ug, Point2D start, double direction) {
		final double cornerX = start.getX() - radius;
		final double cornerY = start.getY() - radius;
		final double cornerX2 = start.getX() - radius2 - 0 * Math.sin(direction * Math.PI / 180.0);
		final double cornerY2 = start.getY() - radius2 - 0 * Math.cos(direction * Math.PI / 180.0);

//		if (style == LinkStyle.__toremove_INTERFACE_USER) {
//			direction += 180;
//		}
		if (direction >= 360) {
			direction -= 360;
		}

		final UEllipse arc = new UEllipse(2 * radius2, 2 * radius2, direction + 15, 180 - 30);
		ug = ug.apply(new UStroke(1.5));
		ug.apply(new UTranslate(cornerX2, cornerY2)).draw(arc);
		ug.apply(new UTranslate(cornerX, cornerY)).draw(new UEllipse(2 * radius, 2 * radius));
	}

}
