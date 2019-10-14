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
package net.sourceforge.plantuml.timingdiagram;

import java.util.StringTokenizer;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class CommandDefineStateShort extends SingleLineCommand2<TimingDiagram> {

	public CommandDefineStateShort() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandDefineStateShort.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("PLAYER", "([\\p{L}0-9_.@]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("has"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("STATE", "([\\p{L}0-9_.@]+)"), //
				new RegexLeaf("STATES", "((,([\\p{L}0-9_.@]+))*)"), RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg) {
		final String playerCode = arg.get("PLAYER", 0);
		final Player player = diagram.getPlayer(playerCode);
		if (player == null) {
			return CommandExecutionResult.error("Unknown " + playerCode);
		}
		final String stateCode = arg.get("STATE", 0);
		player.defineState(stateCode, stateCode);
		final String states = arg.get("STATES", 0);
		if (states != null) {
			for (StringTokenizer st = new StringTokenizer(states, ","); st.hasMoreTokens();) {
				final String token = st.nextToken();
				player.defineState(token, token);
			}
		}

		return CommandExecutionResult.ok();
	}
}
