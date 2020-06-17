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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolylineBreakeable extends AbstractPolyline implements Polyline {

	static class Breakure {
		private int d;
		private int u;

		public Breakure(int u, int d) {
			this.u = u;
			this.d = d;
		}
	}

	private final List<Breakure> breakures = new ArrayList<Breakure>();

	public PolylineBreakeable copy(Pointable newStart, Pointable newEnd) {
		final PolylineBreakeable result = new PolylineBreakeable(newStart, newEnd);
		result.breakures.addAll(this.breakures);
		return result;
	}

	public PolylineBreakeable(Pointable start, Pointable end) {
		super(start, end);
	}

	public List<LineSegmentInt> segments() {
		if (breakures.size() == 0) {
			return Collections.singletonList(new LineSegmentInt(getStart().getPosition(), getEnd().getPosition()));
		}
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		Point2DInt cur = getStart().getPosition();
		for (Breakure breakure : breakures) {
			final Point2DInt next = getBreakurePoint(breakure);
			result.add(new LineSegmentInt(cur, next));
			cur = next;
		}
		result.add(new LineSegmentInt(cur, getEnd().getPosition()));
		assert nbSegments() == result.size();
		return Collections.unmodifiableList(result);
	}

	private Point2DInt getBreakurePoint(Breakure breakure) {
		final LineSegmentInt seg = new LineSegmentInt(getStart().getPosition(), getEnd().getPosition());
		return seg.ortho(seg.startTranslatedAsVector(breakure.u), breakure.d);
	}

	public int nbSegments() {
		return breakures.size() + 1;
	}

	public List<XMoveable> getFreedoms() {
		final List<XMoveable> allFreedom = new ArrayList<XMoveable>();

		for (final Breakure breakure : breakures) {
			allFreedom.add(new XMoveable() {
				@Override
				public String toString() {
					return super.toString() + " " + PolylineBreakeable.this.toString() + "(d)";
				}

				public void moveX(int delta) {
					breakure.d += delta;
				}
			});
			allFreedom.add(new XMoveable() {
				@Override
				public String toString() {
					return super.toString() + " " + PolylineBreakeable.this.toString() + "(u)";
				}

				public void moveX(int delta) {
					breakure.u += delta;
				}
			});
			allFreedom.add(new XMoveable() {
				@Override
				public String toString() {
					return super.toString() + " " + PolylineBreakeable.this.toString() + "(ud)";
				}

				public void moveX(int delta) {
					breakure.u += delta;
					breakure.d += delta;
				}
			});
			allFreedom.add(new XMoveable() {
				@Override
				public String toString() {
					return super.toString() + " " + PolylineBreakeable.this.toString() + "(dud)";
				}

				public void moveX(int delta) {
					breakure.u += delta;
					breakure.d -= delta;
				}
			});
		}

		return Collections.unmodifiableList(allFreedom);
	}

	public void insertBetweenPoint(int u, int d) {
		breakures.add(new Breakure(u, d));
	}

	private void breakMore() {
		if (breakures.size() == 1) {
			final Breakure b = breakures.get(0);
			insertBetweenPoint(b.u / 2, 0);
		}

	}

}
