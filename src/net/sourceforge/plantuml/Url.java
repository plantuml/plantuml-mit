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
package net.sourceforge.plantuml;

import java.util.Comparator;

import net.sourceforge.plantuml.cucadiagram.dot.DotMaker2;

public class Url implements EnsureVisible {

	private final String url;
	private final String tooltip;
	private final String label;
	private boolean member;

	public Url(String url, String tooltip) {
		this(url, tooltip, null);
	}

	public Url(String url, String tooltip, String label) {
		if (url.contains("{")) {
			throw new IllegalArgumentException(url);
		}
		url = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(url, "\"");
		this.url = url;
		if (tooltip == null) {
			this.tooltip = url;
		} else {
			this.tooltip = BackSlash.manageNewLine(tooltip);
		}
		if (label == null || label.length() == 0) {
			this.label = url;
		} else {
			this.label = label;
		}
	}

	public static boolean isLatex(String pendingUrl) {
		return pendingUrl.startsWith("latex://");
	}

	public boolean isLatex() {
		return isLatex(url);
	}

	public final String getUrl() {
		return url;
	}

	public final String getTooltip() {
		return tooltip;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return super.toString() + " " + url + " " + visible.getCoords(1.0);
	}

	public String getCoords(double scale) {
		if (DotMaker2.isJunit() && visible.getCoords(1.0).contains("0,0,0,0")) {
			throw new IllegalStateException(toString());
		}
		return visible.getCoords(scale);
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public final boolean isMember() {
		return member;
	}

	private final BasicEnsureVisible visible = new BasicEnsureVisible();

	public void ensureVisible(double x, double y) {
		visible.ensureVisible(x, y);
	}

	public boolean hasData() {
		return visible.hasData();
	}

	public static final Comparator<Url> SURFACE_COMPARATOR = new Comparator<Url>() {
		public int compare(Url url1, Url url2) {
			final double surface1 = url1.visible.getSurface();
			final double surface2 = url2.visible.getSurface();
			if (surface1 > surface2) {
				return 1;
			} else if (surface1 < surface2) {
				return -1;
			}
			return 0;
		}
	};

}
