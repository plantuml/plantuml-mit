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
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;

public class DriverEllipseG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;
	private final EnsureVisible visible;

	public DriverEllipseG2d(double dpiFactor, EnsureVisible visible) {
		this.dpiFactor = dpiFactor;
		this.visible = visible;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UEllipse ellipse = (UEllipse) ushape;
		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));
		visible.ensureVisible(x, y);
		visible.ensureVisible(x + ellipse.getWidth(), y + ellipse.getHeight());
		final HColor color = param.getColor();
		if (ellipse.getStart() == 0 && ellipse.getExtend() == 0) {
			final Shape shape = new Ellipse2D.Double(x, y, ellipse.getWidth(), ellipse.getHeight());

			// Shadow
			if (ellipse.getDeltaShadow() != 0) {
				drawShadow(g2d, shape, ellipse.getDeltaShadow(), dpiFactor);
			}

			final HColor back = param.getBackcolor();
			if (back instanceof HColorGradient) {
				final GradientPaint paint = DriverRectangleG2d.getPaintGradient(x, y, mapper, ellipse.getWidth(),
						ellipse.getHeight(), back);
				g2d.setPaint(paint);
				g2d.fill(shape);
				DriverRectangleG2d.drawBorder(param, color, mapper, ellipse, shape, g2d, x, y);
			} else {
				if (back != null) {
					g2d.setColor(mapper.toColor(param.getBackcolor()));
					DriverRectangleG2d.managePattern(param, g2d);
					g2d.fill(shape);
				}
				if (color != null && color.equals(param.getBackcolor()) == false) {
					DriverRectangleG2d.drawBorder(param, color, mapper, ellipse, shape, g2d, x, y);
				}
			}
		} else {
			final Shape arc = new Arc2D.Double(x, y, ellipse.getWidth(), ellipse.getHeight(),
					round(ellipse.getStart()), round(ellipse.getExtend()), Arc2D.OPEN);
			if (color != null) {
				g2d.setColor(mapper.toColor(color));
				g2d.draw(arc);
			}
		}
	}

	private static final double ROU = 5.0;

	static double round(double value) {
		return value;
		// final int v = (int) Math.round(value / ROU);
		// return v * ROU;
	}

}
