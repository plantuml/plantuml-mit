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

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseParticipant extends AbstractTextualComponent {

	private final HtmlColor back;
	private final HtmlColor foregroundColor;
	private final double deltaShadow;
	private final double roundCorner;
	private final UStroke stroke;
	private final double minWidth;
	private final boolean collections;
	private final double padding;

	public ComponentRoseParticipant(Style style, Style stereo, SymbolContext biColor, FontConfiguration font,
			Display stringsToDisplay, ISkinSimple spriteContainer, double roundCorner, UFont fontForStereotype,
			HtmlColor htmlColorForStereotype, double minWidth, boolean collections, double padding) {
		super(style, stereo, LineBreakStrategy.NONE, stringsToDisplay, font, HorizontalAlignment.CENTER, 7, 7, 7,
				spriteContainer, false, fontForStereotype, htmlColorForStereotype);
		if (SkinParam.USE_STYLES()) {
			this.roundCorner = style.value(PName.RoundCorner).asInt();
			biColor = style.getSymbolContext(getIHtmlColorSet());
			this.stroke = style.getStroke();
		} else {
			this.roundCorner = roundCorner;
			this.stroke = biColor.getStroke();
		}
		this.padding = padding;
		this.minWidth = minWidth;
		this.collections = collections;
		this.back = biColor.getBackColor();
		this.deltaShadow = biColor.getDeltaShadow();
		this.foregroundColor = biColor.getForeColor();
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UTranslate(padding, 0));
		ug = ug.apply(new UChangeBackColor(back)).apply(new UChangeColor(foregroundColor));
		ug = ug.apply(stroke);
		final URectangle rect = new URectangle(getTextWidth(stringBounder), getTextHeight(stringBounder), roundCorner,
				roundCorner);
		rect.setDeltaShadow(deltaShadow);
		if (collections) {
			ug.apply(new UTranslate(getDeltaCollection(), 0)).draw(rect);
			ug = ug.apply(new UTranslate(0, getDeltaCollection()));
		}
		ug.draw(rect);
		ug = ug.apply(new UStroke());
		final TextBlock textBlock = getTextBlock();
		textBlock.drawU(ug.apply(new UTranslate(getMarginX1() + suppWidth(stringBounder) / 2, getMarginY())));
	}

	private double getDeltaCollection() {
		if (collections) {
			return 4;
		}
		return 0;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + deltaShadow + 1 + getDeltaCollection();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + deltaShadow + getDeltaCollection() + 2 * padding;
	}

	@Override
	protected double getPureTextWidth(StringBounder stringBounder) {
		return Math.max(super.getPureTextWidth(stringBounder), minWidth);
	}

	private final double suppWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) - super.getPureTextWidth(stringBounder);
	}

}
