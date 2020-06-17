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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Locale;

import net.sourceforge.plantuml.Dimension2DDouble;

public class Block implements Clusterable {

	private final int uid;
	private final double width;
	private final double height;
	private double x;
	private double y;
	private final Cluster parent;

	public Block(int uid, double width, double height, Cluster parent) {
		this.uid = uid;
		this.width = width;
		this.height = height;
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "BLOCK " + uid;
	}

	public String toStringPosition() {
		return String.format(Locale.US, "x=%9.2f y=%9.2f w=%9.2f h=%9.2f", x, y, width, height);
	}

	public int getUid() {
		return uid;
	}

	public Cluster getParent() {
		return parent;
	}

	public Point2D getPosition() {
		return new Point2D.Double(x, y);
	}

	public Dimension2D getSize() {
		return new Dimension2DDouble(width, height);
	}

	public void setCenterX(double center) {
		this.x = center - width / 2;
	}

	public void setCenterY(double center) {
		this.y = center - height / 2;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final void setY(double y) {
		this.y = y;
	}
	
	public void moveSvek(double deltaX, double deltaY) {
		throw new UnsupportedOperationException();
	}


}
