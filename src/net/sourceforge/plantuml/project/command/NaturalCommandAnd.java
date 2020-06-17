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
package net.sourceforge.plantuml.project.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.lang.Complement;
import net.sourceforge.plantuml.project.lang.ComplementPattern;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.lang.SubjectPattern;
import net.sourceforge.plantuml.project.lang.Verb;
import net.sourceforge.plantuml.project.lang.VerbPattern;

public class NaturalCommandAnd extends SingleLineCommand2<GanttDiagram> {

	private final SubjectPattern subjectPattern;
	private final VerbPattern verbPattern1;
	private final ComplementPattern complementPattern1;
	private final VerbPattern verbPattern2;
	private final ComplementPattern complementPattern2;

	private NaturalCommandAnd(RegexConcat pattern, SubjectPattern subject, VerbPattern verb1,
			ComplementPattern complement1, VerbPattern verb2, ComplementPattern complement2) {
		super(pattern);
		this.subjectPattern = subject;
		this.verbPattern1 = verb1;
		this.complementPattern1 = complement1;
		this.verbPattern2 = verb2;
		this.complementPattern2 = complement2;
	}

	@Override
	public String toString() {
		return subjectPattern.toString() + " " + verbPattern1.toString() + " " + complementPattern1.toString()
				+ " and " + verbPattern2.toString() + " " + complementPattern2.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram system, LineLocation location, RegexResult arg) {
		final Subject subject = subjectPattern.getSubject(system, arg);
		final Verb verb1 = verbPattern1.getVerb(system, arg);
		final Failable<Complement> complement1 = complementPattern1.getComplement(system, arg, "1");
		if (complement1.isFail()) {
			return CommandExecutionResult.error(complement1.getError());
		}
		final CommandExecutionResult result1 = verb1.execute(subject, complement1.get());
		if (result1.isOk() == false) {
			return result1;
		}
		final Verb verb2 = verbPattern2.getVerb(system, arg);
		final Failable<Complement> complement2 = complementPattern2.getComplement(system, arg, "2");
		if (complement2.isFail()) {
			return CommandExecutionResult.error(complement2.getError());
		}
		return verb2.execute(subject, complement2.get());
	}

	public static Command create(SubjectPattern subject, VerbPattern verb1, ComplementPattern complement1,
			VerbPattern verb2, ComplementPattern complement2) {
		final RegexConcat pattern = new RegexConcat(//
				RegexLeaf.start(), //
				subject.toRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				verb1.toRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				complement1.toRegex("1"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("and"), //
				RegexLeaf.spaceOneOrMore(), //
				verb2.toRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				complement2.toRegex("2"), //
				RegexLeaf.end());
		return new NaturalCommandAnd(pattern, subject, verb1, complement1, verb2, complement2);
	}
}
