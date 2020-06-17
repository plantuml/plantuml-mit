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
package net.sourceforge.plantuml.timingdiagram;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public enum TimingFormat {
	DECIMAL, HOUR, DATE;

	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	private static final GregorianCalendar gc = new GregorianCalendar(TimingFormat.GMT);

	public String formatTime(BigDecimal time) {
		if (this == HOUR || this == DATE) {
			return formatTime(time.longValueExact());
		}
		return time.toPlainString();
	}

	public String formatTime(long time) {
		if (this == HOUR) {
			final int s = (int) time % 60;
			final int m = (int) (time / 60) % 60;
			final int h = (int) (time / 3600);
			return String.format("%d:%02d:%02d", h, m, s);
		}
		if (this == DATE) {
			final int yyyy;
			final int mm;
			final int dd;
			synchronized (gc) {
				gc.setTimeInMillis(time * 1000L);
				yyyy = gc.get(Calendar.YEAR);
				mm = gc.get(Calendar.MONTH) + 1;
				dd = gc.get(Calendar.DAY_OF_MONTH);
			}
			// return String.format("%04d/%02d/%02d", yyyy, mm, dd);
			return String.format("%02d/%02d", mm, dd);
		}
		return "" + time;
	}

	public static TimeTick createDate(final int yyyy, final int mm, final int dd) {
		final long timeInMillis;
		synchronized (gc) {
			gc.setTimeInMillis(0);
			gc.set(yyyy, mm - 1, dd);
			timeInMillis = gc.getTimeInMillis() / 1000L;
		}
		return new TimeTick(new BigDecimal(timeInMillis), TimingFormat.DATE);
	}

}
