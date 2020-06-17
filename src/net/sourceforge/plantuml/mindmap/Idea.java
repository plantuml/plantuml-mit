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
package net.sourceforge.plantuml.mindmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.ugraphic.color.HColor;

class Idea {

	private final Display label;
	private final int level;
	private final Idea parent;
	private final List<Idea> children = new ArrayList<Idea>();
	private final IdeaShape shape;
	private final HColor backColor;
	private final StyleBuilder styleBuilder;
	private final String stereotype;

	public Idea(StyleBuilder styleBuilder, HColor backColor, Display label, IdeaShape shape, String stereotype) {
		this(styleBuilder, backColor, 0, null, label, shape, stereotype);
	}

	public Idea createIdea(StyleBuilder styleBuilder, HColor backColor, int newLevel, Display newDisplay,
			IdeaShape newShape, String stereotype) {
		final Idea result = new Idea(styleBuilder, backColor, newLevel, this, newDisplay, newShape, stereotype);
		this.children.add(result);
		return result;
	}

	private Idea(StyleBuilder styleBuilder, HColor backColor, int level, Idea parent, Display label, IdeaShape shape,
			String stereotype) {
		this.backColor = backColor;
		this.styleBuilder = styleBuilder;
		this.label = label;
		this.level = level;
		this.parent = parent;
		this.shape = shape;
		this.stereotype = stereotype;
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

	public Collection<Idea> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public Idea getParent() {
		return parent;
	}

	public final IdeaShape getShape() {
		return shape;
	}

	public final HColor getBackColor() {
		return backColor;
	}

	public final StyleBuilder getStyleBuilder() {
		return styleBuilder;
	}

	public final String getStereotype() {
		return stereotype;
	}

}
