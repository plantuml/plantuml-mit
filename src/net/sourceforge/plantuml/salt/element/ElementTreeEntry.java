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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ElementTreeEntry {

	private final Element firstElement;
	private final int level;
	private final List<Element> otherElements = new ArrayList<Element>();

	public ElementTreeEntry(int level, Element elmt) {
		this.firstElement = elmt;
		this.level = level;
	}

	public void addCell(Element elmt) {
		this.otherElements.add(elmt);
	}

	public Dimension2D getPreferredDimensionFirstCell(StringBounder stringBounder) {
		return Dimension2DDouble.delta(firstElement.getPreferredDimension(stringBounder, 0, 0), getXDelta(), 0);
	}

	public ListWidth getPreferredDimensionOtherCell(StringBounder stringBounder) {
		final ListWidth result = new ListWidth();
		for (Element element : otherElements) {
			result.add(element.getPreferredDimension(stringBounder, 0, 0).getWidth());
		}
		return result;
	}

	public double getXDelta() {
		return level * 10;
	}

	public void drawFirstCell(UGraphic ug, double x, double y) {
		firstElement.drawU(ug.apply(new UTranslate(x + getXDelta(), y)), 0, null);
	}

	public void drawSecondCell(UGraphic ug, double x, double y, ListWidth otherWidth, double margin) {
		final Iterator<Double> it = otherWidth.iterator();
		for (Element element : otherElements) {
			element.drawU(ug.apply(new UTranslate(x, y)), 0, null);
			x += it.next() + margin;
		}
	}

}
