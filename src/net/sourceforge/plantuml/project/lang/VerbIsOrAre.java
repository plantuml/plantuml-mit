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

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class VerbIsOrAre implements VerbPattern {

	public Collection<ComplementPattern> getComplements() {
		return Arrays
				.<ComplementPattern> asList(new ComplementClose(), new ComplementOpen(), new ComplementInColors2());
	}

	public IRegex toRegex() {
		return new RegexLeaf("(is|are)");
	}

	public Verb getVerb(final GanttDiagram project, final RegexResult arg) {
		return new Verb() {
			public CommandExecutionResult execute(Subject subject, Complement complement) {
				if (complement instanceof ComplementColors) {
					final HColor color = ((ComplementColors) complement).getCenter();
					return manageColor(project, subject, color);
				}
				if (complement == ComplementClose.CLOSE) {
					return manageClose(project, subject);
				}
				if (complement == ComplementOpen.OPEN) {
					return manageOpen(project, subject);
				}
				return CommandExecutionResult.error("assertion fail");
			}
		};
	}

	private CommandExecutionResult manageColor(final GanttDiagram project, Subject subject, HColor color) {
		if (subject instanceof Day) {
			final Day day = (Day) subject;
			project.colorDay(day, color);
		}
		if (subject instanceof DaysAsDates) {
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.colorDay(d, color);
			}
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult manageClose(final GanttDiagram project, Subject subject) {
		if (subject instanceof Day) {
			final Day day = (Day) subject;
			project.closeDayAsDate(day);
		}
		if (subject instanceof DaysAsDates) {
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.closeDayAsDate(d);
			}
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult manageOpen(final GanttDiagram project, Subject subject) {
		if (subject instanceof Day) {
			final Day day = (Day) subject;
			project.openDayAsDate(day);
		}
		if (subject instanceof DaysAsDates) {
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.openDayAsDate(d);
			}
		}
		return CommandExecutionResult.ok();
	}

}
