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

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.api.NumberAnalyzed;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class FormatCounter {

	private ConcurrentMap<FileFormat, NumberAnalyzed> data = new ConcurrentHashMap<FileFormat, NumberAnalyzed>();

	public FormatCounter(String prefix) {
		for (FileFormat format : FileFormat.values()) {
			final String key = prefix + format.name();
			data.put(format, new NumberAnalyzed(key));
		}

	}

	public void plusOne(FileFormat fileFormat, long duration) {
		final NumberAnalyzed n = data.get(fileFormat);
		n.addValue(duration);
	}

	private StatsLine createLine(String name, NumberAnalyzed n) {
		final Map<StatsColumn, Object> result = new EnumMap<StatsColumn, Object>(StatsColumn.class);
		result.put(StatsColumn.FORMAT, name);
		result.put(StatsColumn.GENERATED_COUNT, n.getNb());
		result.put(StatsColumn.GENERATED_MEAN_TIME, n.getMean());
		result.put(StatsColumn.GENERATED_STANDARD_DEVIATION, n.getStandardDeviation());
		result.put(StatsColumn.GENERATED_MAX_TIME, n.getMax());
		return new StatsLineImpl(result);
	}

	public StatsTable getStatsTable(String name) {
		final StatsTableImpl result = new StatsTableImpl(name);
		final NumberAnalyzed total = new NumberAnalyzed();
		for (Map.Entry<FileFormat, NumberAnalyzed> ent : data.entrySet()) {
			final NumberAnalyzed n = ent.getValue();
			if (n.getNb() > 0) {
				result.addLine(createLine(ent.getKey().name(), n));
				total.add(n);
			}
		}
		result.addLine(createLine("Total", total));
		return result;
	}

	public void reload(String prefix, Preferences prefs) throws BackingStoreException {
		for (String key : prefs.keys()) {
			if (key.startsWith(prefix)) {
				try {
					final String name = removeDotSaved(key);
					final NumberAnalyzed value = NumberAnalyzed.load(name, prefs);
					if (value != null) {
						final FileFormat format = FileFormat.valueOf(name.substring(prefix.length()));
						data.put(format, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	static String removeDotSaved(String key) {
		return key.substring(0, key.length() - ".saved".length());
	}

	public void save(Preferences prefs, FileFormat fileFormat) {
		data.get(fileFormat).save(prefs);
	}

}
