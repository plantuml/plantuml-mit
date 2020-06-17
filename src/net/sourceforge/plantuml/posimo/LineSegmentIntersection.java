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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineSegmentIntersection {

	private final Point2D inter;
	
	// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/

	public LineSegmentIntersection(Line2D segment, Line2D lineB) {
		final double x1 = segment.getX1();
		final double y1 = segment.getY1();
		final double x2 = segment.getX2();
		final double y2 = segment.getY2();
		final double x3 = lineB.getX1();
		final double y3 = lineB.getY1();
		final double x4 = lineB.getX2();
		final double y4 = lineB.getY2();

		final double den = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

		if (den == 0) {
			inter = null;
		} else {

			final double uA1 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
			final double uA = uA1 / den;

			final double x = x1 + uA * (x2 - x1);
			final double y = y1 + uA * (y2 - y1);

			if (uA >= 0 && uA <= 1) {
				inter = new Point2D.Double(x, y);
			} else {
				inter = null;
			}
		}
	}

	public final Point2D getIntersection() {
		return inter;
	}

}
