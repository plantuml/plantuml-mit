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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.UDrawable;

public abstract class Extremity implements UDrawable {


	protected double manageround(double angle) {
		final double deg = angle * 180.0 / Math.PI;
		if (isCloseTo(0, deg)) {
			return 0;
		}
		if (isCloseTo(90, deg)) {
			return 90.0 * Math.PI / 180.0;
		}
		if (isCloseTo(180, deg)) {
			return 180.0 * Math.PI / 180.0;
		}
		if (isCloseTo(270, deg)) {
			return 270.0 * Math.PI / 180.0;
		}
		if (isCloseTo(360, deg)) {
			return 0;
		}
		return angle;
	}

	private boolean isCloseTo(double value, double variable) {
		if (Math.abs(value - variable) < 0.05) {
			return true;
		}
		return false;
	}
	
	public abstract Point2D somePoint();
	
	public Point2D isTooSmallSoGiveThePointCloserToThisOne(Point2D pt) {
		return null;
	}

}
