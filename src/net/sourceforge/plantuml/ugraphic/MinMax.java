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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class MinMax {

	private final double maxX;
	private final double maxY;
	private final double minX;
	private final double minY;

	public boolean doesHorizontalCross(Point2D.Double pt1, Point2D.Double pt2) {
		if (pt1.getY() != pt2.getY()) {
			throw new IllegalArgumentException();
		}
		if (pt1.getX() == pt2.getX()) {
			throw new IllegalArgumentException();
		}
		final double y = pt1.getY();
		if (y < minY || y > maxY) {
			return false;
		}
		if (pt1.getX() < minX && pt2.getX() > maxX) {
			return true;
		}
		if (pt2.getX() < minX && pt1.getX() > maxX) {
			return true;
		}
		return false;
	}

	public static MinMax getEmpty(boolean initToZero) {
		if (initToZero) {
			return new MinMax(0, 0, 0, 0);
		}
		return new MinMax(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	}

	@Override
	public String toString() {
		return "(" + minX + "," + minY + ")->(" + maxX + "," + maxY + ")";
	}

	public static MinMax fromMutable(MinMaxMutable minmax) {
		return new MinMax(minmax.getMinX(), minmax.getMinY(), minmax.getMaxX(), minmax.getMaxY());
	}

	private MinMax(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		if (Double.isNaN(minX)) {
			throw new IllegalArgumentException();
		}
		if (Double.isNaN(maxX)) {
			throw new IllegalArgumentException();
		}
		if (Double.isNaN(minY)) {
			throw new IllegalArgumentException();
		}
		if (Double.isNaN(maxY)) {
			throw new IllegalArgumentException();
		}
	}

	public MinMax addPoint(Point2D pt) {
		return addPoint(pt.getX(), pt.getY());
	}

	public MinMax addPoint(double x, double y) {
		return new MinMax(Math.min(x, minX), Math.min(y, minY), Math.max(x, maxX), Math.max(y, maxY));
	}

	public MinMax addMinMax(MinMax other) {
		return new MinMax(Math.min(other.minX, minX), Math.min(other.minY, minY), Math.max(other.maxX, maxX), Math.max(
				other.maxY, maxY));
	}

	public static MinMax fromMax(double maxX, double maxY) {
		return MinMax.getEmpty(true).addPoint(maxX, maxY);
	}

	public static MinMax fromDim(Dimension2D dim) {
		return fromMax(dim.getWidth(), dim.getHeight());
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMaxY() {
		return maxY;
	}

	public final double getMinX() {
		return minX;
	}

	public final double getMinY() {
		return minY;
	}

	public double getHeight() {
		return maxY - minY;
	}

	public double getWidth() {
		return maxX - minX;
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(maxX - minX, maxY - minY);
	}

	public void drawGrey(UGraphic ug) {
		draw(ug, HColorUtils.GRAY);
	}

	public void draw(UGraphic ug, HColor color) {
		ug = ug.apply(color).apply(color.bg());
		ug = ug.apply(new UTranslate(minX, minY));
		ug.draw(new URectangle(getWidth(), getHeight()));
	}

	public MinMax translate(UTranslate translate) {
		final double dx = translate.getDx();
		final double dy = translate.getDy();
		return new MinMax(minX + dx, minY + dy, maxX + dx, maxY + dy);
	}

}
