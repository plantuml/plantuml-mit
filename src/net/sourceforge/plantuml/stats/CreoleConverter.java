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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class CreoleConverter {

	private final DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	private final Stats stats;

	public CreoleConverter(Stats stats) {
		this.stats = stats;
	}

	public List<String> toCreole() {
		final List<String> result = new ArrayList<String>();
		result.add("<b><size:16>Statistics</b>");
		printTableCreole(result, stats.getLastSessions());
		result.add(" ");
		result.add("<b><size:16>Current session statistics</b>");
		printTableCreole(result, stats.getCurrentSessionByDiagramType());
		result.add(" ");
		printTableCreole(result, stats.getCurrentSessionByFormat());
		result.add(" ");
		result.add("<b><size:16>General statistics since ever</b>");
		printTableCreole(result, stats.getAllByDiagramType());
		result.add(" ");
		printTableCreole(result, stats.getAllByFormat());
		return result;
	}

	private void printTableCreole(List<String> strings, StatsTable table) {
		final Collection<StatsColumn> headers = table.getColumnHeaders();
		strings.add(getCreoleHeader(headers));
		final List<StatsLine> lines = table.getLines();
		for (int i = 0; i < lines.size(); i++) {
			final StatsLine line = lines.get(i);
			final boolean bold = i == lines.size() - 1;
			strings.add(getCreoleLine(headers, line, bold));

		}
	}

	private String getCreoleLine(Collection<StatsColumn> headers, StatsLine line, boolean bold) {
		final StringBuilder result = new StringBuilder();
		for (StatsColumn col : headers) {
			final Object v = line.getValue(col);
			result.append("|");
			if (v instanceof Long || v instanceof HumanDuration) {
				result.append("<r> ");
			} else {
				result.append(" ");
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
			result.append(" ");
		}
		result.append("|");
		return result.toString();
	}

	private String getCreoleHeader(Collection<StatsColumn> headers) {
		final StringBuilder sb = new StringBuilder();
		for (StatsColumn col : headers) {
			sb.append("| ");
			sb.append(col.getTitle());
			sb.append(" ");
		}
		sb.append("|");
		return sb.toString();
	}

}
