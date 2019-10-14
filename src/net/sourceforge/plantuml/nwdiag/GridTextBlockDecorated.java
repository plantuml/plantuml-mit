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
package net.sourceforge.plantuml.nwdiag;

import java.util.Collection;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GridTextBlockDecorated extends GridTextBlockSimple {

	public static final HtmlColorSetSimple colors = new HtmlColorSetSimple();

	public static final int NETWORK_THIN = 5;

	private final Collection<DiagGroup> groups;

	public GridTextBlockDecorated(int lines, int cols, Collection<DiagGroup> groups) {
		super(lines, cols);
		this.groups = groups;
	}

	@Override
	public void drawGrid(UGraphic ug) {
		for (DiagGroup group : groups) {
			drawGroups(ug, group);
		}
		drawNetworkTube(ug);
	}

	private void drawGroups(UGraphic ug, DiagGroup group) {
		final StringBounder stringBounder = ug.getStringBounder();

		MinMax size = null;
		double y = 0;
		for (int i = 0; i < data.length; i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data[i].length; j++) {
				final double colWidth = colWidth(stringBounder, j);
				final LinkedElement element = data[i][j];
				if (element != null && group.matches(element)) {
					final MinMax minMax = element.getMinMax(stringBounder, colWidth, lineHeight).translate(
							new UTranslate(x, y));
					size = size == null ? minMax : size.addMinMax(minMax);
				}
				x += colWidth;
			}
			y += lineHeight;
		}
		if (size != null) {
			HtmlColor color = group.getColor();
			if (color == null) {
				color = colors.getColorIfValid("#AAA");
			}
			size.draw(ug, color);
		}

	}

	private void drawNetworkTube(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		for (int i = 0; i < data.length; i++) {
			final Network network = getNetwork(i);
			double x = 0;
			double xmin = -1;
			double xmax = 0;
			for (int j = 0; j < data[i].length; j++) {
				final boolean hline = isPresent(i, j) || isPresent(i - 1, j);
				if (hline && xmin < 0) {
					xmin = x;
				}
				x += colWidth(stringBounder, j);
				if (hline) {
					xmax = x;
				}
			}
			final URectangle rect = new URectangle(xmax - xmin, NETWORK_THIN);
			rect.setDeltaShadow(1.0);
			UGraphic ug2 = ug.apply(new UTranslate(xmin, y));
			if (network != null && network.getColor() != null) {
				ug2 = ug2.apply(new UChangeBackColor(network.getColor()));
			}
			ug2.draw(rect);
			y += lineHeight(stringBounder, i);
		}
	}

	private Network getNetwork(int i) {
		for (int j = 0; j < data[i].length; j++) {
			if (isPresent(i, j)) {
				return data[i][j].getNetwork();
			}
		}
		return null;
	}
}
