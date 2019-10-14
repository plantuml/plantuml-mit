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
package net.sourceforge.plantuml.ugraphic.html5;

import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverRectangleHtml5 implements UDriver<Html5Drawer> {

	private final ClipContainer clipContainer;

	public DriverRectangleHtml5(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Html5Drawer html) {
		final URectangle rect = (URectangle) ushape;

		double width = rect.getWidth();
		double height = rect.getHeight();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Rectangle2D.Double r = clip.getClippedRectangle(new Rectangle2D.Double(x, y, width, height));
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
		}

		final double rx = rect.getRx();
		final double ry = rect.getRy();

//		// Shadow
//		if (rect.getDeltaShadow() != 0) {
//			eps.epsRectangleShadow(x, y, width, height, rx / 2, ry / 2, rect.getDeltaShadow());
//		}

		final HtmlColor back = param.getBackcolor();
		if (back instanceof HtmlColorGradient) {
//			eps.setStrokeColor(mapper.getMappedColor(param.getColor()));
//			eps.epsRectangle(x, y, width, height, rx / 2, ry / 2, (HtmlColorGradient) back, mapper);
		} else {
			final String color = param.getColor() == null ? null : StringUtils.getAsHtml(mapper.getMappedColor(param
					.getColor()));
			final String backcolor = param.getColor() == null ? null : StringUtils.getAsHtml(mapper.getMappedColor(param
					.getBackcolor()));

			html.setStrokeColor(color);
			html.setFillColor(backcolor);
//			eps.setStrokeWidth("" + param.getStroke().getThickness(), param.getStroke().getDashVisible(), param
//					.getStroke().getDashSpace());
			html.htmlRectangle(x, y, width, height, rx / 2, ry / 2);
		}

	}
}
