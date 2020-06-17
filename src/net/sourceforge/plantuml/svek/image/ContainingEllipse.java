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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UEllipse;

public class ContainingEllipse {

	private final SmallestEnclosingCircle sec = new SmallestEnclosingCircle();
	private final YTransformer ytransformer;

	@Override
	public String toString() {
		return "ContainingEllipse " + getWidth() + " " + getHeight();
	}

	public ContainingEllipse(double coefY) {
		ytransformer = new YTransformer(coefY);
	}

	public void append(Point2D pt) {
		pt = ytransformer.getReversePoint2D(pt);
		sec.append(pt);
	}

	public void append(double x, double y) {
		append(new Point2D.Double(x, y));
	}

	public double getWidth() {
		return 2 * sec.getCircle().getRadius();
	}

	public double getHeight() {
		return 2 * sec.getCircle().getRadius() * ytransformer.getAlpha();
	}

	public Point2D getCenter() {
		return ytransformer.getPoint2D(sec.getCircle().getCenter());
	}

	public UEllipse asUEllipse() {
		final UEllipse ellipse = new UEllipse(getWidth(), getHeight());
		ellipse.setDeltaShadow(deltaShadow);
		return ellipse;
	}

	private double deltaShadow;

	public void setDeltaShadow(double deltaShadow) {
		this.deltaShadow = deltaShadow;
	}

}
