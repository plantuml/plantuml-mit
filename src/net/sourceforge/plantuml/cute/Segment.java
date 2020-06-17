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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Segment {

	private final Point2D a;
	private final Point2D b;
	private final double length;

	public Segment(Point2D a, Point2D b) {
		this.a = a;
		this.b = b;
		this.length = a.distance(b);
		if (length < 0.0001) {
			throw new IllegalArgumentException();
		}
	}

	public Point2D getFromAtoB(double dist) {
		final double dx = b.getX() - a.getX();
		final double dy = b.getY() - a.getY();
		final double coef = dist / length;
		final double x = dx * coef;
		final double y = dy * coef;
		return new Point2D.Double(a.getX() + x, a.getY() + y);
	}

	public Point2D getA() {
		return a;
	}

	public Point2D getB() {
		return b;
	}

	public Point2D getMiddle() {
		return new Point2D.Double((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
	}

	private Point2D orthoDirection() {
		final double dx = b.getX() - a.getX();
		final double dy = b.getY() - a.getY();
		return new Point2D.Double(-dy / length, dx / length);
	}

	public Point2D getOrthoPoint(double value) {
		final Point2D ortho = orthoDirection();
		final double dx = -ortho.getX() * value;
		final double dy = -ortho.getY() * value;
		return new Point2D.Double((a.getX() + b.getX()) / 2 + dx, (a.getY() + b.getY()) / 2 + dy);
	}


	private boolean isLeft(Point2D point) {
		return ((b.getX() - a.getX()) * (point.getY() - a.getY()) - (b.getY() - a.getY()) * (point.getX() - a.getX())) > 0;
	}

	public double getLength() {
		return length;
	}

	public void debugMe(UGraphic ug) {
		final double dx = b.getX() - a.getX();
		final double dy = b.getY() - a.getY();
		ug = ug.apply(new UTranslate(a));
		ug.draw(new ULine(dx, dy));
		
	}

}
