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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class GroupingGraphicalElementHeader extends GroupingGraphicalElement {

	private final Component comp;
	private double endY;
	private final boolean isParallel;
	private final List<Component> notes = new ArrayList<Component>();

	public GroupingGraphicalElementHeader(double currentY, Component comp, InGroupableList inGroupableList,
			boolean isParallel) {
		super(currentY, inGroupableList);
		this.comp = comp;
		this.isParallel = isParallel;
	}

	@Override
	public String toString() {
		return super.toString() + " " + (getInGroupableList() == null ? "no" : getInGroupableList().toString());
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		double width = comp.getPreferredWidth(stringBounder);
		for (Component note : notes) {
			final Dimension2D dimNote = note.getPreferredDimension(stringBounder);
			width += dimNote.getWidth();
		}
		return width + 5;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return comp.getPreferredHeight(stringBounder);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		if (isParallel) {
			return;
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = getInGroupableList().getMinX(stringBounder);
		final double x2 = getInGroupableList().getMaxX(stringBounder) - getInGroupableList().getHack2();
		ug = ug.apply(new UTranslate(x1, getStartingY()));
		double height = comp.getPreferredHeight(stringBounder);
		if (endY > 0) {
			height = endY - getStartingY();
		} else {
			// assert false;
			return;
		}
		final Dimension2D dim = new Dimension2DDouble(x2 - x1, height);
		comp.drawU(ug, new Area(dim), context);
		for (Component note : notes) {
			final Dimension2D dimNote = note.getPreferredDimension(stringBounder);
			note.drawU(ug.apply(new UTranslate(x2 - x1, 0)), new Area(dimNote), context);
		}
	}

	public void setEndY(double y) {
		this.endY = y;
	}

	public void addNotes(StringBounder stringBounder, Collection<Component> notes) {
		for (Component note : notes) {
			this.notes.add(note);
			getInGroupableList().changeHack2(note.getPreferredWidth(stringBounder));
		}
	}

}
