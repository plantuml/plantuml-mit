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
package net.sourceforge.plantuml.mindmap;

public class Stripe implements Comparable<Stripe> {

	private final double x1;
	private final double x2;
	private final double value;

	@Override
	public String toString() {
		return "" + (int) x1 + "->" + (int) x2 + " (" + (int) value + ")";
	}

	public Stripe(double x1, double x2, double value) {
		if (x2 <= x1) {
			System.err.println("x1=" + x1);
			System.err.println("x2=" + x2);
			throw new IllegalArgumentException();
		}
		this.x1 = x1;
		this.x2 = x2;
		this.value = value;
	}

	public boolean contains(double x) {
		return x >= x1 && x <= x2;
	}

	public int compareTo(Stripe other) {
		return (int) Math.signum(this.x1 - other.x1);
	}

	public double getValue() {
		return value;
	}

	public final double getStart() {
		return x1;
	}

	public final double getEnd() {
		return x2;
	}

}
