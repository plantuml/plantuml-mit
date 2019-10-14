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
package net.sourceforge.plantuml.project3;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class Arrows {

	final static private double delta2 = 4;

	private static UPolygon asToUp() {
		final UPolygon polygon = new UPolygon("asToUp");
		polygon.addPoint(-delta2, 0);
		polygon.addPoint(0, 0);
		polygon.addPoint(delta2, 0);
		polygon.addPoint(0, -4);
		return polygon;
	}

	private static UPolygon asToDown() {
		final UPolygon polygon = new UPolygon("asToDown");
		polygon.addPoint(-delta2, 0);
		polygon.addPoint(0, 0);
		polygon.addPoint(delta2, 0);
		polygon.addPoint(0, 4);
		return polygon;
	}

	private static UPolygon asToRight() {
		final UPolygon polygon = new UPolygon("asToRight");
		polygon.addPoint(0, -delta2);
		polygon.addPoint(0, 0);
		polygon.addPoint(0, delta2);
		polygon.addPoint(4, 0);
		return polygon.translate(-4, 0);
	}

	private static UPolygon asToLeft() {
		final UPolygon polygon = new UPolygon("asToLeft");
		polygon.addPoint(0, -delta2);
		polygon.addPoint(0, 0);
		polygon.addPoint(0, delta2);
		polygon.addPoint(-4, 0);
		return polygon.translate(4, 0);
	}

	public static UPolygon asTo(Direction direction) {
		if (direction == Direction.UP) {
			return asToUp();
		}
		if (direction == Direction.DOWN) {
			return asToDown();
		}
		if (direction == Direction.LEFT) {
			return asToLeft();
		}
		if (direction == Direction.RIGHT) {
			return asToRight();
		}
		throw new IllegalArgumentException();
	}

}
