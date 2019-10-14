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
package net.sourceforge.plantuml.hector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrowingTree {

	private final List<PinLink> all = new ArrayList<PinLink>();
	private final Map<Pin, ArrayList<Pin>> directlyAfter = new HashMap<Pin, ArrayList<Pin>>();

	public Skeleton createSkeleton() {
		final Set<Pin> pins = new LinkedHashSet<Pin>();
		for (PinLink link : all) {
			pins.add(link.getPin1());
			pins.add(link.getPin2());
		}
		normalizeRowToZero(pins);
		return new Skeleton(new ArrayList<Pin>(pins), new ArrayList<PinLink>(all));
	}

	private void normalizeRowToZero(Collection<Pin> pins) {
		int minRow = Integer.MAX_VALUE;
		for (Pin p : pins) {
			final int r = p.getRow();
			if (r == Integer.MAX_VALUE) {
				throw new IllegalStateException();
			}
			if (r < minRow) {
				minRow = r;
			}
		}
		for (Pin p : pins) {
			p.push(-minRow);
		}
	}

	public boolean canBeAdded(PinLink candidat) {
		if (all.size() == 0) {
			return true;
		}
		final Pin p1 = candidat.getPin1();
		final Pin p2 = candidat.getPin2();
		if (p1.getRow() == Integer.MAX_VALUE && p2.getRow() == Integer.MAX_VALUE) {
			return false;
		}
		return true;
	}

	public void add(PinLink newPinLink) {
		final Pin p1 = newPinLink.getPin1();
		final Pin p2 = newPinLink.getPin2();
		if (all.size() == 0) {
			newPinLink.getPin1().setRow(0);
			simpleRowComputation(newPinLink);
		} else if (isPartiallyNew(newPinLink)) {
			simpleRowComputation(newPinLink);
		} else if (p1.getRow() != Integer.MAX_VALUE && p2.getRow() != Integer.MAX_VALUE) {
			final int actualRowDiff = p2.getRow() - p1.getRow();
			final int neededPushForP2 = newPinLink.getLengthStandard() - actualRowDiff;
			push(p2, neededPushForP2);
		} else {
			throw new IllegalArgumentException();
		}
		all.add(newPinLink);
		getDirectlyAfter(p1).add(p2);
	}

	private List<Pin> getDirectlyAfter(Pin p) {
		ArrayList<Pin> result = directlyAfter.get(p);
		if (result == null) {
			result = new ArrayList<Pin>();
			directlyAfter.put(p, result);
		}
		return result;
	}

	private Collection<Pin> getIndirectlyAfter(Pin pin) {
		final Set<Pin> result = new HashSet<Pin>(getDirectlyAfter(pin));
		int lastSize = result.size();
		while (true) {
			for (Pin p : new ArrayList<Pin>(result)) {
				result.addAll(getDirectlyAfter(p));
			}
			if (result.size() == lastSize) {
				return result;
			}
			lastSize = result.size();
		}
	}

	private void push(Pin p, int push) {
		if (push <= 0) {
			return;
		}
		final Collection<Pin> after = getIndirectlyAfter(p);
		if (after.contains(p)) {
			throw new IllegalStateException();
		}
		p.push(push);
		for (Pin pp : after) {
			pp.push(push);
		}
	}

	private void simpleRowComputation(PinLink link) {
		final Pin p1 = link.getPin1();
		final Pin p2 = link.getPin2();
		if (p1.getRow() == Integer.MAX_VALUE && p2.getRow() != Integer.MAX_VALUE) {
			p1.setRow(p2.getRow() - link.getLengthStandard());
		} else if (p1.getRow() != Integer.MAX_VALUE && p2.getRow() == Integer.MAX_VALUE) {
			p2.setRow(p1.getRow() + link.getLengthStandard());
		} else {
			throw new IllegalArgumentException();
		}
	}

	private boolean isPartiallyNew(PinLink link) {
		final Pin p1 = link.getPin1();
		final Pin p2 = link.getPin2();
		if (p1.getRow() == Integer.MAX_VALUE && p2.getRow() != Integer.MAX_VALUE) {
			return true;
		} else if (p1.getRow() != Integer.MAX_VALUE && p2.getRow() == Integer.MAX_VALUE) {
			return true;
		} else {
			return false;
		}
	}

	public void normalizeRowToZero() {
		// TODO Auto-generated method stub

	}

}
