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

public class FoxSignature {

	private static final long masks[] = new long[127];

	static {
		final String full = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0!\"#$%&\'()*+,-./:;<=>?@[\\]^_{|}~";
		long m = 1L;
		for (int i = 0; i < full.length(); i++) {
			char ch = full.charAt(i);
			masks[ch] = m;
			if (ch >= 'A' && ch <= 'Z') {
				ch = (char) (ch + ('a' - 'A'));
				masks[ch] = m;
			}
			m = m << 1;
		}
	}

	public static void printMe() {
		for (int i = 0; i < masks.length; i++) {
			if (masks[i] > 0) {
				final char ch = (char) i;
				System.err.println("ch=" + ch + " " + masks[i]);
			}
		}
	}

	private static long getMask(char ch) {
		if (ch < masks.length) {
			return masks[ch];
		}
		return 0L;
	}

	public static long getFoxSignature(String s) {
		long result = 0;
		for (int i = 0; i < s.length(); i++) {
			result = result | getMask(s.charAt(i));
		}
		return result;
	}

	public static String backToString(long check) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < masks.length; i++) {
			if (masks[i] != 0L && (check & masks[i]) != 0L) {
				final char ch = (char) i;
				sb.append(ch);
			}
		}
		return sb.toString();
	}

}
