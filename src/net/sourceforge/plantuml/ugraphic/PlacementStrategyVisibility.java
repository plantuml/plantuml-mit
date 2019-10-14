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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class PlacementStrategyVisibility extends AbstractPlacementStrategy {

	private final int col2;

	public PlacementStrategyVisibility(StringBounder stringBounder, int col2) {
		super(stringBounder);
		this.col2 = col2;
	}

	public Map<TextBlock, Point2D> getPositions(double width, double height) {
		final Map<TextBlock, Point2D> result = new LinkedHashMap<TextBlock, Point2D>();
		double y = 0;
		for (final Iterator<Map.Entry<TextBlock, Dimension2D>> it = getDimensions().entrySet().iterator(); it.hasNext();) {
			final Map.Entry<TextBlock, Dimension2D> ent1 = it.next();
			final Map.Entry<TextBlock, Dimension2D> ent2 = it.next();
			final double height1 = ent1.getValue().getHeight();
			final double height2 = ent2.getValue().getHeight();
			final double maxHeight12 = Math.max(height1, height2);
			// result.put(ent1.getKey(), new Point2D.Double(0, 2 + y + (maxHeight - height1) / 2));
			result.put(ent1.getKey(), new Point2D.Double(0, 2 + y));
			result.put(ent2.getKey(), new Point2D.Double(col2, y + (maxHeight12 - height2) / 2));
			y += maxHeight12;
		}
		return result;
	}

}
