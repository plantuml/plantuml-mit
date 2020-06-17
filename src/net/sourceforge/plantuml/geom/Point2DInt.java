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
package net.sourceforge.plantuml.geom;

import java.awt.geom.Point2D;

public class Point2DInt extends Point2D implements Pointable {

	private final int x;
	private final int y;

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Point2DInt(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getXint() {
		return x;
	}

	public int getYint() {
		return y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setLocation(double x, double y) {
		throw new UnsupportedOperationException();
	}

	public Point2DInt getPosition() {
		return this;
	}

	public Point2DInt translate(int deltaX, int deltaY) {
		return new Point2DInt(x + deltaX, y + deltaY);
	}

	public Point2DInt inflateX(int xpos, int inflation) {
		if (inflation % 2 != 0) {
			throw new IllegalArgumentException();
		}
		if (x < xpos) {
			return this;
		}
		if (x == xpos) {
			// throw new IllegalArgumentException();
			return translate(inflation / 2, 0);
		}
		return translate(inflation, 0);
	}

	public Point2DInt inflateX(InflateData inflateData) {
		return inflateX(inflateData.getPos(), inflateData.getInflation());
	}

	public Point2DInt inflateY(InflateData inflateData) {
		return inflateY(inflateData.getPos(), inflateData.getInflation());
	}

	public Point2DInt inflateY(int ypos, int inflation) {
		if (inflation % 2 != 0) {
			throw new IllegalArgumentException();
		}
		if (y < ypos) {
			return this;
		}
		if (y == ypos) {
			// throw new IllegalArgumentException();
			return translate(0, inflation / 2);
		}
		return translate(0, inflation);
	}

}
