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
package net.sourceforge.plantuml.timingdiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.Player;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;

abstract class CommandChangeState extends SingleLineCommand2<TimingDiagram> {

	CommandChangeState(IRegex pattern) {
		super(pattern);
	}

	static final String STATE_CODE = "([\\p{L}0-9_][\\p{L}0-9_.]*)";

	static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	protected CommandExecutionResult addState(TimingDiagram diagram, RegexResult arg, final Player player,
			final TimeTick now) {
		final String comment = arg.get("COMMENT", 0);
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		player.setState(now, comment, colors, getStates(arg));
		return CommandExecutionResult.ok();
	}

	private String[] getStates(RegexResult arg) {
		if (arg.get("STATE7", 0) != null) {
			final String state1 = arg.get("STATE7", 0);
			final String state2 = arg.get("STATE7", 1);
			return new String[] { state1, state2 };
		}
		return new String[] { arg.getLazzy("STATE", 0) };
	}

	static IRegex getStateOrHidden() {
		return new RegexOr(//
				new RegexLeaf("STATE1", "[%g]([^%g]*)[%g]"), //
				new RegexLeaf("STATE2", STATE_CODE), //
				new RegexLeaf("STATE3", "(\\{hidden\\})"), //
				new RegexLeaf("STATE4", "(\\{\\.\\.\\.\\})"), //
				new RegexLeaf("STATE5", "(\\{-\\})"), //
				new RegexLeaf("STATE6", "(\\{\\?\\})"), //
				new RegexLeaf("STATE7", "(?:\\{" + STATE_CODE + "," + STATE_CODE + "\\})") //
		);
	}

}
