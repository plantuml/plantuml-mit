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
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Line;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class EntityImageLollipopInterfaceEye1 extends AbstractEntityImage {

	private static final int SIZE = 24;
	private final TextBlock desc;
	private final Bibliotekon bibliotekon;
	final private Url url;

	public EntityImageLollipopInterfaceEye1(ILeaf entity, ISkinParam skinParam, Bibliotekon bibliotekon) {
		super(entity, skinParam);
		this.bibliotekon = bibliotekon;
		final Stereotype stereotype = entity.getStereotype();
		this.desc = entity.getDisplay().create(new FontConfiguration(getSkinParam(), FontParam.CLASS, stereotype),
				HorizontalAlignment.CENTER, skinParam);
		this.url = entity.getUrl99();

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {
		ug = ug.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.classBorder));
		ug = ug.apply(SkinParamUtils.getColor(getSkinParam(), getStereo(),
		ColorParam.classBackground).bg());
		if (url != null) {
			ug.startUrl(url);
		}
		final double sizeSmall = 14;
		final double diff = (SIZE - sizeSmall) / 2;
		final UEllipse circle1 = new UEllipse(sizeSmall, sizeSmall);
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			// circle.setDeltaShadow(4);
		}
		ug.apply(new UStroke(1.5)).apply(new UTranslate(diff, diff)).draw(circle1);
		ug = ug.apply(new HColorNone().bg());

		Point2D pos = bibliotekon.getNode(getEntity()).getPosition();

		final List<Line> lines = bibliotekon.getAllLineConnectedTo(getEntity());
		final UTranslate reverse = new UTranslate(pos).reverse();
		final ConnectedCircle connectedCircle = new ConnectedCircle(SIZE / 2);
		for (Line line : lines) {
			Point2D pt = line.getMyPoint(getEntity());
			pt = reverse.getTranslated(pt);
			connectedCircle.addSecondaryConnection(pt);

		}
		// connectedCircle.drawU(ug.apply(new UStroke(1.5)));
		connectedCircle.drawU(ug);

		//
		// final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		// final double widthDesc = dimDesc.getWidth();
		// // final double totalWidth = Math.max(widthDesc, SIZE);
		//
		// final double x = SIZE / 2 - widthDesc / 2;
		// final double y = SIZE;
		// desc.drawU(ug.apply(new UTranslate(x, y)));
		if (url != null) {
			ug.closeUrl();
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
