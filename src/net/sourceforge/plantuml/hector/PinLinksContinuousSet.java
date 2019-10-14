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
import java.util.Iterator;

public class PinLinksContinuousSet {

	private final Collection<PinLink> all = new ArrayList<PinLink>();

	public Skeleton createSkeleton() {
		final GrowingTree tree = new GrowingTree();
		final Collection<PinLink> pendings = new ArrayList<PinLink>(all);
		while (pendings.size() > 0) {
			for (Iterator<PinLink> it = pendings.iterator(); it.hasNext();) {
				final PinLink candidat = it.next();
				if (tree.canBeAdded(candidat)) {
					tree.add(candidat);
					it.remove();
				}
			}
		}
		return tree.createSkeleton();

	}

	public void add(PinLink newPinLink) {
		if (all.size() == 0) {
			all.add(newPinLink);
			return;
		}
		if (all.contains(newPinLink)) {
			throw new IllegalArgumentException("already");
		}
		for (PinLink aLink : all) {
			if (newPinLink.doesTouch(aLink)) {
				all.add(newPinLink);
				return;
			}
		}
		throw new IllegalArgumentException("not connex");
	}

	public void addAll(PinLinksContinuousSet other) {
		if (doesTouch(other) == false) {
			throw new IllegalArgumentException();
		}
		this.all.addAll(other.all);
	}

	public boolean doesTouch(PinLink other) {
		for (PinLink aLink : all) {
			if (other.doesTouch(aLink)) {
				return true;
			}
		}
		return false;
	}

	public boolean doesTouch(PinLinksContinuousSet otherSet) {
		for (PinLink otherLink : otherSet.all) {
			if (doesTouch(otherLink)) {
				return true;
			}
		}
		return false;
	}

}
