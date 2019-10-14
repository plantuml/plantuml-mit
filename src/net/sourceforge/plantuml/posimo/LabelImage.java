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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LabelImage {

	// private final Entity entity;
	final private ISkinParam param;
	final private Rose rose;
	final private TextBlock name;

	public LabelImage(Link link, Rose rose, ISkinParam param) {
		if (link == null) {
			throw new IllegalArgumentException();
		}
		// this.entity = entity;
		this.param = param;
		this.rose = rose;
//		this.name = link.getLabel().create(
//				new FontConfiguration(param.getFont(FontParam.CLASS, null, false), HtmlColorUtils.BLACK,
//						param.getHyperlinkColor(), param.useUnderlineForHyperlink()), HorizontalAlignment.CENTER,
//				new SpriteContainerEmpty());
		throw new UnsupportedOperationException();
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = name.calculateDimension(stringBounder);
		return dim;
		// return Dimension2DDouble.delta(dim, 2 * margin);
	}

	public void drawU(UGraphic ug, double x, double y) {
		// final Dimension2D dim = getDimension(ug.getStringBounder());
		// ug.getParam().setBackcolor(rose.getHtmlColor(param,
		// ColorParam.classBackground).getColor());
		// ug.getParam().setColor(rose.getHtmlColor(param,
		// ColorParam.classBorder).getColor());
		// ug.draw(x, y, new URectangle(dim.getWidth(), dim.getHeight()));
		name.drawU(ug.apply(new UTranslate(x, y)));
	}
}
