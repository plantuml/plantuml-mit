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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileWithNoteOpale;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionFork extends WithNote implements Instruction {

	private final List<InstructionList> forks = new ArrayList<InstructionList>();
	private final Instruction parent;
	private final LinkRendering inlinkRendering;
	private final ISkinParam skinParam;
	private final Swimlane swimlaneIn;
	private Swimlane swimlaneOut;
	private ForkStyle style = ForkStyle.FORK;
	private String label;
	boolean finished = false;

	public boolean containsBreak() {
		for (InstructionList fork : forks) {
			if (fork.containsBreak()) {
				return true;
			}
		}
		return false;
	}

	public InstructionFork(Instruction parent, LinkRendering inlinkRendering, ISkinParam skinParam, Swimlane swimlane) {
		this.parent = parent;
		this.inlinkRendering = inlinkRendering;
		this.skinParam = skinParam;
		this.swimlaneIn = swimlane;
		this.swimlaneOut = swimlane;
		this.forks.add(new InstructionList());
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
	}

	private InstructionList getLastList() {
		return forks.get(forks.size() - 1);
	}

	public void add(Instruction ins) {
		getLastList().add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final List<Ftile> all = new ArrayList<Ftile>();
		for (InstructionList list : forks) {
			all.add(list.createFtile(factory));
		}
		Ftile result = factory.createParallel(all, style, label, swimlaneIn, swimlaneOut);
		if (getPositionedNotes().size() > 0) {
			result = FtileWithNoteOpale.create(result, getPositionedNotes(), skinParam, false);
		}
		return result;
	}

	public Instruction getParent() {
		return parent;
	}

	public void forkAgain(Swimlane swimlane) {
		this.swimlaneOut = swimlane;
		this.forks.add(new InstructionList());
	}

	final public boolean kill() {
		return getLastList().kill();
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	@Override
	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (finished) {
			return super.addNote(note, position, type, colors, swimlaneNote);
		}
		if (getLastList().getLast() == null) {
			return getLastList().addNote(note, position, type, colors, swimlaneNote);
		}
		return getLastList().addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>(InstructionList.getSwimlanes2(forks));
		result.add(swimlaneIn);
		result.add(swimlaneOut);
		return result;
	}

	public Swimlane getSwimlaneIn() {
		return swimlaneIn;
	}

	public Swimlane getSwimlaneOut() {
		return swimlaneOut;
	}

	public void manageOutRendering(LinkRendering nextLinkRenderer, boolean endFork) {
		if (endFork) {
			this.finished = true;
		}
		if (nextLinkRenderer == null) {
			return;
		}
		getLastList().setOutRendering(nextLinkRenderer);
	}

	public void setStyle(ForkStyle style, String label, Swimlane swimlane) {
		this.style = style;
		this.label = label;
		this.swimlaneOut = swimlane;
	}

}
