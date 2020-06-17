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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;

import net.sourceforge.plantuml.SvgString;
import net.sourceforge.plantuml.ugraphic.MutableImage;

public class LatexBuilder implements ScientificEquation {

	private final String tex;

	public LatexBuilder(String tex) {
		this.tex = tex;
	}

	private Dimension2D dimension;

	public Dimension2D getDimension() {
		return dimension;
	}

	private Icon buildIcon(Color foregroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return new TeXIconBuilder(tex, foregroundColor).getIcon();
	}

	public SvgString getSvg(double scale, Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, IOException {
		final Icon icon = buildIcon(foregroundColor);
		final ConverterSvg converterSvg = new ConverterSvg(icon);
		final String svg = converterSvg.getSvg(scale, true, backgroundColor);
		dimension = converterSvg.getDimension();
		return new SvgString(svg, scale);
	}

	public MutableImage getImage(Color foregroundColor, Color backgroundColor)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Icon icon = buildIcon(foregroundColor);
		return new LatexImage(icon, 1, foregroundColor, backgroundColor);
	}

	class LatexImage implements MutableImage {

		private final double scale;
		private final Icon icon;
		private final Color foregroundColor;
		private final Color backgroundColor;
		private BufferedImage cache = null;

		public LatexImage(Icon icon, double scale, Color foregroundColor, Color backgroundColor) {
			this.scale = scale;
			this.foregroundColor = foregroundColor;
			this.backgroundColor = backgroundColor;
			this.icon = icon;
		}

		public BufferedImage getImage() {
			if (cache == null) {
				cache = new BufferedImage((int) (icon.getIconWidth() * scale), (int) (icon.getIconHeight() * scale),
						BufferedImage.TYPE_INT_ARGB);
				final Graphics2D g2 = cache.createGraphics();
				g2.scale(scale, scale);
				if (backgroundColor != null) {
					g2.setColor(backgroundColor);
					g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
				}
				icon.paintIcon(null, g2, 0, 0);
			}
			return cache;
		}

		public MutableImage withScale(double scale) {
			return new LatexImage(icon, this.scale * scale, foregroundColor, backgroundColor);
		}

		public MutableImage muteColor(Color newColor) {
			return this;
		}

		public MutableImage muteTransparentColor(Color newColor) {
			return this;
		}

	}

	public String getSource() {
		return tex;
	}

}
