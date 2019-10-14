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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNotes;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionGroup implements Instruction, InstructionCollection {

	private final InstructionList list;
	private final Instruction parent;
	private final HtmlColor backColor;
	private final HtmlColor borderColor;
	private final HtmlColor titleColor;
	private final LinkRendering linkRendering;
	private final USymbol type;

	private final Display test;
	private final double roundCorner;
	private PositionedNote note = null;

	public boolean containsBreak() {
		return list.containsBreak();
	}

	public InstructionGroup(Instruction parent, Display test, HtmlColor backColor, HtmlColor titleColor,
			Swimlane swimlane, HtmlColor borderColor, LinkRendering linkRendering, USymbol type, double roundCorner) {
		this.list = new InstructionList(swimlane);
		this.type = type;
		this.linkRendering = linkRendering;
		this.parent = parent;
		this.test = test;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.titleColor = titleColor;
		this.roundCorner = roundCorner;
	}

	public void add(Instruction ins) {
		list.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		Ftile tmp = list.createFtile(factory);
		if (note != null) {
			tmp = new FtileWithNotes(tmp, Collections.singleton(note), factory.skinParam());
		}
		return factory.createGroup(tmp, test, backColor, titleColor, null, borderColor, type, roundCorner);
	}

	public Instruction getParent() {
		return parent;
	}

	final public boolean kill() {
		return list.kill();
	}

	public LinkRendering getInLinkRendering() {
		return linkRendering;
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (list.isEmpty()) {
			this.note = new PositionedNote(note, position, type, colors, swimlaneNote);
			return true;
		}
		return list.addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		return list.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return list.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return list.getSwimlaneOut();
	}

	public Instruction getLast() {
		return list.getLast();
	}

}
