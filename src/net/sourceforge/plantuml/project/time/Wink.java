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
package net.sourceforge.plantuml.project.time;

import net.sourceforge.plantuml.project.Value;

public class Wink implements Value, Comparable<Wink> {

	private final int wink;

	public Wink(int wink) {
		this.wink = wink;
	}

	@Override
	public String toString() {
		return "(Wink +" + wink + ")";
	}

	public Wink increment() {
		return new Wink(wink + 1);
	}

	public Wink decrement() {
		return new Wink(wink - 1);
	}

	public final int getWink() {
		return wink;
	}

	public int compareTo(Wink other) {
		return this.wink - other.wink;
	}

	public String toShortString() {
		return "" + (wink + 1);
	}

	public static Wink min(Wink wink1, Wink wink2) {
		if (wink2.wink < wink1.wink) {
			return wink2;
		}
		return wink1;
	}

	public static Wink max(Wink wink1, Wink wink2) {
		if (wink2.wink > wink1.wink) {
			return wink2;
		}
		return wink1;
	}

}
