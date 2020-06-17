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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;

public class Oscillator {

	private int n = 3;
	private int i = 0;
	private char seg = 'A';

	private int x = -1;
	private int y = -1;

	public Point2D.Double nextPosition() {
		assert n % 2 == 1;
		final int halfN = (n - 1) / 2;
		final Point2D.Double result = new Point2D.Double(x, y);
		i++;
		if (seg == 'A') {
			x++;
			if (x > halfN) {
				seg = 'B';
				x = halfN;
				y = -halfN + 1;
			}
		} else if (seg == 'B') {
			y++;
			if (y > halfN) {
				seg = 'C';
				x = halfN - 1;
				y = halfN;
			}
		} else if (seg == 'C') {
			x--;
			if (x < -halfN) {
				seg = 'D';
				x = -halfN;
				y = halfN - 1;
			}
		} else if (seg == 'D') {
			y--;
			if (y == -halfN) {
				n += 2;
				i = 0;
				x = -((n - 1) / 2);
				y = x;
				seg = 'A';
			}
		} else {
			throw new UnsupportedOperationException();
		}
		return result;
	}
}
