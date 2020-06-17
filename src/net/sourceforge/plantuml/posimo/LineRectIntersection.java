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
import java.awt.geom.Rectangle2D;

public class LineRectIntersection {

	private final Point2D inter;

	public LineRectIntersection(Line2D line, Rectangle2D rect) {
		final Point2D p1 = new Point2D.Double(rect.getMinX(), rect.getMinY());
		final Point2D p2 = new Point2D.Double(rect.getMaxX(), rect.getMinY());
		final Point2D p3 = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
		final Point2D p4 = new Point2D.Double(rect.getMinX(), rect.getMaxY());

		final Point2D inter1 = new LineSegmentIntersection(new Line2D.Double(p1, p2), line).getIntersection();
		final Point2D inter2 = new LineSegmentIntersection(new Line2D.Double(p2, p3), line).getIntersection();
		final Point2D inter3 = new LineSegmentIntersection(new Line2D.Double(p3, p4), line).getIntersection();
		final Point2D inter4 = new LineSegmentIntersection(new Line2D.Double(p4, p1), line).getIntersection();

		final Point2D o = line.getP1();
		inter = getCloser(o, inter1, inter2, inter3, inter4);

	}

	public static Point2D getCloser(final Point2D o, final Point2D... other) {
		double minDist = Double.MAX_VALUE;
		Point2D result = null;

		for (Point2D pt : other) {
			if (pt != null) {
				final double dist = pt.distanceSq(o);
				if (dist < minDist) {
					minDist = dist;
					result = pt;
				}
			}
		}

		return result;
	}

	public final Point2D getIntersection() {
		return inter;
	}

}
