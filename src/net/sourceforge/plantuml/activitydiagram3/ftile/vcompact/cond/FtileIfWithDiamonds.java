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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileIfWithDiamonds extends FtileIfNude {

	private static final double SUPP_WIDTH = 20;
	protected final Ftile diamond1;
	protected final Ftile diamond2;

	public FtileIfWithDiamonds(Ftile diamond1, Ftile tile1, Ftile tile2, Ftile diamond2, Swimlane in,
			StringBounder stringBounder) {
		super(tile1, tile2, in);
		this.diamond1 = diamond1;
		this.diamond2 = diamond2;
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Arrays.asList(diamond1, diamond2, tile1, tile2);
	}

	public int getYdelta1a(StringBounder stringBounder) {
		// if (getSwimlanes().size() > 1 && hasTwoBranches(stringBounder)) {
		if (getSwimlanes().size() > 1) {
			return 20;
		}
		return 10;
		// return hasTwoBranches(stringBounder) ? 6 : 6;
	}

	public int getYdelta1b(StringBounder stringBounder) {
		// if (getSwimlanes().size() > 1 && hasTwoBranches(stringBounder)) {
		if (getSwimlanes().size() > 1) {
			return 10;
		}
		return hasTwoBranches(stringBounder) ? 6 : 0;
	}

	@Override
	protected double widthInner(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		return Math.max(super.widthInner(stringBounder), dim1.getWidth() + SUPP_WIDTH);
	}

	@Override
	protected FtileGeometry calculateDimensionInternalSlow(StringBounder stringBounder) {
		final FtileGeometry dim1 = diamond1.calculateDimension(stringBounder);
		final FtileGeometry dim2 = diamond2.calculateDimension(stringBounder);

		final FtileGeometry dimNude = super.calculateDimensionInternalSlow(stringBounder);

		final FtileGeometry all = dim1.appendBottom(dimNude).appendBottom(dim2);

		return all.addDim(0, getYdelta1a(stringBounder) + getYdelta1b(stringBounder));

		// final double height = dimNude.getHeight() + dim1.getHeight() + dim2.getHeight() + getYdelta1a(stringBounder)
		// + getYdelta1b(stringBounder);
		// return new Dimension2DDouble(width, height);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		ug.apply(getTranslateDiamond1(stringBounder)).draw(diamond1);
		super.drawU(ug);
		ug.apply(getTranslateDiamond2(stringBounder)).draw(diamond2);
	}

	@Override
	protected UTranslate getTranslate1(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return super.getTranslate1(stringBounder).compose(
				new UTranslate(0, dimDiamond1.getHeight() + getYdelta1a(stringBounder)));
	}

	@Override
	protected UTranslate getTranslate2(StringBounder stringBounder) {
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		return super.getTranslate2(stringBounder).compose(
				new UTranslate(0, dimDiamond1.getHeight() + getYdelta1a(stringBounder)));
	}

	protected UTranslate getTranslateDiamond1(StringBounder stringBounder) {
		final double y1 = 0;
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		// final double x1 = getLeft(stringBounder) - dimDiamond1.getWidth() / 2;
		final double x1 = dimTotal.getLeft() - dimDiamond1.getLeft();
		return new UTranslate(x1, y1);
	}

	protected UTranslate getTranslateDiamond2(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		final FtileGeometry dimDiamond2 = diamond2.calculateDimension(stringBounder);
		final double y2 = dimTotal.getHeight() - dimDiamond2.getHeight();
		final double x2 = dimTotal.getLeft() - dimDiamond2.getWidth() / 2;
		return new UTranslate(x2, y2);
	}

	public double computeMarginNeedForBranchLabe1(StringBounder stringBounder, Dimension2D label1) {
		final double widthLabelBranch1 = label1.getWidth();
		final double dxDiamond = getTranslateDiamond1(stringBounder).getDx();
		final double diff = widthLabelBranch1 - dxDiamond;
		if (diff > 0) {
			return diff;
		}
		return 0;
	}

	public double computeMarginNeedForBranchLabe2(StringBounder stringBounder, Dimension2D label2) {
		final double widthLabelBranch2 = label2.getWidth();
		final double theoricalEndNeeded = getTranslateDiamond1(stringBounder).getDx()
				+ diamond1.calculateDimension(stringBounder).getWidth() + widthLabelBranch2;
		final double diff = theoricalEndNeeded - calculateDimension(stringBounder).getWidth();
		if (diff > 0) {
			return diff;
		}
		return 0;
	}

	public double computeVerticalMarginNeedForBranchs(StringBounder stringBounder, Dimension2D label1,
			Dimension2D label2) {
		final double heightLabels = Math.max(label1.getHeight(), label2.getHeight());
		final FtileGeometry dimDiamond1 = diamond1.calculateDimension(stringBounder);
		final double dyDiamond = dimDiamond1.getHeight();
		final double diff = heightLabels - dyDiamond;
		if (diff > 0) {
			return diff;
		}
		return 0;
	}

}
