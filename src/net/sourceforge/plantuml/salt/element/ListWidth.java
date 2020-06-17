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
package net.sourceforge.plantuml.salt.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListWidth {

	private final List<Double> allWidth = new ArrayList<Double>();

	public void add(double width) {
		this.allWidth.add(width);
	}

	public ListWidth mergeMax(ListWidth other) {
		final ListWidth result = new ListWidth();
		for (int i = 0; i < this.allWidth.size() || i < other.allWidth.size(); i++) {
			final double w1 = this.getWidthSafe(i);
			final double w2 = other.getWidthSafe(i);
			result.add(Math.max(w1, w2));
		}
		return result;
	}

	private double getWidthSafe(int i) {
		if (i < allWidth.size()) {
			return allWidth.get(i);
		}
		return 0;
	}

	public double getTotalWidthWithMargin(final double margin) {
		double result = 0;
		for (Double w : allWidth) {
			if (result > 0) {
				result += margin;
			}
			result += w;
		}
		return result;
	}

	public Iterator<Double> iterator() {
		return allWidth.iterator();
	}

}
