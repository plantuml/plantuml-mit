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
package net.sourceforge.plantuml.geom.kinetic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Quadrant {

	static final private int SIZE = 100;

	private final int x;
	private final int y;

	public Quadrant(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Quadrant(Point2DCharge pt) {
		this((int) pt.getX() / SIZE, (int) pt.getY() / SIZE);
	}

	@Override
	public boolean equals(Object obj) {
		final Quadrant other = (Quadrant) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public int hashCode() {
		return x * 3571 + y;
	}

	@Override
	public String toString() {
		return "" + x + "-" + y;
	}

	public Collection<Quadrant> neighbourhood() {
		final Collection<Quadrant> result = Arrays.asList(new Quadrant(x - 1, y - 1), new Quadrant(x, y - 1),
				new Quadrant(x + 1, y - 1), new Quadrant(x - 1, y), this, new Quadrant(x + 1, y), new Quadrant(x - 1,
						y + 1), new Quadrant(x, y + 1), new Quadrant(x + 1, y + 1));
		assert new HashSet<Quadrant>(result).size() == 9;
		return result;
	}

}
