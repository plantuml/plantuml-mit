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
package net.sourceforge.plantuml.api;

import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.Log;

public class NumberAnalyzed implements INumberAnalyzed {

	private static final int SLIDING_WINDOW = 1024;

	private long nb;
	private long sum;
	private long min;
	private long max;
	private long sumOfSquare;
	// See https://fossies.org/linux/haproxy/include/proto/freq_ctr.h
	private long sliddingSum;
	private final String name;

	private final Lock saveLock = new ReentrantLock();

	public NumberAnalyzed(String name) {
		this.name = name;
	}

	public synchronized void reset() {
		this.nb = 0;
		this.sum = 0;
		this.min = 0;
		this.max = 0;
		this.sumOfSquare = 0;
		this.sliddingSum = 0;
	}

	public NumberAnalyzed() {
		this("");
	}

	public final void save(Preferences prefs) {
		if (name.length() == 0) {
			throw new UnsupportedOperationException();
		}
		if (saveLock.tryLock())
			try {
				prefs.put(name + ".saved", getSavedString());
			} finally {
				saveLock.unlock();
			}
	}

	private String getSavedString() {
		final long nb1;
		final long sum1;
		final long min1;
		final long max1;
		final long sumOfSquare1;
		final long sliddingSum1;
		final String supp1;
		synchronized (this) {
			nb1 = nb;
			sum1 = sum;
			min1 = min;
			max1 = max;
			sumOfSquare1 = sumOfSquare;
			sliddingSum1 = sliddingSum;
		}
		supp1 = getSavedSupplementatyData();
		return longToString(nb1) + ";" + longToString(sum1) + ";" + longToString(min1) + ";" + longToString(max1) + ";"
				+ longToString(sumOfSquare1) + ";" + longToString(sliddingSum1) + ";" + supp1 + ";";
	}

	protected String getSavedSupplementatyData() {
		return "";
	}

	protected final String longToString(long val) {
		return Long.toString(val, 36);
	}

	public static NumberAnalyzed load(String name, Preferences prefs) {
		final String value = prefs.get(name + ".saved", "");
		if (value.length() == 0) {
			Log.info("Cannot load " + name);
			return null;
		}
		try {
			final StringTokenizer st = new StringTokenizer(value, ";");
			return new NumberAnalyzed(name, Long.parseLong(st.nextToken(), 36), Long.parseLong(st.nextToken(), 36),
					Long.parseLong(st.nextToken(), 36), Long.parseLong(st.nextToken(), 36), Long.parseLong(
							st.nextToken(), 36), Long.parseLong(st.nextToken(), 36));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error reading " + value);
			return null;
		}
	}

	@Override
	public synchronized String toString() {
		return "sum=" + sum + " nb=" + nb + " min=" + min + " max=" + max + " mean=" + getMean();
	}

	protected NumberAnalyzed(String name, long nb, long sum, long min, long max, long sumOfSquare, long sliddingSum) {
		this(name);
		this.nb = nb;
		this.sum = sum;
		this.min = min;
		this.max = max;
		this.sumOfSquare = sumOfSquare;
		this.sliddingSum = sliddingSum;
	}

	public synchronized INumberAnalyzed getCopyImmutable() {
		final NumberAnalyzed copy = new NumberAnalyzed(name, nb, sum, min, max, sumOfSquare, sliddingSum);
		return copy;
	}

	public synchronized void addValue(long v) {
		nb++;
		if (nb == 1) {
			min = v;
			max = v;
		} else if (v > max) {
			max = v;
		} else if (v < min) {
			min = v;
		}
		sum += v;
		sumOfSquare += v * v;
		sliddingSum = sliddingSum * (SLIDING_WINDOW - 1) / SLIDING_WINDOW + v;
	}

	public void add(NumberAnalyzed other) {
		final long nb1;
		final long sum1;
		final long min1;
		final long max1;
		final long sumOfSquare1;
		final long sliddingSum1;
		synchronized (other) {
			nb1 = other.nb;
			sum1 = other.sum;
			min1 = other.min;
			max1 = other.max;
			sumOfSquare1 = other.sumOfSquare;
			sliddingSum1 = other.sliddingSum;
		}
		synchronized (this) {
			this.sum += sum1;
			this.nb += nb1;
			this.min = Math.min(this.min, min1);
			this.max = Math.max(this.max, max1);
			this.sumOfSquare += sumOfSquare1;
			// Not good!
			this.sliddingSum += sliddingSum1;
		}
	}

	synchronized public final long getNb() {
		return nb;
	}

	synchronized public final long getSum() {
		return sum;
	}

	synchronized public final long getMin() {
		return min;
	}

	synchronized public final long getMax() {
		return max;
	}

	synchronized public final long getMean() {
		if (nb == 0) {
			return 0;
		}
		return sum / nb;
	}

	synchronized public final long getSliddingMean() {
		if (nb == 0) {
			return 0;
		}
		if (nb < SLIDING_WINDOW) {
			return sum / nb;
		}
		return sliddingSum / nb;
	}

	public final long getStandardDeviation() {
		final long sum1;
		final long sumOfSquare1;
		final long nb1;
		synchronized (this) {
			sum1 = this.sum;
			sumOfSquare1 = this.sumOfSquare;
			nb1 = this.nb;
		}
		if (nb1 == 0) {
			return 0;
		}
		final long mean = sum1 / nb1;
		return Math.round(Math.sqrt(sumOfSquare1 / nb1 - mean * mean));
	}

	final public String getName() {
		return name;
	}

}
