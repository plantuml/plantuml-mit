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
import java.util.StringTokenizer;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Task;

public class SubjectTask implements SubjectPattern {

	public Collection<VerbPattern> getVerbs() {
		return Arrays.<VerbPattern>asList(new VerbLasts(), new VerbTaskStarts(), new VerbTaskStartsAbsolute(),
				new VerbHappens(), new VerbEnds(), new VerbTaskEndsAbsolute(), new VerbIsColored(), new VerbIsDeleted(),
				new VerbIsForTask(), new VerbLinksTo());
	}

	public IRegex toRegex() {
		return new RegexConcat( //
				new RegexLeaf("THEN", "(then[%s]+)?"), //
				new RegexLeaf("SUBJECT", "\\[([^\\[\\]]+?)\\](?:[%s]+as[%s]+\\[([^\\[\\]]+?)\\])?"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("on"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("RESOURCE", "((?:\\{[^{}]+\\}[%s]*)+)") //
						)) //
		);
	}

	public Subject getSubject(GanttDiagram project, RegexResult arg) {
		final String s = arg.get("SUBJECT", 0);
		final String shortName = arg.get("SUBJECT", 1);
		final String then = arg.get("THEN", 0);
		final String resource = arg.get("RESOURCE", 0);
		final Task result = project.getOrCreateTask(s, shortName, then != null);
		if (result == null) {
			throw new IllegalStateException();
		}
		if (resource != null) {
			for (final StringTokenizer st = new StringTokenizer(resource, "{}"); st.hasMoreTokens();) {
				final String part = st.nextToken().trim();
				if (part.length() > 0) {
					project.affectResource(result, part);
				}
			}

		}
		return result;
	}
}
