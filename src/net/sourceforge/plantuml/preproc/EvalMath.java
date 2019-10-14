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
public class EvalMath {

	private final String str;
	private int pos = -1;
	private char ch;

	public EvalMath(String str) {
		this.str = str;
	}

	private void nextChar() {
		pos++;
		if (pos < str.length()) {
			ch = str.charAt(pos);
		} else {
			ch = '\0';
		}
	}

	private boolean eat(int charToEat) {
		while (ch == ' ') {
			nextChar();
		}
		if (ch == charToEat) {
			nextChar();
			return true;
		}
		return false;
	}

	private double parseExpression() {
		double x = parseTerm();
		while (true) {
			if (eat('+')) {
				x += parseTerm();
			} else if (eat('-')) {
				x -= parseTerm();
			} else {
				return x;
			}
		}
	}

	private double parseTerm() {
		double x = parseFactor();
		while (true) {
			if (eat('*')) {
				x *= parseFactor();
			} else if (eat('/')) {
				x /= parseFactor();
			} else {
				return x;
			}
		}
	}

	private double parseFactor() {
		if (eat('+')) {
			return parseFactor();
		}
		if (eat('-')) {
			return -parseFactor();
		}

		final double x;
		final int startPos = pos;
		if (eat('(')) {
			x = parseExpression();
			eat(')');
		} else if ((ch >= '0' && ch <= '9') || ch == '.') {
			while ((ch >= '0' && ch <= '9') || ch == '.')
				nextChar();
			x = Double.parseDouble(str.substring(startPos, pos));
		} else if (ch >= 'a' && ch <= 'z') {
			while (ch >= 'a' && ch <= 'z') {
				nextChar();
			}
			final String func = str.substring(startPos, pos);
			x = parseFactor();
			throw new RuntimeException("Unknown function: " + func);
		} else {
			throw new RuntimeException("Unexpected: " + (char) ch);
		}

		return x;
	}

	public double eval() {
		nextChar();
		double x = parseExpression();
		if (pos < str.length()) {
			throw new RuntimeException("Unexpected: " + (char) ch);
		}
		return x;
	}

	public static void main(String[] args) {
		final EvalMath eval = new EvalMath("33+2*(4+1)");
		System.err.println(eval.eval());
	}
}
