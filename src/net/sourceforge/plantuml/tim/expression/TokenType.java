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
package net.sourceforge.plantuml.tim.expression;

import net.sourceforge.plantuml.tim.Eater;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TLineType;

public enum TokenType {
	QUOTED_STRING, JSON_DATA, OPERATOR, OPEN_PAREN_MATH, COMMA, CLOSE_PAREN_MATH, NUMBER, PLAIN_TEXT, SPACES, FUNCTION_NAME, OPEN_PAREN_FUNC, CLOSE_PAREN_FUNC;

	private boolean isSingleChar1() {
		return this == OPEN_PAREN_MATH || this == COMMA || this == CLOSE_PAREN_MATH;
	}

	private static boolean isPlainTextBreak(char ch, char ch2) {
		final TokenType tmp = fromChar(ch, ch2);
		if (tmp.isSingleChar1() || tmp == TokenType.OPERATOR || tmp == SPACES) {
			return true;
		}
		return false;
	}

	private static TokenType fromChar(char ch, char ch2) {
		TokenType result = PLAIN_TEXT;
		if (TLineType.isQuote(ch)) {
			result = QUOTED_STRING;
		} else if (TokenOperator.getTokenOperator(ch, ch2) != null) {
			result = OPERATOR;
		} else if (ch == '(') {
			result = OPEN_PAREN_MATH;
		} else if (ch == ')') {
			result = CLOSE_PAREN_MATH;
		} else if (ch == ',') {
			result = COMMA;
		} else if (TLineType.isLatinDigit(ch)) {
			result = NUMBER;
		} else if (TLineType.isSpaceChar(ch)) {
			result = SPACES;
		}
		return result;
	}

	final static public Token eatOneToken(Eater eater, boolean manageColon) throws EaterException {
		final char ch = eater.peekChar();
		if (ch == 0) {
			return null;
		}
		if (manageColon && ch == ':') {
			return null;
		}
		final TokenOperator tokenOperator = TokenOperator.getTokenOperator(ch, eater.peekCharN2());
		if (TLineType.isQuote(ch)) {
			return new Token(eater.eatAndGetQuotedString(), TokenType.QUOTED_STRING, null);
		} else if (tokenOperator != null) {
			if (tokenOperator.getDisplay().length() == 1) {
				return new Token(eater.eatOneChar(), TokenType.OPERATOR, null);
			}
			return new Token("" + eater.eatOneChar() + eater.eatOneChar(), TokenType.OPERATOR, null);
		} else if (ch == '(') {
			return new Token(eater.eatOneChar(), TokenType.OPEN_PAREN_MATH, null);
		} else if (ch == ')') {
			return new Token(eater.eatOneChar(), TokenType.CLOSE_PAREN_MATH, null);
		} else if (ch == ',') {
			return new Token(eater.eatOneChar(), TokenType.COMMA, null);
		} else if (TLineType.isLatinDigit(ch)) {
			return new Token(eater.eatAndGetNumber(), TokenType.NUMBER, null);
		} else if (TLineType.isSpaceChar(ch)) {
			return new Token(eater.eatAndGetSpaces(), TokenType.SPACES, null);
		}
		return new Token(eatAndGetTokenPlainText(eater), TokenType.PLAIN_TEXT, null);
	}

	static private String eatAndGetTokenPlainText(Eater eater) throws EaterException {
		final StringBuilder result = new StringBuilder();
		while (true) {
			final char ch = eater.peekChar();
			if (ch == 0 || TokenType.isPlainTextBreak(ch, eater.peekCharN2())) {
				return result.toString();
			}
			result.append(eater.eatOneChar());
		}
	}

}
