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
package net.sourceforge.plantuml.utils;

public class CharHidder {

	public static String addTileAtBegin(String s) {
		return "~" + s;
	}

	public static String hide(String s) {
		// System.err.println("hide " + s);
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '~' && i + 1 < s.length()) {
				i++;
				final char c2 = s.charAt(i);
				if (isToBeHidden(c2)) {
					result.append(hideChar(c2));
				} else {
					result.append(c);
					result.append(c2);
				}

			} else {
				result.append(c);
			}
		}
		// System.err.println("---> " + result);
		return result.toString();
	}

	private static boolean isToBeHidden(final char c) {
		if (c == '_' || c == '-' || c == '\"' || c == '#' || c == ']' || c == '[' || c == '*' || c == '.' || c == '/'
				|| c == '<') {
			return true;
		}
		return false;
	}

	private static char hideChar(char c) {
		if (c > 255) {
			throw new IllegalArgumentException();
		}
		return (char) ('\uE000' + c);
	}

	private static char unhideChar(char c) {
		if (c >= '\uE000' && c <= '\uE0FF') {
			return (char) (c - '\uE000');
		}
		return c;
	}

	public static String unhide(String s) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			result.append(unhideChar(c));
		}
		// System.err.println("unhide " + result);
		return result.toString();
	}

}
