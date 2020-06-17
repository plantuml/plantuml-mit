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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayerBinary extends Player {

	private final List<TimeConstraint> constraints = new ArrayList<TimeConstraint>();
	private final SortedMap<TimeTick, Boolean> values = new TreeMap<TimeTick, Boolean>();
	private Boolean initialState;

	public PlayerBinary(String code, ISkinParam skinParam, TimingRuler ruler, boolean compact) {
		super(code, skinParam, ruler, compact);
		this.suggestedHeight = 30;
	}

	private double getHeightForConstraints(StringBounder stringBounder) {
		return TimeConstraint.getHeightForConstraints(stringBounder, constraints);
	}

	public double getFullHeight(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + suggestedHeight;
	}

	public void drawFrameTitle(UGraphic ug) {
	}

	private SymbolContext getContext() {
		return new SymbolContext(HColorUtils.COL_D7E0F2, HColorUtils.COL_038048).withStroke(new UStroke(1.5));
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		return new IntricatedPoint(new Point2D.Double(x, getYpos(stringBounder, false)),
				new Point2D.Double(x, getYpos(stringBounder, true)));
	}

	public void addNote(TimeTick now, Display note, Position position) {
		throw new UnsupportedOperationException();
	}

	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	public void setState(TimeTick now, String comment, Colors color, String... states) {
		final boolean state = getState(states[0]);
		if (now == null) {
			this.initialState = state;
		} else {
			this.values.put(now, state);
		}
	}

	private boolean getState(String value) {
		return "1".equals(value) || "high".equalsIgnoreCase(value);
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		this.constraints.add(new TimeConstraint(tick1, tick2, message, skinParam));
	}

	private final double ymargin = 8;

	private double getYpos(StringBounder stringBounder, boolean state) {
		return state ? getYhigh(stringBounder) : getYlow(stringBounder);
	}

	private double getYlow(StringBounder stringBounder) {
		return getFullHeight(stringBounder) - ymargin;
	}

	private double getYhigh(StringBounder stringBounder) {
		return ymargin + getHeightForConstraints(stringBounder);
	}

	public TextBlock getPart1(double fullAvailableWidth, double specialVSpace) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final TextBlock title = getTitle();
				final Dimension2D dim = title.calculateDimension(stringBounder);
				final double y = (getFullHeight(stringBounder) - dim.getHeight()) / 2;
				title.drawU(ug.apply(UTranslate.dy(y)));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim = getTitle().calculateDimension(stringBounder);
				return Dimension2DDouble.delta(dim, 5, 0);
			}
		};
	}

	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = getContext().apply(ug);
				double lastx = 0;
				boolean lastValue = initialState == null ? false : initialState;
				final StringBounder stringBounder = ug.getStringBounder();
				final ULine vline = ULine.vline(getYlow(stringBounder) - getYhigh(stringBounder));
				for (Map.Entry<TimeTick, Boolean> ent : values.entrySet()) {
					final double x = ruler.getPosInPixel(ent.getKey());
					ug.apply(new UTranslate(lastx, getYpos(stringBounder, lastValue))).draw(ULine.hline(x - lastx));
					if (lastValue != ent.getValue()) {
						ug.apply(new UTranslate(x, getYhigh(stringBounder))).draw(vline);
					}
					lastx = x;
					lastValue = ent.getValue();
				}
				ug.apply(new UTranslate(lastx, getYpos(stringBounder, lastValue)))
						.draw(ULine.hline(ruler.getWidth() - lastx));

				drawConstraints(ug.apply(UTranslate.dy(getHeightForConstraints(ug.getStringBounder()))));

			}
		};
	}

	private void drawConstraints(final UGraphic ug) {
		for (TimeConstraint constraint : constraints) {
			constraint.drawU(ug, ruler);
		}
	}

}
