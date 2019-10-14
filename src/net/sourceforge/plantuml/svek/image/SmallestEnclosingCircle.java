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
import java.util.ArrayList;
import java.util.List;

public class SmallestEnclosingCircle {

	private final List<Point2D> all = new ArrayList<Point2D>();
	private Circle lastSolution;

	public void append(Point2D pt) {
		if (all.contains(pt) == false) {
			all.add(pt);
		}
		this.lastSolution = null;
	}

	public Circle getCircle() {
		if (lastSolution == null) {
			lastSolution = findSec(all.size(), all, 0, new ArrayList<Point2D>(all));
		}
		return lastSolution;
	}

	private Circle findSec(int n, List<Point2D> p, int m, List<Point2D> b) {
		Circle sec = new Circle();

		// Compute the Smallest Enclosing Circle defined by B
		if (m == 1) {
			sec = new Circle(b.get(0));
		} else if (m == 2) {
			sec = new Circle(b.get(0), b.get(1));
		} else if (m == 3) {
			return Circle.getCircle(b.get(0), b.get(1), b.get(2));
		}

		// Check if all the points in p are enclosed
		for (int i = 0; i < n; i++) {
			if (sec.isOutside(p.get(i))) {
				// Compute B <--- B union P[i].
				b.set(m, p.get(i));
				// Recurse
				sec = findSec(i, p, m + 1, b);
			}
		}

		return sec;
	}

}
