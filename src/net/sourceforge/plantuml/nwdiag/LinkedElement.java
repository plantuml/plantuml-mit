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
package net.sourceforge.plantuml.nwdiag;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.MathUtils;

public class LinkedElement {

	private final TextBlock ad1;
	private final TextBlock box;
	private final TextBlock ad2;
	private final Network network;
	private final DiagElement element;

	public LinkedElement(TextBlock ad1, TextBlock box, TextBlock ad2, Network network, DiagElement element) {
		this.ad1 = ad1;
		this.box = box;
		this.ad2 = ad2;
		this.network = network;
		this.element = element;
	}

	private final double marginAd = 10;
	private final double marginBox = 15;

	public MinMax getMinMax(StringBounder stringBounder, double width, double height) {
		final double xMiddle = width / 2;
		final double yMiddle = height / 2;
		final Dimension2D dimBox = box.calculateDimension(stringBounder);

		final double x1 = xMiddle - dimBox.getWidth() / 2;
		final double y1 = yMiddle - dimBox.getHeight() / 2;
		final double x2 = xMiddle + dimBox.getWidth() / 2;
		final double y2 = yMiddle + dimBox.getHeight() / 2;
		return MinMax.getEmpty(false).addPoint(x1 - 5, y1 - 5).addPoint(x2 + 5, y2 + 5);
	}

	public void drawMe(UGraphic ug, double width, double height) {
		final double xMiddle = width / 2;
		final double yMiddle = height / 2;
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimBox = box.calculateDimension(stringBounder);

		final double y1 = yMiddle - dimBox.getHeight() / 2;
		final double y2 = yMiddle + dimBox.getHeight() / 2;

		drawCenter(ug, box, xMiddle, yMiddle);

		final HColor color = ColorParam.activityBorder.getDefaultValue();
		ug = ug.apply(color);
		drawHLine(ug, xMiddle, GridTextBlockDecorated.NETWORK_THIN, y1);
		if (ad2 != null) {
			drawHLine(ug, xMiddle, y2, height);
		}

		drawCenter(ug, ad1, xMiddle, (GridTextBlockDecorated.NETWORK_THIN + y1) / 2);
		if (ad2 != null) {
			drawCenter(ug, ad2, xMiddle, (y2 + height - GridTextBlockDecorated.NETWORK_THIN) / 2);
		}
	}

	private void drawCenter(UGraphic ug, TextBlock block, double x, double y) {
		final Dimension2D dim = block.calculateDimension(ug.getStringBounder());
		block.drawU(ug.apply(new UTranslate(x - dim.getWidth() / 2, y - dim.getHeight() / 2)));

	}

	private void drawHLine(UGraphic ug, double x, double y1, double y2) {
		final ULine line = ULine.vline(y2 - y1);
		ug.apply(new UTranslate(x, y1)).draw(line);
	}

	public Dimension2D naturalDimension(StringBounder stringBounder) {
		final Dimension2D dim1 = ad1.calculateDimension(stringBounder);
		final Dimension2D dimBox = box.calculateDimension(stringBounder);
		final Dimension2D dim2 = ad2 == null ? new Dimension2DDouble(0, 0) : ad2.calculateDimension(stringBounder);
		final double width = MathUtils.max(dim1.getWidth() + 2 * marginAd, dimBox.getWidth() + 2 * marginBox,
				dim2.getWidth() + 2 * marginAd);
		final double height = dim1.getHeight() + 2 * marginAd + dimBox.getHeight() + 2 * marginBox + dim2.getHeight()
				+ 2 * marginAd;
		return new Dimension2DDouble(width, height);
	}

	public final Network getNetwork() {
		return network;
	}

	public final DiagElement getElement() {
		return element;
	}

}
