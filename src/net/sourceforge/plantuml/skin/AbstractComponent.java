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
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public abstract class AbstractComponent implements Component {

	public final Style[] getUsedStyles() {
		throw new UnsupportedOperationException();
	}

	public StyleSignature getDefaultStyleDefinition() {
		throw new UnsupportedOperationException();
	}

	private final Style style;

	public AbstractComponent(Style style) {
		this.style = style;
	}

	protected final Style getStyle() {
		return style;
	}


	abstract protected void drawInternalU(UGraphic ug, Area area);

	protected void drawBackgroundInternalU(UGraphic ug, Area area) {
	}

	public final void drawU(UGraphic ug, Area area, Context2D context) {
		ug = ug.apply(new UTranslate(getPaddingX(), getPaddingY()));
		if (context.isBackground()) {
			drawBackgroundInternalU(ug, area);
		} else {
			drawInternalU(ug, area);
		}
	}

	public double getPaddingX() {
		return 0;
	}

	public double getPaddingY() {
		return 0;
	}

	public abstract double getPreferredWidth(StringBounder stringBounder);

	public abstract double getPreferredHeight(StringBounder stringBounder);

	public final Dimension2D getPreferredDimension(StringBounder stringBounder) {
		final double w = getPreferredWidth(stringBounder);
		final double h = getPreferredHeight(stringBounder);
		return new Dimension2DDouble(w, h);
	}

}
