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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class RealMax extends AbstractReal implements Real {

	private final List<Real> all = new ArrayList<Real>();
	private final Throwable creationPoint;

	RealMax(Collection<Real> reals) {
		super(line(reals));
		this.all.addAll(reals);
		this.creationPoint = new Throwable();
		this.creationPoint.fillInStackTrace();
	}

	static RealLine line(Collection<Real> reals) {
		return ((AbstractReal) reals.iterator().next()).getLine();
	}

	public String getName() {
		return "max " + all;
	}

	@Override
	double getCurrentValueInternal() {
		double result = all.get(0).getCurrentValue();
		for (int i = 1; i < all.size(); i++) {
			Throwable t = new Throwable();
			t.fillInStackTrace();
			final int stackLength = t.getStackTrace().length;
			if (stackLength > 1000) {
				System.err.println("The faulty RealMax " + getName());
				System.err.println("has been created here:");
				printCreationStackTrace();
				throw new IllegalStateException("Infinite recursion?");
			}
			final double v = all.get(i).getCurrentValue();
			if (v > result) {
				result = v;
			}
		}
		return result;
	}

	public Real addFixed(double delta) {
		return new RealDelta(this, delta);
	}

	public Real addAtLeast(double delta) {
		throw new UnsupportedOperationException();
	}

	public void ensureBiggerThan(Real other) {
		throw new UnsupportedOperationException();
	}

	public void printCreationStackTrace() {
		creationPoint.printStackTrace();
	}

}
