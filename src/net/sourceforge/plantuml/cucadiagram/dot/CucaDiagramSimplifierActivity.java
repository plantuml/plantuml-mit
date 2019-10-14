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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.svek.GroupPngMakerActivity;
import net.sourceforge.plantuml.svek.IEntityImage;

public final class CucaDiagramSimplifierActivity {

	private final CucaDiagram diagram;
	private final StringBounder stringBounder;

	public CucaDiagramSimplifierActivity(CucaDiagram diagram, List<String> dotStrings, StringBounder stringBounder)
			throws IOException, InterruptedException {
		this.diagram = diagram;
		this.stringBounder = stringBounder;
		boolean changed;
		do {
			changed = false;
			final Collection<IGroup> groups = new ArrayList<IGroup>(diagram.getGroups(false));
			for (IGroup g : groups) {
				if (diagram.isAutarkic(g)) {
					// final EntityType type;
					// if (g.zgetGroupType() == GroupType.INNER_ACTIVITY) {
					// type = EntityType.ACTIVITY;
					// } else if (g.zgetGroupType() == GroupType.CONCURRENT_ACTIVITY) {
					// type = EntityType.ACTIVITY_CONCURRENT;
					// } else {
					// throw new IllegalStateException();
					// }

					final IEntityImage img = computeImage(g);
					g.overrideImage(img, LeafType.ACTIVITY);

					changed = true;
				}
			}
		} while (changed);
	}

	// private void computeImageGroup(EntityMutable g, EntityMutable proxy, List<String> dotStrings) throws IOException,
	// InterruptedException {
	// final GroupPngMakerActivity maker = new GroupPngMakerActivity(diagram, g);
	// proxy.setSvekImage(maker.getImage());
	// }

	private IEntityImage computeImage(IGroup g) throws IOException, InterruptedException {
		final GroupPngMakerActivity maker = new GroupPngMakerActivity(diagram, g, stringBounder);
		return maker.getImage();
	}

}
