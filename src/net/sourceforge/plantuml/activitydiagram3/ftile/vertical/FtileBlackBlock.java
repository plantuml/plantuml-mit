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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileBlackBlock extends AbstractFtile {

	private final double labelMargin = 5;

	private double width;
	private double height;
	private TextBlock label = TextBlockUtils.empty(0, 0);
	private final HtmlColor colorBar;
	private final Swimlane swimlane;

	public FtileBlackBlock(ISkinParam skinParam, HtmlColor colorBar, Swimlane swimlane) {
		super(skinParam);
		this.colorBar = colorBar;
		this.swimlane = swimlane;
	}

	public void setBlackBlockDimension(double width, double height) {
		this.height = height;
		this.width = width;
	}

	public void setLabel(TextBlock label) {
		if (label == null) {
			throw new IllegalArgumentException();
		}
		this.label = label;
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		double supp = label.calculateDimension(stringBounder).getWidth();
		if (supp > 0) {
			supp += labelMargin;
		}
		return new FtileGeometry(width + supp, height, width / 2, 0, height);
	}

	public void drawU(UGraphic ug) {
		final URectangle rect = new URectangle(width, height, 5, 5);
		rect.setIgnoreForCompression(true);
		if (skinParam().shadowing(null)) {
			rect.setDeltaShadow(3);
		}
		ug.apply(new UChangeColor(colorBar)).apply(new UChangeBackColor(colorBar)).draw(rect);
		final Dimension2D dimLabel = label.calculateDimension(ug.getStringBounder());
		label.drawU(ug.apply(new UTranslate(width + labelMargin, -dimLabel.getHeight() / 2)));
	}

	public Set<Swimlane> getSwimlanes() {
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

}
