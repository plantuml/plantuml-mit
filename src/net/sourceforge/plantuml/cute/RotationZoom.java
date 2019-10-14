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

import net.sourceforge.plantuml.ugraphic.UTranslate;

public class RotationZoom {

	private final double angle;
	private final double zoom;

	private RotationZoom(double angle, double zoom) {
		if (zoom < 0) {
			throw new IllegalArgumentException();
		}
		this.angle = angle;
		this.zoom = zoom;
	}

	public RotationZoom compose(RotationZoom other) {
		return new RotationZoom(this.angle + other.angle, this.zoom * other.zoom);
	}

	@Override
	public String toString() {
		return "Rotation=" + Math.toDegrees(angle) + " Zoom=" + zoom;
	}

	public static RotationZoom fromVarArgs(VarArgs varArgs) {
		final double radians = Math.toRadians(varArgs.getAsDouble("rotation", 0));
		final double scale = varArgs.getAsDouble("scale", 1);
		return new RotationZoom(radians, scale);
	}

	public static RotationZoom rotationInDegrees(double angle) {
		return new RotationZoom(Math.toRadians(angle), 1);
	}

	public static RotationZoom rotationInRadians(double angle) {
		return new RotationZoom(angle, 1);
	}

	public static RotationZoom zoom(double zoom) {
		return new RotationZoom(0, zoom);
	}

	public RotationZoom inverse() {
		return new RotationZoom(-angle, 1 / zoom);
	}

	public double getAngleDegree() {
		return Math.toDegrees(angle);
	}

	static public RotationZoom builtRotationOnYaxis(Point2D toRotate) {
		final double a = Math.atan2(toRotate.getX(), toRotate.getY());
		return new RotationZoom(a, 1);
	}

	public Point2D.Double getPoint(double x, double y) {
		if (angle == 0) {
			return new Point2D.Double(x * zoom, y * zoom);
		}
		final double x1 = Math.cos(angle) * x - Math.sin(angle) * y;
		final double y1 = Math.sin(angle) * x + Math.cos(angle) * y;
		return new Point2D.Double(x1 * zoom, y1 * zoom);
	}

	public Point2D getPoint(Point2D p) {
		return getPoint(p.getX(), p.getY());
	}

	public UTranslate getUTranslate(UTranslate translate) {
		return new UTranslate(getPoint(translate.getDx(), translate.getDy()));

	}

	public static RotationZoom none() {
		return new RotationZoom(0, 1);
	}

	public boolean isNone() {
		return angle == 0 && zoom == 1;
	}

	public double applyZoom(double value) {
		return value * zoom;
	}

	public double applyRotation(double alpha) {
		return angle + alpha;
	}

}
