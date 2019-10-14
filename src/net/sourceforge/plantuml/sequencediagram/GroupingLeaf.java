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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.style.StyleBuilder;

final public class GroupingLeaf extends Grouping implements EventWithDeactivate {

	private final GroupingStart start;
	private final HtmlColor backColorGeneral;

	public GroupingLeaf(String title, String comment, GroupingType type, HtmlColor backColorGeneral,
			HtmlColor backColorElement, GroupingStart start, StyleBuilder styleBuilder) {
		super(title, comment, type, backColorElement, styleBuilder);
		if (start == null) {
			throw new IllegalArgumentException();
		}
		this.backColorGeneral = backColorGeneral;
		this.start = start;
		start.addChildren(this);
	}

	public Grouping getJustAfter() {
		final int idx = start.getChildren().indexOf(this);
		if (idx == -1) {
			throw new IllegalStateException();
		}
		if (idx + 1 >= start.getChildren().size()) {
			return null;
		}
		return start.getChildren().get(idx + 1);
	}

	public GroupingStart getGroupingStart() {
		return start;
	}

	@Override
	public int getLevel() {
		return start.getLevel();
	}

	@Override
	public final HtmlColor getBackColorGeneral() {
		if (backColorGeneral == null) {
			return start.getBackColorGeneral();
		}
		return backColorGeneral;
	}

	public boolean dealWith(Participant someone) {
		return false;
	}

	public Url getUrl() {
		return null;
	}

	public boolean hasUrl() {
		return false;
	}

	@Override
	public boolean isParallel() {
		return start.isParallel();
	}

	private double posYendLevel;

	public void setPosYendLevel(double posYendLevel) {
		this.posYendLevel = posYendLevel;
	}

	public double getPosYendLevel() {
		return posYendLevel;
	}

	public boolean addLifeEvent(LifeEvent lifeEvent) {
		return true;
	}

	private List<Note> noteOnMessages = new ArrayList<Note>();

	public final void setNote(Note note) {
		if (note.getPosition() != NotePosition.LEFT && note.getPosition() != NotePosition.RIGHT) {
			throw new IllegalArgumentException();
		}
		this.noteOnMessages.add(note);
	}

	public final List<Note> getNoteOnMessages() {
		return noteOnMessages;
	}

}
