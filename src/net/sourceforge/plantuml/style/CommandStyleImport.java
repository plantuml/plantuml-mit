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
package net.sourceforge.plantuml.style;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class CommandStyleImport extends SingleLineCommand2<UmlDiagram> {

	public CommandStyleImport() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandStyleImport.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("\\<style"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\w+"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("="), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("[%q%g]?"), //
				new RegexLeaf("PATH", "([^%q%g]*)"), //
				new RegexLeaf("[%q%g]?"), //
				new RegexLeaf("\\>"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram diagram, LineLocation location, RegexResult arg) {
		final String path = arg.get("PATH", 0);
		try {
			final File f = FileSystem.getInstance().getFile(path);
			BlocLines lines = null;
			if (f.exists()) {
				lines = BlocLines.load(f, location);
			} else {
				final InputStream internalIs = StyleLoader.class.getResourceAsStream("/skin/" + path);
				if (internalIs != null) {
					lines = BlocLines.load(internalIs, location);
				}
			}
			if (lines == null) {
				return CommandExecutionResult.error("File does not exist: " + path);
			}
			final StyleBuilder styleBuilder = diagram.getSkinParam().getCurrentStyleBuilder();
			for (Style modifiedStyle : StyleLoader.getDeclaredStyles(lines, styleBuilder)) {
				diagram.getSkinParam().muteStyle(modifiedStyle);
			}
		} catch (IOException e) {
			return CommandExecutionResult.error("File does not exist: " + path);
		}
		return CommandExecutionResult.ok();
	}
}
