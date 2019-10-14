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

public class DaysAsDates implements Subject, Complement, Iterable<DayAsDate> {

	private final DayAsDate date1;
	private final DayAsDate date2;

	public DaysAsDates(DayAsDate date1, DayAsDate date2) {
		this.date1 = date1;
		this.date2 = date2;
	}

	public DaysAsDates(GanttDiagram gantt, DayAsDate date1, int count) {
		this.date1 = date1;
		DayAsDate tmp = date1;
		while (count > 0) {
			if (gantt.isOpen(tmp)) {
				count--;
			}
			tmp = tmp.next();
		}
		this.date2 = tmp;
	}

	class MyIterator implements Iterator<DayAsDate> {

		private DayAsDate current;

		public MyIterator(DayAsDate current) {
			this.current = current;
		}

		public boolean hasNext() {
			return current.compareTo(date2) <= 0;
		}

		public DayAsDate next() {
			final DayAsDate result = current;
			current = current.next();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public Iterator<DayAsDate> iterator() {
		return new MyIterator(date1);
	}

}
