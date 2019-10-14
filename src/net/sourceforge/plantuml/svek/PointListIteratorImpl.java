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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Log;

class PointListIteratorImpl implements PointListIterator {

	private final SvgResult svg;
	private int pos = 0;

	static PointListIterator create(SvgResult svg, int lineColor) {
		final PointListIteratorImpl result = new PointListIteratorImpl(svg);
		final int idx = svg.getIndexFromColor(lineColor);
		if (idx == -1) {
			result.pos = -1;
		}
		return result;
	}

	public PointListIterator cloneMe() {
		final PointListIteratorImpl result = new PointListIteratorImpl(svg);
		result.pos = this.pos;
		return result;
	}

	private PointListIteratorImpl(SvgResult svg) {
		this.svg = svg;
	}

	public boolean hasNext() {
		return true;
	}

	public List<Point2D.Double> next() {
		if (pos == -1) {
			return Collections.emptyList();
		}
		try {
			final List<Point2D.Double> result = svg.substring(pos).extractList(SvgResult.POINTS_EQUALS);
			pos = svg.indexOf(SvgResult.POINTS_EQUALS, pos) + SvgResult.POINTS_EQUALS.length() + 1;
			return result;
		} catch (StringIndexOutOfBoundsException e) {
			Log.error("Error " + e);
			return Collections.emptyList();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
