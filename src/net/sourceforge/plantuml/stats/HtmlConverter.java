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
package net.sourceforge.plantuml.stats;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class HtmlConverter {

	private final DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	private final Stats stats;

	public HtmlConverter(Stats stats) {
		this.stats = stats;
	}

	public String toHtml() {
		final StringBuilder result = new StringBuilder();
		result.append("<html>");
		result.append("<style type=\"text/css\">");
		result.append("body { font-family: arial, helvetica, sans-serif; font-size: 12px; font-weight: normal; color: black; background: white;}");
		result.append("th,td { font-size: 12px;}");
		result.append("table { border-collapse: collapse; border-style: none;}");
		result.append("</style>");
		result.append("<h2>Statistics</h2>");
		printTableHtml(result, stats.getLastSessions());
		final StatsTable currentSessionByDiagramType = stats.getCurrentSessionByDiagramType();
		if (currentSessionByDiagramType.getLines().size() > 1) {
			result.append("<h2>Current session statistics</h2>");
			printTableHtml(result, currentSessionByDiagramType);
			result.append("<p>");
			printTableHtml(result, stats.getCurrentSessionByFormat());
		}
		result.append("<h2>General statistics since ever</h2>");
		printTableHtml(result, stats.getAllByDiagramType());
		result.append("<p>");
		printTableHtml(result, stats.getAllByFormat());
		result.append("</html>");
		return result.toString();
	}

	private void printTableHtml(StringBuilder result, StatsTable table) {
		final Collection<StatsColumn> headers = table.getColumnHeaders();
		result.append("<table border=1 cellspacing=0 cellpadding=2>");
		result.append(getHtmlHeader(headers));
		final List<StatsLine> lines = table.getLines();
		for (int i = 0; i < lines.size(); i++) {
			final StatsLine line = lines.get(i);
			final boolean bold = i == lines.size() - 1;
			result.append(getCreoleLine(headers, line, bold));

		}
		result.append("</table>");
	}

	private String getCreoleLine(Collection<StatsColumn> headers, StatsLine line, boolean bold) {
		final StringBuilder result = new StringBuilder();
		if (bold) {
			result.append("<tr bgcolor=#f0f0f0>");
		} else {
			result.append("<tr bgcolor=#fcfcfc>");
		}
		for (StatsColumn col : headers) {
			final Object v = line.getValue(col);
			if (v instanceof Long || v instanceof HumanDuration) {
				result.append("<td align=right>");
			} else {
				result.append("<td>");
			}
			if (bold) {
				result.append("<b>");
			}
			if (v instanceof Long) {
				result.append(String.format("%,d", v));
			} else if (v instanceof Date) {
				result.append(formatter.format(v));
			} else if (v == null || v.toString().length() == 0) {
				result.append(" ");
			} else {
				result.append(v.toString());
			}
			if (bold) {
				result.append("</b>");
			}
			result.append("</td>");
		}
		result.append("</tr>");
		return result.toString();
	}

	private String getHtmlHeader(Collection<StatsColumn> headers) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<tr bgcolor=#e0e0e0>");
		for (StatsColumn col : headers) {
			sb.append("<td><b>");
			sb.append(col.getTitle().replace(BackSlash.BS_BS_N, "<br>"));
			sb.append("</b></td>");
		}
		sb.append("</tr>");
		return sb.toString();
	}

}
