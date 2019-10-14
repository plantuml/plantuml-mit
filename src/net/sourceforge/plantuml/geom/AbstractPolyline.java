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
package net.sourceforge.plantuml.geom;

import java.awt.geom.GeneralPath;

abstract class AbstractPolyline implements Polyline {

	private final Pointable start;
	private final Pointable end;

	public AbstractPolyline(Pointable start, Pointable end) {
		this.start = start;
		this.end = end;
	}

	@Override
	final public String toString() {
		return segments().toString();
	}

	final public boolean doesTouch(Polyline other) {
		final boolean result = doesTouchInternal(other);
		assert result == ((AbstractPolyline) other).doesTouchInternal(this);
		return result;
	}

	private boolean doesTouchInternal(Polyline other) {
		for (int i = 0; i < nbSegments(); i++) {
			final LineSegmentInt seg1 = segments().get(i);
			for (int j = 0; j < other.nbSegments(); j++) {
				final LineSegmentInt seg2 = other.segments().get(j);
				final boolean ignoreExtremities = i == 0 || i == nbSegments() - 1 || j == 0
						|| j == other.nbSegments() - 1;
				if (ignoreExtremities == false && seg1.doesIntersect(seg2)) {
					return true;
				}
				if (ignoreExtremities && seg1.doesIntersectButNotSameExtremity(seg2)) {
					return true;
				}
			}
		}
		return false;
	}

	final public LineSegmentInt getFirst() {
		return segments().get(0);
	}

	final public LineSegmentInt getLast() {
		return segments().get(nbSegments() - 1);
	}

	final public double getLength() {
		double result = 0;
		for (LineSegmentInt seg : segments()) {
			result += seg.getLength();
		}
		return result;
	}

	final public Point2DInt clipStart(Box box) {
		assert box.doesIntersect(segments().get(0));
		final Point2DInt inter[] = box.intersect(segments().get(0));
		assert inter.length == 1;
		segments().set(
				0,
				new LineSegmentInt(inter[0].getXint(), inter[0].getYint(), segments().get(0).getP2().getXint(),
						segments().get(0).getP2().getYint()));
		return inter[0];
	}

	final public Point2DInt clipEnd(Box box) {
		final int last = nbSegments() - 1;
		if (last == -1) {
			return null;
		}
		assert box.doesIntersect(segments().get(last));
		final Point2DInt inter[] = box.intersect(segments().get(last));
		assert inter.length == 1;
		segments().set(
				last,
				new LineSegmentInt(segments().get(last).getP1().getXint(), segments().get(last).getP1().getYint(),
						inter[0].getXint(), inter[0].getYint()));
		return inter[0];
	}

	final public boolean intersectBox(Box b) {
		for (LineSegmentInt seg : segments()) {
			if (b.doesIntersect(seg)) {
				return true;
			}
		}
		return false;
	}

	final public double getDistance(Box b) {
		double result = Double.MAX_VALUE;
		for (LineSegmentInt seg : segments()) {
			if (b.doesIntersect(seg)) {
				result = Math.min(result, seg.getDistance(b.getCenterPoint()));
			}
		}
		return result;
	}

	final public double getDistance(Polyline other) {
		double result = 0;
		for (LineSegmentInt seg1 : segments()) {
			for (LineSegmentInt seg2 : other.segments()) {
				result += seg1.getDistance(seg2);
			}
		}
		return result;
	}

	final public GeneralPath asGeneralPath() {
		final GeneralPath generalPath = new GeneralPath();

		for (LineSegmentInt seg : segments()) {
			generalPath.append(seg, false);
		}

		return generalPath;
	}

	final public int getMinX() {
		int result = Integer.MAX_VALUE;
		for (LineSegmentInt seg : segments()) {
			result = Math.min(result, seg.getMinX());
		}
		return result;
	}

	final public int getMinY() {
		int result = Integer.MAX_VALUE;
		for (LineSegmentInt seg : segments()) {
			result = Math.min(result, seg.getMinY());
		}
		return result;
	}

	final public int getMaxX() {
		int result = Integer.MIN_VALUE;
		for (LineSegmentInt seg : segments()) {
			result = Math.max(result, seg.getMaxX());
		}
		return result;
	}

	final public int getMaxY() {
		int result = Integer.MIN_VALUE;
		for (LineSegmentInt seg : segments()) {
			result = Math.max(result, seg.getMaxY());
		}
		return result;
	}

	public final Pointable getStart() {
		return start;
	}

	public final Pointable getEnd() {
		return end;
	}

}
