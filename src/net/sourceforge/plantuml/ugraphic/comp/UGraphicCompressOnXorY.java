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
package net.sourceforge.plantuml.ugraphic.comp;

import net.sourceforge.plantuml.activitydiagram3.ftile.CenteredText;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.ugraphic.UBackground;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class UGraphicCompressOnXorY extends UGraphicDelegator {

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new UGraphicCompressOnXorY(mode, getUg(), compressionTransform,
					translate.compose((UTranslate) change));
		} else if (change instanceof UStroke || change instanceof UBackground || change instanceof HColor) {
			return new UGraphicCompressOnXorY(mode, getUg().apply(change), compressionTransform, translate);
		}
		throw new UnsupportedOperationException();
	}

	private final CompressionMode mode;
	private final PiecewiseAffineTransform compressionTransform;
	private final UTranslate translate;

	@Override
	public String toString() {
		return "UGraphicCompressOnXorY " + mode;
	}

	public UGraphicCompressOnXorY(CompressionMode mode, UGraphic ug, PiecewiseAffineTransform compressionTransform) {
		this(mode, ug, compressionTransform, new UTranslate());
	}

	private UGraphicCompressOnXorY(CompressionMode mode, UGraphic ug, PiecewiseAffineTransform compressionTransform,
			UTranslate translate) {
		super(ug);
		this.mode = mode;
		this.compressionTransform = compressionTransform;
		this.translate = translate;
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (shape instanceof URectangle) {
			final URectangle rect = (URectangle) shape;
			if (mode == CompressionMode.ON_X) {
				final double x2 = ct(x + rect.getWidth());
				shape = rect.withWidth(x2 - ct(x));
			} else {
				final double y2 = ct(y + rect.getHeight());
				shape = rect.withHeight(y2 - ct(y));
			}
		}
		if (shape instanceof CenteredText) {
			final CenteredText centeredText = (CenteredText) shape;
			final TextBlock text = centeredText.getText();
			final double totalWidth = centeredText.getTotalWidth();
			final double realSpaceWidth;
			if (mode == CompressionMode.ON_X) {
				realSpaceWidth = ct(x + totalWidth) - ct(x);
			} else {
				realSpaceWidth = totalWidth;
			}
			final double textWidth = text.calculateDimension(getStringBounder()).getWidth();
			final double pos = (realSpaceWidth - textWidth) / 2;
			text.drawU(getUg().apply(getTranslate(x, y)).apply(UTranslate.dx(pos)));
			return;
		}
		if (shape instanceof ULine) {
			drawLine(x, y, (ULine) shape);
		} else {
			getUg().apply(getTranslate(x, y)).draw(shape);
		}
	}

	private UTranslate getTranslate(final double x, final double y) {
		if (mode == CompressionMode.ON_X) {
			return new UTranslate(ct(x), y);
		} else {
			return new UTranslate(x, ct(y));
		}
	}

	private void drawLine(double x, double y, ULine shape) {
		if (mode == CompressionMode.ON_X) {
			drawLine(ct(x), y, ct(x + shape.getDX()), y + shape.getDY());
		} else {
			drawLine(x, ct(y), x + shape.getDX(), ct(y + shape.getDY()));
		}
	}

	private double ct(double v) {
		return compressionTransform.transform(v);
	}

	private void drawLine(double x1, double y1, double x2, double y2) {
		if (y1 > y2) {
			drawLine(x2, y2, x1, y1);
			return;
		}
		assert y1 <= y2;
		getUg().apply(new UTranslate(x1, y1)).draw(new ULine(x2 - x1, y2 - y1));
	}

}
