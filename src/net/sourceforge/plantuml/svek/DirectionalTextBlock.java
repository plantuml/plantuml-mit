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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class DirectionalTextBlock extends AbstractTextBlock implements TextBlock {
	private final TextBlock right;
	private final TextBlock left;
	private final TextBlock up;
	private final TextBlock down;
	private final GuideLine guideline;

	public DirectionalTextBlock(GuideLine guideline, TextBlock right, TextBlock left, TextBlock up, TextBlock down) {
		this.right = right;
		this.left = left;
		this.up = up;
		this.down = down;
		this.guideline = guideline;
	}

	public void drawU(UGraphic ug) {
		Direction dir = guideline.getArrowDirection();
		switch (dir) {
		case RIGHT:
			right.drawU(ug);
			break;
		case LEFT:
			left.drawU(ug);
			break;
		case UP:
			up.drawU(ug);
			break;
		case DOWN:
			down.drawU(ug);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return right.calculateDimension(stringBounder);
	}

}
