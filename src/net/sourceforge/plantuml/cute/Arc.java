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

public class Arc {

	private final Segment segment;
	private final Tension tension;

	public Tension getTension() {
		return tension;
	}

	public Arc(final MyPoint2D a, final MyPoint2D b) {
		this(a, b, Tension.none());
	}

	private Arc(final MyPoint2D a, final MyPoint2D b, Tension tension) {
		this.segment = new Segment(a, b);
		this.tension = tension;
	}

	public MyPoint2D getA() {
		return (MyPoint2D) segment.getA();
	}

	public MyPoint2D getB() {
		return (MyPoint2D) segment.getB();
	}

	public Arc withNoTension() {
		return new Arc(getA(), getB(), Tension.none());
	}

	public Arc withTension(String tensionString) {
		if (tensionString == null) {
			return this;
		}
		final double newTension = Double.parseDouble(tensionString);
		return new Arc(getA(), getB(), new Tension(newTension));
	}

	public Arc rotateZoom(RotationZoom rotationZoom) {
		return new Arc(getA().rotateZoom(rotationZoom), getB().rotateZoom(rotationZoom),
				tension.rotateZoom(rotationZoom));
	}

//	public void appendTo(UPath path) {
//		if (tension.isNone()) {
//			path.lineTo(getB());
//		} else {
//			final double a = segment.getLength() / 2;
//			final double b = getTension().getValue();
//			final double radius = (a * a + b * b) / 2 / b;
//			final int sweep_flag = 1;
//			path.arcTo(getB(), radius, 0, sweep_flag);
//		}
//	}

	public Point2D getTensionPoint() {
		if (tension.isNone()) {
			throw new IllegalArgumentException();
		}
		return segment.getOrthoPoint(-tension.getValue());
	}

	// public void appendTo(UPath path) {
	// if (path.isEmpty()) {
	// path.moveTo(getA());
	// }
	// path.lineTo(getB());
	// }

	public double getLength() {
		return segment.getLength();
	}

}
