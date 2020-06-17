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
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;

public class CommandExecutionResult {

	private final String error;
	private final AbstractPSystem newDiagram;
	private final List<String> debugLines;

	private CommandExecutionResult(String error, AbstractPSystem newDiagram, List<String> debugLines) {
		this.error = error;
		this.newDiagram = newDiagram;
		this.debugLines = debugLines;
	}

	public CommandExecutionResult withDiagram(AbstractPSystem newDiagram) {
		return new CommandExecutionResult(error, newDiagram, null);
	}

	@Override
	public String toString() {
		return super.toString() + " " + error;
	}

	public static CommandExecutionResult newDiagram(AbstractPSystem result) {
		return new CommandExecutionResult(null, result, null);
	}

	public static CommandExecutionResult ok() {
		return new CommandExecutionResult(null, null, null);
	}

	public static CommandExecutionResult error(String error) {
		return new CommandExecutionResult(error, null, null);
	}

	public static CommandExecutionResult error(String error, Throwable t) {
		return new CommandExecutionResult(error, null, getStackTrace(t));
	}

	public static List<String> getStackTrace(Throwable exception) {
		final List<String> result = new ArrayList<String>();
		result.add(exception.toString());
		for (StackTraceElement ste : exception.getStackTrace()) {
			result.add("  " + ste.toString());
		}
		if (exception.getCause() != null) {
			final Throwable cause = exception.getCause();
			result.add("  ");
			result.add("Caused by " + cause.toString());
			for (StackTraceElement ste : cause.getStackTrace()) {
				result.add("  " + ste.toString());
			}

		}
		return result;
	}

	public boolean isOk() {
		return error == null;
	}

	public String getError() {
		if (isOk()) {
			throw new IllegalStateException();
		}
		return error;
	}

	public AbstractPSystem getNewDiagram() {
		return newDiagram;
	}

	public List<String> getDebugLines() {
		return debugLines;
	}

}
