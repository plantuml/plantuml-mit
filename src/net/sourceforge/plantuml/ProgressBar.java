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

import java.util.concurrent.atomic.AtomicInteger;

public class ProgressBar {

	private static boolean enable;
	private static String last = null;
	private static final AtomicInteger total = new AtomicInteger();
	private static final AtomicInteger done = new AtomicInteger();

	private synchronized static void print(String message) {
		clear();
		System.err.print(message);
		last = message;
	}

	public synchronized static void clear() {
		if (last != null) {
			for (int i = 0; i < last.length(); i++) {
				System.err.print('\b');
			}
			for (int i = 0; i < last.length(); i++) {
				System.err.print(' ');
			}
			for (int i = 0; i < last.length(); i++) {
				System.err.print('\b');
			}
		}
		last = null;
	}

	public static void incTotal(int nb) {
		total.addAndGet(nb);
		printBar(done.intValue(), total.intValue());
	}

	private synchronized static void printBar(int done, int total) {
		if (enable == false) {
			return;
		}
		if (total == 0) {
			return;
		}
		final String message = "[" + getBar(done, total) + "] " + done + "/" + total;
		print(message);

	}

	private static String getBar(int done, int total) {
		final StringBuilder sb = new StringBuilder();
		final int width = 30;
		final int value = width * done / total;
		for (int i = 0; i < width; i++) {
			sb.append(i < value ? '#' : ' ');
		}
		return sb.toString();
	}

	public static void incDone(boolean error) {
		done.incrementAndGet();
		printBar(done.intValue(), total.intValue());
	}

	public static void setEnable(boolean value) {
		enable = value;
	}

}
