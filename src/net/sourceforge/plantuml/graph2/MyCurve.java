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
package net.sourceforge.plantuml.graph2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MyCurve {

	private final CubicCurve2D.Double curve;
	private final List<Line2D.Double> lines = new ArrayList<Line2D.Double>();
	private final List<Line2D.Double> linesForInters = new ArrayList<Line2D.Double>();
	private Color color = Color.GREEN;
	private double lenght = 0;

	public MyCurve(CubicCurve2D.Double curve) {
		this.curve = curve;
		addCurve(curve);
		if (lenght <= 0) {
			throw new IllegalStateException();
		}
		for (Line2D.Double line : lines) {
			linesForInters.add(change(line, curve.getP1(), curve.getP2()));
		}
	}

	private Line2D.Double change(Line2D.Double line, Point2D p1, Point2D p2) {
		if (line.getP1().equals(p1) == false && line.getP2().equals(p2) == false) {
			return line;
		}
		final double dx = line.x2 - line.x1;
		final double dy = line.y2 - line.y1;
		if (line.getP1().equals(p1)) {
			p1 = new Point2D.Double(line.x1 + dx / 10, line.y1 + dy / 10);
		} else {
			p1 = line.getP1();
		}
		if (line.getP2().equals(p2)) {
			p2 = new Point2D.Double(line.x2 - dx / 10, line.y2 - dy / 10);
		} else {
			p2 = line.getP2();
		}
		return new Line2D.Double(p1, p2);
	}

	public final double getLenght() {
		return lenght;
	}

	private void addCurve(CubicCurve2D.Double peace) {
		final Rectangle2D bounds = peace.getBounds2D();
		final double flat = peace.getFlatness();
		if (flat < 10) {
			lines.add(new Line2D.Double(peace.getP1(), peace.getP2()));
			lenght += Math.sqrt(bounds.getWidth() * bounds.getWidth() + bounds.getHeight() * bounds.getHeight());
			return;
		}
		final CubicCurve2D.Double left = new CubicCurve2D.Double();
		final CubicCurve2D.Double right = new CubicCurve2D.Double();
		peace.subdivide(left, right);
		addCurve(left);
		addCurve(right);
	}

	public void drawDebug(Graphics2D g2d) {
		for (Line2D r : linesForInters) {
			g2d.setColor(color);
			g2d.draw(r);
		}
		g2d.setColor(Color.BLACK);
		// g2d.draw(curve);
	}

	public void draw(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke((float) 1.5));
		g2d.draw(curve);
		g2d.setStroke(new BasicStroke());
	}

	public final void setColor(Color color) {
		this.color = color;
	}

	public boolean intersects(List<MyCurve> others) {
		for (MyCurve other : others) {
			if (this.intersects(other)) {
				return true;
			}
		}
		return false;
	}

	private boolean intersects(MyCurve other) {
		for (Line2D.Double l1 : this.linesForInters) {
			for (Line2D.Double l2 : other.linesForInters) {
				if (l1.intersectsLine(l2)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean intersects(RectanglesCollection forbidden) {
		for (Rectangle2D.Double r : forbidden) {
			for (Line2D.Double line : lines) {
				if (r.intersectsLine(line)) {
					return true;
				}
			}
		}
		return false;
	}
	// public static long TPS6;
	//
	// public RectanglesCollection unrecoveredBy(RectanglesCollection allFrames)
	// {
	// final long start = System.currentTimeMillis();
	// try {
	// final RectanglesCollection result = new RectanglesCollection();
	// for (Rectangle2D.Double r : areas) {
	// if (allFrames.intersect(new RectanglesCollection(r)) == false) {
	// result.add(r);
	// }
	// }
	// return result;
	// } finally {
	// TPS6 += System.currentTimeMillis() - start;
	// }
	// }
	//

}
