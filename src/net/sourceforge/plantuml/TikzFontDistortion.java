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
package net.sourceforge.plantuml;

import java.util.StringTokenizer;

public class TikzFontDistortion {

	private final double magnify;
	private final double distortion;

	private TikzFontDistortion(double magnify, double distortion) {
		this.magnify = magnify;
		this.distortion = distortion;
	}

	@Override
	public String toString() {
		return "" + magnify + ";" + distortion;
	}

	public static TikzFontDistortion fromValue(String value) {
		if (value == null) {
			return getDefault();
		}
		final StringTokenizer st = new StringTokenizer(value, ";");
		if (st.hasMoreElements() == false) {
			return getDefault();
		}
		final String v1 = st.nextToken();
		if (st.hasMoreElements() == false) {
			return getDefault();
		}
		final String v2 = st.nextToken();
		if (v1.matches("[\\d.]+") && v2.matches("[-\\d.]+")) {
			return new TikzFontDistortion(Double.parseDouble(v1), Double.parseDouble(v2));
		}
		return getDefault();
	}

	public static TikzFontDistortion getDefault() {
		return new TikzFontDistortion(1.20, 4.0);
	}

	public final double getMagnify() {
		return magnify;
	}

	public final double getDistortion() {
		return distortion;
	}

}
