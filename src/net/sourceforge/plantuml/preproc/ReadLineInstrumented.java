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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringLocated;

public abstract class ReadLineInstrumented implements ReadLine {

	private static final boolean TRACE = false;

	private static ConcurrentMap<Class, AtomicLong> durations = new ConcurrentHashMap<Class, AtomicLong>();
	private static ConcurrentMap<Class, AtomicLong> maxes = new ConcurrentHashMap<Class, AtomicLong>();

	private long current = 0;

	private AtomicLong get(ConcurrentMap<Class, AtomicLong> source) {
		AtomicLong result = source.get(getClass());
		if (result == null) {
			result = new AtomicLong();
			source.put(getClass(), result);
		}
		return result;
	}

	public final StringLocated readLine() throws IOException {
		if (TRACE == false) {
			return readLineInst();
		}
		final long now = System.currentTimeMillis();
		try {
			return readLineInst();
		} finally {
			final long time = System.currentTimeMillis() - now;
			current += time;
			get(durations).addAndGet(time);
		}
	}

	@Override
	public String toString() {
		return super.toString() + " current=" + current;
	}

	abstract StringLocated readLineInst() throws IOException;

	public final void close() throws IOException {
		if (TRACE) {
			if (current > get(maxes).get()) {
				get(maxes).set(current);
			}
			Log.info("DURATION::" + getClass() + " duration= " + get(durations).get() + " current=" + current + " max="
					+ get(maxes).get());
		}
		closeInst();
	}

	abstract void closeInst() throws IOException;

}
