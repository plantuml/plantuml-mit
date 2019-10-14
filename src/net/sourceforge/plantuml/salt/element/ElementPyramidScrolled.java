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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.salt.Positionner2;
import net.sourceforge.plantuml.salt.factory.ScrollStrategy;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementPyramidScrolled extends ElementPyramid {

	private final double v1 = 15;
	private final double v2 = 12;
	private final ScrollStrategy scrollStrategy;

	public ElementPyramidScrolled(Positionner2 positionner, ISkinSimple spriteContainer, ScrollStrategy scrollStrategy) {
		super(positionner, TableStrategy.DRAW_OUTSIDE, null, spriteContainer);
		this.scrollStrategy = scrollStrategy;
	}

	@Override
	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		final Dimension2D result = super.getPreferredDimension(stringBounder, x, y);
		if (scrollStrategy == ScrollStrategy.HORIZONTAL_ONLY) {
			return Dimension2DDouble.delta(result, 0, 30);
		}
		if (scrollStrategy == ScrollStrategy.VERTICAL_ONLY) {
			return Dimension2DDouble.delta(result, 30, 0);
		}
		return Dimension2DDouble.delta(result, 30);
	}

	@Override
	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		super.drawU(ug, zIndex, dimToUse);
		final Dimension2D dim = super.getPreferredDimension(ug.getStringBounder(), 0, 0);
		if (scrollStrategy == ScrollStrategy.BOTH || scrollStrategy == ScrollStrategy.VERTICAL_ONLY) {
			drawV(ug.apply(new UTranslate(dim.getWidth() + 4, 0)), v1, dim.getHeight());
		}
		if (scrollStrategy == ScrollStrategy.BOTH || scrollStrategy == ScrollStrategy.HORIZONTAL_ONLY) {
			drawH(ug.apply(new UTranslate(0, dim.getHeight() + 4)), dim.getWidth(), v1);
		}
	}

	private UPath getTr0() {
		final UPath poly = new UPath();
		poly.moveTo(3, 0);
		poly.lineTo(6, 5);
		poly.lineTo(0, 5);
		poly.lineTo(3, 0);
		poly.closePath();
		return poly;
	}

	private UPath getTr180() {
		final UPath poly = new UPath();
		poly.moveTo(3, 5);
		poly.lineTo(6, 0);
		poly.lineTo(0, 0);
		poly.lineTo(3, 5);
		poly.closePath();
		return poly;
	}

	private UPath getTr90() {
		final UPath poly = new UPath();
		poly.moveTo(0, 3);
		poly.lineTo(5, 6);
		poly.lineTo(5, 0);
		poly.lineTo(0, 3);
		poly.closePath();
		return poly;
	}

	private UPath getTr270() {
		final UPath poly = new UPath();
		poly.moveTo(5, 3);
		poly.lineTo(0, 6);
		poly.lineTo(0, 0);
		poly.lineTo(5, 3);
		poly.closePath();
		return poly;
	}

	private void drawV(UGraphic ug, double width, double height) {
		ug.draw(new URectangle(width, height));
		ug.apply(new UTranslate(0, v2)).draw(new ULine(width, 0));
		ug.apply(new UTranslate(0, height - v2)).draw(new ULine(width, 0));
		ug.apply(new UTranslate(4, 4)).apply(new UChangeBackColor(HtmlColorUtils.BLACK)).draw(getTr0());
		ug.apply(new UTranslate(4, height - v2 + 4)).apply(new UChangeBackColor(HtmlColorUtils.BLACK)).draw(getTr180());
	}

	private void drawH(UGraphic ug, double width, double height) {
		ug.draw(new URectangle(width, height));
		ug.apply(new UTranslate(v2, 0)).draw(new ULine(0, height));
		ug.apply(new UTranslate(width - v2, 0)).draw(new ULine(0, height));
		ug.apply(new UTranslate(4, 4)).apply(new UChangeBackColor(HtmlColorUtils.BLACK)).draw(getTr90());
		ug.apply(new UTranslate(width - v2 + 4, 4)).apply(new UChangeBackColor(HtmlColorUtils.BLACK)).draw(getTr270());
	}

}
