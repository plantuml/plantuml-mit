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
package net.sourceforge.plantuml.jungle;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GTileStack extends AbstractTextBlock implements GTile {

	private final List<GTile> tiles;
	private final double space;

	public GTileStack(List<GTile> tiles, double space) {
		this.tiles = tiles;
		this.space = space;
		if (tiles.size() == 0) {
			throw new IllegalArgumentException();
		}
	}

	public void drawU(UGraphic ug) {
		for (GTile tile : tiles) {
			tile.drawU(ug);
			final Dimension2D dim = tile.calculateDimension(ug.getStringBounder());
			ug = ug.apply(UTranslate.dy(dim.getHeight() + space));
		}
	}

	public GTileGeometry calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		double delta = 0;
		final List<Double> wests = new ArrayList<Double>();
		for (GTile tile : tiles) {
			final GTileGeometry dim = tile.calculateDimension(stringBounder);
			wests.add(delta + dim.getWestPositions().get(0));
			height += dim.getHeight();
			delta += dim.getHeight() + space;
			width = Math.max(width, dim.getWidth());
		}
		height += (tiles.size() - 1) * space;
		return new GTileGeometry(width, height, wests);
	}

}
