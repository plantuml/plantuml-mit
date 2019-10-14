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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementDroplist extends AbstractElementText implements Element {

	private final int box = 12;
	private final TextBlock openDrop;

	public ElementDroplist(String text, UFont font, ISkinSimple spriteContainer) {
		super(extract(text), font, true, spriteContainer);
		final StringTokenizer st = new StringTokenizer(text, "^");
		final List<String> drop = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			drop.add(st.nextToken());
		}
		if (drop.size() > 0) {
			drop.remove(0);
		}
		if (drop.size() == 0) {
			this.openDrop = null;
		} else {
			this.openDrop = Display.create(drop).create(getConfig(), HorizontalAlignment.LEFT, spriteContainer);
		}
	}

	private static String extract(String text) {
		final int idx = text.indexOf('^');
		if (idx == -1) {
			return text;
		}
		return text.substring(0, idx);
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D dim = getTextDimensionAt(stringBounder, x + 2);
		return Dimension2DDouble.delta(dim, 4 + box, 4);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		final Dimension2D dim = getPreferredDimension(ug.getStringBounder(), 0, 0);
		if (zIndex == 0) {
			ug.apply(new UChangeBackColor(HtmlColorSet.getInstance().getColorIfValid("#EEEEEE"))).draw(
					new URectangle(dim.getWidth() - 1, dim.getHeight() - 1));
			drawText(ug, 2, 2);
			final double xline = dim.getWidth() - box;
			ug.apply(new UTranslate(xline, 0)).draw(new ULine(0, dim.getHeight() - 1));

			final UPolygon poly = new UPolygon();
			poly.addPoint(0, 0);
			poly.addPoint(box - 6, 0);
			final Dimension2D dimText = getPureTextDimension(ug.getStringBounder());
			poly.addPoint((box - 6) / 2, dimText.getHeight() - 8);

			ug.apply(new UChangeBackColor(ug.getParam().getColor())).apply(new UTranslate(xline + 3, 6)).draw(poly);
		}

		if (openDrop != null) {
			final Dimension2D dimOpen = Dimension2DDouble.atLeast(openDrop.calculateDimension(ug.getStringBounder()),
					dim.getWidth() - 1, 0);
			ug.apply(new UChangeBackColor(HtmlColorSet.getInstance().getColorIfValid("#EEEEEE")))
					.apply(new UTranslate(0, dim.getHeight() - 1))
					.draw(new URectangle(dimOpen.getWidth() - 1, dimOpen.getHeight() - 1));
			openDrop.drawU(ug.apply(new UTranslate(0, dim.getHeight() - 1)));
		}
	}
}
