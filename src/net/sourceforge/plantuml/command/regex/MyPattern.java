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

import java.util.regex.Pattern;

// Splitter.java to be finished
public abstract class MyPattern {

	public static Pattern2 cmpile(String p) {
		p = transformAndCheck(p);
		return new Pattern2(Pattern.compile(p));
	}

	public static Pattern2 cmpileNockeck(String p) {
		p = transform(p);
		return new Pattern2(Pattern.compile(p));
	}

	public static Pattern2 cmpile(String p, int type) {
		p = transformAndCheck(p);
		return new Pattern2(Pattern.compile(p, type));
	}

	public static Pattern2 cmpileNockeck(String p, int type) {
		p = transform(p);
		return new Pattern2(Pattern.compile(p, type));
	}

	private static String transformAndCheck(String p) {
		p = transform(p);
		return p;
	}

	private static String transform(String p) {
		// Replace ReadLineReader.java
		p = p.replaceAll("%s", "\\\\s\u00A0"); // space
		p = p.replaceAll("%q", "'\u2018\u2019"); // quote
		p = p.replaceAll("%g", "\"\u201c\u201d\u00ab\u00bb"); // double quote
		return p;
	}

	public static boolean mtches(CharSequence input, String regex) {
		return cmpile(regex).matcher(input).matches();
	}

	public static CharSequence removeAll(CharSequence src, String regex) {
		return src.toString().replaceAll(transform(regex), "");
	}

}
