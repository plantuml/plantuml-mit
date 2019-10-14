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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.Branch;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond.ConditionalBuilder;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;

public class FtileFactoryDelegatorIf extends FtileFactoryDelegator {

	private final Pragma pragma;

	public FtileFactoryDelegatorIf(FtileFactory factory, Pragma pragma) {
		super(factory);
		this.pragma = pragma;
	}

	@Override
	public Ftile createIf(Swimlane swimlane, List<Branch> thens, Branch elseBranch, LinkRendering afterEndwhile,
			LinkRendering topInlinkRendering, Url url) {

		final ConditionStyle conditionStyle = skinParam().getConditionStyle();
		final ConditionEndStyle conditionEndStyle = skinParam().getConditionEndStyle();
		final Branch branch0 = thens.get(0);

		final HtmlColor borderColor;
		final HtmlColor backColor;
		final Rainbow arrowColor;
		final FontConfiguration fcTest;
		final FontParam testParam = conditionStyle == ConditionStyle.INSIDE ? FontParam.ACTIVITY_DIAMOND
				: FontParam.ARROW;
		final FontConfiguration fcArrow;
		if (SkinParam.USE_STYLES()) {
			final Style styleArrow = getDefaultStyleDefinitionArrow().getMergedStyle(
					skinParam().getCurrentStyleBuilder());
			final Style styleDiamond = getDefaultStyleDefinitionDiamond().getMergedStyle(
					skinParam().getCurrentStyleBuilder());
			borderColor = styleDiamond.value(PName.LineColor).asColor(skinParam().getIHtmlColorSet());
			backColor = branch0.getColor() == null ? styleDiamond.value(PName.BackGroundColor).asColor(
					skinParam().getIHtmlColorSet()) : branch0.getColor();
			arrowColor = Rainbow.build(styleArrow, skinParam().getIHtmlColorSet());
			fcTest = styleDiamond.getFontConfiguration(skinParam().getIHtmlColorSet());
			fcArrow = styleArrow.getFontConfiguration(skinParam().getIHtmlColorSet());
		} else {
			borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityDiamondBorder);
			backColor = branch0.getColor() == null ? getRose().getHtmlColor(skinParam(),
					ColorParam.activityDiamondBackground) : branch0.getColor();
			arrowColor = Rainbow.build(skinParam());
			fcTest = new FontConfiguration(skinParam(), testParam, null)
					.changeColor(fontColor(FontParam.ACTIVITY_DIAMOND));
			fcArrow = new FontConfiguration(skinParam(), FontParam.ARROW, null);
		}

		if (thens.size() > 1) {
			if (pragma.useVerticalIf()/* OptionFlags.USE_IF_VERTICAL */)
				return FtileIfLongVertical.create(swimlane, borderColor, backColor, arrowColor, getFactory(),
						conditionStyle, thens, elseBranch, fcArrow, topInlinkRendering, afterEndwhile);
			return FtileIfLongHorizontal.create(swimlane, borderColor, backColor, arrowColor, getFactory(),
					conditionStyle, thens, elseBranch, fcArrow, topInlinkRendering, afterEndwhile, fcTest);
		}
		return ConditionalBuilder.create(swimlane, borderColor, backColor, arrowColor, getFactory(), conditionStyle,
				conditionEndStyle, thens.get(0), elseBranch, skinParam(), getStringBounder(), fcArrow, fcTest, url);
	}

	private HtmlColor fontColor(FontParam param) {
		return skinParam().getFontHtmlColor(null, param);
	}

}
