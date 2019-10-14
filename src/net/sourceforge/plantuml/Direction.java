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
package net.sourceforge.plantuml;

import java.awt.geom.Point2D;

public enum Direction {
	RIGHT, LEFT, DOWN, UP;

	public Direction getInv() {
		if (this == RIGHT) {
			return LEFT;
		}
		if (this == LEFT) {
			return RIGHT;
		}
		if (this == DOWN) {
			return UP;
		}
		if (this == UP) {
			return DOWN;
		}
		throw new IllegalStateException();
	}
	
	public String getShortCode() {
		return name().substring(0, 1);
	}

	public static Direction fromChar(char c) {
		if (c == '<') {
			return Direction.LEFT;
		}
		if (c == '>') {
			return Direction.RIGHT;
		}
		if (c == '^') {
			return Direction.UP;
		}
		return Direction.DOWN;
	}

	public Direction clockwise() {
		if (this == RIGHT) {
			return DOWN;
		}
		if (this == LEFT) {
			return UP;
		}
		if (this == DOWN) {
			return LEFT;
		}
		if (this == UP) {
			return RIGHT;
		}
		throw new IllegalStateException();
	}

	public static Direction leftOrRight(Point2D p1, Point2D p2) {
		if (p1.getX() < p2.getX()) {
			return Direction.LEFT;
		}
		if (p1.getX() > p2.getX()) {
			return Direction.RIGHT;
		}
		throw new IllegalArgumentException();
	}

	public static Direction fromVector(Point2D p1, Point2D p2) {
		final double x1 = p1.getX();
		final double y1 = p1.getY();
		final double x2 = p2.getX();
		final double y2 = p2.getY();
		if (x1 == x2 && y1 == y2) {
			return null;
		}
		if (x1 == x2) {
			if (y2 > y1) {
				return Direction.DOWN;
			}
			return Direction.UP;
		}
		if (y1 == y2) {
			if (x2 > x1) {
				return Direction.RIGHT;
			}
			return Direction.LEFT;
		}
		throw new IllegalArgumentException("Not a H or V line!");

	}
}
