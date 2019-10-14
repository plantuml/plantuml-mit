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
package net.sourceforge.plantuml.jungle;

import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Needle implements UDrawable {

	private final double length;
	private final Display display;
	private final double degreePosition;
	private final double degreeOperture;

	private Needle(Display display, double length, double degreePosition, double degreeOperture) {
		this.display = display;
		this.degreePosition = degreePosition;
		this.degreeOperture = degreeOperture;
		this.length = length;
	}

	public void drawU(UGraphic ug) {
		GTileNode.getTextBlock(display);
		ug.draw(getLine());

		ug = ug.apply(getTranslate(length));
		GTileNode.getTextBlock(display).drawU(ug);
	}

	private ULine getLine() {
		final UTranslate translate = getTranslate(length);
		return new ULine(translate.getDx(), translate.getDy());
	}

	public UTranslate getTranslate(double dist) {
		final double angle = degreePosition * Math.PI / 180.0;
		final double dx = dist * Math.cos(angle);
		final double dy = dist * Math.sin(angle);
		return new UTranslate(dx, dy);
	}

	public UDrawable addChildren(final List<GNode> children) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				Needle.this.drawU(ug);
				if (children.size() == 0) {
					return;
				}
				ug = ug.apply(getTranslate(length / 2));
				final UDrawable child1 = getNeedle(children.get(0), length / 2, degreePosition + degreeOperture,
						degreeOperture / 2);
				child1.drawU(ug);
				if (children.size() == 1) {
					return;
				}
				final UDrawable child2 = getNeedle(children.get(1), length / 2, degreePosition - degreeOperture,
						degreeOperture / 2);
				child2.drawU(ug);

			}
		};
	}

	public static UDrawable getNeedle(GNode root, double length, double degree, double degreeOperture) {
		final Needle needle0 = new Needle(root.getDisplay(), length, degree, degreeOperture);
		final UDrawable n1 = needle0.addChildren(root.getChildren());
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
				n1.drawU(ug);
			}
		};
	}

}
