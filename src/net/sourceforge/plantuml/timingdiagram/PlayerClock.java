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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayerClock extends Player {

	private final int period;
	private final int pulse;
	private final double ymargin = 8;

	public PlayerClock(ISkinParam skinParam, TimingRuler ruler, int period, int pulse, boolean compact) {
		super("", skinParam, ruler, compact);
		this.period = period;
		this.pulse = pulse;
		this.suggestedHeight = 30;
	}

	public double getFullHeight(StringBounder striWngBounder) {
		return suggestedHeight;
	}

	public void drawFrameTitle(UGraphic ug) {
	}

	private SymbolContext getContext() {
		return new SymbolContext(HColorUtils.COL_D7E0F2, HColorUtils.COL_038048).withStroke(new UStroke(1.5));
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

	private double getPulseCoef() {
		if (pulse == 0) {
			return 0.5;
		}
		return 1.0 * pulse / period;
	}

	public final int getPeriod() {
		return period;
	}

	public TextBlock getPart1(double fullAvailableWidth, double specialVSpace) {
		return TextBlockUtils.empty(0, 0);
	}

	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = getContext().apply(ug);
				final ULine vline = ULine.vline(getFullHeight(ug.getStringBounder()) - 2 * ymargin);
				int i = 0;
				double lastx = -Double.MAX_VALUE;
				while (i < 1000) {
					final double x = ruler
							.getPosInPixel(new TimeTick(new BigDecimal(i * period), TimingFormat.DECIMAL));
					if (x > ruler.getWidth()) {
						return;
					}
					i++;
					if (x > lastx) {
						final double dx = x - lastx;
						final ULine hline1 = ULine.hline(dx * getPulseCoef());
						final ULine hline2 = ULine.hline(dx * (1 - getPulseCoef()));
						ug.apply(new UTranslate(lastx, ymargin)).draw(vline);
						ug.apply(new UTranslate(lastx, ymargin)).draw(hline1);
						final double x2 = lastx + dx * getPulseCoef();
						ug.apply(new UTranslate(x2, ymargin)).draw(vline);
						ug.apply(new UTranslate(x2, ymargin + vline.getDY())).draw(hline2);
					}
					lastx = x;
				}
			}
		};
	}

}
