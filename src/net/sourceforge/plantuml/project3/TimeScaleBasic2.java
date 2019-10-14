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
package net.sourceforge.plantuml.project3;

import java.util.Map;
import java.util.TreeMap;

public class TimeScaleBasic2 implements TimeScale {

	private final GCalendar calendar;
	private final GCalendar calendarAllOpen;
	private final TimeScaleBasic basic = new TimeScaleBasic();
	private final Map<Instant, Instant> cache = new TreeMap<Instant, Instant>();

	public TimeScaleBasic2(GCalendarSimple calendar) {
		this.calendar = calendar;
		this.calendarAllOpen = calendar;
	}

	private Instant changeInstantSlow(Instant instant) {
		final DayAsDate day = calendar.toDayAsDate((InstantDay) instant);
		return calendarAllOpen.fromDayAsDate(day);
	}

	private Instant changeInstant(Instant instant) {
		Instant result = cache.get(instant);
		if (result == null) {
			result = changeInstantSlow(instant);
			cache.put(instant, result);
		}
		return result;
	}

	public double getStartingPosition(Instant instant) {
		return basic.getStartingPosition(changeInstant(instant));
	}

	public double getEndingPosition(Instant instant) {
		return basic.getEndingPosition(changeInstant(instant));
	}

	public double getWidth(Instant instant) {
		return basic.getWidth(changeInstant(instant));
	}

}
