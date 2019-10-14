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
package net.sourceforge.plantuml.compositediagram.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.compositediagram.CompositeDiagram;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkBlock extends SingleLineCommand2<CompositeDiagram> {

	public CommandLinkBlock() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandLinkBlock.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("ENT1", "([\\p{L}0-9_.]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("DECO1", "(\\[\\]|\\*\\))?"), //
				new RegexLeaf("QUEUE", "([=-]+|\\.+)"), //
				new RegexLeaf("DECO2", "(\\[\\]|\\(\\*)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("ENT2", "([\\p{L}0-9_.]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("DISPLAY", "(\\S*+)") //
						)), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(CompositeDiagram diagram, LineLocation location, RegexResult arg) {
		final IEntity cl1 = diagram.getOrCreateLeaf(Code.of(arg.get("ENT1", 0)), null, null);
		final IEntity cl2 = diagram.getOrCreateLeaf(Code.of(arg.get("ENT2", 0)), null, null);

		final String deco1 = arg.get("DECO1", 0);
		final String deco2 = arg.get("DECO2", 0);
		LinkType linkType = new LinkType(getLinkDecor(deco1), getLinkDecor(deco2));

		// if ("*)".equals(deco1)) {
		// linkType = linkType.getInterfaceProvider();
		// } else if ("(*".equals(deco2)) {
		// linkType = linkType.getInterfaceUser();
		// }

		final String queue = arg.get("QUEUE", 0);

		final Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(arg.get("DISPLAY", 0)), queue.length(), diagram.getSkinParam().getCurrentStyleBuilder());
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkDecor getLinkDecor(String s) {
		if ("[]".equals(s)) {
			return LinkDecor.SQUARRE_toberemoved;
		}
		return LinkDecor.NONE;
	}

}
