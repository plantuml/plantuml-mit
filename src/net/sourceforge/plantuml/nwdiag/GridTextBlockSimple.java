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
package net.sourceforge.plantuml.nwdiag;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GridTextBlockSimple implements TextBlock {

	protected final LinkedElement data[][];

	public GridTextBlockSimple(int lines, int cols) {
		this.data = new LinkedElement[lines][cols];
	}

	protected boolean isPresent(int i, int j) {
		if (i == -1) {
			return false;
		}
		return data[i][j] != null;
	}

	public void drawGrid(UGraphic ug) {
	}

	public void drawU(UGraphic ug) {
		drawGrid(ug);
		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		for (int i = 0; i < data.length; i++) {
			final double lineHeight = lineHeight(stringBounder, i);
			double x = 0;
			for (int j = 0; j < data[i].length; j++) {
				final double colWidth = colWidth(stringBounder, j);
				if (data[i][j] != null) {
					data[i][j].drawMe(ug.apply(new UTranslate(x, y)), colWidth, lineHeight);
				}
				x += colWidth;
			}
			y += lineHeight;
		}
	}

	protected double colWidth(StringBounder stringBounder, final int j) {
		double width = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i][j] != null) {
				width = Math.max(width, data[i][j].naturalDimension(stringBounder).getWidth());
			}
		}
		return width;
	}

	public double lineHeight(StringBounder stringBounder, final int i) {
		double height = 0;
		for (int j = 0; j < data[i].length; j++) {
			if (data[i][j] != null) {
				height = Math.max(height, data[i][j].naturalDimension(stringBounder).getHeight());
			}
		}
		return height;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (data.length == 0) {
			return new Dimension2DDouble(0, 0);
		}
		double height = 0;
		for (int i = 0; i < data.length; i++) {
			height += lineHeight(stringBounder, i);
		}
		double width = 0;
		for (int j = 0; j < data[0].length; j++) {
			width += colWidth(stringBounder, j);
		}
		return new Dimension2DDouble(width, height);
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		throw new UnsupportedOperationException("member=" + member + " " + getClass().toString());
	}

	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void add(int i, int j, LinkedElement value) {
		data[i][j] = value;
	}

}
