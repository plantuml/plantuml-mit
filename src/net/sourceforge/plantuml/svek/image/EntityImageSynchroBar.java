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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class EntityImageSynchroBar extends AbstractEntityImage {

	public EntityImageSynchroBar(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
	}

	public StyleSignature getDefaultStyleDefinitionBar() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.activityBar);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (getSkinParam().getRankdir() == Rankdir.LEFT_TO_RIGHT) {
			return new Dimension2DDouble(8, 80);
		}
		return new Dimension2DDouble(80, 8);
	}

	final public void drawU(UGraphic ug) {
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		final Shadowable rect = new URectangle(dim.getWidth(), dim.getHeight());
		double shadowing = 0;
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			shadowing = 4;
		}
		HtmlColor color = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.activityBar);
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionBar().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
			color = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
			shadowing = style.value(PName.Shadowing).asDouble();
		}
		rect.setDeltaShadow(shadowing);
		ug.apply(new UChangeColor(null)).apply(new UChangeBackColor(color)).draw(rect);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

}
