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
package net.sourceforge.plantuml.jungle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.UDrawableUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.LimitFinder;

public class PSystemTree extends AbstractPSystem {

	private GNode root;
	private List<GNode> stack = new ArrayList<GNode>();
	private final Rendering rendering = Rendering.NEEDLE;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Tree)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final ImageBuilder builder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE, null, null,
				5, 5, null, false);
		if (rendering == Rendering.NEEDLE) {
			final UDrawable tmp = Needle.getNeedle(root, 200, 0, 60);
			final LimitFinder limitFinder = new LimitFinder(fileFormat.getDefaultStringBounder(), true);
			tmp.drawU(limitFinder);
			final double minY = limitFinder.getMinY();
			builder.setUDrawable(UDrawableUtils.move(tmp, 0, -minY));
		} else {
			builder.setUDrawable(new GTileOneLevelFactory().createGTile(root));
		}
		return builder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public CommandExecutionResult addParagraph(int level, String label) {

		if (level == 1 && root == null) {
			root = new GNode(Display.create(label));
			stack.add(root);
			return CommandExecutionResult.ok();
		} else if (level == 1 && root != null) {
			return CommandExecutionResult.error("Not allowed 1");
		}

		final GNode parent = stack.get(level - 2);
		final GNode newNode = parent.addChild(Display.create(label));

		if (level > stack.size() + 1) {
			return CommandExecutionResult.error("Not allowed 2");
		} else if (level - 1 == stack.size()) {
			stack.add(newNode);
		} else {
			stack.set(level - 1, newNode);
		}

		return CommandExecutionResult.ok();
	}

}
