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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Segment {

	final private double pos1;
	final private double pos2;

	public Segment(double pos1, double pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		if (pos2 < pos1) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean equals(Object obj) {
		final Segment this2 = (Segment) obj;
		return pos1 == this2.pos1 && pos2 == this2.pos2;
	}

	@Override
	public int hashCode() {
		return new Double(pos1).hashCode() + new Double(pos2).hashCode();
	}

	final public boolean contains(double y) {
		return y >= pos1 && y <= pos2;
	}

	final public boolean contains(Segment other) {
		return contains(other.pos1) && contains(other.pos2);
	}

	@Override
	public String toString() {
		return "" + pos1 + " - " + pos2;
	}

	final public double getLength() {
		return pos2 - pos1;
	}

	final public double getPos1() {
		return pos1;
	}

	final public double getPos2() {
		return pos2;
	}

	public Segment merge(Segment this2) {
		return new Segment(Math.min(this.pos1, this2.pos1), Math.max(this.pos2, this2.pos2));
	}

	public Collection<Segment> cutSegmentIfNeed(Collection<Segment> allDelays) {
		final List<Segment> sortedDelay = new ArrayList<Segment>(allDelays);
		Collections.sort(sortedDelay, new SortPos1());
		final List<Segment> result2 = new ArrayList<Segment>();
		double pendingStart = pos1;
		for (Segment d : sortedDelay) {
			if (d.pos1 <= pendingStart) {
				continue;
			}
			if (d.pos1 > this.pos2) {
				result2.add(new Segment(pendingStart, this.pos2));
				return Collections.unmodifiableCollection(result2);
			}
			// if (this.contains(d) == false) {
			// throw new IllegalStateException();
			// }
			if (this.contains(d)) {
				result2.add(new Segment(pendingStart, d.pos1));
				pendingStart = d.pos2;
			}
		}
		result2.add(new Segment(pendingStart, this.pos2));
		return Collections.unmodifiableCollection(result2);
	}

	static class SortPos1 implements Comparator<Segment> {
		public int compare(Segment segA, Segment segB) {
			return (int) Math.signum(segA.pos1 - segB.pos1);
		}
	}

}
