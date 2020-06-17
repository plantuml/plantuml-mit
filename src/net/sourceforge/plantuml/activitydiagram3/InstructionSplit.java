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
package net.sourceforge.plantuml.activitydiagram3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionSplit implements Instruction {

	private final List<InstructionList> splits = new ArrayList<InstructionList>();
	private final Instruction parent;
	private final LinkRendering inlinkRendering;
	private final Swimlane swimlaneIn;
	private Swimlane swimlaneOut;

	public InstructionSplit(Instruction parent, LinkRendering inlinkRendering, Swimlane swimlane) {
		this.parent = parent;
		this.swimlaneIn = swimlane;

		this.splits.add(new InstructionList(swimlane));
		this.inlinkRendering = inlinkRendering;
		if (inlinkRendering == null) {
			throw new IllegalArgumentException();
		}
	}

	public boolean containsBreak() {
		for (InstructionList split : splits) {
			if (split.containsBreak()) {
				return true;
			}
		}
		return false;
	}

	private InstructionList getLast() {
		return splits.get(splits.size() - 1);
	}

	public void add(Instruction ins) {
		getLast().add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		final List<Ftile> all = new ArrayList<Ftile>();
		for (InstructionList list : splits) {
			all.add(list.createFtile(factory));
		}
		return factory.createParallel(all, ForkStyle.SPLIT, null, swimlaneIn, swimlaneOut);
	}

	public Instruction getParent() {
		return parent;
	}

	public void splitAgain(LinkRendering inlinkRendering) {
		if (inlinkRendering != null) {
			getLast().setOutRendering(inlinkRendering);
		}
		final InstructionList list = new InstructionList(swimlaneIn);
		this.splits.add(list);
	}

	public void endSplit(LinkRendering inlinkRendering, Swimlane endSwimlane) {
		if (inlinkRendering != null) {
			getLast().setOutRendering(inlinkRendering);
		}
		this.swimlaneOut = endSwimlane;

	}

	final public boolean kill() {
		return getLast().kill();
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		return getLast().addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		return InstructionList.getSwimlanes2(splits);
	}

	public Swimlane getSwimlaneIn() {
		return parent.getSwimlaneOut();
	}

	public Swimlane getSwimlaneOut() {
		return swimlaneOut;
		// return getLast().getSwimlaneOut();
	}

}
