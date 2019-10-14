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

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.core.Diagram;

public abstract class CommandMultilines2<S extends Diagram> implements Command<S> {

	private final IRegex starting;

	private final MultilinesStrategy strategy;
	
	public CommandMultilines2(IRegex patternStart, MultilinesStrategy strategy) {
		if (patternStart.getPattern().startsWith("^") == false || patternStart.getPattern().endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + patternStart.getPattern());
		}
		this.strategy = strategy;
		this.starting = patternStart;
	}

	public boolean syntaxWithFinalBracket() {
		return false;
	}

	public abstract String getPatternEnd();

	public String[] getDescription() {
		return new String[] { "START: " + starting.getPattern(), "END: " + getPatternEnd() };
	}

	final public CommandControl isValid(BlocLines lines) {
		lines = lines.cleanList2(strategy);
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		if (syntaxWithFinalBracket()) {
			if (lines.size() == 1 && lines.getFirst499().getTrimmed().getString().endsWith("{") == false) {
				final String vline = ((StringLocated) lines.get499(0)).getString() + " {";
				if (isValid(BlocLines.singleString(vline)) == CommandControl.OK_PARTIAL) {
					return CommandControl.OK_PARTIAL;
				}
				return CommandControl.NOT_OK;
			}
			lines = lines.eventuallyMoveBracket();
		}
		final StringLocated first = lines.getFirst499();
		if (first == null) {
			return CommandControl.NOT_OK;
		}
		final boolean result1 = starting.match(first.getTrimmed());
		if (result1 == false) {
			return CommandControl.NOT_OK;
		}
		if (lines.size() == 1) {
			return CommandControl.OK_PARTIAL;
		}

		final Matcher2 m1 = MyPattern.cmpile(getPatternEnd()).matcher(lines.getLast499().getTrimmed().getString());
		if (m1.matches() == false) {
			return CommandControl.OK_PARTIAL;
		}

		actionIfCommandValid();
		return CommandControl.OK;
	}

	public final CommandExecutionResult execute(S system, BlocLines lines) {
		lines = lines.cleanList2(strategy);
		if (syntaxWithFinalBracket()) {
			lines = lines.eventuallyMoveBracket();
		}
		return executeNow(system, lines);
	}

	protected abstract CommandExecutionResult executeNow(S system, BlocLines lines);

	protected boolean isCommandForbidden() {
		return false;
	}

	protected void actionIfCommandValid() {
	}

	protected final IRegex getStartingPattern() {
		return starting;
	}

}
