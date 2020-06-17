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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;

public class ComplementSeveralDays implements ComplementPattern {

	public IRegex toRegex(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("COMPLEMENT" + suffix, "(\\d+)[%s]+(day|week)s?" + //
						"(?:[%s]+and[%s]+(\\d+)[%s]+(day|week)s?)?" //
				)); //
	}

	public Failable<Complement> getComplement(GanttDiagram system, RegexResult arg, String suffix) {
		final String nb1 = arg.get("COMPLEMENT" + suffix, 0);
		final int factor1 = arg.get("COMPLEMENT" + suffix, 1).startsWith("w") ? system.daysInWeek() : 1;
		final int days1 = Integer.parseInt(nb1) * factor1;

		final String nb2 = arg.get("COMPLEMENT" + suffix, 2);
		int days2 = 0;
		if (nb2 != null) {
			final int factor2 = arg.get("COMPLEMENT" + suffix, 3).startsWith("w") ? system.daysInWeek() : 1;
			days2 = Integer.parseInt(nb2) * factor2;
		}

		return Failable.<Complement>ok(Load.inWinks(days1 + days2));
	}

}
