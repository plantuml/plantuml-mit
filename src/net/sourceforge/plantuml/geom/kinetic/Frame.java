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
package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

public class Frame {

	private double x;
	private double y;

	private final int width;
	private final int height;

	public Frame(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	LineSegmentDouble getSide1() {
		return new LineSegmentDouble(x, y, x, y + height);
	}

	LineSegmentDouble getSide2() {
		return new LineSegmentDouble(x, y, x + width, y);
	}

	LineSegmentDouble getSide3() {
		return new LineSegmentDouble(x + width, y, x + width, y + height);
	}

	LineSegmentDouble getSide4() {
		return new LineSegmentDouble(x, y + height, x + width, y + height);
	}

	public Point2D getFrontierPointViewBy(Point2D point) {
		final LineSegmentDouble seg = new LineSegmentDouble(point, getCenter());
		Point2D p = seg.getSegIntersection(getSide1());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide2());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide3());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide4());
		if (p != null) {
			return p;
		}
		return null;
	}

	private Point2D getCenter() {
		return new Point2D.Double(x + width / 2.0, y + height / 2.0);
	}

	public Point2D getMainCorner() {
		return new Point2D.Double(x, y);
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

}
