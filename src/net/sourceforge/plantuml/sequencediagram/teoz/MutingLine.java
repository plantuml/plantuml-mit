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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MutingLine {

	private final Rose skin;
	private final ISkinParam skinParam;
	private final boolean useContinueLineBecauseOfDelay;
	private final Map<Double, Double> delays = new TreeMap<Double, Double>();
	private final StyleBuilder styleBuilder;

	public MutingLine(Rose skin, ISkinParam skinParam, List<Event> events) {
		this.skin = skin;
		this.skinParam = skinParam;
		this.useContinueLineBecauseOfDelay = useContinueLineBecauseOfDelay(events);
		this.styleBuilder = skinParam.getCurrentStyleBuilder();
	}

	private boolean useContinueLineBecauseOfDelay(List<Event> events) {
		final String strategy = skinParam.getValue("lifelineStrategy");
		if ("nosolid".equalsIgnoreCase(strategy)) {
			return false;
		}
		for (Event ev : events) {
			if (ev instanceof Delay) {
				return true;
			}
		}
		return false;
	}

	public void drawLine(UGraphic ug, Context2D context, double createY, double endY) {
		final ComponentType defaultLineType = useContinueLineBecauseOfDelay ? ComponentType.CONTINUE_LINE
				: ComponentType.PARTICIPANT_LINE;
		if (delays.size() > 0) {
			double y = createY;
			for (Map.Entry<Double, Double> ent : delays.entrySet()) {
				if (ent.getKey() >= createY) {
					drawInternal(ug, context, y, ent.getKey(), defaultLineType);
					drawInternal(ug, context, ent.getKey(), ent.getKey() + ent.getValue(), ComponentType.DELAY_LINE);
					y = ent.getKey() + ent.getValue();
				}
			}
			drawInternal(ug, context, y, endY, defaultLineType);
		} else {
			drawInternal(ug, context, createY, endY, defaultLineType);
		}
	}

	private void drawInternal(UGraphic ug, Context2D context, double y1, double y2, final ComponentType defaultLineType) {
		if (y2 == y1) {
			return;
		}
		if (y2 < y1) {
			throw new IllegalArgumentException();
		}
		final Style style = defaultLineType.getDefaultStyleDefinition().getMergedStyle(styleBuilder);
		final Component comp = skin.createComponent(new Style[] { style }, defaultLineType, null, skinParam, null);
		final Dimension2D dim = comp.getPreferredDimension(ug.getStringBounder());
		final Area area = new Area(dim.getWidth(), y2 - y1);
		comp.drawU(ug.apply(new UTranslate(0, y1)), area, context);
	}

	public void delayOn(double y, double height) {
		delays.put(y, height);
	}

}
