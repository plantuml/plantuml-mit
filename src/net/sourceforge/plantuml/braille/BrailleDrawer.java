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
package net.sourceforge.plantuml.braille;

import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class BrailleDrawer implements UDrawable {

	private final BrailleGrid grid;
	private final double step = 9;
	private final double spotSize = 5;

	public BrailleDrawer(BrailleGrid grid) {
		this.grid = grid;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UChangeColor(new HtmlColorSetSimple().getColorIfValid("#F0F0F0")));
		for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
			ug.apply(new UTranslate(x * step + spotSize + 1, 0)).draw(
					new ULine(0, (grid.getMaxY() - grid.getMinY()) * step));
		}
		for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
			ug.apply(new UTranslate(0, y * step + spotSize + 1)).draw(
					new ULine((grid.getMaxX() - grid.getMinX()) * step, 0));
		}
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UChangeBackColor(HtmlColorUtils.BLACK));
		for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
			for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
				if (grid.getState(x, y)) {
					drawCircle(ug, x, y);
				}
			}
		}
	}

	private void drawCircle(UGraphic ug, int x, int y) {
		final double cx = x * step;
		final double cy = y * step;
		ug.apply(new UTranslate(cx, cy)).draw(new UEllipse(spotSize, spotSize));
	}

}
