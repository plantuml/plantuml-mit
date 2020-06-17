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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class YPositionedTile {

	private final Tile tile;
	private final double y;

	public boolean inArea(double ymin, double ymax) {
		return y >= ymin && y < ymax;
	}

	public YPositionedTile(Tile tile, double y) {
		this.tile = tile;
		this.y = y;
		if (tile instanceof TileWithCallbackY) {
			((TileWithCallbackY) tile).callbackY(y);
		}
	}

	@Override
	public String toString() {
		return "y=" + y + " " + tile;
	}

	public void drawInArea(UGraphic ug) {
		// System.err.println("YPositionedTile::drawU y=" + y + " " + tile);
		ug.apply(UTranslate.dy(y)).draw(tile);
	}

	public boolean matchAnchorV2(String anchor) {
		final boolean result = tile.matchAnchorV1(anchor);
		return result;
	}

	public final double getY(StringBounder stringBounder) {
		final TileWithUpdateStairs communicationTile = (TileWithUpdateStairs) tile;
		return y + communicationTile.getYPoint(stringBounder);
	}

	public double getMiddleX(StringBounder stringBounder) {
		final double max = tile.getMaxX(stringBounder).getCurrentValue();
		final double min = tile.getMinX(stringBounder).getCurrentValue();
		return (min + max) / 2;
	}

}
