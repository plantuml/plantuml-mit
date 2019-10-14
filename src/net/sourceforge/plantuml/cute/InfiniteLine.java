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

import net.sourceforge.plantuml.geom.AbstractLineSegment;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class InfiniteLine {

	private final Point2D a;
	private final Point2D b;

	public InfiniteLine(Point2D a, Point2D b) {
		this.a = a;
		this.b = b;
	}

	public InfiniteLine(AbstractLineSegment segment) {
		this(segment.getP1(), segment.getP2());
	}

	@Override
	public String toString() {
		return "{" + a + ";" + b + "}";
	}

	public double getDeltaX() {
		return b.getX() - a.getX();
	}

	public double getDeltaY() {
		return b.getY() - a.getY();
	}

	public double getDr() {
		return a.distance(b);
	}

	public double getDiscriminant() {
		return a.getX() * b.getY() - b.getX() * a.getY();
	}

	public InfiniteLine translate(UTranslate translate) {
		return new InfiniteLine(translate.getTranslated(a), translate.getTranslated(b));
	}

}
