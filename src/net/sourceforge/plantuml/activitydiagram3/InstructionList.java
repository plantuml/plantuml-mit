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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileDecorateWelding;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.WeldingPoint;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteType;

public class InstructionList extends WithNote implements Instruction, InstructionCollection {

	private final List<Instruction> all = new ArrayList<Instruction>();
	private final Swimlane defaultSwimlane;

	public boolean containsBreak() {
		for (Instruction ins : all) {
			if (ins.containsBreak()) {
				return true;
			}
		}
		return false;
	}

	public InstructionList() {
		this(null);
	}

	public boolean isEmpty() {
		return all.isEmpty();
	}

	public boolean isOnlySingleStopOrSpot() {
		if (all.size() != 1) {
			return false;
		}
		if (getLast() instanceof InstructionSpot) {
			return true;
		}
		return getLast() instanceof InstructionStop && ((InstructionStop) getLast()).hasNotes() == false;
	}

	public InstructionList(Swimlane defaultSwimlane) {
		this.defaultSwimlane = defaultSwimlane;
	}

	public void add(Instruction ins) {
		all.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		if (all.size() == 0) {
			return new FtileEmpty(factory.skinParam(), defaultSwimlane);
		}
		final List<WeldingPoint> breaks = new ArrayList<WeldingPoint>();
		Ftile result = eventuallyAddNote(factory, null, getSwimlaneIn());
		for (Instruction ins : all) {
			Ftile cur = ins.createFtile(factory);
			breaks.addAll(cur.getWeldingPoints());
			if (ins.getInLinkRendering().isNone() == false) {
				cur = factory.decorateIn(cur, ins.getInLinkRendering());
			}

			if (result == null) {
				result = cur;
			} else {
				result = factory.assembly(result, cur);
			}
		}
		if (outlinkRendering != null) {
			result = factory.decorateOut(result, outlinkRendering);
		}
		if (breaks.size() > 0) {
			result = new FtileDecorateWelding(result, breaks);
		}

		// if (killed) {
		// result = new FtileKilled(result);
		// }
		return result;
	}

	final public boolean kill() {
		if (all.size() == 0) {
			return false;
		}
		return getLast().kill();
	}

	public LinkRendering getInLinkRendering() {
		return all.iterator().next().getInLinkRendering();
	}

	public Instruction getLast() {
		if (all.size() == 0) {
			return null;
		}
		return all.get(all.size() - 1);
	}

	public boolean addNote(Display note, NotePosition position, NoteType type, Colors colors, Swimlane swimlaneNote) {
		if (getLast() == null) {
			return super.addNote(note, position, type, colors, swimlaneNote);
		}
		return getLast().addNote(note, position, type, colors, swimlaneNote);
	}

	public Set<Swimlane> getSwimlanes() {
		return getSwimlanes2(all);
	}

	public Swimlane getSwimlaneIn() {
		return defaultSwimlane;
	}

	public Swimlane getSwimlaneOut() {
		final Set<Swimlane> swimlanes = getSwimlanes();
		if (swimlanes.size() == 0) {
			return null;
		}
		if (swimlanes.size() == 1) {
			return swimlanes.iterator().next();
		}
		return getLast().getSwimlaneOut();
	}

	public static Set<Swimlane> getSwimlanes2(List<? extends Instruction> list) {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		for (Instruction ins : list) {
			result.addAll(ins.getSwimlanes());
		}
		return Collections.unmodifiableSet(result);
	}

	private LinkRendering outlinkRendering;

	public void setOutRendering(LinkRendering outlinkRendering) {
		this.outlinkRendering = outlinkRendering;
	}

}
