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

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseActiveLine extends AbstractComponent {

	private final SymbolContext symbolContext;
	private final boolean closeUp;
	private final boolean closeDown;

	public ComponentRoseActiveLine(Style style, SymbolContext symbolContext, boolean closeUp, boolean closeDown,
			IHtmlColorSet set) {
		super(style);
		if (SkinParam.USE_STYLES()) {
			symbolContext = style.getSymbolContext(set);
		}
		this.symbolContext = symbolContext;
		this.closeUp = closeUp;
		this.closeDown = closeDown;
	}

	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int x = (int) (dimensionToUse.getWidth() - getPreferredWidth(stringBounder)) / 2;

		final URectangle rect = new URectangle(getPreferredWidth(stringBounder), dimensionToUse.getHeight());
		if (symbolContext.isShadowing()) {
			rect.setDeltaShadow(1);
		}
		ug = ug.apply(new UChangeColor(symbolContext.getForeColor()));
		if (closeUp && closeDown) {
			ug.apply(new UChangeBackColor(symbolContext.getBackColor())).apply(new UTranslate(x, 0)).draw(rect);
			return;
		}
		ug.apply(new UChangeBackColor(symbolContext.getBackColor()))
				.apply(new UChangeColor(symbolContext.getBackColor())).apply(new UTranslate(x, 0)).draw(rect);

		final ULine vline = new ULine(0, dimensionToUse.getHeight());
		ug.apply(new UTranslate(x, 0)).draw(vline);
		ug.apply(new UTranslate(x + getPreferredWidth(stringBounder), 0)).draw(vline);

		final ULine hline = new ULine(getPreferredWidth(stringBounder), 0);
		if (closeUp) {
			ug.apply(new UTranslate(x, 0)).draw(hline);
		}
		if (closeDown) {
			ug.apply(new UTranslate(x, dimensionToUse.getHeight())).draw(hline);
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return 0;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return 10;
	}
}
