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

import java.util.Date;

import net.sourceforge.plantuml.project.lang.Complement;
import net.sourceforge.plantuml.project.lang.Subject;

public class Day implements Complement, Comparable<Day>, Subject {

	private final int dayOfMonth;
	private final MonthYear monthYear;

	public static Day create(int year, String month, int dayOfMonth) {
		return new Day(year, Month.fromString(month), dayOfMonth);
	}

	public static Day create(int year, int month, int dayOfMonth) {
		return new Day(year, Month.values()[month - 1], dayOfMonth);
	}

	public static Day today() {
		final Date now = new Date();
		final int year = now.getYear() + 1900;
		final int month = now.getMonth() + 1;
		final int dayOfMonth = now.getDate();
		return create(year, month, dayOfMonth);
	}

	private Day(int year, Month month, int dayOfMonth) {
		this(MonthYear.create(year, month), dayOfMonth);
	}

	private Day(MonthYear monthYear, int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
		this.monthYear = monthYear;
	}

	public int year() {
		return monthYear.year();
	}

	private int internalNumber() {
		return year() * 100 * 100 + month().ordinal() * 100 + dayOfMonth;
	}

	@Override
	public String toString() {
		return monthYear.toString() + "/" + dayOfMonth;
	}

	@Override
	public int hashCode() {
		return monthYear.hashCode() + dayOfMonth * 17;
	}

	@Override
	public boolean equals(Object obj) {
		final Day other = (Day) obj;
		return other.internalNumber() == this.internalNumber();
	}

	public final int getDayOfMonth() {
		return dayOfMonth;
	}

	private int daysPerMonth() {
		return month().getDaysPerMonth(year());
	}

	public Day next() {
		int newDayOfMonth = dayOfMonth + 1;
		if (newDayOfMonth <= daysPerMonth()) {
			return new Day(year(), month(), newDayOfMonth);
		}
		assert newDayOfMonth > daysPerMonth();
		return new Day(monthYear.next(), 1);
	}

	public Month month() {
		return monthYear.month();
	}

	public MonthYear monthYear() {
		return monthYear;
	}

	// https://en.wikipedia.org/wiki/Zeller%27s_congruence
	public DayOfWeek getDayOfWeek() {
		final int q = dayOfMonth;
		final int m = month().m();
		final int y = m >= 13 ? year() - 1 : year();
		final int k = y % 100;
		final int j = y / 100;
		final int h = ((q + 13 * (m + 1) / 5) + k + k / 4 + j / 4 + 5 * j) % 7;
		return DayOfWeek.fromH(h);
	}

	public static int DOW(int y_, int m_, int d_) {
		final int q = d_;
		final int m = 3 + (m_ - 1 + 10) % 12;
		final int y = m >= 13 ? y_ - 1 : y_;
		final int k = y % 100;
		final int j = y / 100;
		final int h = ((q + 13 * (m + 1) / 5) + k + k / 4 + j / 4 + 5 * j) % 7;
		return h;
	}

	public Wink asInstantDay(Day reference) {
		// if (this.compareTo(reference) < 0) {
		// throw new IllegalArgumentException();
		// }
		int cmp = 0;
		Day current = reference;
		while (current.compareTo(this) < 0) {
			current = current.next();
			cmp++;
		}
		return new Wink(cmp);
	}

	// https://www.staff.science.uu.nl/~gent0113/calendar/isocalendar_text_5.htm
	// https://en.wikipedia.org/wiki/ISO_week_date
	// http://www.proesite.com/timex/wkcalc.htm
	public int ISO_WN() {
		final int y = year();
		int m = month().ordinal() + 1;
		int d = dayOfMonth;
		int dow = DOW(y, m, d);
		int dow0101 = DOW(y, 1, 1);

		if (m == 1 && 3 < dow0101 && dow0101 < 7 - (d - 1)) {
			// days before week 1 of the current year have the same week number as
			// the last day of the last week of the previous year

			dow = dow0101 - 1;
			dow0101 = DOW(y - 1, 1, 1);
			m = 12;
			d = 31;
		} else if (m == 12 && 30 - (d - 1) < DOW(y + 1, 1, 1) && DOW(y + 1, 1, 1) < 4) {
			// days after the last week of the current year have the same week number as
			// the first day of the next year, (i.e. 1)

			return 1;
		}

		return (DOW(y, 1, 1) < 4) ? 1 : 0 + 4 * (m - 1) + (2 * (m - 1) + (d - 1) + dow0101 - dow + 6) * 36 / 256;

	}

	public int compareTo(Day other) {
		return this.internalNumber() - other.internalNumber();
	}

}
