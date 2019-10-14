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
package net.sourceforge.plantuml.project3;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Solver3 {

	private final Map<TaskAttribute, Value> values = new LinkedHashMap<TaskAttribute, Value>();

	private final LoadPlanable loadPlanable;

	public Solver3(LoadPlanable loadPlanable) {
		this.loadPlanable = loadPlanable;
	}

	public void setData(TaskAttribute attribute, Value value) {
		final Value previous = values.remove(attribute);
		if (previous != null && attribute == TaskAttribute.START) {
			final Instant previousInstant = (Instant) previous;
			if (previousInstant.compareTo((Instant) value) > 0) {
				value = previous;
			}
		}
		values.put(attribute, value);
		if (values.size() > 2) {
			removeFirstElement();
		}
		assert values.size() <= 2;

	}

	private void removeFirstElement() {
		final Iterator<Entry<TaskAttribute, Value>> it = values.entrySet().iterator();
		it.next();
		it.remove();
	}

	public Value getData(TaskAttribute attribute) {
		Value result = values.get(attribute);
		if (result == null) {
			if (attribute == TaskAttribute.END) {
				return computeEnd();
			}
			if (attribute == TaskAttribute.START) {
				return computeStart();
			}
			return LoadInDays.inDay(1);
			// throw new UnsupportedOperationException(attribute.toString());
		}
		return result;
	}

	private Instant computeEnd() {
		Instant current = (Instant) values.get(TaskAttribute.START);
		int fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getFullLoad();
		while (fullLoad > 0) {
			fullLoad -= loadPlanable.getLoadAt(current);
			current = current.increment();
		}
		return current.decrement();
	}

	private Instant computeStart() {
		Instant current = (Instant) values.get(TaskAttribute.END);
		int fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getFullLoad();
		while (fullLoad > 0) {
			fullLoad -= loadPlanable.getLoadAt(current);
			current = current.decrement();
		}
		return current.increment();
	}

}
