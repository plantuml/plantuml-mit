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
package net.sourceforge.plantuml.tim.expression;

public class Token {

	private final String surface;
	private final TokenType tokenType;

	@Override
	public String toString() {
		return tokenType + "{" + surface + "}";
	}

	public Token(char surface, TokenType tokenType) {
		this("" + surface, tokenType);
	}

	public Token(String surface, TokenType tokenType) {
		this.surface = surface;
		this.tokenType = tokenType;
	}

	public TokenOperator getTokenOperator() {
		if (this.tokenType != TokenType.OPERATOR) {
			throw new IllegalStateException();
		}
		final char ch2 = surface.length() > 1 ? surface.charAt(1) : 0;
		return TokenOperator.getTokenOperator(surface.charAt(0), ch2);
	}

	public final String getSurface() {
		return surface;
	}

	public final TokenType getTokenType() {
		return tokenType;
	}

	public Token muteToFunction() {
		if (this.tokenType != TokenType.PLAIN_TEXT) {
			throw new IllegalStateException();
		}
		return new Token(surface, TokenType.FUNCTION_NAME);
	}

}
