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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class MessageArrow extends Arrow {

	private final LivingParticipantBox p1;
	private final LivingParticipantBox p2;
	private final Component compAliveBox;

	public MessageArrow(double startingY, Rose skin, ArrowComponent arrow, LivingParticipantBox p1, LivingParticipantBox p2,
			Url url, Component compAliveBox) {
		super(startingY, skin, arrow, url);

		if (p1 == p2) {
			throw new IllegalArgumentException();
		}
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
		this.compAliveBox = compAliveBox;
	}

	@Override
	public double getActualWidth(StringBounder stringBounder) {
		final double r = getRightEndInternal(stringBounder) - getLeftStartInternal(stringBounder);
		assert r > 0;
		return r;
	}

	private double getLeftStartInternal(StringBounder stringBounder) {
		return getParticipantAt(stringBounder, NotePosition.LEFT)
				.getLiveThicknessAt(stringBounder, getArrowYStartLevel(stringBounder)).getSegment().getPos2();
	}

	private double getRightEndInternal(StringBounder stringBounder) {
		final Segment segment = getParticipantAt(stringBounder, NotePosition.RIGHT).getLiveThicknessAt(stringBounder,
				getArrowYStartLevel(stringBounder)).getSegment();
		if (segment.getLength() == 0) {
			return segment.getPos1();
		}
		final double rectWidth = compAliveBox.getPreferredWidth(stringBounder);
		return segment.getPos2() - rectWidth;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getArrowComponent().getPreferredHeight(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return getLeftStartInternal(stringBounder);
	}

	@Override
	public int getDirection(StringBounder stringBounder) {
		final double x1 = p1.getParticipantBox().getCenterX(stringBounder);
		final double x2 = p2.getParticipantBox().getCenterX(stringBounder);
		if (x1 < x2) {
			return 1;
		}
		return -1;
	}

	public LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position) {
		final int direction = getDirection(stringBounder);
		if (direction == 1 && position == NotePosition.RIGHT) {
			return p2;
		}
		if (direction == 1 && position == NotePosition.LEFT) {
			return p1;
		}
		if (direction == -1 && position == NotePosition.RIGHT) {
			return p1;
		}
		if (direction == -1 && position == NotePosition.LEFT) {
			return p2;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getArrowComponent().getPreferredWidth(stringBounder);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(new UTranslate(getStartingX(stringBounder), getStartingY()));
		startUrl(ug);
		getArrowComponent().drawU(ug, new Area(getActualDimension(stringBounder)), context);
		endUrl(ug);
	}

	private Dimension2D getActualDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getActualWidth(stringBounder) - getPaddingArrowHead(), getArrowComponent()
				.getPreferredHeight(stringBounder));
	}

	@Override
	public double getArrowYStartLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getStartPoint(stringBounder, dim).getY();
		}
		return getStartingY();
	}

	@Override
	public double getArrowYEndLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getEndPoint(stringBounder, dim).getY();
		}
		return getStartingY() + getArrowComponent().getPreferredHeight(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return getRightEndInternal(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return getLeftStartInternal(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return getMinX(stringBounder) + "-" + getMaxX(stringBounder);
	}

}
