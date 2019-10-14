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

import java.math.BigDecimal;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PlayerClock extends ReallyAbstractPlayer implements Player {

	private final int period;
	private final int pulse;

	public PlayerClock(TitleStrategy titleStrategy, ISkinParam skinParam, TimingRuler ruler, int period, int pulse) {
		super(titleStrategy, "", skinParam, ruler);
		this.period = period;
		this.pulse = pulse;
	}

	public double getHeight(StringBounder striWngBounder) {
		return 30;
	}

	private SymbolContext getContext() {
		return new SymbolContext(HtmlColorUtils.COL_D7E0F2, HtmlColorUtils.COL_038048).withStroke(new UStroke(1.5));
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		throw new UnsupportedOperationException();
	}

	public void addNote(TimeTick now, Display note, Position position) {
		throw new UnsupportedOperationException();
	}

	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	public void setState(TimeTick now, String comment, Colors color, String... states) {
		throw new UnsupportedOperationException();
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		throw new UnsupportedOperationException();
	}

	private final double ymargin = 8;

	public void drawFrameTitle(UGraphic ug) {
	}

	public void drawContent(UGraphic ug) {
		ug = getContext().apply(ug);
		final ULine vline = new ULine(0, getHeight(ug.getStringBounder()) - 2 * ymargin);
		int i = 0;
		double lastx = -Double.MAX_VALUE;
		while (i < 1000) {
			final double x = ruler.getPosInPixel(new TimeTick(new BigDecimal(i * period), TimingFormat.DECIMAL));
			if (x > ruler.getWidth()) {
				return;
			}
			i++;
			if (x > lastx) {
				final double dx = x - lastx;
				final ULine hline1 = new ULine(dx * getPulseCoef(), 0);
				final ULine hline2 = new ULine(dx * (1 - getPulseCoef()), 0);
				ug.apply(new UTranslate(lastx, ymargin)).draw(vline);
				ug.apply(new UTranslate(lastx, ymargin)).draw(hline1);
				final double x2 = lastx + dx * getPulseCoef();
				ug.apply(new UTranslate(x2, ymargin)).draw(vline);
				ug.apply(new UTranslate(x2, ymargin + vline.getDY())).draw(hline2);
			}
			lastx = x;
		}
	}

	private double getPulseCoef() {
		if (pulse == 0) {
			return 0.5;
		}
		return 1.0 * pulse / period;
	}

	public void drawLeftHeader(UGraphic ug) {

	}

	public double getWidthHeader(StringBounder stringBounder) {
		return 0;
	}

	public final int getPeriod() {
		return period;
	}

}
