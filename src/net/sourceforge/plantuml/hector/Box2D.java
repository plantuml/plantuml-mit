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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

public class Box2D {

	final private double x1;
	final private double y1;
	final private double x2;
	final private double y2;

	private Box2D(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static Box2D create(double x, double y, Dimension2D dim) {
		return new Box2D(x, y, x + dim.getWidth(), y + dim.getHeight());
	}

	@Override
	public String toString() {
		return "Box [" + x1 + "," + y1 + "] [" + x2 + "," + y2 + "]";
	}

	public boolean doesIntersect(LineSegmentDouble seg) {
		if (seg.doesIntersect(new LineSegmentDouble(x1, y1, x2, y1))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x2, y1, x2, y2))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x2, y2, x1, y2))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x1, y2, x1, y1))) {
			return true;
		}
		return false;
	}

}
