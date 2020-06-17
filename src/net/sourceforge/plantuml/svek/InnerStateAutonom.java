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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class InnerStateAutonom extends AbstractTextBlock implements IEntityImage {

	private final IEntityImage im;
	private final TextBlock title;
	private final TextBlockWidth attribute;
	private final HColor borderColor;
	private final HColor backColor;
	private final boolean shadowing;
	private final Url url;
	private final boolean withSymbol;
	private final UStroke stroke;

	public InnerStateAutonom(final IEntityImage im, final TextBlock title, TextBlockWidth attribute,
			HColor borderColor, HColor backColor, boolean shadowing, Url url, boolean withSymbol, UStroke stroke) {
		this.im = im;
		this.withSymbol = withSymbol;
		this.title = title;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.shadowing = shadowing;
		this.attribute = attribute;
		this.url = url;
		this.stroke = stroke;
	}

	public void drawU(UGraphic ug) {
		final Dimension2D text = title.calculateDimension(ug.getStringBounder());
		final Dimension2D attr = attribute.calculateDimension(ug.getStringBounder());
		final Dimension2D total = calculateDimension(ug.getStringBounder());
		final double marginForFields = attr.getHeight() > 0 ? IEntityImage.MARGIN : 0;

		final double titreHeight = IEntityImage.MARGIN + text.getHeight() + IEntityImage.MARGIN_LINE;
		final RoundedContainer r = new RoundedContainer(total, titreHeight, attr.getHeight() + marginForFields,
				borderColor, backColor, im.getBackcolor(), stroke);

		if (url != null) {
			ug.startUrl(url);
		}

		r.drawU(ug, shadowing);
		title.drawU(ug.apply(new UTranslate((total.getWidth() - text.getWidth()) / 2, IEntityImage.MARGIN)));
		attribute.asTextBlock(total.getWidth()).drawU(
				ug.apply(new UTranslate(0 + IEntityImage.MARGIN, IEntityImage.MARGIN + text.getHeight()
								+ IEntityImage.MARGIN)));

		final double spaceYforURL = getSpaceYforURL(ug.getStringBounder());
		im.drawU(ug.apply(new UTranslate(IEntityImage.MARGIN, spaceYforURL)));

		if (withSymbol) {
			EntityImageState.drawSymbol(ug.apply(borderColor), total.getWidth(), total.getHeight());

		}

		if (url != null) {
			ug.closeUrl();
		}
	}

	private double getSpaceYforURL(StringBounder stringBounder) {
		final Dimension2D text = title.calculateDimension(stringBounder);
		final Dimension2D attr = attribute.calculateDimension(stringBounder);
		final double marginForFields = attr.getHeight() > 0 ? IEntityImage.MARGIN : 0;
		final double titreHeight = IEntityImage.MARGIN + text.getHeight() + IEntityImage.MARGIN_LINE;
		final double suppY = titreHeight + marginForFields + attr.getHeight();
		return suppY + IEntityImage.MARGIN_LINE;
	}

	public HColor getBackcolor() {
		return null;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D img = im.calculateDimension(stringBounder);
		final Dimension2D text = title.calculateDimension(stringBounder);
		final Dimension2D attr = attribute.calculateDimension(stringBounder);

		final Dimension2D dim = Dimension2DDouble.mergeTB(text, attr, img);
		final double marginForFields = attr.getHeight() > 0 ? IEntityImage.MARGIN : 0;

		final Dimension2D result = Dimension2DDouble.delta(dim, IEntityImage.MARGIN * 2 + 2 * IEntityImage.MARGIN_LINE
				+ marginForFields);

		return result;
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public boolean isHidden() {
		return im.isHidden();
	}
	
	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}


}
