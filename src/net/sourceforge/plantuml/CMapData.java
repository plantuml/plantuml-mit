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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CMapData {

	private final StringBuilder stringBuilder = new StringBuilder();

	public String asString(String nameId) {
		return "<map id=\"" + nameId + "_map\" name=\"" + nameId + "_map\">\n" + stringBuilder.toString() + "</map>\n";
	}

	public boolean containsData() {
		return stringBuilder.length() > 0;
	}

	public void appendString(String s) {
		stringBuilder.append(s);
	}

	public void appendLong(long s) {
		stringBuilder.append(s);
	}

	private void appendUrl(int seq, Url url, double scale) {
		appendString("<area shape=\"rect\" id=\"id");
		appendLong(seq);
		appendString("\" href=\"");
		appendString(url.getUrl());
		appendString("\" title=\"");
		final String tooltip = url.getTooltip().replaceAll("\\\\n", BackSlash.NEWLINE).replaceAll("&", "&#38;")
				.replaceAll("\"", "&#34;").replaceAll("\'", "&#39;");
		appendString(tooltip);
		appendString("\" alt=\"\" coords=\"");
		appendString(url.getCoords(scale));
		appendString("\"/>");

		appendString(BackSlash.NEWLINE);
	}

	public static CMapData cmapString(Set<Url> allUrlEncountered, double scale) {
		final CMapData cmapdata = new CMapData();

		final List<Url> all = new ArrayList<Url>(allUrlEncountered);
		Collections.sort(all, Url.SURFACE_COMPARATOR);

		int seq = 1;
		for (Url u : all) {
			if (u.hasData() == false) {
				continue;
			}
			cmapdata.appendUrl(seq, u, scale);
			seq++;
		}
		return cmapdata;
	}

}
