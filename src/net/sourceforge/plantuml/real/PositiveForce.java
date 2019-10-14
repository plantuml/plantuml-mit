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
package net.sourceforge.plantuml.real;

class PositiveForce {

	private final Real fixedPoint;
	private final RealMoveable movingPoint;
	private final double minimunDistance;
	private final boolean trace = false;
	private final Throwable creationPoint;

	public PositiveForce(Real fixedPoint, RealMoveable movingPoint, double minimunDistance) {
		if (fixedPoint == movingPoint) {
			throw new IllegalArgumentException();
		}
		this.fixedPoint = fixedPoint;
		this.movingPoint = movingPoint;
		this.minimunDistance = minimunDistance;
		this.creationPoint = new Throwable();
		this.creationPoint.fillInStackTrace();
	}

	@Override
	public String toString() {
		return "PositiveForce fixed=" + fixedPoint + " moving=" + movingPoint + " min=" + minimunDistance;
	}

	public boolean apply() {
		if (trace) {
			System.err.println("apply " + this);
		}
		final double movingPointValue = movingPoint.getCurrentValue();
		final double fixedPointValue;
		try {
			fixedPointValue = fixedPoint.getCurrentValue();
		} catch (IllegalStateException e) {
			System.err.println("Pb with force " + this);
			System.err.println("This force has been created here:");
			creationPoint.printStackTrace();
			System.err.println("The fixed point has been created here: " + fixedPoint);
			fixedPoint.printCreationStackTrace();
			throw e;
		}
		final double distance = movingPointValue - fixedPointValue;
		final double diff = distance - minimunDistance;
		if (diff >= 0) {
			if (trace) {
				System.err.println("Not using ");
			}
			return false;
		}
		if (trace) {
			System.err.println("moving " + (-diff) + " " + movingPoint);
		}
		movingPoint.move(-diff);
		return true;
	}

}
