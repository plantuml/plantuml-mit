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

import java.awt.Color;

public class ShadowManager {
	
	// http://www.w3schools.com/svg/svg_feoffset.asp

	private final int c1;
	private final int c2;

	public ShadowManager(int c1, int c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	public double[] getShadowDeltaPoints(double deltaShadow, double diff, double[] points) {
		assert points.length % 2 == 0;
		double cx = 0;
		double cy = 0;
		for (int i = 0; i < points.length; i += 2) {
			cx += points[i];
			cy += points[i + 1];
		}
		final int nbPoints = points.length / 2;

		cx = cx / nbPoints;
		cy = cy / nbPoints;

		final double[] result = new double[points.length];
		for (int i = 0; i < result.length; i += 2) {
			final double diffx = points[i] > cx ? -diff : diff;
			final double diffy = points[i + 1] > cy ? -diff : diff;
			result[i] = points[i] + diffx + deltaShadow;
			result[i + 1] = points[i + 1] + diffy + deltaShadow;
		}
		return result;
	}

	public Color getColor(double delta, double total) {
		final int c = (int) (c2 + 1.0 * delta / total * (c1 - c2));
		return new Color(c, c, c);
	}

}
