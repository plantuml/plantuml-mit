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
package net.sourceforge.plantuml.ugraphic.comp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SlotSet implements Iterable<Slot> {

	private final List<Slot> all = new ArrayList<Slot>();

	public SlotSet filter(double start, double end) {
		final SlotSet result = new SlotSet();
		for (Slot slot : all) {
			final Slot intersec = slot.intersect(start, end);
			if (intersec != null) {
				result.all.add(intersec);
			}
		}
		return result;
	}

	public void addAll(SlotSet other) {
		this.all.addAll(other.all);
	}

	public void addSlot(double start, double end) {
		final List<Slot> collisions = new ArrayList<Slot>();
		Slot newSlot = new Slot(start, end);
		for (final Iterator<Slot> it = all.iterator(); it.hasNext();) {
			final Slot s = it.next();
			if (s.intersect(newSlot)) {
				it.remove();
				collisions.add(s);
			}
		}
		for (Slot s : collisions) {
			newSlot = newSlot.merge(s);
		}
		all.add(newSlot);
	}

	public SlotSet smaller(double margin) {
		final SlotSet result = new SlotSet();
		for (Slot sl : all) {
			if (sl.size() <= 2 * margin) {
				continue;
			}
			result.addSlot(sl.getStart() + margin, sl.getEnd() - margin);
		}
		return result;
	}

	@Override
	public String toString() {
		return all.toString();
	}

	public List<Slot> getSlots() {
		return Collections.unmodifiableList(all);
	}

	public Iterator<Slot> iterator() {
		return getSlots().iterator();
	}

	public SlotSet reverse() {
		final SlotSet result = new SlotSet();
		Collections.sort(all);
		Slot last = null;
		for (Slot slot : all) {
			if (last != null) {
				result.addSlot(last.getEnd(), slot.getStart());
			}
			last = slot;
		}
		return result;
	}

	public void drawDebugX(UGraphic ug, double size) {
		for (Slot slot : all) {
			final URectangle rect = new URectangle(slot.getEnd() - slot.getStart(), size);
			ug.apply(UTranslate.dx(slot.getStart())).draw(rect);
		}
	}

}
