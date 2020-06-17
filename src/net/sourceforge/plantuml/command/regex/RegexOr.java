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
package net.sourceforge.plantuml.command.regex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.plantuml.StringLocated;

public class RegexOr extends RegexComposed implements IRegex {

	private final String name;

	public RegexOr(IRegex... partial) {
		this(null, partial);
	}

	public RegexOr(String name, IRegex... partials) {
		super(partials);
		this.name = name;
	}

	@Override
	protected String getFullSlow() {
		final StringBuilder sb = new StringBuilder("(");
		if (name == null) {
			sb.append("?:");
		}
		for (IRegex p : partials()) {
			sb.append(p.getPattern());
			sb.append("|");
		}
		sb.setLength(sb.length() - 1);
		sb.append(')');
		return sb.toString();
	}

	protected int getStartCount() {
		return 1;
	}

	final public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		final Map<String, RegexPartialMatch> result = new HashMap<String, RegexPartialMatch>();
		final String fullGroup = name == null ? null : it.next();
		result.putAll(super.createPartialMatch(it));
		if (name != null) {
			final RegexPartialMatch m = new RegexPartialMatch(name);
			m.add(fullGroup);
			result.put(name, m);
		}
		return result;
	}

	public boolean match(StringLocated full) {
		throw new UnsupportedOperationException();
	}

}
