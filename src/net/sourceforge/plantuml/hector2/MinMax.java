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
package net.sourceforge.plantuml.hector2;

import java.util.Collection;

public class MinMax {

	private final int min;
	private final int max;

	private MinMax(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException();
		}
		this.min = min;
		this.max = max;
	}

	private MinMax(int value) {
		this(value, value);
	}

	public MinMax add(int value) {
		final int newMin = Math.min(min, value);
		final int newMax = Math.max(max, value);
		if (min == newMin && max == newMax) {
			return this;
		}
		return new MinMax(newMin, newMax);
	}

	public MinMax add(MinMax other) {
		final int newMin = Math.min(min, other.min);
		final int newMax = Math.max(max, other.max);
		if (min == newMin && max == newMax) {
			return this;
		}
		return new MinMax(newMin, newMax);
	}

	public final int getMin() {
		return min;
	}

	public final int getMax() {
		return max;
	}

	public static MinMax from(Collection<Integer> values) {
		MinMax result = null;
		for (Integer i : values) {
			if (result == null) {
				result = new MinMax(i);
			} else {
				result = result.add(i);
			}
		}
		return result;
	}

	public int getDiff() {
		return max - min;
	}

}
