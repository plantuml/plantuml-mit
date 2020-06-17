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
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class GraphicalReference extends GraphicalElement implements InGroupable {

	private final Component comp;
	private final LivingParticipantBox livingParticipantBox1;
	private final LivingParticipantBox livingParticipantBox2;
	private final Url url;

	public GraphicalReference(double startingY, Component comp, LivingParticipantBox livingParticipantBox1,
			LivingParticipantBox livingParticipantBox2, Url url) {
		super(startingY);
		if (livingParticipantBox1 == null || livingParticipantBox2 == null) {
			throw new IllegalArgumentException();
		}
		this.url = url;
		this.comp = comp;
		this.livingParticipantBox1 = livingParticipantBox1;
		this.livingParticipantBox2 = livingParticipantBox2;
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {

		final StringBounder stringBounder = ug.getStringBounder();
		final double posX = getMinX(stringBounder);

		ug = ug.apply(new UTranslate(posX, getStartingY()));
		final double preferredWidth = comp.getPreferredWidth(stringBounder);
		final double w = getMaxX(stringBounder) - getMinX(stringBounder);

		final double width = Math.max(preferredWidth, w);

		final Dimension2D dim = new Dimension2DDouble(width, comp.getPreferredHeight(stringBounder));
		if (url != null) {
			ug.startUrl(url);
		}
		comp.drawU(ug, new Area(dim), context);
		if (url != null) {
			ug.closeUrl();
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return comp.getPreferredHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return comp.getPreferredWidth(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return getMinX(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return Math.max(livingParticipantBox1.getMaxX(stringBounder), livingParticipantBox2.getMaxX(stringBounder));
	}

	public double getMinX(StringBounder stringBounder) {
		return Math.min(livingParticipantBox1.getMinX(stringBounder), livingParticipantBox2.getMinX(stringBounder));
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

}
