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

public class URectangle extends AbstractShadowable implements Scalable, UShapeSized {

	private final double width;
	private final double height;
	private final double rx;
	private final double ry;
	private final String comment;

	public URectangle withHeight(double newHeight) {
		final URectangle result = new URectangle(width, newHeight, rx, ry, comment);
		result.ignoreForCompression = this.ignoreForCompression;
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public URectangle withWidth(double newWidth) {
		final URectangle result = new URectangle(newWidth, height, rx, ry, comment);
		result.ignoreForCompression = this.ignoreForCompression;
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public UShape getScaled(double scale) {
		if (scale == 1) {
			return this;
		}
		final AbstractShadowable result = new URectangle(width * scale, height * scale, rx * scale, ry * scale, comment);
		result.setDeltaShadow(this.getDeltaShadow());
		return result;
	}

	public URectangle(double width, double height) {
		this(width, height, 0, 0, null);
	}

	public URectangle(double width, double height, double rx, double ry) {
		this(width, height, rx, ry, null);
	}

	public URectangle(double width, double height, double rx, double ry, String comment) {
		// if (height == 0) {
		// throw new IllegalArgumentException();
		// }
		if (width == 0) {
			throw new IllegalArgumentException();
		}
		this.comment = comment;
		this.width = width;
		this.height = height;
		this.rx = rx;
		this.ry = ry;
	}

	public URectangle(Dimension2D dim) {
		this(dim.getWidth(), dim.getHeight());
	}

	public URectangle(Dimension2D dim, double rx, double ry) {
		this(dim.getWidth(), dim.getHeight(), rx, ry);
	}

	@Override
	public String toString() {
		return "width=" + width + " height=" + height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getRx() {
		return rx;
	}

	public double getRy() {
		return ry;
	}

	public URectangle clip(UClip clip) {
		return this;
	}

	public MinMax getMinMax() {
		return MinMax.fromMax(width, height);
	}

	public final String getComment() {
		return comment;
	}

	private boolean ignoreForCompression;

	public final boolean isIgnoreForCompression() {
		return ignoreForCompression;
	}

	public final void setIgnoreForCompression(boolean ignoreForCompression) {
		this.ignoreForCompression = ignoreForCompression;
	}

}
