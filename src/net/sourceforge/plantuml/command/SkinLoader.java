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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class SkinLoader {

	public final static Pattern2 p1 = MyPattern
			.cmpile("^([\\w.]*(?:\\<\\<.*\\>\\>)?[\\w.]*)[%s]+(?:(\\{)|(.*))$|^\\}?$");

	final private List<String> context = new ArrayList<String>();
	final private UmlDiagram diagram;

	public SkinLoader(UmlDiagram diagram) {
		this.diagram = diagram;
	}

	private void push(String s) {
		context.add(s);
	}

	private void pop() {
		context.remove(context.size() - 1);
	}

	private String getFullParam() {
		final StringBuilder sb = new StringBuilder();
		for (String s : context) {
			sb.append(s);
		}
		return sb.toString();
	}

	public CommandExecutionResult execute(BlocLines lines, final String group1) {

		if (group1 != null) {
			this.push(group1);
		}

		lines = lines.subExtract(1, 1);
		lines = lines.trim(true);

		for (StringLocated s : lines) {
			assert s.getString().length() > 0;

			if (s.getString().equals("}")) {
				this.pop();
				continue;
			}
			final Matcher2 m = p1.matcher(s.getString());
			if (m.find() == false) {
				throw new IllegalStateException();
			}
			if (m.group(2) != null) {
				this.push(m.group(1));
			} else if (m.group(3) != null) {
				final String key = this.getFullParam() + m.group(1);
				diagram.setParam(key, m.group(3));
			} else {
				throw new IllegalStateException("." + s.getString() + ".");
			}
		}

		return CommandExecutionResult.ok();
	}

}
