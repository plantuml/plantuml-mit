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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.Arrays;

class FrontierComplex implements Frontier {

	private final double freeY[];

	public FrontierComplex(double freeY, int rangeEnd) {
		this.freeY = new double[rangeEnd + 1];
		for (int i = 0; i <= rangeEnd; i++) {
			this.freeY[i] = freeY;
		}
	}

	private FrontierComplex(double freeY[]) {
		this.freeY = freeY;
	}

	private FrontierComplex(double freeY[], double delta, ParticipantRange range) {
		this(freeY.clone());
		final double newV = getFreeY(range) + delta;
		for (int i = range.start(); i <= range.end(); i++) {
			this.freeY[i] = newV;
		}
	}

	public double getFreeY(ParticipantRange range) {
		if (range == null) {
			throw new IllegalArgumentException();
		}
		double result = freeY[range.start()];
		for (int i = range.start(); i <= range.end(); i++) {
			if (freeY[i] > result) {
				result = freeY[i];
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(freeY);
	}

	public FrontierComplex add(double delta, ParticipantRange range) {
		if (range == null) {
			throw new IllegalArgumentException();
		}
		return new FrontierComplex(freeY, delta, range);
	}

	FrontierComplex copy() {
		return new FrontierComplex(freeY.clone());
	}
	
	FrontierComplex mergeMax(FrontierComplex other) {
		if (this.freeY.length != other.freeY.length) {
			throw new IllegalArgumentException();
		}
		final FrontierComplex result = new FrontierComplex(new double[freeY.length]);
		for (int i=0; i<freeY.length; i++) {
			result.freeY[i] = Math.max(this.freeY[i], other.freeY[i]);
		}
		return result;
	}

	// public double diff(Frontier other) {
	// return freeY - other.freeY;
	// }

}
