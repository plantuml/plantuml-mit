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

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class ComplementDates implements ComplementPattern {

	public IRegex toRegex(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("YEAR1" + suffix, "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH1" + suffix, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY1" + suffix, "([\\d]{1,2})"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("to"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("YEAR2" + suffix, "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH2" + suffix, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY2" + suffix, "([\\d]{1,2})") //

		);
	}

	public Subject getSubject(GanttDiagram project, RegexResult arg) {
		final DayAsDate date1 = getDate(arg, "1");
		final DayAsDate date2 = getDate(arg, "2");
		return new DaysAsDates(date1, date2);
	}

	private DayAsDate getDate(RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("DAY" + suffix, 0));
		final int month = Integer.parseInt(arg.get("MONTH" + suffix, 0));
		final int year = Integer.parseInt(arg.get("YEAR" + suffix, 0));
		return DayAsDate.create(year, month, day);
	}

	public Failable<Complement> getComplement(GanttDiagram system, RegexResult arg, String suffix) {

		final int day1 = Integer.parseInt(arg.get("DAY1" + suffix, 0));
		final int month1 = Integer.parseInt(arg.get("MONTH1" + suffix, 0));
		final int year1 = Integer.parseInt(arg.get("YEAR1" + suffix, 0));
		final DayAsDate date1 = DayAsDate.create(year1, month1, day1);

		final int day2 = Integer.parseInt(arg.get("DAY2" + suffix, 0));
		final int month2 = Integer.parseInt(arg.get("MONTH2" + suffix, 0));
		final int year2 = Integer.parseInt(arg.get("YEAR2" + suffix, 0));
		final DayAsDate date2 = DayAsDate.create(year2, month2, day2);

		return Failable.<Complement> ok(new DaysAsDates(date1, date2));
	}

}
