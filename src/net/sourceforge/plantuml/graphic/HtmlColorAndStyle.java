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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;

public class HtmlColorAndStyle {

	private final HtmlColor color;
	private final LinkStyle style;

	@Override
	public String toString() {
		return color + " " + style;
	}

	public HtmlColorAndStyle(HtmlColor color) {
		this(color, LinkStyle.NORMAL());
	}

	public HtmlColorAndStyle(HtmlColor color, LinkStyle style) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		this.color = color;
		this.style = style;
	}

	public HtmlColor getColor() {
		return color;
	}

	public LinkStyle getStyle() {
		return style;
	}

	static final public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public static HtmlColorAndStyle build(ISkinParam skinParam, String definition) {
		HtmlColor color;
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam.getCurrentStyleBuilder());
			color = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		} else {
			color = Rainbow.build(skinParam).getColors().get(0).color;
		}
		LinkStyle style = LinkStyle.NORMAL();
		final IHtmlColorSet set = skinParam.getIHtmlColorSet();
		for (String s : definition.split(",")) {
			final LinkStyle tmpStyle = LinkStyle.fromString1(s);
			if (tmpStyle.isNormal() == false) {
				style = tmpStyle;
				continue;
			}
			final HtmlColor tmpColor = set.getColorIfValid(s);
			if (tmpColor != null) {
				color = tmpColor;
			}
		}
		return new HtmlColorAndStyle(color, style);
	}

}
