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
package net.sourceforge.plantuml.timingdiagram;

import java.awt.geom.Dimension2D;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimingRuler {

	private final SortedSet<TimeTick> times = new TreeSet<TimeTick>();

	private final ISkinParam skinParam;

	private long tickIntervalInPixels = 50;
	private long tickUnitary;

	private TimingFormat format = TimingFormat.DECIMAL;

	public void ensureNotEmpty() {
		if (times.size() == 0) {
			this.times.add(new TimeTick(BigDecimal.ZERO, TimingFormat.DECIMAL));
		}

	}

	public TimingRuler(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public void scaleInPixels(long tick, long pixel) {
		this.tickIntervalInPixels = pixel;
		this.tickUnitary = tick;
	}

	private long tickUnitary() {
		if (tickUnitary == 0) {
			return highestCommonFactor();
		}
		return tickUnitary;

	}

	private long highestCommonFactorInternal = -1;

	private long highestCommonFactor() {
		if (highestCommonFactorInternal == -1) {
			for (TimeTick time : times) {
				long tick = time.getTime().longValue();
				if (tick > 0) {
					if (highestCommonFactorInternal == -1) {
						highestCommonFactorInternal = time.getTime().longValue();
					} else {
						highestCommonFactorInternal = computeHighestCommonFactor(highestCommonFactorInternal,
								Math.abs(time.getTime().longValue()));
					}
				}
			}
		}
		return highestCommonFactorInternal;
	}

	private int getNbTick(boolean capped) {
		if (times.size() == 0) {
			return 1;
		}
		final long delta = getMax().getTime().longValue() - getMin().getTime().longValue();
		return Math.min(1000, (int) (1 + delta / tickUnitary()));
	}

	public double getWidth() {
		return getNbTick(false) * tickIntervalInPixels;
	}

	public final double getPosInPixel(TimeTick when) {
		return getPosInPixelInternal(when.getTime().doubleValue());
	}

	private double getPosInPixelInternal(double time) {
		time -= getMin().getTime().doubleValue();
		return time / tickUnitary() * tickIntervalInPixels;
	}

	public void addTime(TimeTick time) {
		this.highestCommonFactorInternal = -1;
		times.add(time);
		if (time.getFormat() != TimingFormat.DECIMAL) {
			this.format = time.getFormat();
		}
	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.TIMING, null);
	}

	private TextBlock getTimeTextBlock(long time) {
		final Display display = Display.getWithNewlines(format.formatTime(time));
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public void drawTimeAxis(UGraphic ug) {
		ug = ug.apply(new UStroke(2.0)).apply(new UChangeColor(HtmlColorUtils.BLACK));
		final double tickHeight = 5;
		final ULine line = new ULine(0, tickHeight);
		final int nb = getNbTick(true);
		for (int i = 0; i <= nb; i++) {
			ug.apply(new UTranslate(tickIntervalInPixels * i, 0)).draw(line);
		}
		ug.draw(new ULine(nb * tickIntervalInPixels, 0));

		for (long round : roundValues()) {
			final TextBlock text = getTimeTextBlock(round);
			final Dimension2D dim = text.calculateDimension(ug.getStringBounder());
			text.drawU(ug.apply(new UTranslate(getPosInPixelInternal(round) - dim.getWidth() / 2, tickHeight + 1)));
		}
	}

	private Collection<Long> roundValues() {
		final SortedSet<Long> result = new TreeSet<Long>();
		if (tickUnitary == 0) {
			for (TimeTick tick : times) {
				final long round = tick.getTime().longValue();
				result.add(round);
			}
		} else {
			final int nb = getNbTick(true);
			for (int i = 0; i <= nb; i++) {
				final long round = tickUnitary * i;
				result.add(round);
			}
		}
		if (result.first() < 0 && result.last() > 0) {
			result.add(0L);
		}
		return result;
	}

	public void draw0(UGraphic ug, double height) {
		ug = ug.apply(new UStroke(3, 5, 0.5)).apply(new UChangeColor(new HtmlColorSetSimple().getColorIfValid("#AAA")));
		final ULine line = new ULine(0, height);
		final int nb = getNbTick(true);
		for (int i = 0; i <= nb; i++) {
			ug.apply(new UTranslate(tickIntervalInPixels * i, 0)).draw(line);
		}
	}

	public double getHeight(StringBounder stringBounder) {
		return getTimeTextBlock(0).calculateDimension(stringBounder).getHeight();
	}

	private TimeTick getMax() {
		return times.last();
	}

	private TimeTick getMin() {
		return times.first();
	}

	private static long computeHighestCommonFactor(long a, long b) {
		long r = a;
		while (r != 0) {
			r = a % b;
			a = b;
			b = r;
		}
		return (Math.abs(a));
	}

}