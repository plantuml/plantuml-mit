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
package net.sourceforge.plantuml.bpm;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ConnectorPuzzleEmpty extends AbstractConnectorPuzzle implements Placeable, TextBlock, ConnectorPuzzle {

	public static ConnectorPuzzleEmpty get(String value) {
		final ConnectorPuzzleEmpty result = new ConnectorPuzzleEmpty();
		for (Where w : Where.values()) {
			if (value.contains(w.toShortString())) {
				result.append(w);
			}
		}
		return result;
	}

	public boolean checkDirections(String directions) {
		return connections().equals(get(directions).connections());
	}

	@Override
	public String toString() {
		if (connections().size() == 0) {
			return "NONE";

		}
		return connections().toString();
	}

	public Dimension2D getDimension(StringBounder stringBounder, ISkinParam skinParam) {
		return new Dimension2DDouble(20, 20);
	}

	public TextBlock toTextBlock(ISkinParam skinParam) {
		return this;
	}

	public String getId() {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		// System.err.println("DRAWING " + toString());
		ug = ug.apply(HColorUtils.BLUE);
		for (Where w : Where.values()) {
			if (have(w)) {
				drawLine(ug, w);
			}
		}

	}

	private void drawLine(UGraphic ug, Where w) {
		if (w == Where.WEST) {
			ug.apply(UTranslate.dy(10)).draw(ULine.hline(10));
		}
		if (w == Where.EAST) {
			ug.apply(new UTranslate(10, 10)).draw(ULine.hline(10));
		}
		if (w == Where.NORTH) {
			ug.apply(UTranslate.dx(10)).draw(ULine.vline(10));
		}
		if (w == Where.SOUTH) {
			ug.apply(new UTranslate(10, 10)).draw(ULine.vline(10));
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(20, 20);
	}
	
	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}


	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

}
