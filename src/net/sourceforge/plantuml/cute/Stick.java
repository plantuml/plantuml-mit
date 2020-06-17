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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;

public class Stick implements CuteShape {

	private final double width;
	private final double height;
	private final RotationZoom rotationZoom;

	public Stick(VarArgs varArgs) {
		final Point2D dim = varArgs.getAsPoint("dimension");
		this.width = dim.getX();
		this.height = dim.getY();
		this.rotationZoom = RotationZoom.none();
	}

	private Stick(double width, double height, RotationZoom rotation) {
		this.width = width;
		this.height = height;
		this.rotationZoom = rotation;
	}

	public void drawU(UGraphic ug) {
		if (width > height) {
			drawRotate1(ug);
		} else {
			drawRotate2(ug);
		}
	}

	private void drawRotate1(UGraphic ug) {
		assert width > height;
		final UPath path = new UPath();
		final double small = height / 2;
		path.moveTo(rotationZoom.getPoint(small, 0));
		path.lineTo(rotationZoom.getPoint(width - small, 0));
		path.arcTo(rotationZoom.getPoint(width - small, height), small, 0, 1);
		path.lineTo(rotationZoom.getPoint(small, height));
		path.arcTo(rotationZoom.getPoint(small, 0), small, 0, 1);
		path.closePath();
		ug.draw(path);
	}

	private void drawRotate2(UGraphic ug) {
		assert height > width;
		final UPath path = new UPath();
		final double small = width / 2;
		path.moveTo(rotationZoom.getPoint(width, small));
		path.lineTo(rotationZoom.getPoint(width, height - small));
		path.arcTo(rotationZoom.getPoint(0, height - small), small, 0, 1);
		path.lineTo(rotationZoom.getPoint(0, small));
		path.arcTo(rotationZoom.getPoint(width, small), small, 0, 1);
		path.closePath();
		ug.draw(path);
	}

	public Stick rotateZoom(RotationZoom other) {
		return new Stick(width, height, this.rotationZoom.compose(other));
	}

}
