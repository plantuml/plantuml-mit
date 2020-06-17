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
package net.sourceforge.plantuml.golem;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class Tile extends AbstractTextBlock implements TextBlock {

	private static double SIZE = 40;
	private final int num;

	private final UFont numberFont = UFont.monospaced(11);
	private final FontConfiguration fc = FontConfiguration.blackBlueTrue(numberFont);
	private final Map<TileGeometry, TileArea> geometries;

	Tile(int num) {
		this.num = num;
		final Map<TileGeometry, TileArea> tmp = new EnumMap<TileGeometry, TileArea>(TileGeometry.class);
		for (TileGeometry g : TileGeometry.values()) {
			tmp.put(g, new TileArea(this, g));
		}
		this.geometries = Collections.unmodifiableMap(tmp);
	}

	public TileArea getArea(TileGeometry geometry) {
		return this.geometries.get(geometry);
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(HColorUtils.BLACK);
		final TextBlock n = Display.create("" + num).create(fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		final Dimension2D dimNum = n.calculateDimension(ug.getStringBounder());
		final Dimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final double diffx = dimTotal.getWidth() - dimNum.getWidth();
		final double diffy = dimTotal.getHeight() - dimNum.getHeight();
		final double radius = Math.max(dimNum.getWidth(), dimNum.getHeight());
		final double diffx2 = dimTotal.getWidth() - radius;
		final double diffy2 = dimTotal.getHeight() - radius;
		n.drawU(ug.apply(new UTranslate((diffx / 2), (diffy / 2))));
		ug.draw(new URectangle(SIZE, SIZE));
		ug.apply(new UTranslate(diffx2 / 2, diffy2 / 2)).draw(new UEllipse(radius, radius));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}
}
