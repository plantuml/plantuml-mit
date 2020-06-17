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

public abstract class Log {

	private static final long start = System.currentTimeMillis();

	public synchronized static void debug(String s) {
	}

	public synchronized static void info(String s) {
		if (OptionFlags.getInstance().isVerbose()) {
			ProgressBar.clear();
			System.err.println(format(s));
		}
	}

	public synchronized static void error(String s) {
		ProgressBar.clear();
		System.err.println(s);
	}

	private static String format(String s) {
		final long delta = System.currentTimeMillis() - start;
		// final HealthCheck healthCheck = Performance.getHealthCheck();
		// final long cpu = healthCheck.jvmCpuTime() / 1000L / 1000L;
		// final long dot = healthCheck.dotTime().getSum();

		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long maxMemory = Runtime.getRuntime().maxMemory();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long usedMemory = totalMemory - freeMemory;
		final int threadActiveCount = Thread.activeCount();

		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(delta / 1000L);
		sb.append(".");
		sb.append(String.format("%03d", delta % 1000L));
		// if (cpu != -1) {
		// sb.append(" - ");
		// sb.append(cpu / 1000L);
		// sb.append(".");
		// sb.append(String.format("%03d", cpu % 1000L));
		// }
		// sb.append(" - ");
		// sb.append(dot / 1000L);
		// sb.append(".");
		// sb.append(String.format("%03d", dot % 1000L));
		// sb.append("(");
		// sb.append(healthCheck.dotTime().getNb());
		// sb.append(")");
		sb.append(" - ");
		final long total = totalMemory / 1024 / 1024;
		final long free = freeMemory / 1024 / 1024;
		sb.append(total);
		sb.append(" Mo) ");
		sb.append(free);
		sb.append(" Mo - ");
		sb.append(s);
		return sb.toString();

	}

	public static void println(Object s) {
		// if (header2.get() == null) {
		// System.err.println("L = " + s);
		// } else {
		// System.err.println(header2.get() + " " + s);
		// }
	}

	// private static final ThreadLocal<String> header2 = new ThreadLocal<String>();
	//
	public static void header(String s) {
		// header2.set(s);
	}
}
