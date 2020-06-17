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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileHeightFixedCentered;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;

public abstract class AbstractParallelFtilesBuilder {

	protected final double barHeight = 6;

	private final Rose rose = new Rose();

	private final ISkinParam skinParam;
	private final StringBounder stringBounder;
	protected final List<Ftile> list99 = new ArrayList<Ftile>();

	public StyleSignature getDefaultStyleDefinition() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public AbstractParallelFtilesBuilder(ISkinParam skinParam, StringBounder stringBounder, List<Ftile> all) {
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
		this.list99.addAll(getFoo2(all));
	}

	protected List<Ftile> getFoo2(List<Ftile> all) {
		final double maxHeight = computeMaxHeight(all);
		final List<Ftile> result = new ArrayList<Ftile>();
		for (Ftile ftile : all) {
			final Ftile newFtile = computeNewFtile(ftile, maxHeight);
			result.add(newFtile);
		}
		return result;
	}

	private Ftile computeNewFtile(Ftile ftile, double maxHeight) {
		final double spaceArroundBlackBar = 20;
		final double xMargin = 14;
		Ftile tmp;
		tmp = FtileUtils.addHorizontalMargin(ftile, xMargin);
		tmp = new FtileHeightFixedCentered(tmp, maxHeight + 2 * spaceArroundBlackBar);
		return tmp;
	}

	final protected double computeMaxHeight(List<Ftile> all) {
		double height = 0;
		for (Ftile tmp : all) {
			height = Math.max(height, tmp.calculateDimension(getStringBounder()).getHeight());
		}
		return height;
	}

	public final Ftile build(Ftile inner) {
		final Ftile step1 = doStep1(inner);
		return doStep2(inner, step1);
	}

	protected abstract Ftile doStep1(Ftile inner);

	protected abstract Ftile doStep2(Ftile inner, Ftile step1);

	protected StringBounder getStringBounder() {
		return stringBounder;
	}

	protected Rose getRose() {
		return rose;
	}

	protected ISkinParam skinParam() {
		return skinParam;
	}

	protected final TextBlock getTextBlock(Display display) {
		// DUP3945
		if (Display.isNull(display)) {
			return null;
		}
		final FontConfiguration fontConfiguration;
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
			fontConfiguration = style.getFontConfiguration(skinParam().getIHtmlColorSet());
		} else {
			fontConfiguration = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}
		return display.create7(fontConfiguration, HorizontalAlignment.LEFT, skinParam(), CreoleMode.SIMPLE_LINE);
	}

	protected TextBlock getTextBlock(LinkRendering linkRendering) {
		// DUP1433
		final Display display = linkRendering.getDisplay();
		return getTextBlock(display);
	}

	protected final double getHeightOfMiddle(Ftile middle) {
		return middle.calculateDimension(getStringBounder()).getHeight();
	}

	protected Swimlane swimlaneOutForStep2() {
		return list99.get(list99.size() - 1).getSwimlaneOut();
	}

}
