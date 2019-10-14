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
package net.sourceforge.plantuml.ugraphic.comp;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockCompressedOnXorY extends AbstractTextBlock implements TextBlock {

	private final TextBlock textBlock;
	private final CompressionMode mode;

	public TextBlockCompressedOnXorY(CompressionMode mode, TextBlock textBlock) {
		this.textBlock = textBlock;
		this.mode = mode;
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final CompressionTransform compressionTransform = getCompressionTransform(stringBounder);
		textBlock.drawU(new UGraphicCompressOnXorY(mode, ug, compressionTransform));
	}

	private MinMax cachedMinMax;

	@Override
	public MinMax getMinMax(StringBounder stringBounder) {
		if (cachedMinMax == null) {
			cachedMinMax = TextBlockUtils.getMinMax(this, stringBounder);
		}
		return cachedMinMax;
	}

	private CompressionTransform cachedCompressionTransform;

	private CompressionTransform getCompressionTransform(final StringBounder stringBounder) {
		if (cachedCompressionTransform == null) {
			cachedCompressionTransform = getCompressionTransformSlow(stringBounder);
		}
		return cachedCompressionTransform;
	}

	private CompressionTransform getCompressionTransformSlow(final StringBounder stringBounder) {
		final SlotFinder slotFinder = new SlotFinder(mode, stringBounder);
		textBlock.drawU(slotFinder);
		final SlotSet ysSlotSet = slotFinder.getSlotSet().reverse().smaller(5.0);
		final CompressionTransform compressionTransform = new CompressionTransform(ysSlotSet);
		return compressionTransform;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final CompressionTransform compressionTransform = getCompressionTransform(stringBounder);
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		if (mode == CompressionMode.ON_X) {
			return new Dimension2DDouble(compressionTransform.transform(dim.getWidth()), dim.getHeight());
		} else {
			return new Dimension2DDouble(dim.getWidth(), compressionTransform.transform(dim.getHeight()));
		}
	}
}
