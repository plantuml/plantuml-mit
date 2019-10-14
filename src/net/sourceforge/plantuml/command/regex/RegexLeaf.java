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
package net.sourceforge.plantuml.command.regex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringLocated;

public class RegexLeaf implements IRegex {

	private static final RegexLeaf END = new RegexLeaf("$");
	private static final RegexLeaf START = new RegexLeaf("^");
	private final String pattern;
	private final String name;

	private int count = -1;

	public RegexLeaf(String regex) {
		this(null, regex);
	}

	public static RegexLeaf spaceZeroOrMore() {
		return new RegexLeaf("[%s]*");
	}

	public static RegexLeaf spaceOneOrMore() {
		return new RegexLeaf("[%s]+");
	}

	public static RegexLeaf start() {
		return START;
	}

	public static RegexLeaf end() {
		return END;
	}

	public RegexLeaf(String name, String regex) {
		this.pattern = regex;
		this.name = name;
		// unknow=(left[%s]to[%s]right|top[%s]to[%s]bottom)
		// unknow=is off on
		// unknow=(-+)\>
		// unknow=\[([^\[\]]+?)\]
		// unknow=([*]+)

		// if (regex.equals("([*]+)") || regex.equals("\\[([^\\[\\]]+?)\\]") || regex.equals("(-+)\\>")
		// || regex.equals("is off on")) {
		// Thread.dumpStack();
		// System.exit(0);
		// }
	}

	@Override
	public String toString() {
		return super.toString() + " " + name + " " + pattern;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}

	public int count() {
		if (count == -1) {
			count = MyPattern.cmpile(pattern, Pattern.CASE_INSENSITIVE).matcher("").groupCount();
		}
		return count;
	}

	public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		final RegexPartialMatch m = new RegexPartialMatch(name);
		for (int i = 0; i < count(); i++) {
			final String group = it.next();
			m.add(group);
		}
		if (name == null) {
			return Collections.emptyMap();
		}
		return Collections.singletonMap(name, m);
	}

	public boolean match(StringLocated full) {
		throw new UnsupportedOperationException();
	}

	public RegexResult matcher(String full) {
		throw new UnsupportedOperationException();
	}

	static private final Set<String> UNKNOWN = new HashSet<String>();

	static private final Pattern p1 = Pattern.compile("^[-0A-Za-z_!:@;/=,\"]+$");
	static private final Pattern p2 = Pattern.compile("^[-0A-Za-z_!:@;/=,\"]+\\?$");
	static private final Pattern p3 = Pattern
			.compile("^\\(?[-0A-Za-z_!:@;/=\" ]+\\??(\\|[-0A-Za-z_!:@;/=,\" ]+\\??)+\\)?$");

	private static long getSignatureP3(String s) {
		long result = -1L;
		for (StringTokenizer st = new StringTokenizer(s, "()|"); st.hasMoreTokens();) {
			final String val = st.nextToken();
			final long sig = FoxSignature.getFoxSignature(val.endsWith("?") ? val.substring(0, val.length() - 2) : val);
			result = result & sig;
		}
		return result;
	}

	public long getFoxSignatureNone() {
		return 0;
	}

	public long getFoxSignature() {
		if (p1.matcher(pattern).matches()) {
			return FoxSignature.getFoxSignature(pattern);
		}
		if (p2.matcher(pattern).matches()) {
			return FoxSignature.getFoxSignature(pattern.substring(0, pattern.length() - 2));
		}
		if (p3.matcher(pattern).matches()) {
			// System.err.println("special " + pattern);
			// System.err.println("result " + FoxSignature.backToString(getSignatureP3(pattern)));
			return getSignatureP3(pattern);
		}
		if (pattern.length() == 2 && pattern.startsWith("\\") && Character.isLetterOrDigit(pattern.charAt(1)) == false) {
			return FoxSignature.getFoxSignature(pattern.substring(1));
		}
		if (pattern.equals("\\<\\>") || pattern.equals("(\\<\\<.*\\>\\>)")) {
			return FoxSignature.getFoxSignature("<>");
		}
		if (pattern.equals("\\<-\\>")) {
			return FoxSignature.getFoxSignature("<->");
		}
		if (pattern.equals("(-+)")) {
			return FoxSignature.getFoxSignature("-");
		}
		if (pattern.equals("\\|+") || pattern.equals("\\|\\|")) {
			return FoxSignature.getFoxSignature("|");
		}
		if (pattern.equals("([*]+)")) {
			return FoxSignature.getFoxSignature("*");
		}
		if (pattern.equals("[%s]+") || pattern.equals("[%s]*")) {
			return 0;
		}
//		synchronized (UNKNOWN) {
//			final boolean changed = UNKNOWN.add(pattern);
//			if (changed)
//				System.err.println("unknow=" + pattern);
//
//		}
		return 0;
	}

}
