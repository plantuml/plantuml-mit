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
package net.sourceforge.plantuml.stats;

import java.io.PrintStream;
import java.util.Date;

import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;

public class TextConverter {

	private final Stats stats;
	private int linesUsed;

	public TextConverter(Stats stats) {
		this.stats = stats;
	}

	public void printMe(PrintStream ps) {
		final TextTable table = new TextTable();
		table.addSeparator();
		table.addLine("ID", "Start", "Duration", "Generated", "Mean(ms)");
		// table.addLine("ID", "Start", "Last", "Parsed", "Mean(ms)", "Generated", "Mean(ms)");
		table.addSeparator();
		for (StatsLine line : stats.getLastSessions().getLines()) {
			Object id = (Long) line.getValue(StatsColumn.SESSION_ID);
			if (id == null) {
				id = "";
			}
			final Date start = (Date) line.getValue(StatsColumn.STARTING);
			// final Date end = (Date) line.getValue(StatsColumn.LAST);
			// final Long parsed = (Long) line.getValue(StatsColumn.PARSED_COUNT);
			final String duration = line.getValue(StatsColumn.DURATION_STRING).toString();
			final Long generated = (Long) line.getValue(StatsColumn.GENERATED_COUNT);
			final Long generated_ms = (Long) line.getValue(StatsColumn.GENERATED_MEAN_TIME);
			table.addLine(id, start, duration, generated, generated_ms);

		}
		table.addSeparator();
		linesUsed = table.getLines();
		table.printMe(ps);
	}

	public int getLinesUsed() {
		return linesUsed;
	}

	public static void main(String[] args) {
		StatsUtils.dumpStats();

	}

}
