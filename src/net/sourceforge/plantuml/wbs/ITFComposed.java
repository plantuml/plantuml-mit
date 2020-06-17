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
package net.sourceforge.plantuml.wbs;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ITFComposed extends WBSTextBlock implements ITF {

	private final List<ITF> left;
	private final List<ITF> right;

	private final TextBlock main;

	final private double delta1x = 10;
	final private double marginBottom;// = 15;

	private ITFComposed(ISkinParam skinParam, WElement idea, List<ITF> left, List<ITF> right) {
		super(skinParam, idea.getStyleBuilder(), idea.getLevel());
		this.left = left;
		this.right = right;
		this.main = buildMain(idea);
		final Style style = idea.getStyle();
		this.marginBottom = style.getMargin().getBottom();
	}

	public static ITF build2(ISkinParam skinParam, WElement idea) {
		if (idea.isLeaf()) {
			return new ITFLeaf(idea.getStyle(), skinParam, idea.getLabel(), idea.getShape());
		}
		final List<ITF> left = new ArrayList<ITF>();
		final List<ITF> right = new ArrayList<ITF>();
		for (WElement child : idea.getChildren(Direction.LEFT)) {
			left.add(build2(skinParam, child));
		}
		for (WElement child : idea.getChildren(Direction.RIGHT)) {
			right.add(build2(skinParam, child));
		}
		return new ITFComposed(skinParam, idea, left, right);
	}

	final protected double getw1(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double mainWidth = mainDim.getWidth();
		return Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, left));
	}

	final public Point2D getT1(StringBounder stringBounder) {
		final double x = getw1(stringBounder);
		final double y = 0;
		return new Point2D.Double(x, y);
	}

	final public Point2D getT2(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder);
		final double y = mainDim.getHeight();
		return new Point2D.Double(x, y);
	}

	final public Point2D getF1(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder) - mainDim.getWidth() / 2;
		final double y = mainDim.getHeight() / 2;
		return new Point2D.Double(x, y);
	}

	final public Point2D getF2(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder) + mainDim.getWidth() / 2;
		final double y = mainDim.getHeight() / 2;
		return new Point2D.Double(x, y);
	}

	public final Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double mainWidth = mainDim.getWidth();
		final double height = mainDim.getHeight()
				+ Math.max(getCollHeight(stringBounder, left, marginBottom), getCollHeight(stringBounder, right, marginBottom));
		final double width = Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, left))
				+ Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, right));
		return new Dimension2DDouble(width, height);
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double wx = getw1(stringBounder) - mainDim.getWidth() / 2;
		main.drawU(ug.apply(UTranslate.dx(wx)));
		final double x = getw1(stringBounder);
		double y = mainDim.getHeight();
		double lastY1 = y;
		for (ITF child : left) {
			y += marginBottom;
			final Dimension2D childDim = child.calculateDimension(stringBounder);
			lastY1 = y + child.getF2(stringBounder).getY();
			drawLine(ug, x - childDim.getWidth() - delta1x + child.getF2(stringBounder).getX(), lastY1, x, lastY1);
			child.drawU(ug.apply(new UTranslate(x - childDim.getWidth() - delta1x, y)));
			y += childDim.getHeight();
		}

		y = mainDim.getHeight();
		double lastY2 = y;
		for (ITF child : right) {
			y += marginBottom;
			final Dimension2D childDim = child.calculateDimension(stringBounder);
			lastY2 = y + child.getF1(stringBounder).getY();
			drawLine(ug, x, lastY2, x + delta1x + child.getF1(stringBounder).getX(), lastY2);
			child.drawU(ug.apply(new UTranslate(x + delta1x, y)));
			y += childDim.getHeight();

		}
		drawLine(ug, x, mainDim.getHeight(), x, Math.max(lastY1, lastY2));
	}

	final private double getCollWidth(StringBounder stringBounder, Collection<? extends TextBlock> all) {
		double result = 0;
		for (TextBlock child : all) {
			result = Math.max(result, child.calculateDimension(stringBounder).getWidth());
		}
		return result;
	}

	final private double getCollHeight(StringBounder stringBounder, Collection<? extends TextBlock> all, double deltay) {
		double result = 0;
		for (TextBlock child : all) {
			result += deltay + child.calculateDimension(stringBounder).getHeight();
		}
		return result;
	}

}
