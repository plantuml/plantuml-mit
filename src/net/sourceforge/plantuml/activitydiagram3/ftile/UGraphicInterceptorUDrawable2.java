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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Point2D;
import java.util.Map;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.svek.UGraphicForSnake;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class UGraphicInterceptorUDrawable2 extends UGraphicDelegator {

	private final Map<String, UTranslate> positions;

	public UGraphicInterceptorUDrawable2(UGraphic ug, Map<String, UTranslate> positions) {
		super(ug);
		this.positions = positions;
	}

	public void draw(UShape shape) {
		if (shape instanceof Ftile) {
			final Ftile ftile = (Ftile) shape;
			// System.err.println("ftile=" + ftile);
			ftile.drawU(this);
			if (ftile instanceof FtileLabel) {
				positions.put(((FtileLabel) ftile).getName(), getPosition());
				// System.err.println("ug=" + getUg().getClass());
			}
			if (ftile instanceof FtileGoto) {
				// System.err.println("positions=" + positions);
				drawGoto((FtileGoto) ftile);
			}
		} else if (shape instanceof UDrawable) {
			final UDrawable drawable = (UDrawable) shape;
			drawable.drawU(this);
		} else {
			getUg().draw(shape);
		}

	}

	private UTranslate getPosition() {
		return ((UGraphicForSnake) getUg()).getTranslation();
	}

	private void drawGoto(FtileGoto ftile) {
		final FtileGeometry geom = ftile.calculateDimension(getStringBounder());
		final Point2D pt = geom.getPointIn();
		UGraphic ugGoto = getUg().apply(new UChangeColor(HtmlColorUtils.GREEN)).apply(
				new UChangeBackColor(HtmlColorUtils.GREEN));
		ugGoto = ugGoto.apply(new UTranslate(pt));
		final UTranslate posNow = getPosition();
		final UTranslate dest = positions.get(ftile.getName());
		final double dx = dest.getDx() - posNow.getDx();
		final double dy = dest.getDy() - posNow.getDy();
		ugGoto.draw(new UEllipse(3, 3));
		ugGoto.apply(new UTranslate(dx, dy)).draw(new UEllipse(3, 3));
		ugGoto.draw(new ULine(dx, 0));
		ugGoto.apply(new UTranslate(dx, 0)).draw(new ULine(0, dy));
		// ugGoto.draw(new ULine(dx, dy));
	}

	public UGraphic apply(UChange change) {
		return new UGraphicInterceptorUDrawable2(getUg().apply(change), positions);
	}

}
