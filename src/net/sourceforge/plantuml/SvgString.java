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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvgString {

	private final String svg;
	private final double scale;

	public SvgString(String svg, double scale) {
		if (svg == null) {
			throw new IllegalArgumentException();
		}
		this.svg = svg;
		this.scale = scale;
	}

	public String getMD5Hex() {
		return SignatureUtils.getMD5Hex(svg);
	}

	public String getSvg(boolean raw) {
		String result = svg;
		if (raw) {
			return result;
		}
		if (result.startsWith("<?xml")) {
			final int idx = result.indexOf("<svg");
			result = result.substring(idx);
		}
		if (result.startsWith("<svg")) {
			final int idx = result.indexOf(">");
			result = "<svg>" + result.substring(idx + 1);
		}
		if (result.startsWith("<svg>") == false) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	public int getData(String name) {
		final Pattern p = Pattern.compile("(?i)" + name + "\\W+(\\d+)");
		final Matcher m = p.matcher(svg);
		if (m.find()) {
			final String s = m.group(1);
			return Integer.parseInt(s);
		}
		throw new IllegalStateException("Cannot find " + name);
	}

	public double getScale() {
		return scale;
	}

}
