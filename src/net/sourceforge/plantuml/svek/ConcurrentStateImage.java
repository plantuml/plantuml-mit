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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public final class ConcurrentStateImage extends AbstractTextBlock implements IEntityImage {

	private final List<IEntityImage> inners = new ArrayList<IEntityImage>();
	private final Separator separator;
	private final ISkinParam skinParam;
	private final HtmlColor backColor;

	static enum Separator {
		VERTICAL, HORIZONTAL;

		static Separator fromChar(char sep) {
			if (sep == '|') {
				return VERTICAL;
			}
			if (sep == '-') {
				return HORIZONTAL;
			}
			throw new IllegalArgumentException();
		}

		UTranslate move(Dimension2D dim) {
			if (this == VERTICAL) {
				return new UTranslate(dim.getWidth(), 0);
			}
			return new UTranslate(0, dim.getHeight());
		}

		Dimension2D add(Dimension2D orig, Dimension2D other) {
			if (this == VERTICAL) {
				return new Dimension2DDouble(orig.getWidth() + other.getWidth(), Math.max(orig.getHeight(),
						other.getHeight()));
			}
			return new Dimension2DDouble(Math.max(orig.getWidth(), other.getWidth()), orig.getHeight()
					+ other.getHeight());
		}

		void drawSeparator(UGraphic ug, Dimension2D dimTotal) {
			final double THICKNESS_BORDER = 1.5;
			final int DASH = 8;
			ug = ug.apply(new UStroke(DASH, 10, THICKNESS_BORDER));
			if (this == VERTICAL) {
				ug.draw(new ULine(0, dimTotal.getHeight() + DASH));
			} else {
				ug.draw(new ULine(dimTotal.getWidth() + DASH, 0));
			}

		}
	}

	private HtmlColor getColor(ColorParam colorParam) {
		return new Rose().getHtmlColor(skinParam, colorParam);
	}

	public ConcurrentStateImage(Collection<IEntityImage> images, char concurrentSeparator, ISkinParam skinParam,
			HtmlColor backColor) {
		this.separator = Separator.fromChar(concurrentSeparator);
		this.skinParam = skinParam;
		this.backColor = skinParam.getBackgroundColor();
		this.inners.addAll(images);
	}

	public void drawU(UGraphic ug) {
		System.err.println("drawing " + inners.size());
		final HtmlColor dotColor = getColor(ColorParam.stateBorder);
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		for (int i = 0; i < inners.size(); i++) {
			final IEntityImage inner = inners.get(i);
			inner.drawU(ug);
			final Dimension2D dim = inner.calculateDimension(stringBounder);
			ug = ug.apply(separator.move(dim));
			if (i < inners.size() - 1) {
				separator.drawSeparator(ug.apply(new UChangeColor(dotColor)), dimTotal);
			}
		}

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (IEntityImage inner : inners) {
			final Dimension2D dim = inner.calculateDimension(stringBounder);
			result = separator.add(result, dim);
		}
		return result;
	}

	public HtmlColor getBackcolor() {
		return backColor;
	}

	public boolean isHidden() {
		return false;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}
	
	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}
