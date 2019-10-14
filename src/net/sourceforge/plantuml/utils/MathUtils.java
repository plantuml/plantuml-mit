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

public class MathUtils {

	public static double max(double a, double b) {
		return Math.max(a, b);
	}

	public static double max(double a, double b, double c) {
		return max(max(a, b), c);
	}

	public static double max(double a, double b, double c, double d) {
		return max(max(a, b), max(c, d));
	}

	public static double max(double a, double b, double c, double d, double e) {
		return max(max(a, b, c), max(d, e));
	}

	public static double min(double a, double b) {
		return Math.min(a, b);
	}

	public static double min(double a, double b, double c) {
		return min(min(a, b), c);
	}

	public static double min(double a, double b, double c, double d) {
		return min(min(a, b), min(c, d));
	}

	public static double min(double a, double b, double c, double d, double e) {
		return min(min(a, b, c), min(d, e));
	}

	public static double limitation(double v, double min, double max) {
		if (min >= max) {
			// assert false : "min="+min+" max="+max+" v="+v;
			return v;
			// throw new IllegalArgumentException("min="+min+" max="+max+" v="+v);
		}
		if (v < min) {
			return min;
		}
		if (v > max) {
			return max;
		}
		return v;
	}

}
