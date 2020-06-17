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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;

public class SvgResult {

	public static final String D_EQUALS = "d=\"";
	public static final String POINTS_EQUALS = "points=\"";

	private final String svg;
	private final Point2DFunction function;

	public SvgResult(String svg, Point2DFunction function) {
		this.svg = svg;
		this.function = function;
	}

	public PointListIterator getPointsWithThisColor(int lineColor) {
		return PointListIteratorImpl.create(this, lineColor);
	}

	public List<Point2D.Double> extractList(final String searched) {
		final int p2 = this.indexOf(searched, 0);
		if (p2 == -1) {
			return Collections.emptyList();
		}
		final int p3 = this.indexOf("\"", p2 + searched.length());
		if (p3 == -1) {
			return Collections.emptyList();
		}
		return this.substring(p2 + searched.length(), p3).getPoints(" MC");
	}

	public int getIndexFromColor(int color) {
		String s = "stroke=\"" + StringUtils.goLowerCase(DotStringFactory.sharp000000(color)) + "\"";
		int idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = ";stroke:" + StringUtils.goLowerCase(DotStringFactory.sharp000000(color)) + ";";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = "fill=\"" + StringUtils.goLowerCase(DotStringFactory.sharp000000(color)) + "\"";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		// Log.info("Cannot find color=" + color + " " + StringUtils.goLowerCase(StringUtils.getAsHtml(color)));
		return -1;

	}

	public List<Point2D.Double> getPoints(String separator) {
		try {
			final StringTokenizer st = new StringTokenizer(svg, separator);
			final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
			while (st.hasMoreTokens()) {
				result.add(getFirstPoint(st.nextToken()));
			}
			return result;
		} catch (NumberFormatException e) {
			return Collections.emptyList();
		}
	}

	public Point2D.Double getNextPoint() {
		return getFirstPoint(svg);
	}

	private Point2D.Double getFirstPoint(final String tmp) {
		final StringTokenizer st = new StringTokenizer(tmp, ",");
		final double startX = Double.parseDouble(st.nextToken());
		final double startY = Double.parseDouble(st.nextToken());
		return function.apply(new Point2D.Double(startX, startY));
	}

	public int indexOf(String s, int pos) {
		return svg.indexOf(s, pos);
	}

	public SvgResult substring(int pos) {
		return new SvgResult(svg.substring(pos), function);
	}

	public SvgResult substring(int start, int end) {
		return new SvgResult(svg.substring(start, end), function);
	}

	public final String getSvg() {
		return svg;
	}
}
