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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;
import java.util.StringTokenizer;

public class MyPoint2D extends Point2D {

	public static final double NO_CURVE = 0;
	private final double x;
	private final double y;
	private final double curvation;

	public MyPoint2D(StringTokenizer st) {
		this.x = java.lang.Double.parseDouble(st.nextToken());
		this.y = java.lang.Double.parseDouble(st.nextToken());
		if (st.hasMoreTokens()) {
			this.curvation = java.lang.Double.parseDouble(st.nextToken());
		} else {
			this.curvation = NO_CURVE;
		}
	}

	@Override
	public boolean equals(Object arg0) {
		final MyPoint2D other = (MyPoint2D) arg0;
		return this.x == other.x && this.y == other.y && this.curvation == other.curvation;
	}

	public static MyPoint2D from(double x, double y) {
		return new MyPoint2D(x, y, NO_CURVE);
	}

	public MyPoint2D withCurvation(double curvation) {
		if (curvation == NO_CURVE) {
			return this;
		}
		return new MyPoint2D(x, y, curvation);
	}

	private MyPoint2D(Point2D p, double curvation) {
		this.x = p.getX();
		this.y = p.getY();
		this.curvation = curvation;
	}

	private MyPoint2D(double x, double y, double curvation) {
		this.x = x;
		this.y = y;
		this.curvation = curvation;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public double getCurvation(double def) {
		if (curvation == NO_CURVE) {
			return def;
		}
		return curvation;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setLocation(double arg0, double arg1) {
		throw new UnsupportedOperationException();
	}

	public MyPoint2D rotateZoom(RotationZoom rotationZoom) {
		final Point2D p = rotationZoom.getPoint(x, y);
		final double curvation = this.curvation == NO_CURVE ? NO_CURVE : rotationZoom.applyZoom(this.curvation);
		return new MyPoint2D(p, curvation);
	}

	public boolean hasCurvation() {
		return curvation != NO_CURVE;
	}

}
