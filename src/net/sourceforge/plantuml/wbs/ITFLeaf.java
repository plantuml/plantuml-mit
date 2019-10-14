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
package net.sourceforge.plantuml.wbs;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class ITFLeaf extends AbstractTextBlock implements ITF {

	private final TextBlock box;

	public ITFLeaf(Style style, ISkinParam skinParam, Display label, IdeaShape shape) {
		// this.skinParam = skinParam;

		if (shape == IdeaShape.BOX) {
			this.box = FtileBox.createWbs(style, skinParam, label);
		} else {
			final UFont font = skinParam.getFont(null, false, FontParam.ACTIVITY);
			final TextBlock text = label.create(FontConfiguration.blackBlueTrue(font), HorizontalAlignment.LEFT,
					skinParam);
			this.box = TextBlockUtils.withMargin(text, 0, 3, 1, 1);
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return box.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		box.drawU(ug);
	}

	public Point2D getT1(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(dim.getWidth() / 2, 0);
	}

	public Point2D getT2(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(dim.getWidth() / 2, dim.getHeight());
	}

	public Point2D getF1(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(0, dim.getHeight() / 2);
	}

	public Point2D getF2(StringBounder stringBounder) {
		final Dimension2D dim = calculateDimension(stringBounder);
		return new Point2D.Double(dim.getWidth(), dim.getHeight() / 2);
	}

}
