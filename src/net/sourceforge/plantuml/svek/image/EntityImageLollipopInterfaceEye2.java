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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageLollipopInterfaceEye2 extends AbstractEntityImage {

	public static final double SIZE = 14;
	private final TextBlock desc;
	private final TextBlock stereo;
	private final SymbolContext ctx;
	final private Url url;

	public EntityImageLollipopInterfaceEye2(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();

		final USymbol symbol = entity.getUSymbol() == null ? skinParam.componentStyle().toUSymbol()
				: entity.getUSymbol();
		if (symbol == null) {
			throw new IllegalArgumentException();
		}

		this.desc = new BodyEnhanced(entity.getDisplay(), symbol.getFontParam(), skinParam, HorizontalAlignment.CENTER,
				stereotype, symbol.manageHorizontalLine(), false, entity, SName.componentDiagram);

		this.url = entity.getUrl99();

		HColor backcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.BACK);
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), symbol.getColorParamBack());
		}
		// backcolor = HtmlColorUtils.BLUE;
		final HColor forecolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), symbol.getColorParamBorder());
		this.ctx = new SymbolContext(backcolor, forecolor).withStroke(new UStroke(1.5))
				.withShadow(getSkinParam().shadowing(getEntity().getStereotype()) ? 3 : 0);

		if (stereotype != null && stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) != null
				&& portionShower.showPortion(EntityPortion.STEREOTYPE, entity)) {
			stereo = Display.getWithNewlines(stereotype.getLabel(getSkinParam().guillemet())).create(
					new FontConfiguration(getSkinParam(), symbol.getFontParamStereotype(), stereotype),
					HorizontalAlignment.CENTER, skinParam);
		} else {
			stereo = TextBlockUtils.empty(0, 0);
		}

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			circle.setDeltaShadow(4);
		}
		ctx.apply(ug).draw(circle);

		final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double x1 = SIZE / 2 - dimDesc.getWidth() / 2;
		final double y1 = SIZE * 1.4;
		desc.drawU(ug.apply(new UTranslate(x1, y1)));

		final Dimension2D dimStereo = stereo.calculateDimension(ug.getStringBounder());
		final double x2 = SIZE / 2 - dimStereo.getWidth() / 2;
		final double y2 = -dimStereo.getHeight();
		stereo.drawU(ug.apply(new UTranslate(x2, y2)));

		if (url != null) {
			ug.closeUrl();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
