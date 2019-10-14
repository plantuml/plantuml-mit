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
package net.sourceforge.plantuml.postit;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Area implements Elastic {

	private final String title;
	private final char id;

	private Dimension2D minimumDimension;

	private final List<PostIt> postIts = new ArrayList<PostIt>();

	public Area(char id, String title) {
		this.id = id;
		this.title = title;
	}

	public char getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Dimension2D getMinimumDimension() {
		return minimumDimension;
	}

	public void setMinimunDimension(Dimension2D minimumDimension) {
		this.minimumDimension = minimumDimension;
	}

	public Dimension2D getDimension() {
		throw new UnsupportedOperationException();
	}

	public double heightWhenWidthIs(double width, StringBounder stringBounder) {
		final AreaLayoutFixedWidth layout = new AreaLayoutFixedWidth(width);
		final Map<PostIt, Point2D> pos = layout.getPositions(postIts, stringBounder);
		double max = 10;
		for (Map.Entry<PostIt, Point2D> ent : pos.entrySet()) {
			final double y = ent.getKey().getDimension(stringBounder).getHeight() + ent.getValue().getY();
			max = Math.max(max, y);
		}

		return max + 10;
	}

	public double widthWhenHeightIs(double height, StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public void add(PostIt postIt) {
		postIts.add(postIt);
	}

	public void drawU(UGraphic ug, double width) {
		final AreaLayout layout = new AreaLayoutFixedWidth(width);
		final Map<PostIt, Point2D> pos = layout.getPositions(postIts, ug.getStringBounder());
		for (Map.Entry<PostIt, Point2D> ent : pos.entrySet()) {
			final UGraphic ugTranslated = ug.apply(new UTranslate(ent.getValue().getX(), ent.getValue().getY()));
			ent.getKey().drawU(ugTranslated);
		}

	}

}
