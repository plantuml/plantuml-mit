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
package net.sourceforge.plantuml;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

public class Guillemet {

	public static final Guillemet NONE = new Guillemet("", "");
	public static final Guillemet DOUBLE_COMPARATOR = new Guillemet("<<", ">>");
	public static final Guillemet GUILLEMET = new Guillemet("\u00AB", "\u00BB");

	private final String start;
	private final String end;

	public Guillemet fromDescription(String value) {
		if (value != null) {
			if ("false".equalsIgnoreCase(value)) {
				return Guillemet.DOUBLE_COMPARATOR;
			}
			if ("<< >>".equalsIgnoreCase(value)) {
				return Guillemet.DOUBLE_COMPARATOR;
			}
			if ("none".equalsIgnoreCase(value)) {
				return Guillemet.NONE;
			}
			if (value.contains(" ")) {
				final StringTokenizer st = new StringTokenizer(value, " ");
				return new Guillemet(st.nextToken(), st.nextToken());
			}
		}
		return Guillemet.GUILLEMET;
	}

	private Guillemet(String start, String end) {
		this.start = start;
		this.end = end;

	}

	public String manageGuillemet(String st) {
		if (this == DOUBLE_COMPARATOR) {
			return st;
		}
		return st.replaceAll("\\<\\<\\s?((?:\\<&\\w+\\>|[^<>])+?)\\s?\\>\\>", Matcher.quoteReplacement(start) + "$1"
				+ Matcher.quoteReplacement(end));
	}

	public String manageGuillemetStrict(String st) {
		if (this == DOUBLE_COMPARATOR) {
			return st;
		}
		if (st.startsWith("<< ")) {
			st = start + st.substring(3);
		} else if (st.startsWith("<<")) {
			st = start + st.substring(2);
		}
		if (st.endsWith(" >>")) {
			st = st.substring(0, st.length() - 3) + end;
		} else if (st.endsWith(">>")) {
			st = st.substring(0, st.length() - 2) + end;
		}
		return st;
	}

}
