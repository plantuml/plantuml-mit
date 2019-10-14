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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ExtremityCircleLine extends Extremity {

	private final Point2D contact;
	private final double angle;

	@Override
	public Point2D somePoint() {
		return contact;
	}

	public ExtremityCircleLine(Point2D p1, double angle) {
		this.contact = new Point2D.Double(p1.getX(), p1.getY());
		this.angle = manageround(angle + Math.PI / 2);
	}

	public void drawU(UGraphic ug) {
		final double thickness = ug.getParam().getStroke().getThickness();
		final double radius = 4 + thickness - 1;
		final double lineHeight = 4 + thickness - 1;
		final int xWing = 4;
		final AffineTransform rotate = AffineTransform.getRotateInstance(this.angle);
		Point2D middle = new Point2D.Double(0, 0);
		Point2D base = new Point2D.Double(-xWing - radius - 3, 0);
		Point2D circleBase = new Point2D.Double(-xWing - radius - 3, 0);

		Point2D lineTop = new Point2D.Double(-xWing, -lineHeight);
		Point2D lineBottom = new Point2D.Double(-xWing, lineHeight);

		rotate.transform(lineTop, lineTop);
		rotate.transform(lineBottom, lineBottom);
		rotate.transform(base, base);
		rotate.transform(circleBase, circleBase);

		drawLine(ug, contact.getX(), contact.getY(), base, middle);
		final UStroke stroke = new UStroke(thickness);
		ug.apply(
				new UTranslate(contact.getX() + circleBase.getX() - radius, contact.getY() + circleBase.getY() - radius))
				.apply(stroke).draw(new UEllipse(2 * radius, 2 * radius));
		drawLine(ug.apply(stroke), contact.getX(), contact.getY(), lineTop, lineBottom);
	}

	static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(x + p1.getX(), y + p1.getY())).draw(new ULine(dx, dy));
	}

}
