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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class HeaderLayout {

	final private TextBlock name;
	final private TextBlock stereo;
	final private TextBlock generic;
	final private TextBlock circledCharacter;

	public HeaderLayout(TextBlock circledCharacter, TextBlock stereo, TextBlock name, TextBlock generic) {
		this.circledCharacter = protectAgaintNull(circledCharacter);
		this.stereo = protectAgaintNull(stereo);
		this.name = protectAgaintNull(name);
		this.generic = protectAgaintNull(generic);
	}

	private static TextBlock protectAgaintNull(TextBlock block) {
		if (block == null) {
			return new TextBlockEmpty();
		}
		return block;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D genericDim = generic.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo.calculateDimension(stringBounder);
		final Dimension2D circleDim = circledCharacter.calculateDimension(stringBounder);

		final double width = circleDim.getWidth() + Math.max(stereoDim.getWidth(), nameDim.getWidth())
				+ genericDim.getWidth();
		final double height = MathUtils.max(circleDim.getHeight(), stereoDim.getHeight() + nameDim.getHeight() + 10,
				genericDim.getHeight());
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug, double width, double height) {

		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D genericDim = generic.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo.calculateDimension(stringBounder);
		final Dimension2D circleDim = circledCharacter.calculateDimension(stringBounder);

		final double widthStereoAndName = Math.max(stereoDim.getWidth(), nameDim.getWidth());
		final double suppWith = Math.max(0, width - circleDim.getWidth() - widthStereoAndName - genericDim.getWidth());
		assert suppWith >= 0;

		final double h2 = Math.min(circleDim.getWidth() / 4, suppWith * 0.1);
		final double h1 = (suppWith - h2) / 2;
		assert h1 >= 0;
		assert h2 >= 0;

		final double xCircle = h1;
		final double yCircle = (height - circleDim.getHeight()) / 2;
		circledCharacter.drawU(ug.apply(new UTranslate(xCircle, yCircle)));

		final double diffHeight = height - stereoDim.getHeight() - nameDim.getHeight();
		final double xStereo = circleDim.getWidth() + (widthStereoAndName - stereoDim.getWidth()) / 2 + h1 + h2;
		final double yStereo = diffHeight / 2;
		stereo.drawU(ug.apply(new UTranslate(xStereo, yStereo)));

		final double xName = circleDim.getWidth() + (widthStereoAndName - nameDim.getWidth()) / 2 + h1 + h2;
		final double yName = diffHeight / 2 + stereoDim.getHeight();
		name.drawU(ug.apply(new UTranslate(xName, yName)));

		if (genericDim.getWidth() > 0) {
			final double delta = 4;
			final double xGeneric = width - genericDim.getWidth() + delta;
			final double yGeneric = -delta;
			generic.drawU(ug.apply(new UTranslate(xGeneric, yGeneric)));
		}
	}

}
