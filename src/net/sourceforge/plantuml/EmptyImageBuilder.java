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
package net.sourceforge.plantuml;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class EmptyImageBuilder {

	private final BufferedImage im;
	private final Graphics2D g2d;

	public EmptyImageBuilder(double width, double height, Color background) {
		this((int) width, (int) height, background);
	}

	public EmptyImageBuilder(int width, int height, Color background) {
		if (width > GraphvizUtils.getenvImageLimit()) {
			Log.info("Width too large " + width + ". You should set PLANTUML_LIMIT_SIZE");
			width = GraphvizUtils.getenvImageLimit();
		}
		if (height > GraphvizUtils.getenvImageLimit()) {
			Log.info("Height too large " + height + ". You should set PLANTUML_LIMIT_SIZE");
			height = GraphvizUtils.getenvImageLimit();
		}
		Log.info("Creating image " + width + "x" + height);
		im = new BufferedImage(width, height, background == null ? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB);
		g2d = im.createGraphics();
		UAntiAliasing.ANTI_ALIASING_ON.apply(g2d);
		if (background != null) {
			g2d.setColor(background);
			g2d.fillRect(0, 0, width, height);
		}
	}

	public EmptyImageBuilder(int width, int height, Color background, double dpiFactor) {
		this(width * dpiFactor, height * dpiFactor, background);
		if (dpiFactor != 1.0) {
			g2d.setTransform(AffineTransform.getScaleInstance(dpiFactor, dpiFactor));
		}
	}

	public BufferedImage getBufferedImage() {
		return im;
	}

	public Graphics2D getGraphics2D() {
		return g2d;
	}

	public UGraphicG2d getUGraphicG2d() {
		final UGraphicG2d result = new UGraphicG2d(new ColorMapperIdentity(), g2d, 1.0);
		result.setBufferedImage(im);
		return result;
	}

}
