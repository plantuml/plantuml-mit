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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class Highlight {

	private final TimeTick tickFrom;
	private final TimeTick tickTo;
	private final Display caption;
	private final Colors colors;

	public Highlight(TimeTick tickFrom, TimeTick tickTo, Display caption, Colors colors) {
		this.tickFrom = tickFrom;
		this.tickTo = tickTo;
		this.caption = caption;
		this.colors = colors;
	}

	private HColor getBackColor() {
		final HColor result = colors.getColor(ColorType.BACK);
		if (result == null) {
			return HColorUtils.COL_A9DCDF;
		}
		return result;
	}

	private HColor getLineColor() {
		final HColor result = colors.getColor(ColorType.LINE);
		if (result == null) {
			return HColorUtils.BLACK;
		}
		return result;
	}

	public final TimeTick getTickFrom() {
		return tickFrom;
	}

	public final TimeTick getTickTo() {
		return tickTo;
	}

	public final Display getCaption() {
		return caption;
	}

	public TextBlock getCaption(ISkinParam skinParam) {
		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.TIMING, null);
		return caption.create(fc, HorizontalAlignment.LEFT, skinParam);
	}

	public void drawHighlightsBack(UGraphic ug, TimingRuler ruler, double height) {
		ug = ug.apply(new HColorNone()).apply(getBackColor().bg());
		final double start = ruler.getPosInPixel(this.getTickFrom());
		final double end = ruler.getPosInPixel(this.getTickTo());
		final URectangle rect = new URectangle(end - start, height);
		ug.apply(UTranslate.dx(start)).draw(rect);
	}

	public void drawHighlightsLines(UGraphic ug, TimingRuler ruler, double height) {
		ug = ug.apply(new UStroke(4, 4, 2));
		ug = ug.apply(getLineColor());
		final ULine line = ULine.vline(height);
		final double start = ruler.getPosInPixel(this.getTickFrom());
		final double end = ruler.getPosInPixel(this.getTickTo());
		ug.apply(UTranslate.dx(start)).draw(line);
		ug.apply(UTranslate.dx(end)).draw(line);
	}

}
