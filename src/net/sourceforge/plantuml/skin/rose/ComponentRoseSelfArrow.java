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
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ComponentRoseSelfArrow extends AbstractComponentRoseArrow {

	private final double arrowWidth = 45;
	private final boolean niceArrow;

	public ComponentRoseSelfArrow(Style style, HtmlColor foregroundColor, FontConfiguration font,
			Display stringsToDisplay, ArrowConfiguration arrowConfiguration, ISkinSimple spriteContainer,
			LineBreakStrategy maxMessageSize, boolean niceArrow) {
		super(style, foregroundColor, font, stringsToDisplay, arrowConfiguration, spriteContainer,
				HorizontalAlignment.LEFT, maxMessageSize);
		this.niceArrow = niceArrow;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		if (getArrowConfiguration().isHidden()) {
			return;
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final double textHeight = getTextHeight(stringBounder);

		ug = ug.apply(new UChangeColor(getForegroundColor()));
		final double xRight = arrowWidth - 3;

		final UGraphic ug2 = getArrowConfiguration().applyStroke(ug);

		double x1 = area.getDeltaX1() < 0 ? area.getDeltaX1() : 0;
		double x2 = area.getDeltaX1() > 0 ? -area.getDeltaX1() : 0 + 1;

		final double textAndArrowHeight = textHeight + getArrowOnlyHeight(stringBounder);
		final UEllipse circle = new UEllipse(ComponentRoseArrow.diamCircle, ComponentRoseArrow.diamCircle);
		if (getArrowConfiguration().getDecoration1() == ArrowDecoration.CIRCLE) {
			ug2.apply(new UStroke(ComponentRoseArrow.thinCircle))
					.apply(new UChangeColor(getForegroundColor()))
					.apply(new UTranslate(x1 + 1 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x1 += ComponentRoseArrow.diamCircle / 2;
		}
		if (getArrowConfiguration().getDecoration2() == ArrowDecoration.CIRCLE) {
			ug2.apply(new UStroke(ComponentRoseArrow.thinCircle))
					.apply(new UChangeColor(getForegroundColor()))
					.apply(new UTranslate(x2 - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle,
							textAndArrowHeight - ComponentRoseArrow.diamCircle / 2 - ComponentRoseArrow.thinCircle / 2))
					.draw(circle);
			x2 += ComponentRoseArrow.diamCircle / 2;
		}
		final boolean hasFinalCrossX = getArrowConfiguration().getDressing2().getHead() == ArrowHead.CROSSX;
		if (hasFinalCrossX) {
			x2 += 2 * ComponentRoseArrow.spaceCrossX;
		}

		final double arrowHeight = textAndArrowHeight - textHeight;
		ug2.apply(new UTranslate(x1, textHeight)).draw(new ULine(xRight - x1, 0));
		ug2.apply(new UTranslate(xRight, textHeight)).draw(new ULine(0, arrowHeight));
		ug2.apply(new UTranslate(x2, textAndArrowHeight)).draw(new ULine(xRight - x2, 0));

		if (getArrowConfiguration().isAsync()) {
			if (getArrowConfiguration().getPart() != ArrowPart.BOTTOM_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x2, textAndArrowHeight))
						.draw(new ULine(getArrowDeltaX(), -getArrowDeltaY()));
			}
			if (getArrowConfiguration().getPart() != ArrowPart.TOP_PART) {
				getArrowConfiguration().applyThicknessOnly(ug).apply(new UTranslate(x2, textAndArrowHeight))
						.draw(new ULine(getArrowDeltaX(), getArrowDeltaY()));
			}
		} else if (hasFinalCrossX) {
			ug = ug.apply(new UStroke(2));
			ug.apply(
					new UTranslate(ComponentRoseArrow.spaceCrossX, textHeight - getArrowDeltaX() / 2
							+ getArrowOnlyHeight(stringBounder))).draw(new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.apply(
					new UTranslate(ComponentRoseArrow.spaceCrossX, textHeight + getArrowDeltaX() / 2
							+ getArrowOnlyHeight(stringBounder))).draw(new ULine(getArrowDeltaX(), -getArrowDeltaX()));

		} else {
			final UPolygon polygon = getPolygon(textAndArrowHeight);
			ug.apply(new UChangeBackColor(getForegroundColor())).apply(new UTranslate(x2, 0)).draw(polygon);

		}

		getTextBlock().drawU(ug.apply(new UTranslate(getMarginX1(), 0)));
	}

	private UPolygon getPolygon(final double textAndArrowHeight) {
		final UPolygon polygon = new UPolygon();
		if (getArrowConfiguration().getPart() == ArrowPart.TOP_PART) {
			polygon.addPoint(getArrowDeltaX(), textAndArrowHeight - getArrowDeltaY());
			polygon.addPoint(0, textAndArrowHeight);
			polygon.addPoint(getArrowDeltaX(), textAndArrowHeight);
		} else if (getArrowConfiguration().getPart() == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(getArrowDeltaX(), textAndArrowHeight);
			polygon.addPoint(0, textAndArrowHeight);
			polygon.addPoint(getArrowDeltaX(), textAndArrowHeight + getArrowDeltaY());
		} else {
			polygon.addPoint(getArrowDeltaX(), textAndArrowHeight - getArrowDeltaY());
			polygon.addPoint(0, textAndArrowHeight);
			polygon.addPoint(getArrowDeltaX(), textAndArrowHeight + getArrowDeltaY());
			if (niceArrow) {
				polygon.addPoint(getArrowDeltaX() - 4, textAndArrowHeight);
			}
		}
		return polygon;
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final double textHeight = getTextHeight(stringBounder);
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final double textHeight = getTextHeight(stringBounder);
		final double textAndArrowHeight = (textHeight + getArrowOnlyHeight(stringBounder));
		return new Point2D.Double(getPaddingX(), textAndArrowHeight + getPaddingY());
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		final double textHeight = getTextHeight(stringBounder);
		final double textAndArrowHeight = (textHeight + getArrowOnlyHeight(stringBounder));
		return (textHeight + textAndArrowHeight) / 2 + getPaddingX();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + getArrowOnlyHeight(stringBounder) + 2 * getPaddingY();
	}

	private double getArrowOnlyHeight(StringBounder stringBounder) {
		return 13;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(getTextWidth(stringBounder), arrowWidth);
	}

}
