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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class USegment {

	private final double coord[];
	private final USegmentType pathType;

	public USegment(double[] coord, USegmentType pathType) {
		this.coord = coord.clone();
		this.pathType = pathType;
	}

	@Override
	public String toString() {
		return pathType.toString() + " " + Arrays.toString(coord);
	}

	public final double[] getCoord() {
		return coord;
	}

	public final USegmentType getSegmentType() {
		return pathType;
	}

	public USegment translate(double dx, double dy) {
		if (coord.length != 2) {
			throw new UnsupportedOperationException();
		}
		Point2D p1 = new Point2D.Double(coord[0] + dx, coord[1] + dy);
		return new USegment(new double[] { p1.getX(), p1.getY() }, pathType);
	}

	public USegment rotate(double theta) {
		if (coord.length != 2) {
			throw new UnsupportedOperationException();
		}
		Point2D p1 = new Point2D.Double(coord[0], coord[1]);
		final AffineTransform rotate = AffineTransform.getRotateInstance(theta);
		rotate.transform(p1, p1);
		return new USegment(new double[] { p1.getX(), p1.getY() }, pathType);
	}

}
