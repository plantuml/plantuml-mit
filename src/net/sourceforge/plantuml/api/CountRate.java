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

public final class CountRate {

	private final MagicArray lastMinute = new MagicArray(60);
	private final MagicArray lastHour = new MagicArray(60);
	private final MagicArray lastDay = new MagicArray(140);

	public void increment() {
		final long now = System.currentTimeMillis();
		lastMinute.incKey(now / 1000L);
		lastHour.incKey(now / (60 * 1000L));
		lastDay.incKey(now / (10 * 60 * 1000L));
	}

	public void increment(int value) {
		final long now = System.currentTimeMillis();
		lastMinute.incKey(now / 1000L, value);
		lastHour.incKey(now / (60 * 1000L), value);
		lastDay.incKey(now / (10 * 60 * 1000L), value);
	}

	public long perMinute() {
		return lastMinute.getSum();
	}

	public long perHour() {
		return lastHour.getSum();
	}

	public long perDay() {
		return lastDay.getSum();
	}

	public long perMinuteMax() {
		return lastMinute.getMaxSum();
	}

	public long perHourMax() {
		return lastHour.getMaxSum();
	}

	public long perDayMax() {
		return lastDay.getMaxSum();
	}

}
