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
package net.sourceforge.plantuml.wbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;

final class WElement {

	private final Display label;
	private final int level;
	private final String stereotype;
	private final WElement parent;
	private final StyleBuilder styleBuilder;
	private final List<WElement> childrenLeft = new ArrayList<WElement>();
	private final List<WElement> childrenRight = new ArrayList<WElement>();
	private final IdeaShape shape;

	private StyleSignature getDefaultStyleDefinitionNode(int level) {
		final String depth = SName.depth(level);
		if (level == 0) {
			return StyleSignature.of(SName.root, SName.element, SName.wbsDiagram, SName.node, SName.rootNode)
					.add(stereotype).add(depth);
		}
		if (isLeaf()) {
			return StyleSignature.of(SName.root, SName.element, SName.wbsDiagram, SName.node, SName.leafNode)
					.add(stereotype).add(depth);
		}
		return StyleSignature.of(SName.root, SName.element, SName.wbsDiagram, SName.node).add(stereotype).add(depth);
	}

	public Style getStyle() {
		Style result = getDefaultStyleDefinitionNode(level).getMergedStyle(styleBuilder);
		for (WElement up = parent; up != null; up = up.parent) {
			final StyleSignature ss = up.getDefaultStyleDefinitionNode(level).addStar();
			final Style p = ss.getMergedStyle(styleBuilder);
			result = result.mergeWith(p);
		}
		return result;
	}

	public WElement(Display label, String stereotype, StyleBuilder styleBuilder) {
		this(0, label, stereotype, null, IdeaShape.BOX, styleBuilder);
	}

	private WElement(int level, Display label, String stereotype, WElement parent, IdeaShape shape,
			StyleBuilder styleBuilder) {
		this.label = label;
		this.level = level;
		this.parent = parent;
		this.shape = shape;
		this.styleBuilder = styleBuilder;
		this.stereotype = stereotype;
	}

	public boolean isLeaf() {
		return childrenLeft.size() == 0 && childrenRight.size() == 0;
	}

	public WElement createElement(int newLevel, Display newLabel, String stereotype, Direction direction,
			IdeaShape shape, StyleBuilder styleBuilder) {
		final WElement result = new WElement(newLevel, newLabel, stereotype, this, shape, styleBuilder);
		if (direction == Direction.LEFT) {
			this.childrenLeft.add(result);
		} else {
			this.childrenRight.add(result);
		}
		return result;
	}

	@Override
	public String toString() {
		return label.toString();
	}

	public final int getLevel() {
		return level;
	}

	public final Display getLabel() {
		return label;
	}

	public Collection<WElement> getChildren(Direction direction) {
		if (direction == Direction.LEFT) {
			return Collections.unmodifiableList(childrenLeft);
		}
		return Collections.unmodifiableList(childrenRight);
	}

	public WElement getParent() {
		return parent;
	}

	public final IdeaShape getShape() {
		return shape;
	}

	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

}
