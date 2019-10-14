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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CreoleHorizontalLine extends AbstractAtom implements Atom {

	private final FontConfiguration fontConfiguration;
	private final String line;
	private final char style;
	private final ISkinSimple skinParam;

	public static CreoleHorizontalLine create(FontConfiguration fontConfiguration, String line, char style,
			ISkinSimple skinParam) {
		return new CreoleHorizontalLine(fontConfiguration, line, style, skinParam);
	}

	private CreoleHorizontalLine(FontConfiguration fontConfiguration, String line, char style, ISkinSimple skinParam) {
		this.fontConfiguration = fontConfiguration;
		this.line = line;
		this.style = style;
		this.skinParam = skinParam;
	}

	private UHorizontalLine getHorizontalLine() {
		if (line.length() == 0) {
			return UHorizontalLine.infinite(0, 0, style);
		}
		final TextBlock tb = getTitle();
		return UHorizontalLine.infinite(0, 0, tb, style);
	}

	private TextBlock getTitle() {
		if (line.length() == 0) {
			return TextBlockUtils.empty(0, 0);
		}
		final CreoleParser parser = new CreoleParser(fontConfiguration, HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL);
		final Sheet sheet = parser.createSheet(Display.getWithNewlines(line));
		final TextBlock tb = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		return tb;
	}

	public void drawU(UGraphic ug) {
		// ug = ug.apply(new UChangeColor(fontConfiguration.getColor()));
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		ug = ug.apply(new UTranslate(0, dim.getHeight() / 2));
		ug.draw(getHorizontalLine());
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (line.length() == 0) {
			return new Dimension2DDouble(10, 10);
		}
		final TextBlock tb = getTitle();
		return tb.calculateDimension(stringBounder);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}
	
}
