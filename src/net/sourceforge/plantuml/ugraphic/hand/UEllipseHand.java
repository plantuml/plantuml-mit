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
package net.sourceforge.plantuml.ugraphic.hand;

import java.awt.geom.Point2D;
import java.util.Random;

import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class UEllipseHand {

	private Shadowable poly;
	private final Random rnd;

	private double randomMe() {
		return rnd.nextDouble();
	}

	public UEllipseHand(UEllipse source, Random rnd) {
		this.rnd = rnd;
		if (source.getStart() != 0 || source.getExtend() != 0) {
			this.poly = source;
			return;
		}
		poly = new UPolygon();
		final double width = source.getWidth();
		final double height = source.getHeight();
		double angle = 0;
		if (width == height) {
			while (angle < Math.PI * 2) {
				angle += (10 + randomMe() * 10) * Math.PI / 180;
				final double variation = 1 + (randomMe() - 0.5) / 8;
				final double x = width / 2 + Math.cos(angle) * width * variation / 2;
				final double y = height / 2 + Math.sin(angle) * height * variation / 2;
				// final Point2D.Double p = new Point2D.Double(x, y);
				((UPolygon) poly).addPoint(x, y);
			}
		} else {
			while (angle < Math.PI * 2) {
				angle += Math.PI / 20;
				final Point2D pt = getPoint(width, height, angle);
				((UPolygon) poly).addPoint(pt.getX(), pt.getY());
			}

		}

		this.poly.setDeltaShadow(source.getDeltaShadow());
	}

	private Point2D getPoint(double width, double height, double angle) {
		final double x = width / 2 + Math.cos(angle) * width / 2;
		final double y = height / 2 + Math.sin(angle) * height / 2;
		final double variation = (randomMe() - 0.5) / 50;
		return new Point2D.Double(x + variation * width, y + variation * height);

	}

	public Shadowable getHanddrawn() {
		return this.poly;
	}

}
