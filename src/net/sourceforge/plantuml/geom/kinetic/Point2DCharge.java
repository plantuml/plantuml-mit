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
package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Log;

public class Point2DCharge extends Point2D.Double {

	private double charge = 1.0;

	private MoveObserver moveObserver = null;

	public Point2DCharge(double x, double y) {
		super(x, y);
	}

	public Point2DCharge(Point2D pt, double ch) {
		super(pt.getX(), pt.getY());
		this.charge = ch;
	}

	public void apply(VectorForce value) {
		Log.println("Applying " + value);
		x += value.getX();
		y += value.getY();
		if (moveObserver != null) {
			moveObserver.pointMoved(this);
		}
	}

	@Override
	final public void setLocation(double x, double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public void setLocation(Point2D p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return System.identityHashCode(this) + " " + String.format("[%8.2f %8.2f]", x, y);
	}

	public final double getCharge() {
		return charge;
	}

	public final void setCharge(double charge) {
		this.charge = charge;
	}

	private final int hash = System.identityHashCode(this);

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	public final void setMoveObserver(MoveObserver moveObserver) {
		this.moveObserver = moveObserver;
	}

}
