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
package net.sourceforge.plantuml.salt.element;

import java.util.List;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Grid2 {

	private final List<Double> rowsStart;
	private final List<Double> colsStart;
	private final TableStrategy strategy;

	public Grid2(List<Double> rowsStart, List<Double> colsStart, TableStrategy strategy) {
		this.rowsStart = rowsStart;
		this.colsStart = colsStart;
		this.strategy = strategy;
	}

	public void drawU(UGraphic ug) {
		final double xmin = colsStart.get(0);
		final double xmax = colsStart.get(colsStart.size() - 1);
		final double ymin = rowsStart.get(0);
		final double ymax = rowsStart.get(rowsStart.size() - 1);
		if (strategy == TableStrategy.DRAW_OUTSIDE || strategy == TableStrategy.DRAW_OUTSIDE_WITH_TITLE) {
			ug.apply(new UTranslate(xmin, ymin)).draw(new ULine(xmax - xmin, 0));
			ug.apply(new UTranslate(xmin, ymax)).draw(new ULine(xmax - xmin, 0));
			ug.apply(new UTranslate(xmin, ymin)).draw(new ULine(0, ymax - ymin));
			ug.apply(new UTranslate(xmax, ymin)).draw(new ULine(0, ymax - ymin));
		}
		if (drawHorizontal()) {
			for (Double y : rowsStart) {
				ug.apply(new UTranslate(xmin, y)).draw(new ULine(xmax - xmin, 0));
			}
		}
		if (drawVertical()) {
			for (Double x : colsStart) {
				ug.apply(new UTranslate(x, ymin)).draw(new ULine(0, ymax - ymin));
			}
		}
	}

	private boolean drawHorizontal() {
		if (strategy == TableStrategy.DRAW_HORIZONTAL || strategy == TableStrategy.DRAW_ALL) {
			return true;
		}
		return false;
	}

	private boolean drawVertical() {
		if (strategy == TableStrategy.DRAW_VERTICAL || strategy == TableStrategy.DRAW_ALL) {
			return true;
		}
		return false;
	}

}
