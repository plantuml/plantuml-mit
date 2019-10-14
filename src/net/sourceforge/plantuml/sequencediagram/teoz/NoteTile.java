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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class NoteTile extends AbstractTile implements Tile {

	private final LivingSpace livingSpace1;
	private final LivingSpace livingSpace2;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Note note;

	public Event getEvent() {
		return note;
	}

	@Override
	public double getYPoint(StringBounder stringBounder) {
		return getComponent(stringBounder).getPreferredHeight(stringBounder) / 2;
	}

	public NoteTile(LivingSpace livingSpace1, LivingSpace livingSpace2, Note note, Rose skin, ISkinParam skinParam) {
		this.livingSpace1 = livingSpace1;
		this.livingSpace2 = livingSpace2;
		this.note = note;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(note.getUsedStyles(), getNoteComponentType(note.getNoteStyle()),
				null, note.getSkinParamBackcolored(skinParam), note.getStrings());
		return comp;
	}

	private ComponentType getNoteComponentType(NoteStyle noteStyle) {
		if (noteStyle == NoteStyle.HEXAGONAL) {
			return ComponentType.NOTE_HEXAGONAL;
		}
		if (noteStyle == NoteStyle.BOX) {
			return ComponentType.NOTE_BOX;
		}
		return ComponentType.NOTE;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double x = getX(stringBounder).getCurrentValue();
		final Area area = new Area(getUsedWidth(stringBounder), dim.getHeight());

		ug = ug.apply(new UTranslate(x, 0));
		comp.drawU(ug, area, (Context2D) ug);
	}

	private double getUsedWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();
		if (note.getPosition() == NotePosition.OVER_SEVERAL) {
			final double x1 = livingSpace1.getPosB().getCurrentValue();
			final double x2 = livingSpace2.getPosD(stringBounder).getCurrentValue();
			final double w = x2 - x1;
			if (width < w) {
				return w;
			}
		}
		return width;
	}

	private Real getX(StringBounder stringBounder) {
		final NotePosition position = note.getPosition();
		final double width = getUsedWidth(stringBounder);
		if (position == NotePosition.LEFT) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width);
		} else if (position == NotePosition.RIGHT) {
			final int level = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final double dx = level * CommunicationTile.LIVE_DELTA_SIZE;
			return livingSpace1.getPosC(stringBounder).addFixed(dx);
		} else if (position == NotePosition.OVER_SEVERAL) {
			final Real x1 = livingSpace1.getPosC(stringBounder);
			final Real x2 = livingSpace2.getPosC(stringBounder);
			return RealUtils.middle(x1, x2).addFixed(-width / 2);
		} else if (position == NotePosition.OVER) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width / 2);
		} else {
			throw new UnsupportedOperationException(position.toString());
		}
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getHeight();
	}

	public void addConstraints(StringBounder stringBounder) {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double width = dim.getWidth();
	}

	public Real getMinX(StringBounder stringBounder) {
		final Real result = getX(stringBounder);
		if (note.getPosition() == NotePosition.OVER_SEVERAL) {
			final Real x1 = livingSpace1.getPosB();
			return RealUtils.min(result, x1);
		}
		return result;
	}

	public Real getMaxX(StringBounder stringBounder) {
		final Real result = getX(stringBounder).addFixed(getUsedWidth(stringBounder));
		if (note.getPosition() == NotePosition.OVER_SEVERAL) {
			final Real x2 = livingSpace2.getPosD(stringBounder);
			return RealUtils.max(result, x2);
		}
		return result;
	}

}
