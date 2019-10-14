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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import net.sourceforge.plantuml.Log;

public class DriverShadowedG2d {

	private ConvolveOp getConvolveOp(int blurRadius, double dpiFactor) {
		blurRadius = (int) (blurRadius * dpiFactor);
		final int blurRadius2 = blurRadius * blurRadius;
		final float blurRadius2F = blurRadius2;
		// final float weight = (float) (1.0 / blurRadius2F / dpiFactor);
		final float weight = (float) (1.0 / blurRadius2F);
		final float[] elements = new float[blurRadius2];
		for (int k = 0; k < blurRadius2; k++) {
			elements[k] = weight;
		}
		final Kernel myKernel = new Kernel(blurRadius, blurRadius, elements);

		// if EDGE_NO_OP is not selected, EDGE_ZERO_FILL is the default which
		// creates a black border
		return new ConvolveOp(myKernel, ConvolveOp.EDGE_NO_OP, null);
	}

	private final Color color = new Color(170, 170, 170);
	private final Color colorLine = new Color(30, 30, 30);

	protected void drawShadow(Graphics2D g2d, Shape shape, double deltaShadow, double dpiFactor) {
		if (dpiFactor < 1) {
			dpiFactor = 1;
		}
		// dpiFactor = 1;
		// Shadow
		final Rectangle2D bounds = shape.getBounds2D();
		final double ww = bounds.getMaxX() - bounds.getMinX();
		final double hh = bounds.getMaxY() - bounds.getMinY();

		final double w = (ww + deltaShadow * 2 + 6) * dpiFactor;
		final double h = (hh + deltaShadow * 2 + 6) * dpiFactor;
		BufferedImage destination = null;
		try {
			destination = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D gg = destination.createGraphics();
			gg.scale(dpiFactor, dpiFactor);
			gg.translate(deltaShadow - bounds.getMinX(), deltaShadow - bounds.getMinY());
			final boolean isLine = shape instanceof Line2D.Double;
			if (isLine) {
				gg.setColor(colorLine);
				gg.draw(shape);
			} else {
				gg.setColor(color);
				gg.fill(shape);
			}
			gg.dispose();

			final ConvolveOp simpleBlur = getConvolveOp(6, dpiFactor);
			destination = simpleBlur.filter(destination, null);
		} catch (OutOfMemoryError error) {
			Log.info("Warning: Cannot draw shadow, image too big.");
		} catch (Exception e) {
			Log.info("Warning: Cannot draw shadow: " + e);
		}
		if (destination != null) {
			final AffineTransform at = g2d.getTransform();
			g2d.scale(1 / dpiFactor, 1 / dpiFactor);
			g2d.drawImage(destination, (int) (bounds.getMinX() * dpiFactor), (int) (bounds.getMinY() * dpiFactor), null);
			g2d.setTransform(at);
		}
	}
}
