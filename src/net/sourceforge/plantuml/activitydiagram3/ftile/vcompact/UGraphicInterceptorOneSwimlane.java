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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UShape;

public class UGraphicInterceptorOneSwimlane extends UGraphicDelegator {

	private final Swimlane swimlane;

	public UGraphicInterceptorOneSwimlane(UGraphic ug, Swimlane swimlane) {
		super(ug);
		this.swimlane = swimlane;
	}

	public void draw(UShape shape) {
		// System.err.println("inter=" + shape.getClass());
		if (shape instanceof Ftile) {
			final Ftile tile = (Ftile) shape;
			final Set<Swimlane> swinlanes = tile.getSwimlanes();
			final boolean contained = swinlanes.contains(swimlane);
			if (contained) {
				tile.drawU(this);
				// drawGoto();
			}
		} else if (shape instanceof Connection) {
			final Connection connection = (Connection) shape;
			final Ftile tile1 = connection.getFtile1();
			final Ftile tile2 = connection.getFtile2();
			final boolean contained1 = tile1 == null || tile1.getSwimlaneOut() == null
					|| tile1.getSwimlaneOut() == swimlane;
			final boolean contained2 = tile2 == null || tile2.getSwimlaneIn() == null
					|| tile2.getSwimlaneIn() == swimlane;

			if (contained1 && contained2) {
				connection.drawU(this);
			}
		} else {
			getUg().draw(shape);
			// System.err.println("Drawing " + shape);
		}

	}

	private void drawGoto() {
		final UGraphic ugGoto = getUg().apply(new UChangeColor(HtmlColorUtils.GREEN)).apply(
				new UChangeBackColor(HtmlColorUtils.GREEN));
		ugGoto.draw(new ULine(100, 100));
	}

	public UGraphic apply(UChange change) {
		return new UGraphicInterceptorOneSwimlane(getUg().apply(change), swimlane);
	}

	public final Swimlane getSwimlane() {
		return swimlane;
	}

}
