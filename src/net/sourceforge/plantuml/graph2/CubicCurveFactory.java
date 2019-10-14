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
package net.sourceforge.plantuml.graph2;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CubicCurveFactory {

	private final Point2D.Double start;
	private final Point2D.Double end;
	private final RectanglesCollection forbiddenRect = new RectanglesCollection();
	private final List<MyCurve> forbiddenCurves = new ArrayList<MyCurve>();

	public CubicCurveFactory(Point2D start, Point2D end) {
		this.start = new Point2D.Double(start.getX(), start.getY());
		this.end = new Point2D.Double(end.getX(), end.getY());
	}

	public void addForbidden(Rectangle2D.Double rect) {
		forbiddenRect.add(rect);
	}

	public void addForbidden(MyCurve curve) {
		forbiddenCurves.add(curve);
	}

	public MyCurve getCubicCurve2D() {
		MyCurve result = new MyCurve(new CubicCurve2D.Double(start.getX(), start.getY(), start.getX(), start.getY(),
				end.getX(), end.getY(), end.getX(), end.getY()));
		if (result.intersects(forbiddenRect) || result.intersects(forbiddenCurves)) {
			final Set<Point2D.Double> all = new HashSet<Point2D.Double>();
			all.addAll(MagicPointsFactory.get(start, end));
			for (Rectangle2D.Double rect : forbiddenRect) {
				all.addAll(MagicPointsFactory.get(rect));
			}
// Log.println("s1 " + all.size());
//			final long t1 = System.currentTimeMillis();
			double min = Double.MAX_VALUE;
			for (Point2D.Double p1 : all) {
				for (Point2D.Double p2 : all) {
					final MyCurve me = new MyCurve(new CubicCurve2D.Double(start.getX(), start.getY(), p1.getX(), p1
							.getY(), p2.getX(), p2.getY(), end.getX(), end.getY()));
					if (me.getLenght() < min && me.intersects(forbiddenRect) == false
							&& me.intersects(forbiddenCurves) == false) {
						result = me;
						min = me.getLenght();
					}
				}
			}
//			final long t2 = System.currentTimeMillis() - t1;
// Log.println("s2 = " + t2);
// Log.println("TPS1 = " + RectanglesCollection.TPS1);
// Log.println("TPS2 = " + RectanglesCollection.TPS2);
		}
		return result;
	}

}
