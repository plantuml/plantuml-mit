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

import net.sourceforge.plantuml.StringUtils;

public enum Month {

	JANUARY(31), FEBRUARY(28), MARCH(31), APRIL(30), MAY(31), JUNE(30), //
	JULY(31), AUGUST(31), SEPTEMBER(30), OCTOBER(31), NOVEMBER(30), DECEMBER(31);

	private final int daysPerMonth;

	private Month(int daysPerMonth) {
		this.daysPerMonth = daysPerMonth;
	}

	public String shortName() {
		return longName().substring(0, 3);
	}

	public String longName() {
		return StringUtils.capitalize(name());
	}

	static public String getRegexString() {
		final StringBuilder sb = new StringBuilder();
		for (Month month : Month.values()) {
			if (sb.length() > 0) {
				sb.append("|");
			}
			sb.append(month.name().substring(0, 3) + "[a-z]*");
		}
		return sb.toString();
	}

	public static Month fromString(String value) {
		value = StringUtils.goUpperCase(value).substring(0, 3);
		for (Month m : Month.values()) {
			if (m.name().startsWith(value)) {
				return m;
			}
		}
		throw new IllegalArgumentException();
	}

	public final int getDaysPerMonth(int year) {
		if (this == FEBRUARY && year % 4 == 0) {
			return 29;
		}
		return daysPerMonth;
	}

	public Month next() {
		return Month.values()[(this.ordinal() + 1) % 12];
	}

	public int m() {
		return 3 + (ordinal() + 10) % 12;
	}

}
