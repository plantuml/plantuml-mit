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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileForkInner extends AbstractFtile {

	private final List<Ftile> forks = new ArrayList<Ftile>();

	public FtileForkInner(List<Ftile> forks) {
		super(forks.get(0).skinParam());
		for (Ftile ftile : forks) {
			this.forks.add(ftile);
		}
	}

	public Swimlane getSwimlaneIn() {
		return forks.get(0).getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	public Set<Swimlane> getSwimlanes() {
		return mergeSwimlanes(forks);
	}

	public static Set<Swimlane> mergeSwimlanes(List<Ftile> tiles) {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		for (Ftile tile : tiles) {
			result.addAll(tile.getSwimlanes());
		}
		return Collections.unmodifiableSet(result);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		double xpos = 0;
		for (Ftile ftile : forks) {
			ug.apply(new UTranslate(xpos, 0)).draw(ftile);
			final Dimension2D dim = ftile.calculateDimension(stringBounder);
			xpos += dim.getWidth();
		}
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		double height = 0;
		double width = 0;
		for (Ftile ftile : forks) {
			final Dimension2D dim = ftile.calculateDimension(stringBounder);
			width += dim.getWidth();
			if (dim.getHeight() > height) {
				height = dim.getHeight();
			}
		}
		final Dimension2D dimTotal = new Dimension2DDouble(width, height);
		return new FtileGeometry(dimTotal, dimTotal.getWidth() / 2, 0, dimTotal.getHeight());
	}

	public UTranslate getTranslateFor(Ftile searched, StringBounder stringBounder) {
		double xpos = 0;
		for (Ftile ftile : forks) {
			if (ftile == searched) {
				return new UTranslate(xpos, 0);
			}
			final Dimension2D dim = ftile.calculateDimension(stringBounder);
			xpos += dim.getWidth();
		}
		throw new IllegalArgumentException();
	}

}
