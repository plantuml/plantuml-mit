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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class AtomWithMargin extends AbstractAtom implements Atom {

	private final double marginY1;
	private final double marginY2;
	private final Atom atom;

	public AtomWithMargin(Atom atom, double marginY1, double marginY2) {
		this.atom = atom;
		this.marginY1 = marginY1;
		this.marginY2 = marginY2;
	}

	@Override
	public List<Atom> splitInTwo(StringBounder stringBounder, double width) {
		final List<Atom> result = new ArrayList<Atom>();
		final List<Atom> list = atom.splitInTwo(stringBounder, width);
		for (Atom a : list) {
			double y1 = marginY1;
			double y2 = marginY2;
			if (list.size() == 2 && result.size() == 0) {
				y2 = 0;
			}
			if (list.size() == 2 && result.size() == 1) {
				y1 = 0;
			}
			result.add(new AtomWithMargin(a, y1, y2));
		}
		return Collections.unmodifiableList(result);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.delta(atom.calculateDimension(stringBounder), 0, marginY1 + marginY2);
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return atom.getStartingAltitude(stringBounder);
	}

	public void drawU(UGraphic ug) {
		atom.drawU(ug.apply(new UTranslate(0, marginY1)));
	}

}
