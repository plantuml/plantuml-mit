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
package net.sourceforge.plantuml.flowdiagram;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.golem.Tile;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ActivityBox extends AbstractTextBlock {

	private static final int CORNER = 25;
	private static final int MARGIN = 10;

	private final Tile tile;
	private final String id;
	private final String label;
	private final TextBlock tb;

	public ActivityBox(Tile tile, String id, String label) {
		this.tile = tile;
		this.id = id;
		this.label = label;
		final UFont font = UFont.serif(14);
		final FontConfiguration fc = FontConfiguration.blackBlueTrue(font);
		tb = Display.create(label).create(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	public Tile getTile() {
		return tile;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void drawU(UGraphic ug) {
		final Dimension2D dimTotal = calculateDimension(ug.getStringBounder());
		// final Dimension2D dimDesc = tb.calculateDimension(ug.getStringBounder());

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal, CORNER, CORNER);
		ug = ug.apply(new UChangeColor(HtmlColorUtils.MY_RED));
		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.MY_YELLOW));
		ug.apply(new UStroke(1.5)).draw(rect);

		tb.drawU(ug.apply(new UTranslate(MARGIN, MARGIN)));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = tb.calculateDimension(stringBounder);

		return Dimension2DDouble.delta(dim, 2 * MARGIN, 2 * MARGIN);
	}

}
