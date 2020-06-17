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
package net.sourceforge.plantuml.graph2;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MagicPointsFactory {

	private MagicPointsFactory() {

	}

	public static List<Point2D.Double> get(Rectangle2D.Double rect) {
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		result.add(new Point2D.Double(rect.x - rect.width, rect.y - rect.height));
		result.add(new Point2D.Double(rect.x, rect.y - rect.height));
		result.add(new Point2D.Double(rect.x + rect.width, rect.y - rect.height));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y - rect.height));

		result.add(new Point2D.Double(rect.x - rect.width, rect.y));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y));

		result.add(new Point2D.Double(rect.x - rect.width, rect.y + rect.height));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y + rect.height));

		result.add(new Point2D.Double(rect.x - rect.width, rect.y + 2 * rect.height));
		result.add(new Point2D.Double(rect.x, rect.y + 2 * rect.height));
		result.add(new Point2D.Double(rect.x + rect.width, rect.y + 2 * rect.height));
		result.add(new Point2D.Double(rect.x + 2 * rect.width, rect.y + 2 * rect.height));
		return result;
	}

	public static List<Point2D.Double> get(Point2D.Double p1, Point2D.Double p2) {
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		result.add(new Point2D.Double(p1.x, p2.y));
		result.add(new Point2D.Double(p2.x, p1.y));
		return result;
	}
}
