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
package net.sourceforge.plantuml.hector;

class UnlinearCompression {

	static enum Rounding {
		BORDER_1, CENTRAL, BORDER_2;
	}

	private final double inner;
	private final double outer;

	public UnlinearCompression(double inner, double outer) {
		this.inner = inner;
		this.outer = outer;
	}

	public double compress(double x) {
		final double pour = x / (inner + outer);
		final double pourInter = Math.floor(pour);
		x -= pourInter * (inner + outer);
		if (x < inner) {
			return pourInter * outer;
		}
		return x - inner + pourInter * outer;
	}

	public double uncompress(double x, Rounding rounding) {
		final int pourInter = nbOuterBefore(x);
		final boolean onBorder = equals(x, pourInter * outer);
		if (onBorder && rounding == Rounding.BORDER_1) {
			// Nothing
		} else if (onBorder && rounding == Rounding.CENTRAL) {
			x += inner / 2.0;
		} else {
			x += inner;
		}
		x += pourInter * inner;
		return x;
	}

	private static boolean equals(double d1, double d2) {
		return Math.abs(d1 - d2) < .001;
	}

	private int nbOuterBefore(double x) {
		final double pour = x / outer;
		final int pourInter = (int) Math.floor(pour);
		return pourInter;
	}

	public double[] encounteredSingularities(double from, double to) {
		final int outer1 = nbOuterBefore(from) + 1;
		int outer2 = nbOuterBefore(to) + 1;
		if (equals(to, (outer2 - 1) * outer)) {
			outer2--;
		}
		final double result[];
		if (from <= to) {
			result = new double[outer2 - outer1];
			for (int i = 0; i < result.length; i++) {
				result[i] = (outer1 + i) * outer;
			}
		} else {
			result = new double[outer1 - outer2];
			for (int i = 0; i < result.length; i++) {
				result[i] = (outer1 - 1 - i) * outer;
			}

		}
		return result;
	}

	public double innerSize() {
		return inner;
	}

}
