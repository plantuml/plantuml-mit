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
package net.sourceforge.plantuml.graph2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SortedListImpl<V> implements SortedList<V> {

	static class NullableAndEvenMeasurer<V> implements Measurer<V> {
		private final Measurer<V> wrapped;
		private final int valueForNull;

		NullableAndEvenMeasurer(Measurer<V> wrapped, int valueForNull, boolean plus) {
			this.wrapped = wrapped;
			if (plus) {
				this.valueForNull = valueForNull * 2 + 1;
			} else {
				this.valueForNull = valueForNull * 2 - 1;
			}
		}

		public int getMeasure(V data) {
			if (data == null) {
				return valueForNull;
			}
			return wrapped.getMeasure(data) * 2;
		}
	}

	private final Measurer<V> measurer;
	private final List<V> all = new ArrayList<V>();
	private final Comparator<V> comparator;

	public SortedListImpl(Measurer<V> m) {
		this.measurer = m;
		this.comparator = new Comparator<V>() {
			public int compare(V o1, V o2) {
				final int v1 = measurer.getMeasure(o1);
				final int v2 = measurer.getMeasure(o2);
				return v1 - v2;
			}
		};
	}

	public void add(V data) {
		final int pos = Collections.binarySearch(all, data, comparator);
		if (pos >= 0) {
			all.add(pos, data);
		} else {
			all.add(-pos - 1, data);
		}
		assert isSorted();
	}

	private int getPos(int v, boolean plus) {
		final Measurer<V> m = new NullableAndEvenMeasurer<V>(measurer, v, plus);
		final Comparator<V> myComp = new Comparator<V>() {
			public int compare(V o1, V o2) {
				final int v1 = m.getMeasure(o1);
				final int v2 = m.getMeasure(o2);
				return v1 - v2;
			}
		};
		final int pos = Collections.binarySearch(all, null, myComp);
		assert pos < 0;
		return -pos - 1;
	}

	public List<V> lesserOrEquals(int v) {
		return all.subList(0, getPos(v, true));
	}

	public List<V> biggerOrEquals(int v) {
		return all.subList(getPos(v, false), all.size());
	}

	private boolean isSorted() {
		for (int i = 0; i < all.size() - 1; i++) {
			final int v1 = measurer.getMeasure(all.get(i));
			final int v2 = measurer.getMeasure(all.get(i + 1));
			if (v1 > v2) {
				return false;
			}
		}
		return true;
	}

	public Iterator<V> iterator() {
		return all.iterator();
	}

}
