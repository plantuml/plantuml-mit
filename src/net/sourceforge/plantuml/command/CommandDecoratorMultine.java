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

import net.sourceforge.plantuml.core.Diagram;

public class CommandDecoratorMultine<D extends Diagram> implements Command<D> {

	private final SingleLineCommand2<D> cmd;
	private final boolean removeEmptyColumn;
	private final int nbMaxLines;
	
	public CommandDecoratorMultine(SingleLineCommand2<D> cmd, int nbMaxLines) {
		this(cmd, false, nbMaxLines);
	}

	public CommandDecoratorMultine(SingleLineCommand2<D> cmd, boolean removeEmptyColumn, int nbMaxLines) {
		this.cmd = cmd;
		this.removeEmptyColumn = removeEmptyColumn;
		this.nbMaxLines = nbMaxLines;
	}

	public CommandExecutionResult execute(D diagram, BlocLines lines) {
		if (removeEmptyColumn) {
			lines = lines.removeEmptyColumns();
		}
		lines = lines.toSingleLineWithHiddenNewLine();
		return cmd.execute(diagram, lines);
	}

	public CommandControl isValid(BlocLines lines) {
		if (cmd.isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		lines = lines.toSingleLineWithHiddenNewLine();
		if (cmd.isForbidden(lines.getFirst499().getString())) {
			return CommandControl.NOT_OK;
		}
		final CommandControl tmp = cmd.isValid(lines);
		if (tmp == CommandControl.OK_PARTIAL) {
			throw new IllegalStateException();
		}
		if (tmp == CommandControl.OK) {
			return tmp;
		}
		return CommandControl.OK_PARTIAL;
	}

	public String[] getDescription() {
		return cmd.getDescription();
	}

	public int getNbMaxLines() {
		return nbMaxLines;
	}

}
