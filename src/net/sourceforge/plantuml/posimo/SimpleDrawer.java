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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

public class SimpleDrawer {

	private final Cluster root;
	private final Collection<Path> paths;

	public SimpleDrawer(Cluster root, Collection<Path> paths) {
		this.root = root;
		this.paths = paths;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		for (Clusterable cl : root.getContents()) {
			final Block b = (Block) cl;
			final Point2D pos = b.getPosition();
			final Dimension2D dim = b.getSize();
			// drawRectCentered(g2d, pos, dim);
			drawRect(g2d, pos, dim);
		}

		g2d.setColor(Color.GREEN);
		for (Path p : paths) {
			final Label label = p.getLabel();
			final Point2D labelPos = label.getPosition();
			final Dimension2D labelDim = label.getSize();
			// final double x1 = labelPos.getX();
			// final double y1 = labelPos.getY();
			// g2d.draw(new Ellipse2D.Double(x1 - 1, y1 - 1, 3, 3));
			// drawRectCentered(g2d, labelPos, labelDim);
			drawRect(g2d, labelPos, labelDim);
		}

		g2d.setColor(Color.RED);
		for (Path p : paths) {
			p.getDotPath().draw(g2d, 0, 0);
		}

		for (Cluster sub : root.getSubClusters()) {
			new SimpleDrawer(sub, new ArrayList<Path>()).draw(g2d);
		}

	}

	private void drawRectCentered(Graphics2D g2d, final Point2D pos, final Dimension2D dim) {
		final Rectangle2D rect = new Rectangle2D.Double(pos.getX() - dim.getWidth() / 2, pos.getY() - dim.getHeight()
				/ 2, dim.getWidth(), dim.getHeight());
		g2d.draw(rect);
	}

	private void drawRect(Graphics2D g2d, final Point2D pos, final Dimension2D dim) {
		final Rectangle2D rect = new Rectangle2D.Double(pos.getX(), pos.getY(), dim.getWidth(), dim.getHeight());
		g2d.draw(rect);
	}
}
