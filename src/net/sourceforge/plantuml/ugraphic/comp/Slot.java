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
package net.sourceforge.plantuml.ugraphic.comp;

public class Slot implements Comparable<Slot> {

	private final double start;
	private final double end;

	public Slot(double start, double end) {
		if (start >= end) {
			throw new IllegalArgumentException();
		}
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return "(" + start + "," + end + ")";
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}

	public double size() {
		return end - start;
	}

	public boolean contains(double v) {
		return v >= start && v <= end;
	}

	public boolean intersect(Slot other) {
		return contains(other.start) || contains(other.end) || other.contains(start) || other.contains(end);
	}

	public Slot merge(Slot other) {
		return new Slot(Math.min(start, other.start), Math.max(end, other.end));
	}

	public Slot intersect(double otherStart, double otherEnd) {
		if (otherStart >= end) {
			return null;
		}
		if (otherEnd <= start) {
			return null;
		}
		return new Slot(Math.max(start, otherStart), Math.min(end, otherEnd));
	}

	public int compareTo(Slot other) {
		if (this.start < other.start) {
			return -1;
		}
		if (this.start > other.start) {
			return 1;
		}
		return 0;
	}

}
