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
package net.sourceforge.plantuml.mindmap;

import java.awt.geom.Line2D;

public class SymetricalTeePositioned {

	private final SymetricalTee tee;
	private double y;

	@Override
	public String toString() {
		return "y=" + y + " " + tee;
	}

	public SymetricalTeePositioned(SymetricalTee tee) {
		this(tee, 0);
	}

	private SymetricalTeePositioned(SymetricalTee tee, double y) {
		this.tee = tee;
		this.y = y;
	}

	public void moveSoThatSegmentA1isOn(double newY) {
		final double current = getSegmentA1().getY1();
		y += newY - current;
	}

	public void moveSoThatSegmentA2isOn(double newY) {
		final double current = getSegmentA2().getY1();
		y += newY - current;
	}

	public void move(double delta) {
		y += delta;
	}

	public Line2D getSegmentA1() {
		return new Line2D.Double(0, y - tee.getThickness1() / 2, tee.getElongation1(), y - tee.getThickness1() / 2);
	}

	public Line2D getSegmentB1() {
		return new Line2D.Double(0, y + tee.getThickness1() / 2, tee.getElongation1(), y + tee.getThickness1() / 2);
	}

	public Line2D getSegmentA2() {
		return new Line2D.Double(tee.getElongation1(), y - tee.getThickness2() / 2, tee.getElongation1()
				+ tee.getElongation2(), y - tee.getThickness2() / 2);
	}

	public Line2D getSegmentB2() {
		return new Line2D.Double(tee.getElongation1(), y + tee.getThickness2() / 2, tee.getElongation1()
				+ tee.getElongation2(), y + tee.getThickness2() / 2);
	}

	public double getMaxX() {
		return tee.getElongation1() + tee.getElongation2();
	}

	public double getMaxY() {
		return y + Math.max(tee.getThickness1() / 2, tee.getThickness2() / 2);
	}

	public double getMinY() {
		return y - Math.max(tee.getThickness1() / 2, tee.getThickness2() / 2);
	}

	public final double getY() {
		return y;
	}

	public SymetricalTeePositioned getMax(SymetricalTeePositioned other) {
		if (this.tee != other.tee) {
			throw new IllegalArgumentException();
		}
		if (other.y > this.y) {
			return other;
		}
		return this;
	}

}
