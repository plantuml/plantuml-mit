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
package net.sourceforge.plantuml.svek;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.posimo.BezierUtils;

public class ClusterPosition {

	private final double minX;
	private final double minY;
	private final double maxX;
	private final double maxY;

	public ClusterPosition(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public boolean contains(double x, double y) {
		return x >= minX && x < maxX && y >= minY && y < maxY;
	}

	public ClusterPosition merge(ClusterPosition other) {
		return new ClusterPosition(Math.min(this.minX, other.minX), Math.min(this.minY, other.minY), Math.max(
				this.maxX, other.maxX), Math.max(this.maxY, other.maxY));
	}

	public ClusterPosition merge(Point2D point) {
		final double x = point.getX();
		final double y = point.getY();
		return new ClusterPosition(Math.min(this.minX, x), Math.min(this.minY, y), Math.max(this.maxX, x), Math.max(
				this.maxY, y));
	}

	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	@Override
	public String toString() {
		return "minX=" + minX + " maxX=" + maxX + " minY=" + minY + " maxY=" + maxY;
	}

	public final double getMinX() {
		return minX;
	}

	public final double getMinY() {
		return minY;
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMaxY() {
		return maxY;
	}

	public PointDirected getIntersection(CubicCurve2D.Double bez) {
		if (contains(bez.x1, bez.y1) == contains(bez.x2, bez.y2)) {
			return null;
		}
		final double dist = bez.getP1().distance(bez.getP2());
		if (dist < 2) {
			final double angle = BezierUtils.getStartingAngle(bez);
			return new PointDirected(bez.getP1(), angle);
		}
		final CubicCurve2D.Double left = new CubicCurve2D.Double();
		final CubicCurve2D.Double right = new CubicCurve2D.Double();
		bez.subdivide(left, right);
		final PointDirected int1 = getIntersection(left);
		if (int1 != null) {
			return int1;
		}
		final PointDirected int2 = getIntersection(right);
		if (int2 != null) {
			return int2;
		}
		throw new IllegalStateException();
	}

	public Point2D getPointCenter() {
		return new Point2D.Double((minX + maxX) / 2, (minY + maxY) / 2);
	}

	public ClusterPosition withMinX(double d) {
		return new ClusterPosition(d, minY, maxX, maxY);
	}

	public ClusterPosition withMaxX(double d) {
		return new ClusterPosition(minX, minY, d, maxY);
	}

	public ClusterPosition addMaxX(double d) {
		return new ClusterPosition(minX, minY, maxX + d, maxY);
	}

	public ClusterPosition addMaxY(double d) {
		return new ClusterPosition(minX, minY, maxX, maxY + d);
	}

	public ClusterPosition addMinX(double d) {
		return new ClusterPosition(minX + d, minY, maxX, maxY);
	}

	public ClusterPosition addMinY(double d) {
		return new ClusterPosition(minX, minY + d, maxX, maxY);
	}

	public ClusterPosition withMinY(double d) {
		return new ClusterPosition(minX, d, maxX, maxY);
	}

	public ClusterPosition withMaxY(double d) {
		return new ClusterPosition(minX, minY, maxX, d);
	}

	public Point2D getProjectionOnFrontier(Point2D pt) {
		final double x = pt.getX();
		final double y = pt.getY();
		if (x > maxX && y >= minY && y <= maxY) {
			return new Point2D.Double(maxX - 1, y);
		}
		if (x < minX && y >= minY && y <= maxY) {
			return new Point2D.Double(minX + 1, y);
		}
		if (y > maxY && x >= minX && x <= maxX) {
			return new Point2D.Double(x, maxY - 1);
		}
		if (y < minY && x >= minX && x <= maxX) {
			return new Point2D.Double(x, minY + 1);
		}
		return new Point2D.Double(x, y);
	}

	public ClusterPosition delta(double m1, double m2) {
		return new ClusterPosition(minX, minY, maxX + m1, maxY + m2);
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(maxX - minX, maxY - minY);
	}

	public boolean isPointJustUpper(Point2D pt) {
		if (pt.getX() >= minX && pt.getX() <= maxX && pt.getY() <= minY) {
			return true;
		}
		return false;
	}

	public Side getClosestSide(Point2D pt) {
		final double distNorth = Math.abs(minY - pt.getY());
		final double distSouth = Math.abs(maxY - pt.getY());
		final double distWest = Math.abs(minX - pt.getX());
		final double distEast = Math.abs(maxX - pt.getX());
		if (isSmallerThan(distNorth, distWest, distEast, distSouth)) {
			return Side.NORTH;
		}
		if (isSmallerThan(distSouth, distNorth, distWest, distEast)) {
			return Side.SOUTH;
		}
		if (isSmallerThan(distEast, distNorth, distWest, distSouth)) {
			return Side.EAST;
		}
		if (isSmallerThan(distWest, distNorth, distEast, distSouth)) {
			return Side.WEST;
		}
		return null;
	}

	private boolean isSmallerThan(double value, double a, double b, double c) {
		return value <= a && value <= b && value <= c;
	}

}
