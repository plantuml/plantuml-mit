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
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseDivider extends AbstractTextualComponent {

	// private final int outMargin = 5;
	private final HtmlColor borderColor;
	private final HtmlColor background;
	private final boolean empty;
	private final boolean withShadow;
	private final UStroke stroke;
	private final double roundCorner;

	public ComponentRoseDivider(Style style, FontConfiguration font, HtmlColor background, Display stringsToDisplay,
			ISkinSimple spriteContainer, boolean withShadow, UStroke stroke, HtmlColor borderColor) {
		super(style, LineBreakStrategy.NONE, stringsToDisplay, font, HorizontalAlignment.CENTER, 4, 4, 4,
				spriteContainer, false, null, null);
		if (SkinParam.USE_STYLES()) {
			this.background = style.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
			this.borderColor = style.value(PName.LineColor).asColor(getIHtmlColorSet());
			this.stroke = style.getStroke();
			this.roundCorner = style.value(PName.RoundCorner).asInt();
		} else {
			this.background = background;
			this.borderColor = borderColor;
			this.stroke = stroke;
			this.roundCorner = 0;
		}
		this.empty = stringsToDisplay.get(0).length() == 0;
		this.withShadow = withShadow;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();

		ug = ug.apply(new UChangeBackColor(background));
		if (empty) {
			drawSep(ug.apply(new UTranslate(0, dimensionToUse.getHeight() / 2)), dimensionToUse.getWidth());
		} else {
			final TextBlock textBlock = getTextBlock();
			final StringBounder stringBounder = ug.getStringBounder();
			final double textWidth = getTextWidth(stringBounder);
			final double textHeight = getTextHeight(stringBounder);
			final double deltaX = 6;
			final double xpos = (dimensionToUse.getWidth() - textWidth - deltaX) / 2;
			final double ypos = (dimensionToUse.getHeight() - textHeight) / 2;

			drawSep(ug.apply(new UTranslate(0, dimensionToUse.getHeight() / 2)), dimensionToUse.getWidth());

			ug = ug.apply(new UChangeColor(borderColor));
			ug = ug.apply(stroke);
			final URectangle rect = new URectangle(textWidth + deltaX, textHeight, roundCorner, roundCorner);
			if (withShadow) {
				rect.setDeltaShadow(4);
			}
			ug.apply(new UTranslate(xpos, ypos)).draw(rect);
			textBlock.drawU(ug.apply(new UTranslate(xpos + deltaX, ypos + getMarginY())));

			// drawSep(ug.apply(new UTranslate(xpos + deltaX + textWidth + stroke.getThickness() + , dimensionToUse
			// .getHeight() / 2)), 10);
		}
	}

	private void drawSep(UGraphic ug, double width) {
		ug = ug.apply(new UChangeColor(background));
		drawRectLong(ug.apply(new UTranslate(0, -1)), width);
		drawDoubleLine(ug, width);
	}

	private void drawRectLong(UGraphic ug, double width) {
		final URectangle rectLong = new URectangle(width, 3, roundCorner, roundCorner);
		if (withShadow) {
			rectLong.setDeltaShadow(2);
		}
		ug = ug.apply(new UStroke());
		ug.draw(rectLong);
	}

	private void drawDoubleLine(UGraphic ug, final double width) {
		ug = ug.apply(new UStroke(stroke.getThickness() / 2)).apply(new UChangeColor(borderColor));
		final ULine line = new ULine(width, 0);
		ug.apply(new UTranslate(0, -1)).draw(line);
		ug.apply(new UTranslate(0, 2)).draw(line);
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + 30;
	}

}
