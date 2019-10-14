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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MagicPointsFactory2 {

	private final Point2D.Double p1;
	private final Point2D.Double p2;

	private final List<Point2D.Double> result = new ArrayList<Point2D.Double>();

	public MagicPointsFactory2(Point2D.Double p1, Point2D.Double p2) {
		this.p1 = p1;
		this.p2 = p2;
		final double dx = p2.x - p1.x;
		final double dy = p2.y - p1.y;

		final int interv = 5;
		final int intervAngle = 10;
		final double theta = Math.PI * 2 / intervAngle;
		for (int a = 0; a < 10; a++) {
			final AffineTransform at = AffineTransform.getRotateInstance(theta * a, p1.x, p1.y);
			for (int i = 0; i < interv * 2; i++) {
				final Point2D.Double p = new Point2D.Double(p1.x + dx * i / interv, p1.y + dy * i / interv);
				result.add((Point2D.Double) at.transform(p, null));
			}
		}

	}

	public List<Point2D.Double> get() {
		return result;
	}

}
