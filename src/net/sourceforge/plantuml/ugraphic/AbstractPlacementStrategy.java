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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public abstract class AbstractPlacementStrategy implements PlacementStrategy {

	private final StringBounder stringBounder;
	private final Map<TextBlock, Dimension2D> dimensions = new LinkedHashMap<TextBlock, Dimension2D>();

	public AbstractPlacementStrategy(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public void add(TextBlock block) {
		this.dimensions.put(block, block.calculateDimension(stringBounder));
	}

	protected Map<TextBlock, Dimension2D> getDimensions() {
		return dimensions;
	}

	protected double getSumWidth() {
		return getSumWidth(dimensions.values().iterator());
	}

	protected double getSumHeight() {
		return getSumHeight(dimensions.values().iterator());
	}

	protected double getMaxHeight() {
		return getMaxHeight(dimensions.values().iterator());
	}

	protected double getMaxWidth() {
		return getMaxWidth(dimensions.values().iterator());
	}

	protected double getSumWidth(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result += it.next().getWidth();
		}
		return result;
	}

	protected double getSumHeight(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result += it.next().getHeight();
		}
		return result;
	}

	protected double getMaxWidth(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result = Math.max(result, it.next().getWidth());
		}
		return result;
	}

	protected double getMaxHeight(Iterator<Dimension2D> it) {
		double result = 0;
		while (it.hasNext()) {
			result = Math.max(result, it.next().getHeight());
		}
		return result;
	}

	protected final StringBounder getStringBounder() {
		return stringBounder;
	}

}
