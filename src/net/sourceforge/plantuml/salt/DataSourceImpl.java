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
package net.sourceforge.plantuml.salt;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class DataSourceImpl implements DataSource {

	private int i = 0;
	private final List<Terminated<String>> data = new ArrayList<Terminated<String>>();

	public DataSourceImpl(List<String> data) {
		final Pattern2 p = MyPattern.cmpile("\\{(?:[-+^#!*/]|S-|SI|S)?");

		for (String s : data) {
			final StringTokenizer st = new StringTokenizer(s, "|}", true);
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trin(st.nextToken());
				if (token.equals("|")) {
					continue;
				}
				final Terminator terminator = st.hasMoreTokens() ? Terminator.NEWCOL : Terminator.NEWLINE;
				final Matcher2 m = p.matcher(token);
				final boolean found = m.find();
				if (found == false) {
					addInternal(token, terminator);
					continue;
				}
				int lastStart = 0;
				int end = 0;
				do {
					final int start = m.start();
					if (start > lastStart) {
						addInternal(token.substring(lastStart, start), Terminator.NEWCOL);
					}
					end = m.end();
					final Terminator t = end == token.length() ? terminator : Terminator.NEWCOL;
					addInternal(token.substring(start, end), t);
					lastStart = end;
				} while (m.find());
				if (end < token.length()) {
					addInternal(token.substring(end), terminator);
				}
			}
		}
	}


	private void addInternal(String s, Terminator t) {
		s = StringUtils.trin(s);
		if (s.length() > 0) {
			data.add(new Terminated<String>(s, t));
		}
	}

	public Terminated<String> peek(int nb) {
		return data.get(i + nb);
	}

	public boolean hasNext() {
		return i < data.size();
	}

	public Terminated<String> next() {
		final Terminated<String> result = data.get(i);
		i++;
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return super.toString() + " " + (hasNext() ? peek(0) : "$$$");
	}

}
