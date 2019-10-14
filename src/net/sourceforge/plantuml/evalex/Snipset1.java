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
package net.sourceforge.plantuml.evalex;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Snipset1 {

	public static void main(String[] args) {
		Number result = null;

		Expression expression = new Expression("1+1/3");
		result = expression.eval();
		expression.setPrecision(2);
		result = expression.eval();

		result = new Expression("(3.4 + -4.1)/2").eval();

		result = new Expression("SQRT(a^2 + b^2)").with("a", "2.4").and("b", "9.253").eval();

		BigDecimal a = new BigDecimal("2.4");
		BigDecimal b = new BigDecimal("9.235");
		result = new Expression("SQRT(a^2 + b^2)").with("a", a).and("b", b).eval();

		result = new Expression("2.4/PI").setPrecision(128).setRoundingMode(RoundingMode.UP).eval();

		result = new Expression("random() > 0.5").eval();

		result = new Expression("not(x<7 || sqrt(max(x,9,3,min(4,3))) <= 3)").with("x", "22.9").eval();
		System.err.println("foo1=" + result);

		result = new Expression("log10(100)").eval();
	}
}
