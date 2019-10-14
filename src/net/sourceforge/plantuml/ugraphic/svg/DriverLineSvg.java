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
package net.sourceforge.plantuml.ugraphic.svg;

import java.awt.geom.Line2D;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverLineSvg implements UDriver<SvgGraphics> {

	private final ClipContainer clipContainer;

	public DriverLineSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final ULine shape = (ULine) ushape;

		double x2 = x + shape.getDX();
		double y2 = y + shape.getDY();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Line2D.Double line = clip.getClippedLine(new Line2D.Double(x, y, x2, y2));
			if (line == null) {
				return;
			}
			x = line.x1;
			y = line.y1;
			x2 = line.x2;
			y2 = line.y2;
		}

		// // Shadow
		// if (shape.getDeltaShadow() != 0) {
		// svg.svgLineShadow(x, y, x2, y2, shape.getDeltaShadow());
		// }

		final HtmlColor color = param.getColor();
		if (color instanceof HtmlColorGradient) {
			final HtmlColorGradient gr = (HtmlColorGradient) color;
			svg.setStrokeColor(StringUtils.getAsSvg(mapper, gr.getColor1()));
		} else {
			svg.setStrokeColor(StringUtils.getAsSvg(mapper, color));
		}
		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());
		svg.svgLine(x, y, x2, y2, shape.getDeltaShadow());
	}
}
