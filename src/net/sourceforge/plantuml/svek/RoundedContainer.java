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

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public final class RoundedContainer {

	private final Dimension2D dim;
	private final double titleHeight;
	private final double attributeHeight;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	private final HtmlColor imgBackcolor;
	private final UStroke stroke;

	public RoundedContainer(Dimension2D dim, double titleHeight, double attributeHeight, HtmlColor borderColor,
			HtmlColor backColor, HtmlColor imgBackcolor, UStroke stroke) {
		if (dim.getWidth() == 0) {
			throw new IllegalArgumentException();
		}
		this.dim = dim;
		this.imgBackcolor = imgBackcolor;
		this.titleHeight = titleHeight;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.attributeHeight = attributeHeight;
		this.stroke = stroke;
	}

	public void drawU(UGraphic ug, boolean shadowing) {

		ug = ug.apply(new UChangeBackColor(backColor)).apply(new UChangeColor(borderColor));
		final URectangle rect = new URectangle(dim.getWidth(), dim.getHeight(), IEntityImage.CORNER,
				IEntityImage.CORNER);
		if (shadowing) {
			rect.setDeltaShadow(3.0);
		}
		ug.apply(stroke).draw(rect);

		final double yLine = titleHeight + attributeHeight;

		ug = ug.apply(new UChangeBackColor(imgBackcolor));

		final double thickness = stroke.getThickness();

		final URectangle inner = new URectangle(dim.getWidth() - 4 * thickness, dim.getHeight() - titleHeight - 4
				* thickness - attributeHeight, IEntityImage.CORNER, IEntityImage.CORNER);
		ug.apply(new UChangeColor(imgBackcolor)).apply(new UTranslate(2 * thickness, yLine + 2 * thickness))
				.draw(inner);

		if (titleHeight > 0) {
			ug.apply(stroke).apply(new UTranslate(0, yLine)).draw(new ULine(dim.getWidth(), 0));
		}

		if (attributeHeight > 0) {
			ug.apply(stroke).apply(new UTranslate(0, yLine - attributeHeight)).draw(new ULine(dim.getWidth(), 0));
		}

	}
}
