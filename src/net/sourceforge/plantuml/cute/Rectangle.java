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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;

public class Rectangle implements CuteShape {

	private final double width;
	private final double height;
	private final RotationZoom rotationZoom;
	private final double curvation;

	public Rectangle(VarArgs varArgs) {
		final Point2D dim = varArgs.getAsPoint("dimension");
		this.width = dim.getX();
		this.height = dim.getY();
		this.rotationZoom = RotationZoom.none();
		this.curvation = varArgs.getAsDouble("curve", MyPoint2D.NO_CURVE);
	}

	private Rectangle(double width, double height, RotationZoom rotationZoom, double curvation) {
		this.width = width;
		this.height = height;
		this.rotationZoom = rotationZoom;
		this.curvation = curvation;
	}

	public void drawU(UGraphic ug) {
		CutePath cutePath = new CutePath();
		cutePath.add(new Arc(MyPoint2D.from(0, 0).withCurvation(curvation), MyPoint2D.from(width, 0).withCurvation(
				curvation)));
		cutePath.add(new Arc(MyPoint2D.from(width, 0).withCurvation(curvation), MyPoint2D.from(width, height)
				.withCurvation(curvation)));
		cutePath.add(new Arc(MyPoint2D.from(width, height).withCurvation(curvation), MyPoint2D.from(0, height)
				.withCurvation(curvation)));
		cutePath.add(new Arc(MyPoint2D.from(0, height).withCurvation(curvation), MyPoint2D.from(0, 0).withCurvation(
				curvation)));
		cutePath = cutePath.rotateZoom(rotationZoom);
		cutePath.drawU(ug);
	}

	public void drawUOld(UGraphic ug) {
		final UPath path = new UPath();
		if (curvation == MyPoint2D.NO_CURVE) {
			path.moveTo(rotationZoom.getPoint(0, 0));
			path.lineTo(rotationZoom.getPoint(width, 0));
			path.lineTo(rotationZoom.getPoint(width, height));
			path.lineTo(rotationZoom.getPoint(0, height));
			path.lineTo(rotationZoom.getPoint(0, 0));
		} else {
			path.moveTo(rotationZoom.getPoint(width, curvation));
			path.lineTo(rotationZoom.getPoint(width, height - curvation));
			path.arcTo(rotationZoom.getPoint(width - curvation, height), curvation, 0, 1);
			path.lineTo(rotationZoom.getPoint(curvation, height));
			path.arcTo(rotationZoom.getPoint(0, height - curvation), curvation, 0, 1);
			path.lineTo(rotationZoom.getPoint(0, curvation));
			path.arcTo(rotationZoom.getPoint(curvation, 0), curvation, 0, 1);
			path.lineTo(rotationZoom.getPoint(width - curvation, 0));
			path.arcTo(rotationZoom.getPoint(width, curvation), curvation, 0, 1);
		}
		path.closePath();
		ug.draw(path);
	}

	public Rectangle rotateZoom(RotationZoom other) {
		return new Rectangle(width, height, rotationZoom.compose(other), curvation);
	}

}
