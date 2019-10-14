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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Padder {

	private final double margin;
	private final double padding;
	private final HtmlColor backgroundColor;
	private final HtmlColor borderColor;
	private final double roundCorner;

	public static final Padder NONE = new Padder(0, 0, null, null, 0);

	@Override
	public String toString() {
		return "" + margin + "/" + padding + "/" + borderColor + "/" + backgroundColor;
	}

	private Padder(double margin, double padding, HtmlColor backgroundColor, HtmlColor borderColor, double roundCorner) {
		this.margin = margin;
		this.padding = padding;
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.roundCorner = roundCorner;
	}

	public Padder withMargin(double margin) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withPadding(double padding) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withBackgroundColor(HtmlColor backgroundColor) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withBorderColor(HtmlColor borderColor) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withRoundCorner(double roundCorner) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public final double getMargin() {
		return margin;
	}

	public final double getPadding() {
		return padding;
	}

	public final HtmlColor getBackgroundColor() {
		return backgroundColor;
	}

	public final HtmlColor getBorderColor() {
		return borderColor;
	}

	public TextBlock apply(final TextBlock orig) {
		if (this == NONE) {
			return orig;
		}
		return new AbstractTextBlock() {
			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return Dimension2DDouble.delta(orig.calculateDimension(stringBounder), 2 * (margin + padding));
			}

			public void drawU(UGraphic ug) {
				ug = ug.apply(new UTranslate(margin, margin));
				final UGraphic ug2 = ug.apply(new UChangeBackColor(backgroundColor)).apply(
						new UChangeColor(borderColor));
				final Dimension2D originalDim = orig.calculateDimension(ug.getStringBounder());
				final URectangle rect = new URectangle(Dimension2DDouble.delta(originalDim, 2 * padding), roundCorner,
						roundCorner);
				ug2.draw(rect);
				orig.drawU(ug.apply(new UTranslate(padding, padding)));
			}
		};
	}
}
