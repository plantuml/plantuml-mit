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

import java.util.StringTokenizer;

public class MyDouble {

	private static final double NO_CURVE = java.lang.Double.MIN_VALUE;
	private final double value;
	private final double curvation;

	public MyDouble(String s) {
		final StringTokenizer st = new StringTokenizer(s, ",");
		this.value = java.lang.Double.parseDouble(st.nextToken());
		if (st.hasMoreTokens()) {
			this.curvation = java.lang.Double.parseDouble(st.nextToken());
		} else {
			this.curvation = NO_CURVE;
		}
	}

	@Override
	public String toString() {
		return value + "[" + curvation + "]";
	}

	private MyDouble(double value, double curvation) {
		this.value = value;
		this.curvation = curvation;
	}

	public double getCurvation(double def) {
		if (curvation == NO_CURVE) {
			return def;
		}
		return curvation;
	}

	public double getValue() {
		return value;
	}

	public boolean hasCurvation() {
		return curvation != NO_CURVE;
	}

	public MyDouble rotateZoom(RotationZoom rotationZoom) {
		final double newValue = rotationZoom.applyZoom(value);
		final double curvation = this.curvation == NO_CURVE ? NO_CURVE : rotationZoom.applyZoom(this.curvation);
		return new MyDouble(newValue, curvation);
	}

	public MyDouble toRadians() {
		return new MyDouble(Math.toRadians(value), curvation);
	}

}
