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
package net.sourceforge.plantuml.golem;

public class Position {

	private final int xmin;
	private final int ymin;
	private final int xmax;
	private final int ymax;

	public Position(int xmin, int ymin, int xmax, int ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	@Override
	public boolean equals(Object o) {
		final Position other = (Position) o;
		return this.xmin == other.xmin && this.xmax == other.xmax && this.ymin == other.ymin && this.ymax == other.ymax;
	}

	@Override
	public int hashCode() {
		return xmin + ymin << 8 + xmax << 16 + ymax << 24;
	}

	@Override
	public String toString() {
		return "(" + xmin + "," + ymin + ")-(" + xmax + "," + ymax + ")";
	}

	public Position move(TileGeometry position, int sizeMove) {
		if (position == null || position == TileGeometry.CENTER) {
			throw new IllegalArgumentException();
		}
		switch (position) {
		case NORTH:
			return new Position(xmin, ymin - sizeMove, xmax, ymax - sizeMove);
		case SOUTH:
			return new Position(xmin, ymin + sizeMove, xmax, ymax + sizeMove);
		case WEST:
			return new Position(xmin - sizeMove, ymin, xmax - sizeMove, ymax);
		case EAST:
			return new Position(xmin + sizeMove, ymin, xmax + sizeMove, ymax);
		default:
			throw new IllegalStateException();
		}
	}

	public int getXmin() {
		return xmin;
	}

	public int getXmax() {
		return xmax;
	}

	public int getYmin() {
		return ymin;
	}

	public int getYmax() {
		return ymax;
	}

	public int getCenterX() {
		if ((xmin + xmax + 1) % 2 != 0) {
			throw new IllegalStateException();
		}
		return (xmin + xmax + 1) / 2;
	}

	public int getCenterY() {
		if ((ymin + ymax + 1) % 2 != 0) {
			throw new IllegalStateException();
		}
		return (ymin + ymax + 1) / 2;
	}
}
