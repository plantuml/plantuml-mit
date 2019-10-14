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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.salt.element.Skeleton2;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class AtomTree extends AbstractAtom implements Atom {

	private final HtmlColor lineColor;
	private final List<Atom> cells = new ArrayList<Atom>();
	private final Map<Atom, Integer> levels = new HashMap<Atom, Integer>();
	private final double margin = 2;
	
	public AtomTree(HtmlColor lineColor) {
		this.lineColor = lineColor;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Skeleton2 skeleton = new Skeleton2();
		double width = 0;
		double height = 0;
		for (Atom cell : cells) {
			final Dimension2D dim = cell.calculateDimension(stringBounder);
			height += dim.getHeight();
			final int level = getLevel(cell);
			width = Math.max(width, skeleton.getXEndForLevel(level) + margin + dim.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public void drawU(final UGraphic ugInit) {
		final Skeleton2 skeleton = new Skeleton2();
		double y = 0;
		UGraphic ug = ugInit;
		for (Atom cell : cells) {
			final int level = getLevel(cell);
			cell.drawU(ug.apply(new UTranslate(margin + skeleton.getXEndForLevel(level), 0)));
			final Dimension2D dim = cell.calculateDimension(ug.getStringBounder());
			skeleton.add(level, y + dim.getHeight() / 2);
			ug = ug.apply(new UTranslate(0, dim.getHeight()));
			y += dim.getHeight();
		}
		skeleton.draw(ugInit.apply(new UChangeColor(this.lineColor)));
	}

	private int getLevel(Atom atom) {
		return levels.get(atom);
	}

	public void addCell(Atom cell, int level) {
		this.cells.add(cell);
		this.levels.put(cell, level);
	}
	
}
