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
package net.sourceforge.plantuml.ugraphic.hand;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class HandJiggle {
	private final Collection<Point2D> points = new ArrayList<Point2D>();

	private double startX;
	private double startY;
	private final double defaultVariation;
	private final Random rnd;

	private double randomMe() {
		return rnd.nextDouble();
	}

	public HandJiggle(double startX, double startY, double defaultVariation, Random rnd) {
		this.startX = startX;
		this.startY = startY;
		this.defaultVariation = defaultVariation;
		this.rnd = rnd;
		points.add(new Point2D.Double(startX, startY));
	}

	public HandJiggle(Point2D start, double defaultVariation, Random rnd) {
		this(start.getX(), start.getY(), defaultVariation, rnd);
	}

	public void lineTo(Point2D end) {
		lineTo(end.getX(), end.getY());
	}

	public void arcTo(double angle0, double angle1, double centerX, double centerY, double rx, double ry) {
		lineTo(pointOnCircle(centerX, centerY, (angle0 + angle1) / 2, rx, ry));
		lineTo(pointOnCircle(centerX, centerY, angle1, rx, ry));
	}

	private static Point2D pointOnCircle(double centerX, double centerY, double angle, double rx, double ry) {
		final double x = centerX + Math.cos(angle) * rx;
		final double y = centerY + Math.sin(angle) * ry;
		return new Point2D.Double(x, y);

	}

	public void lineTo(final double endX, final double endY) {

		final double diffX = Math.abs(endX - startX);
		final double diffY = Math.abs(endY - startY);
		final double distance = Math.sqrt(diffX * diffX + diffY * diffY);
		if (distance < 0.001) {
			return;
		}

		int segments = (int) Math.round(distance / 10);
		double variation = defaultVariation;
		if (segments < 5) {
			segments = 5;
			variation /= 3;
		}

		final double stepX = Math.signum(endX - startX) * diffX / segments;
		final double stepY = Math.signum(endY - startY) * diffY / segments;

		final double fx = diffX / distance;
		final double fy = diffY / distance;

		for (int s = 0; s < segments; s++) {
			double x = stepX * s + startX;
			double y = stepY * s + startY;

			final double offset = (randomMe() - 0.5) * variation;
			points.add(new Point2D.Double(x - offset * fy, y - offset * fx));
		}
		points.add(new Point2D.Double(endX, endY));

		this.startX = endX;
		this.startY = endY;
	}

	public void curveTo(CubicCurve2D curve) {
		final double flatness = curve.getFlatness();
		final double dist = curve.getP1().distance(curve.getP2());
		if (flatness > 0.1 && dist > 20) {
			final CubicCurve2D left = new CubicCurve2D.Double();
			final CubicCurve2D right = new CubicCurve2D.Double();
			curve.subdivide(left, right);
			curveTo(left);
			curveTo(right);
			return;
		}
		lineTo(curve.getP2());
	}

	public UPolygon toUPolygon() {
		final UPolygon result = new UPolygon();
		for (Point2D p : points) {
			result.addPoint(p.getX(), p.getY());
		}
		return result;
	}

	public UPath toUPath() {
		UPath path = null;
		for (Point2D p : points) {
			if (path == null) {
				path = new UPath();
				path.moveTo(p);
			} else {
				path.lineTo(p);
			}
		}
		if (path == null) {
			throw new IllegalStateException();
		}
		return path;
	}

	public void appendTo(UPath result) {
		for (Point2D p : points) {
			result.lineTo(p);
		}
	}

}
