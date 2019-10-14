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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PlayerBinary extends ReallyAbstractPlayer implements Player {

	private static final int HEIGHT = 30;
	private final SortedMap<TimeTick, Boolean> values = new TreeMap<TimeTick, Boolean>();

	public PlayerBinary(TitleStrategy titleStrategy, String code, ISkinParam skinParam, TimingRuler ruler) {
		super(titleStrategy, code, skinParam, ruler);
	}

	public double getHeight(StringBounder stringBounder) {
		return HEIGHT;
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
		final boolean state = getState(states[0]);
		this.values.put(now, state);
	}

	private boolean getState(String value) {
		return "1".equals(value) || "high".equalsIgnoreCase(value);
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		throw new UnsupportedOperationException();
	}

	private final double ymargin = 8;

	public void drawFrameTitle(UGraphic ug) {
	}

	private double getYpos(boolean state) {
		return state ? ymargin : HEIGHT - ymargin;
	}

	public void drawContent(UGraphic ug) {
		ug = getContext().apply(ug);
		double lastx = 0;
		boolean lastValue = false;
		for (Map.Entry<TimeTick, Boolean> ent : values.entrySet()) {
			final double x = ruler.getPosInPixel(ent.getKey());
			ug.apply(new UTranslate(lastx, getYpos(lastValue))).draw(new ULine(x - lastx, 0));
			if (lastValue != ent.getValue()) {
				ug.apply(new UTranslate(x, ymargin)).draw(new ULine(0, HEIGHT - 2 * ymargin));
			}
			lastx = x;
			lastValue = ent.getValue();
		}
		ug.apply(new UTranslate(lastx, getYpos(lastValue))).draw(new ULine(ruler.getWidth() - lastx, 0));
	}

	public void drawLeftHeader(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlock title = getTitle();
		final Dimension2D dim = title.calculateDimension(stringBounder);
		final double y = (getHeight(stringBounder) - dim.getHeight()) / 2;
		title.drawU(ug.apply(new UTranslate(0, y)));
	}

	public double getWidthHeader(StringBounder stringBounder) {
		return getTitle().calculateDimension(stringBounder).getWidth() + 5;
	}

}
