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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class ULayoutGroup {

	private final PlacementStrategy placementStrategy;

	public ULayoutGroup(PlacementStrategy placementStrategy) {
		this.placementStrategy = placementStrategy;
	}

	public void drawU(UGraphic ug, double width, double height) {
		for (Map.Entry<TextBlock, Point2D> ent : placementStrategy.getPositions(width, height).entrySet()) {
			final TextBlock block = ent.getKey();
			final Point2D pos = ent.getValue();
			block.drawU(ug.apply(new UTranslate(pos)));
		}
	}

	public void add(TextBlock block) {
		placementStrategy.add(block);

	}

	public Rectangle2D getInnerPosition(String member, double width, double height, StringBounder stringBounder) {
		final Set<Entry<TextBlock, Point2D>> all = placementStrategy.getPositions(width, height).entrySet();
		Rectangle2D result = tryOne(all, member, stringBounder, InnerStrategy.STRICT);
		if (result == null) {
			result = tryOne(all, member, stringBounder, InnerStrategy.LAZZY);
		}
		return result;
	}

	private Rectangle2D tryOne(final Set<Entry<TextBlock, Point2D>> all, String member, StringBounder stringBounder,
			InnerStrategy mode) {
		for (Map.Entry<TextBlock, Point2D> ent : all) {
			final TextBlock block = ent.getKey();
			final Rectangle2D result = block.getInnerPosition(member, stringBounder, mode);
			if (result != null) {
				final UTranslate translate = new UTranslate(ent.getValue());
				return translate.apply(result);
			}
		}
		return null;
	}

}
