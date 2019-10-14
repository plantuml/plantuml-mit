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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ConnectedCircle implements UDrawable {

	private final double radius;
	private final List<Double> angles = new ArrayList<Double>();
	private final List<Point2D> points = new ArrayList<Point2D>();

	public ConnectedCircle(double radius) {
		this.radius = radius;
	}

	public void drawU(UGraphic ug) {
		final UEllipse circle = new UEllipse(2 * radius, 2 * radius);
		// ug.draw(circle);
		for (Double angle : angles) {
			final double delta = 30;
			final UEllipse part = new UEllipse(2 * radius, 2 * radius, angle - delta, 2 * delta);
			ug.draw(part);
		}
		ug = ug.apply(new UChangeColor(HtmlColorUtils.GREEN)).apply(new UChangeBackColor(HtmlColorUtils.GREEN));
		for (Point2D pt : points) {
			final UTranslate tr = new UTranslate(pt);
			// ug.apply(tr).draw(new UEllipse(2, 2));
		}

	}

	public void addSecondaryConnection(Point2D pt) {
		points.add(pt);
		// double angle = Math.atan2(pt.getY() - radius, pt.getX() - radius);
		// double angle = Math.atan2(pt.getX() - radius, pt.getY() - radius);
		double angle = Math.atan2(radius - pt.getY(), pt.getX() - radius);
		angle = angle * 180.0 / Math.PI;
		System.err.println("pt1=" + pt + " " + angle);
		angles.add(angle);

	}

}
