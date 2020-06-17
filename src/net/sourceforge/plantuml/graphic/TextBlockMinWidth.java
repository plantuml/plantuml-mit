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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockMinWidth extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;
	private final double minWidth;
	private final HorizontalAlignment horizontalAlignment;

	TextBlockMinWidth(TextBlock textBlock, double minWidth, HorizontalAlignment horizontalAlignment) {
		this.textBlock = textBlock;
		this.minWidth = minWidth;
		this.horizontalAlignment = horizontalAlignment;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		return Dimension2DDouble.atLeast(dim, minWidth, 0);
	}

	public void drawU(UGraphic ug) {
		if (horizontalAlignment == HorizontalAlignment.LEFT) {
			textBlock.drawU(ug);
		} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
			final Dimension2D dimText = textBlock.calculateDimension(ug.getStringBounder());
			final Dimension2D dimFull = calculateDimension(ug.getStringBounder());
			final double diffx = dimFull.getWidth() - dimText.getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(diffx / 2)));
		} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
			final Dimension2D dimText = textBlock.calculateDimension(ug.getStringBounder());
			final Dimension2D dimFull = calculateDimension(ug.getStringBounder());
			final double diffx = dimFull.getWidth() - dimText.getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(diffx)));
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
