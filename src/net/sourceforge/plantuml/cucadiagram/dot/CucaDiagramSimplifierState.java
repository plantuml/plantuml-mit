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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.svek.GroupPngMakerState;
import net.sourceforge.plantuml.svek.IEntityImage;

public final class CucaDiagramSimplifierState {

	private final CucaDiagram diagram;
	private final StringBounder stringBounder;

	public CucaDiagramSimplifierState(CucaDiagram diagram, List<String> dotStrings, StringBounder stringBounder)
			throws IOException, InterruptedException {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		boolean changed;
		do {
			changed = false;
			final Collection<IGroup> groups = getOrdered(diagram.getRootGroup());
			for (IGroup g : groups) {
				if (diagram.isAutarkic(g)) {
					final IEntityImage img = computeImage(g);
					g.overrideImage(img, g.getGroupType() == GroupType.CONCURRENT_STATE ? LeafType.STATE_CONCURRENT
							: LeafType.STATE);

					changed = true;
				}
			}
		} while (changed);
	}

	private Collection<IGroup> getOrdered(IGroup root) {
		final Collection<IGroup> ordered = new LinkedHashSet<IGroup>();
		ordered.add(root);
		int size = 1;
		while (true) {
			size = ordered.size();
			addOneLevel(ordered);
			if (size == ordered.size()) {
				break;
			}
		}
		final List<IGroup> result = new ArrayList<IGroup>();
		for (IGroup g : ordered) {
			if (g instanceof GroupRoot == false) {
				result.add(0, g);
			}
		}
		return result;
	}

	private void addOneLevel(Collection<IGroup> currents) {
		for (IGroup g : new ArrayList<IGroup>(currents)) {
			for (IGroup child : reverse(g.getChildren())) {
				currents.add(child);
			}
		}
	}

	private List<IGroup> reverse(Collection<IGroup> source) {
		final List<IGroup> result = new ArrayList<IGroup>();
		for (IGroup g : source) {
			result.add(0, g);
		}
		return result;
	}

	private IEntityImage computeImage(IGroup g) throws IOException, InterruptedException {
		final GroupPngMakerState maker = new GroupPngMakerState(diagram, g, stringBounder);
		return maker.getImage();
	}

}
