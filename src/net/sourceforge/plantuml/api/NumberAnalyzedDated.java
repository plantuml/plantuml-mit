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
package net.sourceforge.plantuml.api;

import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.Log;

public class NumberAnalyzedDated extends NumberAnalyzed {

	private final AtomicLong created = new AtomicLong();
	private final AtomicLong modified = new AtomicLong();
	private String comment;

	private NumberAnalyzedDated(String name, long nb, long sum, long min, long max, long sumOfSquare, long sliddingSum,
			long created, long modified, String comment) {
		super(name, nb, sum, min, max, sumOfSquare, sliddingSum);
		this.created.set(created);
		this.modified.set(modified);
		this.comment = comment;
	}

	@Override
	public synchronized void reset() {
		super.reset();
		resetCreatedModifiedComment();
	}

	public NumberAnalyzedDated() {
		super();
		resetCreatedModifiedComment();
	}

	public NumberAnalyzedDated(String name) {
		super(name);
		resetCreatedModifiedComment();
	}

	private void resetCreatedModifiedComment() {
		final long now = System.currentTimeMillis();
		this.created.set(now);
		this.modified.set(now);
		this.comment = " ";
	};

	@Override
	public void addValue(long v) {
		super.addValue(v);
		this.modified.set(System.currentTimeMillis());
	}

	@Override
	public void add(NumberAnalyzed other) {
		super.add(other);
		this.modified.set(System.currentTimeMillis());
	}

	@Override
	protected String getSavedSupplementatyData() {
		return longToString(created.get()) + ";" + longToString(modified.get()) + ";" + comment;
	}

	public static NumberAnalyzedDated load(String name, Preferences prefs) {
		final String value = prefs.get(name + ".saved", "");
		if (value.length() == 0) {
			Log.info("Cannot load " + name);
			return null;
		}
		try {
			final StringTokenizer st = new StringTokenizer(value, ";");
			return new NumberAnalyzedDated(name, Long.parseLong(st.nextToken(), 36),
					Long.parseLong(st.nextToken(), 36), Long.parseLong(st.nextToken(), 36), Long.parseLong(
							st.nextToken(), 36), Long.parseLong(st.nextToken(), 36),
					Long.parseLong(st.nextToken(), 36), Long.parseLong(st.nextToken(), 36), Long.parseLong(
							st.nextToken(), 36), st.nextToken());
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error reading " + value);
			return null;
		}
	}

	final public long getCreationTime() {
		return created.get();
	}

	final public long getModificationTime() {
		return modified.get();
	}

	final public synchronized String getComment() {
		return comment;
	}

	final public synchronized void setComment(String comment) {
		this.comment = comment;
	}

}
