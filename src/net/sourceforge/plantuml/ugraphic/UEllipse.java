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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;

public class UEllipse extends AbstractShadowable implements Scalable, UShapeSized {

	private final double width;
	private final double height;
	private final double start;
	private final double extend;

	public UShape getScaled(double scale) {
		if (scale == 1) {
			return this;
		}
		final AbstractShadowable result = new UEllipse(width * scale, height * scale, start, extend);
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public UEllipse(double width, double height) {
		this(width, height, 0, 0);
	}

	public UEllipse(double width, double height, double start, double extend) {
		this.width = width;
		this.height = height;
		this.start = start;
		this.extend = extend;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public final double getStart() {
		return start;
	}

	public final double getExtend() {
		return extend;
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(width, height);
	}

	public UEllipse bigger(double more) {
		final UEllipse result = new UEllipse(width + more, height + more);
		result.setDeltaShadow(getDeltaShadow());
		return result;
	}

	public double getStartingX(double y) {
		y = y / height * 2;
		final double x = 1 - Math.sqrt(1 - (y - 1) * (y - 1));
		return x * width / 2;
	}

	public double getEndingX(double y) {
		y = y / height * 2;
		final double x = 1 + Math.sqrt(1 - (y - 1) * (y - 1));
		return x * width / 2;
	}

}
