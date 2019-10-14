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

public class SymetricalTee {

	private final double thickness1;
	private final double elongation1;
	private final double thickness2;
	private final double elongation2;

	@Override
	public String toString() {
		return "t1=" + thickness1 + " e1=" + elongation1 + " t2=" + thickness2 + " e2=" + elongation2;
	}

	public SymetricalTee(double thickness1, double elongation1, double thickness2, double elongation2) {
		this.thickness1 = thickness1;
		this.elongation1 = elongation1;
		this.thickness2 = thickness2;
		this.elongation2 = elongation2;
	}

	public double getThickness1() {
		return thickness1;
	}

	public double getElongation1() {
		return elongation1;
	}

	public double getThickness2() {
		return thickness2;
	}

	public double getElongation2() {
		return elongation2;
	}

	public double getFullElongation() {
		return elongation1 + elongation2;
	}

	public double getFullThickness() {
		return Math.max(thickness1, thickness2);
	}

}
