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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TileParallel implements Tile, TileWithUpdateStairs, TileWithCallbackY {

	private final List<Tile> tiles = new ArrayList<Tile>();

	public void callbackY(double y) {
		for (Tile tile : tiles) {
			if (tile instanceof TileWithCallbackY) {
				((TileWithCallbackY) tile).callbackY(y);
			}
		}
	}

	public void updateStairs(StringBounder stringBounder, double y) {
		for (Tile tile : tiles) {
			if (tile instanceof TileWithUpdateStairs) {
				((TileWithUpdateStairs) tile).updateStairs(stringBounder, y);
			}
		}
	}

	public void add(Tile tile) {
		this.tiles.add(tile);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		// final double totalHeight = getPreferredHeight(stringBounder);
		final double yPointAll = getYPoint(stringBounder);
		for (Tile tile : tiles) {
			final double yPoint = tile.getYPoint(stringBounder);
			// tile.drawU(ug.apply(new UTranslate(0, totalHeight - tile.getPreferredHeight(stringBounder))));
			tile.drawU(ug.apply(new UTranslate(0, yPointAll - yPoint)));
		}
	}

	public double getYPoint(StringBounder stringBounder) {
		double result = 0;
		for (Tile tile : tiles) {
			result = Math.max(result, tile.getYPoint(stringBounder));
		}
		return result;
	}

	public double getZ(StringBounder stringBounder) {
		double result = 0;
		for (Tile tile : tiles) {
			result = Math.max(result, tile.getZ(stringBounder));
		}
		return result;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return getYPoint(stringBounder) + getZ(stringBounder);
	}

	public void addConstraints(StringBounder stringBounder) {
		for (Tile tile : tiles) {
			tile.addConstraints(stringBounder);
		}
	}

	public Real getMinX(final StringBounder stringBounder) {
		return RealUtils.min(new AbstractCollection<Real>() {
			public Iterator<Real> iterator() {
				return new Iterator<Real>() {
					private final Iterator<Tile> source = tiles.iterator();

					public boolean hasNext() {
						return source.hasNext();
					}

					public Real next() {
						return source.next().getMinX(stringBounder);
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			public int size() {
				return tiles.size();
			}
		});
	}

	public Real getMaxX(final StringBounder stringBounder) {
		return RealUtils.max(new AbstractCollection<Real>() {
			public Iterator<Real> iterator() {
				return new Iterator<Real>() {
					private final Iterator<Tile> source = tiles.iterator();

					public boolean hasNext() {
						return source.hasNext();
					}

					public Real next() {
						return source.next().getMaxX(stringBounder);
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			public int size() {
				return tiles.size();
			}
		});
	}

	public Event getEvent() {
		return null;
	}

	public boolean matchAnchor(String anchor) {
		for (Tile tile : tiles) {
			if (tile.matchAnchor(anchor)) {
				return true;
			}
		}
		return false;
	}

}
