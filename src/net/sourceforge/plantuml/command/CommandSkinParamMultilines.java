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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class CommandSkinParamMultilines extends CommandMultilinesBracket<UmlDiagram> {

	public CommandSkinParamMultilines() {
		super("(?i)^skinparam[%s]*(?:[%s]+([\\w.]*(?:\\<\\<.*\\>\\>)?[\\w.]*))?[%s]*\\{$");
	}

	@Override
	protected boolean isLineConsistent(String line, int level) {
		line = StringUtils.trin(line);
		if (hasStartingQuote(line)) {
			return true;
		}
		return SkinLoader.p1.matcher(line).matches();
	}

	private boolean hasStartingQuote(CharSequence line) {
		// return MyPattern.mtches(line, "[%s]*[%q].*");
		return MyPattern.mtches(line, CommandMultilinesComment.COMMENT_SINGLE_LINE);
	}

	public CommandExecutionResult execute(UmlDiagram diagram, BlocLines lines) {
		final SkinLoader skinLoader = new SkinLoader(diagram);

		final Matcher2 mStart = getStartingPattern().matcher(lines.getFirst499().getTrimmed().getString());
		if (mStart.find() == false) {
			throw new IllegalStateException();
		}
		final String group1 = mStart.group(1);

		return skinLoader.execute(lines, group1);

	}

}
