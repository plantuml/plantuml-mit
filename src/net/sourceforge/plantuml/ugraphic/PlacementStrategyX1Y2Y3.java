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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class PlacementStrategyX1Y2Y3 extends AbstractPlacementStrategy {

	public PlacementStrategyX1Y2Y3(StringBounder stringBounder) {
		super(stringBounder);
	}

	public Map<TextBlock, Point2D> getPositions(double width, double height) {
		final Dimension2D first = getDimensions().values().iterator().next();

		final double maxWidthButFirst = getMaxWidth(butFirst());
		final double sumHeightButFirst = getSumHeight(butFirst());

		final double space = (width - first.getWidth() - maxWidthButFirst) / 3;

		final Map<TextBlock, Point2D> result = new LinkedHashMap<TextBlock, Point2D>();
		// double x = space * 2;

		final Iterator<Map.Entry<TextBlock, Dimension2D>> it = getDimensions().entrySet().iterator();
		final Map.Entry<TextBlock, Dimension2D> ent = it.next();
		double y = (height - ent.getValue().getHeight()) / 2;
		result.put(ent.getKey(), new Point2D.Double(space, y));

		// x += ent.getValue().getWidth() + space;

		y = (height - sumHeightButFirst) / 2;
		while (it.hasNext()) {
			final Map.Entry<TextBlock, Dimension2D> ent2 = it.next();
			final TextBlock textBlock = ent2.getKey();
			final Dimension2D dim = getDimensions().get(textBlock);
			final double x = 2 * space + first.getWidth() + (maxWidthButFirst - dim.getWidth()) / 2;
			result.put(textBlock, new Point2D.Double(x, y));
			y += ent2.getValue().getHeight();
		}
		return result;
	}

	private Iterator<Dimension2D> butFirst() {
		final Iterator<Dimension2D> iterator = getDimensions().values().iterator();
		iterator.next();
		return iterator;
	}

}
