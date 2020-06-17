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
package net.sourceforge.plantuml.hector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkeletonBuilder {

	private List<PinLinksContinuousSet> sets = new ArrayList<PinLinksContinuousSet>();

	public void add(PinLink pinLink) {
		addInternal(pinLink);
		merge();

	}

	private void merge() {
		for (int i = 0; i < sets.size() - 1; i++) {
			for (int j = i + 1; j < sets.size(); j++) {
				if (sets.get(i).doesTouch(sets.get(j))) {
					sets.get(i).addAll(sets.get(j));
					sets.remove(j);
					return;
				}
			}
		}
	}

	private void addInternal(PinLink pinLink) {
		for (PinLinksContinuousSet set : sets) {
			if (set.doesTouch(pinLink)) {
				set.add(pinLink);
				return;
			}
		}
		final PinLinksContinuousSet newSet = new PinLinksContinuousSet();
		newSet.add(pinLink);
		sets.add(newSet);
	}

	public List<Skeleton> createSkeletons() {
		final List<Skeleton> result = new ArrayList<Skeleton>();

		for (PinLinksContinuousSet set : sets) {
			result.add(set.createSkeleton());
		}

		return Collections.unmodifiableList(result);
	}
}
