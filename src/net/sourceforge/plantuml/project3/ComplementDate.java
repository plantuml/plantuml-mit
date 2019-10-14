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
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class ComplementDate implements ComplementPattern {

	public IRegex toRegex(String suffix) {
		return new RegexOr(toRegexA(suffix), toRegexB(suffix), toRegexC(suffix), toRegexD(suffix));
	}

	private IRegex toRegexA(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("ADAY" + suffix, "([\\d]+)"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf("AMONTH" + suffix, "(" + Month.getRegexString() + ")"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf("AYEAR" + suffix, "([\\d]{4})"));
	}

	private IRegex toRegexB(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("BYEAR" + suffix, "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("BMONTH" + suffix, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("BDAY" + suffix, "([\\d]{1,2})"));
	}

	private IRegex toRegexC(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("CMONTH" + suffix, "(" + Month.getRegexString() + ")"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf("CDAY" + suffix, "([\\d]+)"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf("CYEAR" + suffix, "([\\d]{4})"));
	}

	private IRegex toRegexD(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("DCOUNT" + suffix, "([\\d]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("days?"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("after"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("start") //
				);
	}

	public Failable<Complement> getComplement(GanttDiagram system, RegexResult arg, String suffix) {
		if (arg.get("ADAY" + suffix, 0) != null) {
			return Failable.<Complement> ok(resultA(arg, suffix));
		}
		if (arg.get("BDAY" + suffix, 0) != null) {
			return Failable.<Complement> ok(resultB(arg, suffix));
		}
		if (arg.get("CDAY" + suffix, 0) != null) {
			return Failable.<Complement> ok(resultC(arg, suffix));
		}
		if (arg.get("DCOUNT" + suffix, 0) != null) {
			return Failable.<Complement> ok(resultD(system, arg, suffix));
		}
		throw new IllegalStateException();
	}

	private Complement resultD(GanttDiagram system, RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("DCOUNT" + suffix, 0));
		return system.getStartingDate(day);
	}

	private Complement resultA(RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("ADAY" + suffix, 0));
		final String month = arg.get("AMONTH" + suffix, 0);
		final int year = Integer.parseInt(arg.get("AYEAR" + suffix, 0));
		return DayAsDate.create(year, month, day);
	}

	private Complement resultB(RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("BDAY" + suffix, 0));
		final int month = Integer.parseInt(arg.get("BMONTH" + suffix, 0));
		final int year = Integer.parseInt(arg.get("BYEAR" + suffix, 0));
		return DayAsDate.create(year, month, day);
	}

	private Complement resultC(RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("CDAY" + suffix, 0));
		final String month = arg.get("CMONTH" + suffix, 0);
		final int year = Integer.parseInt(arg.get("CYEAR" + suffix, 0));
		return DayAsDate.create(year, month, day);
	}
}
