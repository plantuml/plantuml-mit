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
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;

public class CommandExoArrowRight extends CommandExoArrowAny {

	public CommandExoArrowRight() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandExoArrowRight.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf("ANCHOR", CommandArrow.ANCHOR), //
				new RegexLeaf("PARTICIPANT", "([\\p{L}0-9_.@]+|[%g][^%g]+[%g])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("ARROW_SUPPCIRCLE", "([%s]+[ox])?"), //
				new RegexOr( //
						new RegexConcat( //
								new RegexLeaf("ARROW_BOTHDRESSING", "(<<?|//?|\\\\\\\\?)?"), //
								new RegexLeaf("ARROW_BODYA1", "(-+)"), //
								new RegexLeaf("ARROW_STYLE1", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYB1", "(-*)"), //
								new RegexLeaf("ARROW_DRESSING1", "(>>?|//?|\\\\\\\\?)")), //
						new RegexConcat( //
								new RegexLeaf("ARROW_DRESSING2", "(<<?|//?|\\\\\\\\?)"), //
								new RegexLeaf("ARROW_BODYB2", "(-*)"), //
								new RegexLeaf("ARROW_STYLE2", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYA2", "(-+)"))), //
				new RegexLeaf("SHORT", "([ox]?[?\\]\\[])?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("ACTIVATION", "(?:([+*!-]+)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("LIFECOLOR", "(?:(#\\w+)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("LABEL", "(.*)") //
						)), RegexLeaf.end());
	}

	@Override
	MessageExoType getMessageExoType(RegexResult arg2) {
		final String start = arg2.get("SHORT", 0);
		final String dressing1 = arg2.get("ARROW_DRESSING1", 0);
		final String dressing2 = arg2.get("ARROW_DRESSING2", 0);
		if (start != null && start.contains("[")) {
			if (dressing1 != null) {
				return MessageExoType.TO_LEFT;
			}
			if (dressing2 != null) {
				return MessageExoType.FROM_LEFT;
			}
			throw new IllegalArgumentException();
		}
		if (dressing1 != null) {
			return MessageExoType.TO_RIGHT;
		}
		if (dressing2 != null) {
			return MessageExoType.FROM_RIGHT;
		}
		throw new IllegalArgumentException();
	}

}
