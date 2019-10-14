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
import java.util.ArrayList;
import java.util.List;

public class CrossingSimple {

	// http://mathworld.wolfram.com/Circle-LineIntersection.html

	private final double radius;
	private final InfiniteLine line;

	public CrossingSimple(double radius, InfiniteLine line) {
		this.radius = radius;
		this.line = line;
	}

	private double pow2(double x) {
		return x * x;
	}

	private double sgn(double x) {
		if (x < 0) {
			return -1;
		}
		return 1;
	}

	public List<Point2D> intersection() {
		final List<Point2D> result = new ArrayList<Point2D>();
		final double delta = pow2(radius * line.getDr()) - pow2(line.getDiscriminant());

		if (delta < 0) {
			return result;
		}

		double x;
		double y;

		x = (line.getDiscriminant() * line.getDeltaY() + sgn(line.getDeltaY()) * line.getDeltaX() * Math.sqrt(delta))
				/ pow2(line.getDr());
		y = (-line.getDiscriminant() * line.getDeltaX() + Math.abs(line.getDeltaY()) * Math.sqrt(delta))
				/ pow2(line.getDr());
		result.add(new Point2D.Double(x, y));

		x = (line.getDiscriminant() * line.getDeltaY() - sgn(line.getDeltaY()) * line.getDeltaX() * Math.sqrt(delta))
				/ pow2(line.getDr());
		y = (-line.getDiscriminant() * line.getDeltaX() - Math.abs(line.getDeltaY()) * Math.sqrt(delta))
				/ pow2(line.getDr());
		result.add(new Point2D.Double(x, y));

		return result;
	}

}
