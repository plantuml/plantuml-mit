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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ActorAwesome extends AbstractTextBlock implements TextBlock {

	private final double headDiam = 32;
	private final double bodyWidth = 54;
	private final double shoulder = 16;
	private final double collar = 4;
	private final double radius = 8;
	private final double bodyHeight = 28;

	private final SymbolContext symbolContext;

	public ActorAwesome(SymbolContext symbolContext) {
		this.symbolContext = symbolContext.withStroke(new UStroke(1.5));
	}

	public void drawU(UGraphic ug) {

		final UEllipse head = new UEllipse(headDiam, headDiam);
		final double centerX = getPreferredWidth() / 2;

		final UPath path = new UPath();
		path.moveTo(0, collar);
		path.cubicTo(collar, collar, bodyWidth / 2 - shoulder - collar, collar, bodyWidth / 2 - shoulder, 0);
		path.cubicTo(bodyWidth / 2 - shoulder / 2, 0, bodyWidth / 2, shoulder / 2, bodyWidth / 2, shoulder);
		path.lineTo(bodyWidth / 2, bodyHeight - radius);
		path.cubicTo(bodyWidth / 2, bodyHeight - radius / 2, bodyWidth / 2 - radius / 2, bodyHeight, bodyWidth / 2
				- radius, bodyHeight);
		path.lineTo(-bodyWidth / 2 + radius, bodyHeight);
		path.cubicTo(-bodyWidth / 2 + radius / 2, bodyHeight, -bodyWidth / 2, bodyHeight - radius / 2, -bodyWidth / 2,
				bodyHeight - radius);
		path.lineTo(-bodyWidth / 2, shoulder);
		path.cubicTo(-bodyWidth / 2, shoulder / 2, -bodyWidth / 2 + shoulder / 2, 0, -bodyWidth / 2 + shoulder, 0);
		path.cubicTo(-bodyWidth / 2 + shoulder + collar, collar, -collar, collar, 0, collar);
		path.closePath();

		if (symbolContext.getDeltaShadow() != 0) {
			head.setDeltaShadow(symbolContext.getDeltaShadow());
			path.setDeltaShadow(symbolContext.getDeltaShadow());
		}
		ug = symbolContext.apply(ug);
		ug.apply(new UTranslate(centerX - head.getWidth() / 2, thickness())).draw(head);
		ug.apply(new UTranslate(centerX, head.getHeight() + thickness())).draw(path);

	}

	private double thickness() {
		return symbolContext.getStroke().getThickness();
	}

	public double getPreferredWidth() {
		return bodyWidth + thickness() * 2;
	}

	public double getPreferredHeight() {
		return headDiam + bodyHeight + thickness() * 2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(), getPreferredHeight());
	}
}
