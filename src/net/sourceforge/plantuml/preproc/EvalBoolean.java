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

// http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
public class EvalBoolean {

	private final String str;
	private int pos = -1;
	private char ch;
	private final Truth truth;

	public EvalBoolean(String str, Truth truth) {
		this.str = str;
		this.truth = truth;
	}

	private void nextChar() {
		pos++;
		if (pos < str.length()) {
			ch = str.charAt(pos);
		} else {
			ch = '\0';
		}
	}

	private boolean eat(char charToEat) {
		while (ch == ' ') {
			nextChar();
		}
		if (ch == charToEat) {
			nextChar();
			return true;
		}
		return false;
	}

	private boolean parseExpression() {
		boolean x = parseTerm();
		while (true) {
			if (eat('|')) {
				eat('|');
				x = x | parseTerm();
			} else {
				return x;
			}
		}
	}

	private boolean parseTerm() {
		boolean x = parseFactor();
		while (true) {
			if (eat('&')) {
				eat('&');
				x = x & parseFactor();
			} else {
				return x;
			}
		}
	}

	private boolean parseFactor() {
		if (eat('!')) {
			return !(parseFactor());
		}

		final boolean x;
		final int startPos = pos;
		if (eat('(')) {
			x = parseExpression();
			eat(')');
		} else if (isIdentifier()) {
			while (isIdentifier()) {
				nextChar();
			}
			final String func = str.substring(startPos, pos);
			x = truth.isTrue(func);
		} else {
			throw new IllegalArgumentException("Unexpected: " + (char) ch);
		}

		return x;
	}

	private boolean isIdentifier() {
		return ch == '_' || ch == '$' || Character.isLetterOrDigit(ch);
	}

	public boolean eval() {
		nextChar();
		final boolean x = parseExpression();
		if (pos < str.length()) {
			throw new IllegalArgumentException("Unexpected: " + (char) ch);
		}
		return x;
	}
}
