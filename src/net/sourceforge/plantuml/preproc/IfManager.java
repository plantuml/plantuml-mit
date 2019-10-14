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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.version.Version;

public class IfManager extends ReadLineInstrumented implements ReadLine {

	protected static final Pattern2 ifdefPattern = MyPattern.cmpile("^[%s]*!if(n)?def[%s]+(.+)$");
	protected static final Pattern2 ifcomparePattern = MyPattern
			.cmpile("^[%s]*!if[%s]+\\%(\\w+)\\%[%s]*(\\<|\\<=|\\>|\\>=|=|==|!=|\\<\\>)[%s]*(\\d+)$");
	protected static final Pattern2 elsePattern = MyPattern.cmpile("^[%s]*!else[%s]*$");
	protected static final Pattern2 endifPattern = MyPattern.cmpile("^[%s]*!endif[%s]*$");

	private final DefinesGet defines;
	private final ReadLine source;

	private IfManager child;

	public IfManager(ReadLine source, DefinesGet defines) {
		this.defines = defines;
		this.source = source;
	}

	@Override
	final StringLocated readLineInst() throws IOException {
		if (child != null) {
			final StringLocated s = child.readLine();
			if (s != null) {
				return s;
			}
			child = null;
		}

		return readLineInternal();
	}

	protected StringLocated readLineInternal() throws IOException {
		final StringLocated s = source.readLine();
		if (s == null) {
			return null;
		}

		Matcher2 m = ifcomparePattern.matcher(s.getString());
		if (m.find()) {
			final int value1 = getValue(m.group(1));
			final String operator = m.group(2);
			final int value2 = Integer.parseInt(m.group(3));
			final boolean ok = new NumericCompare(operator).isCompareOk(value1, value2);
			if (ok) {
				child = new IfManagerPositif(source, defines);
			} else {
				child = new IfManagerNegatif(source, defines);
			}
			return this.readLine();
		}

		m = ifdefPattern.matcher(s.getString());
		if (m.find()) {
			boolean ok = defines.get().isDefine(m.group(2));
			if (m.group(1) != null) {
				ok = !ok;
			}
			if (ok) {
				child = new IfManagerPositif(source, defines);
			} else {
				child = new IfManagerNegatif(source, defines);
			}
			return this.readLine();
		}

		return s;
	}

	private int getValue(final String arg) {
		if (arg.equalsIgnoreCase("PLANTUML_VERSION")) {
			return Version.versionPatched();
		}
		return 0;
	}

	@Override
	void closeInst() throws IOException {
		source.close();
	}

}
