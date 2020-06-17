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
package net.sourceforge.plantuml.openiconic;

public class SvgPosition {

	final private SvgCommandNumber x;
	final private SvgCommandNumber y;

	public SvgPosition() {
		this(new SvgCommandNumber("0"), new SvgCommandNumber("0"));
	}

	public SvgPosition(SvgCommandNumber x, SvgCommandNumber y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x.toSvg() + "," + y.toSvg();
	}

	public SvgPosition(double x, double y) {
		this.x = new SvgCommandNumber(x);
		this.y = new SvgCommandNumber(y);
	}

	public SvgCommandNumber getX() {
		return x;
	}

	public SvgCommandNumber getY() {
		return y;
	}

	public double getXDouble() {
		return x.getDouble();
	}

	public double getYDouble() {
		return y.getDouble();
	}

	public SvgPosition add(SvgPosition other) {
		return new SvgPosition(x.add(other.x), y.add(other.y));
	}

	public SvgPosition getMirror(SvgPosition tobeMirrored) {
		final double centerX = getXDouble();
		final double centerY = getYDouble();
		// x1+x2 = 2*xc
		final double x = 2 * centerX - tobeMirrored.getXDouble();
		final double y = 2 * centerY - tobeMirrored.getYDouble();
		return new SvgPosition(x, y);
	}
}
