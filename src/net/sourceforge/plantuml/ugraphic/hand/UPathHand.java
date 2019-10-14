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
package net.sourceforge.plantuml.ugraphic.hand;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Random;

import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;

public class UPathHand {

	private final UPath path;
	private final double defaultVariation = 4.0;

	public UPathHand(UPath source, Random rnd) {

		final UPath result = new UPath();

		Point2D last = new Point2D.Double();

		for (USegment segment : source) {
			final USegmentType type = segment.getSegmentType();
			if (type == USegmentType.SEG_MOVETO) {
				final double x = segment.getCoord()[0];
				final double y = segment.getCoord()[1];
				result.moveTo(x, y);
				last = new Point2D.Double(x, y);
			} else if (type == USegmentType.SEG_CUBICTO) {
				final double x2 = segment.getCoord()[4];
				final double y2 = segment.getCoord()[5];
				final HandJiggle jiggle = new HandJiggle(last, 2.0, rnd);

				final CubicCurve2D tmp = new CubicCurve2D.Double(last.getX(), last.getY(), segment.getCoord()[0],
						segment.getCoord()[1], segment.getCoord()[2], segment.getCoord()[3], x2, y2);
				jiggle.curveTo(tmp);
				jiggle.appendTo(result);
				last = new Point2D.Double(x2, y2);
			} else if (type == USegmentType.SEG_LINETO) {
				final double x = segment.getCoord()[0];
				final double y = segment.getCoord()[1];
				final HandJiggle jiggle = new HandJiggle(last.getX(), last.getY(), defaultVariation, rnd);
				jiggle.lineTo(x, y);
				for (USegment seg2 : jiggle.toUPath()) {
					if (seg2.getSegmentType() == USegmentType.SEG_LINETO) {
						result.lineTo(seg2.getCoord()[0], seg2.getCoord()[1]);
					}
				}
				last = new Point2D.Double(x, y);
			} else {
				this.path = source;
				return;
			}
		}
		this.path = result;
		this.path.setDeltaShadow(source.getDeltaShadow());
	}

	public UPath getHanddrawn() {
		return this.path;
	}

}
