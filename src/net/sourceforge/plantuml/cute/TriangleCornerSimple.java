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

public class TriangleCornerSimple {

	private final Point2D a;
	private final Point2D b;

	@Override
	public String toString() {
		return "TriangleCornerSimple a=" + a + " " + Math.toDegrees(getAngleA()) + " b=" + b + " "
				+ Math.toDegrees(getAngleB());
	}

	public TriangleCornerSimple(Point2D a, Point2D b) {
		if (isZero(a.getX()) == false) {
			throw new IllegalArgumentException("a=" + a);
		}
		this.a = a;
		this.b = b;
	}

	private static boolean isZero(double v) {
		return Math.abs(v) < 0.0001;
	}

	double getAngleA() {
		return getAngle(a);
	}

	double getAngleB() {
		return getAngle(b);
	}

	double getAngle(Point2D pt) {
		final double dx = pt.getX();
		final double dy = pt.getY();
		return Math.atan2(dy, dx);

	}

	static double solveY(double alpha, double x) {
		if (alpha < 0 || alpha > Math.PI / 2) {
			throw new IllegalArgumentException();
		}
		return x * Math.tan(alpha);
	}

	static double solveX(double alpha, double y) {
		if (alpha < -Math.PI / 2 || alpha > Math.PI / 2) {
			// throw new IllegalArgumentException("y=" + y + " alpha=" + Math.toDegrees(alpha));
		}
		final double beta = Math.PI / 2 - alpha;
		// System.err.println("alpha1=" + Math.toDegrees(alpha));
		// System.err.println("beta11=" + Math.toDegrees(beta));
		// System.err.println("XX=" + y * Math.tan(beta));
		return y * Math.tan(beta);

	}

	public Point2D getCenterWithFixedRadius(double radius) {
		final double alpha = (getAngleA() + getAngleB()) / 2;
		final double y = solveY(alpha, radius);
		return new Point2D.Double(radius, y);
	}

	public Balloon getBalloonWithFixedY(double y) {
		// System.err.println("TriangleCornerSimple::getCenterWithFixedY y=" + y);
		// System.err.println("a=" + a + " " + Math.toDegrees(getAngleA()));
		// System.err.println("b=" + b + " " + Math.toDegrees(getAngleB()));
		final double alpha = (getAngleA() + getAngleB()) / 2;
		// System.err.println("alpha=" + Math.toDegrees(alpha));
		final double sign = Math.signum(a.getY());
		// System.err.println("sgn=" + sign);
		final double x = solveX(alpha, y);
		final Balloon result = new Balloon(new Point2D.Double(x * sign, y * sign), Math.abs(x));
		// System.err.println("result=" + result);
		return result;
	}

}
