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
package net.sourceforge.plantuml.creole;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SvgString;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSimple;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.math.ScientificEquationSafe;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UImageSvg;

public class AtomMath extends AbstractAtom implements Atom {

	private final double scale;
	private final ScientificEquationSafe math;
	private final HtmlColor foreground;
	private final HtmlColor background;
	private final ColorMapper colorMapper;

	public AtomMath(ScientificEquationSafe math, HtmlColor foreground, HtmlColor background, double scale,
			ColorMapper colorMapper) {
		this.math = math;
		this.colorMapper = colorMapper;
		this.foreground = foreground;
		this.background = background;
		this.scale = scale;
	}

	private Dimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final BufferedImage image = math.getImage(scale, Color.BLACK, Color.WHITE);
		return new Dimension2DDouble(image.getWidth(), image.getHeight());
	}

	private Dimension2D dim;

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (dim == null) {
			dim = calculateDimensionSlow(stringBounder);
		}
		return dim;
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(UGraphic ug) {
		final boolean isSvg = ug.matchesProperty("SVG");
		final Color back;
		if (background == null) {
			back = null;
		} else {
			back = getColor(background, Color.WHITE);
		}
		final Color fore = getColor(foreground, Color.BLACK);
		// final double dpiFactor = ug.dpiFactor();
		if (isSvg) {
			final SvgString svg = math.getSvg(scale, fore, back);
			ug.draw(new UImageSvg(svg));
		} else {
			final UImage image = new UImage(math.getImage(scale, fore, back), math.getFormula());
			ug.draw(image);
		}
	}

	private Color getColor(HtmlColor color, Color defaultValue) {
		if (color instanceof HtmlColorSimple) {
			return colorMapper.getMappedColor(color);
			// return ((HtmlColorSimple) color).getColor999();
		}
		return defaultValue;

	}
}
