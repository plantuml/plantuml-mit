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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileIfNude extends FtileDimensionMemoize {

	protected final Ftile tile1;
	protected final Ftile tile2;
	private final Swimlane in;

	FtileIfNude(Ftile tile1, Ftile tile2, Swimlane in) {
		super(tile1.skinParam());
		this.tile1 = tile1;
		this.tile2 = tile2;
		this.in = in;
	}
	
	@Override
	public Collection<Ftile> getMyChildren() {
		return Arrays.asList(tile1, tile2);
	}

	public boolean hasTwoBranches(StringBounder stringBounder) {
		return tile1.calculateDimension(stringBounder).hasPointOut()
				&& tile2.calculateDimension(stringBounder).hasPointOut();
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (getSwimlaneIn() != null) {
			result.add(getSwimlaneIn());
		}
		result.addAll(tile1.getSwimlanes());
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return in;
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	protected UTranslate getTranslate1(StringBounder stringBounder) {
//		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
//		final Dimension2D dim1 = tile1.calculateDimension(stringBounder);

		final double x1 = 0;
		final double y1 = 0;
		return new UTranslate(x1, y1);
	}

	protected UTranslate getTranslate2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dim2 = tile2.calculateDimension(stringBounder);

		final double x2 = dimTotal.getWidth() - dim2.getWidth();
		final double y2 = 0;
		return new UTranslate(x2, y2);

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile1) {
			return getTranslate1(stringBounder);
		}
		if (child == tile2) {
			return getTranslate2(stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug.apply(getTranslate1(stringBounder)).draw(tile1);
		ug.apply(getTranslate2(stringBounder)).draw(tile2);
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		if (tile1.calculateDimension(stringBounder).hasPointOut()
				|| tile2.calculateDimension(stringBounder).hasPointOut()) {
			return dimTotal;
		}
		return dimTotal.withoutPointOut();
	}

	@Override
	protected FtileGeometry calculateDimensionInternalSlow(StringBounder stringBounder) {
		final FtileGeometry dim1 = tile1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = tile2.calculateDimension(stringBounder);

		final double innerMargin = widthInner(stringBounder);
		final double width = dim1.getLeft() + innerMargin + (dim2.getWidth() - dim2.getLeft());

		final Dimension2D dim12 = Dimension2DDouble.mergeLR(dim1, dim2);

		return new FtileGeometry(width, dim12.getHeight(), dim1.getLeft() + innerMargin / 2, 0);
	}

	protected double widthInner(StringBounder stringBounder) {
		final FtileGeometry dim1 = tile1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = tile2.calculateDimension(stringBounder);
		return (dim1.getWidth() - dim1.getLeft()) + dim2.getLeft();
	}

}
