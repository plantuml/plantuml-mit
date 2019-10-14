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
package net.sourceforge.plantuml.braille;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.posimo.DotPath;

public class BrailleGrid {

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	private final double quanta;
	private final Set<Coords> on = new HashSet<Coords>();

	public BrailleGrid(double quanta) {
		this.quanta = quanta;
	}

	public boolean getState(int x, int y) {
		final Coords coords = new Coords(x, y);
		return on.contains(coords);
	}

	private void setStateDouble(double x, double y, boolean state) {
		setState(toInt(x), toInt(y), state);
	}

	public void setState(int x, int y, boolean state) {
		final Coords coords = new Coords(x, y);
		if (state) {
			on.add(coords);
		} else {
			on.remove(coords);
		}
		minX = Math.min(minX, x);
		maxX = Math.max(maxX, x);
		minY = Math.min(minY, y);
		maxY = Math.max(maxY, y);
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void rectangle(double x, double y, double width, double height) {
		hline(y, x, x + width);
		hline(y + height, x, x + width);
		vline(x, y, y + height);
		vline(x + width, y, y + height);

	}

	private void vline(double x, double y1, double y2) {
		final int i = toInt(x);
		final int j1 = toInt(y1);
		final int j2 = toInt(y2);
		for (int j = j1; j <= j2; j++) {
			setState(i, j, true);
		}
	}

	private void hline(double y, double x1, double x2) {
		final int j = toInt(y);
		final int i1 = toInt(x1);
		final int i2 = toInt(x2);
		for (int i = i1; i <= i2; i++) {
			setState(i, j, true);
		}
	}

	public int toInt(double value) {
		return (int) Math.round(value / quanta);
	}

	public void line(double x1, double y1, double x2, double y2) {
		if (x1 == x2) {
			vline(x1, y1, y2);
		} else if (y1 == y2) {
			hline(y1, x1, x2);
		} else {
			System.err.println("warning line");
		}

	}

	public double getQuanta() {
		return quanta;
	}

	public void drawDotPath(double x, double y, DotPath shape) {
		for (CubicCurve2D.Double bez : shape.getBeziers()) {
			drawCubic(x, y, bez);

		}
	}

	private void drawCubic(double x, double y, CubicCurve2D.Double bez) {
		drawPointInternal(x, y, bez.getP1());
		drawPointInternal(x, y, bez.getP2());
		if (bez.getP1().distance(bez.getP2()) > quanta) {
			final CubicCurve2D.Double part1 = new CubicCurve2D.Double();
			final CubicCurve2D.Double part2 = new CubicCurve2D.Double();
			bez.subdivide(part1, part2);
			drawCubic(x, y, part1);
			drawCubic(x, y, part2);
		}
	}

	private void drawPointInternal(double x, double y, Point2D pt) {
		setStateDouble(x + pt.getX(), y + pt.getY(), true);
	}

	public void drawPolygon(List<Point2D> points) {
		for (int i = 0; i < points.size() - 1; i++) {
			drawLineInternal(points.get(i), points.get(i + 1));
		}
		drawLineInternal(points.get(points.size() - 1), points.get(0));

	}

	private void drawLineInternal(Point2D a, Point2D b) {
		drawPointInternal(0, 0, a);
		drawPointInternal(0, 0, b);
		if (a.distance(b) > quanta) {
			final Point2D middle = new Point2D.Double((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
			drawLineInternal(a, middle);
			drawLineInternal(middle, b);

		}
	}
}
