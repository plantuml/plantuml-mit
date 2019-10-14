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

public class Circle {

	private Point2D center;

	private double radius;

	public Circle() {
		this(new Point2D.Double());
	}

	public Circle(Point2D center) {
		this.center = center;
		this.radius = 0;
	}

	public Circle(Point2D p1, Point2D p2) {
		center = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		radius = p1.distance(center);
	}

	public static Circle getCircle(Point2D p1, Point2D p2, Point2D p3) {
		if (p3.getY() != p2.getY()) {
			return new Circle(p1, p2, p3);
		}
		return new Circle(p2, p1, p3);
	}


	private Circle(Point2D p1, Point2D p2, Point2D p3) {
		final double num1 = p3.getX() * p3.getX() * (p1.getY() - p2.getY())
				+ (p1.getX() * p1.getX() + (p1.getY() - p2.getY()) * (p1.getY() - p3.getY())) * (p2.getY() - p3.getY())
				+ p2.getX() * p2.getX() * (-p1.getY() + p3.getY());
		final double den1 = 2 * (p3.getX() * (p1.getY() - p2.getY()) + p1.getX() * (p2.getY() - p3.getY()) + p2.getX()
				* (-p1.getY() + p3.getY()));
		final double x = num1 / den1;
		final double den2 = p3.getY() - p2.getY();
		final double y = (p2.getY() + p3.getY()) / 2 - (p3.getX() - p2.getX()) / den2
				* (x - (p2.getX() + p3.getX()) / 2);
		center = new Point2D.Double(x, y);
		radius = center.distance(p1);
	}

	public Point2D getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public boolean isOutside(Point2D point) {
		final double d = center.distance(point);
		if (d > radius) {
			return true;
		}
		return false;
	}

}
