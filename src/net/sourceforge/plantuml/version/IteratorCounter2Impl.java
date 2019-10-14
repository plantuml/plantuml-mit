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
package net.sourceforge.plantuml.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringLocated;

public class IteratorCounter2Impl implements IteratorCounter2 {

	private List<StringLocated> data;
	private List<StringLocated> trace;
	private int nb;

	public void copyStateFrom(IteratorCounter2 other) {
		final IteratorCounter2Impl source = (IteratorCounter2Impl) other;
		this.nb = source.nb;
		this.data = source.data;
		this.trace = source.trace;
	}

	public IteratorCounter2Impl(List<StringLocated> data) {
		this(data, 0, new ArrayList<StringLocated>());
	}

	private IteratorCounter2Impl(List<StringLocated> data, int nb, List<StringLocated> trace) {
		this.data = data;
		this.nb = nb;
		this.trace = trace;
	}

	public IteratorCounter2 cloneMe() {
		return new IteratorCounter2Impl(data, nb, new ArrayList<StringLocated>(trace));
	}

	public int currentNum() {
		return nb;
	}

	public boolean hasNext() {
		return nb < data.size();
	}

	public StringLocated next() {
		final StringLocated result = data.get(nb);
		nb++;
		trace.add(result);
		return result;
	}

	public StringLocated peek() {
		return data.get(nb);
	}

	public StringLocated peekPrevious() {
		if (nb == 0) {
			return null;
		}
		return data.get(nb - 1);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public final List<StringLocated> getTrace() {
		return Collections.unmodifiableList(trace);
	}

}
