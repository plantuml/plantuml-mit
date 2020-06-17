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

public class VectorForce {

	private final double x;
	private final double y;

	public VectorForce(double x, double y) {
		if (Double.isNaN(x) || Double.isNaN(y) || Double.isInfinite(x) || Double.isInfinite(y)) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
	}

	public VectorForce(Point2D src, Point2D dest) {
		this(dest.getX() - src.getX(), dest.getY() - src.getY());
	}

	public VectorForce plus(VectorForce other) {
		return new VectorForce(this.x + other.x, this.y + other.y);
	}

	public VectorForce multiply(double v) {
		return new VectorForce(x * v, y * v);
	}

	@Override
	public String toString() {
		return String.format("{%8.2f %8.2f}", x, y);
	}

	public VectorForce negate() {
		return new VectorForce(-x, -y);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public VectorForce normaliseTo(double newLength) {
		if (Double.isInfinite(newLength) || Double.isNaN(newLength)) {
			throw new IllegalArgumentException();
		}
		final double actualLength = length();
		if (actualLength == 0) {
			return this;
		}
		final double f = newLength / actualLength;
		return new VectorForce(x * f, y * f);

	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
}
