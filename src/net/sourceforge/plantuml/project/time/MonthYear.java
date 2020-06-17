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
package net.sourceforge.plantuml.project.time;

public class MonthYear implements Comparable<MonthYear> {

	private final int year;
	private final Month month;

	public static MonthYear create(int year, Month month) {
		return new MonthYear(year, month);
	}

	public String shortName() {
		return month.shortName();
	}

	public String shortNameYYYY() {
		return month.shortName() + " " + year;
	}

	public String longName() {
		return month.longName();
	}

	public String longNameYYYY() {
		return month.longName() + " " + year;
	}

	private MonthYear(int year, Month month) {
		this.year = year;
		this.month = month;
	}

	public int year() {
		return year;
	}

	public MonthYear next() {
		final Month newMonth = month.next();
		final int newYear = newMonth == Month.JANUARY ? year + 1 : year;
		return new MonthYear(newYear, newMonth);
	}

	public Month month() {
		return month;
	}

	private int internalNumber() {
		return year * 100 + month.ordinal();
	}

	@Override
	public String toString() {
		return "" + year + "/" + month;
	}

	@Override
	public int hashCode() {
		return year * 113 + month.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		final MonthYear other = (MonthYear) obj;
		return other.internalNumber() == this.internalNumber();
	}

	public int compareTo(MonthYear other) {
		return this.internalNumber() - other.internalNumber();
	}

}
