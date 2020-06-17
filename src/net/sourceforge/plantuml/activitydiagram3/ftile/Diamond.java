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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class Diamond {

	final static public double diamondHalfSize = 12;

	public static UPolygon asPolygon(double shadowing) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(diamondHalfSize, 0);
		diams.addPoint(diamondHalfSize * 2, diamondHalfSize);
		diams.addPoint(diamondHalfSize, diamondHalfSize * 2);
		diams.addPoint(0, diamondHalfSize);
		diams.addPoint(diamondHalfSize, 0);

		// if (shadowing) {
		// diams.setDeltaShadow(3);
		// }
		diams.setDeltaShadow(shadowing);

		return diams;
	}

	public static UPolygon asPolygon(double shadowing, double width, double height) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(diamondHalfSize, 0);
		diams.addPoint(width - diamondHalfSize, 0);
		diams.addPoint(width, height / 2);
		diams.addPoint(width - diamondHalfSize, height);
		diams.addPoint(diamondHalfSize, height);
		diams.addPoint(0, height / 2);
		diams.addPoint(diamondHalfSize, 0);

		// if (shadowing) {
		// diams.setDeltaShadow(3);
		// }
		diams.setDeltaShadow(shadowing);

		return diams;
	}

	public static Stencil asStencil(final TextBlock tb) {
		return new Stencil() {

			private final double getDeltaX(double height, double y) {
				final double p = y / height * 2;
				if (p <= 1) {
					return diamondHalfSize * p;
				}
				return diamondHalfSize * (2 - p);
			}

			public double getStartingX(StringBounder stringBounder, double y) {
				final Dimension2D dim = tb.calculateDimension(stringBounder);
				return -getDeltaX(dim.getHeight(), y);
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				final Dimension2D dim = tb.calculateDimension(stringBounder);
				return dim.getWidth() + getDeltaX(dim.getHeight(), y);
			}
		};
	}

	public static UPolygon asPolygonFoo1(boolean shadowing, double width, double height) {
		final UPolygon diams = new UPolygon();

		diams.addPoint(width / 2, 0);
		diams.addPoint(width, height / 2);
		diams.addPoint(width / 2, height);
		diams.addPoint(0, height / 2);

		if (shadowing) {
			diams.setDeltaShadow(3);
		}

		return diams;
	}

}
