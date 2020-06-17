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
package net.sourceforge.plantuml.svg;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;

public final class SvgData {

	private String svg;
	private double startX;
	private double startY;
	private double width;
	private double height;

	public static SvgData fromGraphViz(String s) {
		return new SvgData(removeSvgXmlHeader1(s));
	}

	private SvgData(String svg) {
		this.svg = svg;
		init();
	}

	private SvgData() {
	}

	private static String removeSvgXmlHeader1(String svg) {
		final String newString = "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">";
		svg = svg.replaceFirst("(?i)<svg[^>]*>", newString);
		return svg;
	}

	public SvgData mutateFromSvgTitler(String newSvg, double suppHeight, double suppWidth) {
		final SvgData result = new SvgData();
		result.svg = newSvg;
		result.startX = this.startX - suppWidth / 2;
		result.startY = this.startY;
		result.width = this.width + suppWidth;
		result.height = this.height + suppHeight;
		return result;
	}

	public final String getSvg() {
		return modifiedSvgXmlHeader();
	}

	private void init() {
		final Pattern p = Pattern.compile("(?i)<polygon\\s+[^>]*points=\"([^\"]+)\"");
		final Matcher m = p.matcher(svg);
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;
		if (m.find() == false) {
			return;
		}
		final String points = m.group(1);
		final StringTokenizer st = new StringTokenizer(points, " ");
		while (st.hasMoreTokens()) {
			final String token = st.nextToken();
			final StringTokenizer st2 = new StringTokenizer(token, ",");
			final double x = Double.parseDouble(StringUtils.trin(st2.nextToken()));
			final double y = Double.parseDouble(StringUtils.trin(st2.nextToken()));
			if (x < minX) {
				minX = x;
			}
			if (y < minY) {
				minY = y;
			}
			if (x > maxX) {
				maxX = x;
			}
			if (y > maxY) {
				maxY = y;
			}
		}

		this.width = maxX - minX;
		this.height = maxY - minY;
	}

	private String modifiedSvgXmlHeader() {
		final StringBuilder newString = new StringBuilder(
				"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
		newString.append("style=\"width:");
		newString.append(Math.round(getWidth()));
		newString.append(";height:");
		newString.append(Math.round(getHeight()));
		newString.append(";\" ");
		newString.append("width=\"" + Math.round(getWidth()) + "pt\" ");
		newString.append("height=\"" + Math.round(getHeight()) + "pt\" ");
		newString.append("viewBox=\"" + Math.round(startX) + " " + Math.round(startY) + " " + Math.round(getWidth()) + " "
				+ Math.round(getHeight()) + "\"");
		// newString.append("viewBox=\"" + Math.round(minX) + " " +
		// Math.round(minY) + " " + Math.round(getWidth()) + " "
		// + Math.round(getHeight()) + "\"");
		newString.append(">");
		return svg.replaceFirst("(?i)<svg[^>]*>", newString.toString());
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

}
