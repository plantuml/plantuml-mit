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
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;

public class DriverTextG2d implements UDriver<Graphics2D> {

	private final EnsureVisible visible;

	public DriverTextG2d(EnsureVisible visible) {
		this.visible = visible;
	}

	private static void printFont() {
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final String fontNames[] = ge.getAvailableFontFamilyNames();
		final int j = fontNames.length;
		for (int i = 0; i < j; i++) {
			Log.info("Available fonts: " + fontNames[i]);
		}
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UText shape = (UText) ushape;
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();

		final UFont font = fontConfiguration.getFont().scaled(param.getScale());
		final Dimension2D dimBack = calculateDimension(FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault()), font, shape.getText());
		final HtmlColor extended = fontConfiguration.getExtendedColor();
		if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
			final Rectangle2D.Double area = new Rectangle2D.Double(x, y - dimBack.getHeight() + 1.5,
					dimBack.getWidth(), dimBack.getHeight());
			if (extended instanceof HtmlColorGradient) {
				final GradientPaint paint = DriverRectangleG2d.getPaintGradient(x, y, mapper, dimBack.getWidth(),
						dimBack.getHeight(), extended);
				g2d.setPaint(paint);
				g2d.fill(area);
			} else {
				final Color backColor = mapper.getMappedColor(extended);
				if (backColor != null) {
					g2d.setColor(backColor);
					g2d.setBackground(backColor);
					g2d.fill(area);
				}
			}
		}
		visible.ensureVisible(x, y - dimBack.getHeight() + 1.5);
		visible.ensureVisible(x + dimBack.getWidth(), y + 1.5);

		g2d.setFont(font.getFont());
		g2d.setColor(mapper.getMappedColor(fontConfiguration.getColor()));
		g2d.drawString(shape.getText(), (float) x, (float) y);

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			if (extended != null) {
				g2d.setColor(mapper.getMappedColor(extended));
			}
			final Dimension2D dim = calculateDimension(FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault()), font, shape.getText());
			final int ypos = (int) (y + 2.5);
			g2d.setStroke(new BasicStroke((float) 1));
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
			g2d.setStroke(new BasicStroke());
		}
		if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
			final Dimension2D dim = calculateDimension(FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault()), font, shape.getText());
			final int ypos = (int) (y + 2.5) - 1;
			if (extended != null) {
				g2d.setColor(mapper.getMappedColor(extended));
			}
			for (int i = (int) x; i < x + dim.getWidth() - 5; i += 6) {
				g2d.drawLine(i, ypos - 0, i + 3, ypos + 1);
				g2d.drawLine(i + 3, ypos + 1, i + 6, ypos - 0);
			}
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final Dimension2D dim = calculateDimension(FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault()), font, shape.getText());
			final FontMetrics fm = g2d.getFontMetrics(font.getFont());
			final int ypos = (int) (y - fm.getDescent() - 0.5);
			if (extended != null) {
				g2d.setColor(mapper.getMappedColor(extended));
			}
			g2d.setStroke(new BasicStroke((float) 1.5));
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
			g2d.setStroke(new BasicStroke());
		}
	}

	static public Dimension2D calculateDimension(StringBounder stringBounder, UFont font, String text) {
		final Dimension2D rect = stringBounder.calculateDimension(font, text);
		double h = rect.getHeight();
		if (h < 10) {
			h = 10;
		}
		return new Dimension2DDouble(rect.getWidth(), h);
	}

}
