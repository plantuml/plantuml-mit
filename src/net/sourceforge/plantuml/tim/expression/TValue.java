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

import net.sourceforge.plantuml.json.JsonValue;

public class TValue {

	private final int intValue;
	private final String stringValue;
	private final JsonValue jsonValue;

	private TValue(int value) {
		this.intValue = value;
		this.stringValue = null;
		this.jsonValue = null;
	}

	private TValue(String stringValue) {
		if (stringValue == null) {
			throw new IllegalArgumentException();
		}
		this.intValue = 0;
		this.jsonValue = null;
		this.stringValue = stringValue;
	}

	public TValue(JsonValue json) {
		this.jsonValue = json;
		this.intValue = 0;
		this.stringValue = null;
	}

	public static TValue fromInt(int v) {
		return new TValue(v);
	}

	public static TValue fromBoolean(boolean b) {
		return new TValue(b ? 1 : 0);
	}

	public static TValue fromJson(JsonValue json) {
		return new TValue(json);
	}

	@Override
	public String toString() {
		if (jsonValue != null && jsonValue.isString()) {
			return jsonValue.asString();
		}
		if (jsonValue != null) {
			return jsonValue.toString();
		}
		if (stringValue == null) {
			return "" + intValue;
		}
		return stringValue;
	}

	public static TValue fromString(Token token) {
		if (token.getTokenType() != TokenType.QUOTED_STRING) {
			throw new IllegalArgumentException();
		}
		return new TValue(token.getSurface());
	}

	public static TValue fromString(String s) {
		return new TValue(s);
	}

	public static TValue fromNumber(Token token) {
		if (token.getTokenType() != TokenType.NUMBER) {
			throw new IllegalArgumentException();
		}
		return new TValue(Integer.parseInt(token.getSurface()));
	}

	public TValue add(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return new TValue(this.intValue + v2.intValue);
		}
		return new TValue(toString() + v2.toString());
	}

	public TValue minus(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return new TValue(this.intValue - v2.intValue);
		}
		return new TValue(toString() + v2.toString());
	}

	public TValue multiply(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return new TValue(this.intValue * v2.intValue);
		}
		return new TValue(toString() + v2.toString());
	}

	public TValue dividedBy(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return new TValue(this.intValue / v2.intValue);
		}
		return new TValue(toString() + v2.toString());
	}

	public boolean isNumber() {
		return this.jsonValue == null && this.stringValue == null;
	}

	public boolean isJson() {
		return this.jsonValue != null;
	}

	public Token toToken() {
		if (isNumber()) {
			return new Token(toString(), TokenType.NUMBER, null);
		}
		if (isJson()) {
			return new Token(toString(), TokenType.JSON_DATA, jsonValue);
		}
		return new Token(toString(), TokenType.QUOTED_STRING, null);
	}

	public TValue greaterThanOrEquals(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return fromBoolean(this.intValue >= v2.intValue);
		}
		return fromBoolean(toString().compareTo(v2.toString()) >= 0);
	}

	public TValue greaterThan(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return fromBoolean(this.intValue > v2.intValue);
		}
		return fromBoolean(toString().compareTo(v2.toString()) > 0);
	}

	public TValue lessThanOrEquals(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return fromBoolean(this.intValue <= v2.intValue);
		}
		return fromBoolean(toString().compareTo(v2.toString()) <= 0);
	}

	public TValue lessThan(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return fromBoolean(this.intValue < v2.intValue);
		}
		return fromBoolean(toString().compareTo(v2.toString()) < 0);
	}

	public TValue equalsOperation(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return fromBoolean(this.intValue == v2.intValue);
		}
		return fromBoolean(toString().compareTo(v2.toString()) == 0);
	}

	public TValue notEquals(TValue v2) {
		if (this.isNumber() && v2.isNumber()) {
			return fromBoolean(this.intValue != v2.intValue);
		}
		return fromBoolean(toString().compareTo(v2.toString()) != 0);
	}

	public boolean toBoolean() {
		if (this.isNumber()) {
			return this.intValue != 0;
		}
		return toString().length() > 0;
	}

	public int toInt() {
		return this.intValue;
	}

	public TValue logicalAnd(TValue v2) {
		return fromBoolean(this.toBoolean() && v2.toBoolean());
	}

	public TValue logicalOr(TValue v2) {
		return fromBoolean(this.toBoolean() || v2.toBoolean());
	}

	public JsonValue toJson() {
		return jsonValue;
	}

}
