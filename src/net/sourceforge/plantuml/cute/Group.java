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
package net.sourceforge.plantuml.cute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Group implements Positionned {

	private final String groupName;
	private final List<Positionned> shapes;
	private final Group parent;
	private final Map<String, Group> children;

	// private final List<Group> children = new ArrayList<Group>();

	@Override
	public String toString() {
		return "Group " + groupName + " (" + shapes.size() + ") ";
	}

	// public static Group fromList(List<Positionned> shapes) {
	// return new Group("Automatic", shapes);
	// }

	public static Group createRoot() {
		return new Group(null, "ROOT");
	}

	private Group(Group parent, String groupName) {
		this.parent = parent;
		this.groupName = groupName;
		this.shapes = new ArrayList<Positionned>();
		this.children = new HashMap<String, Group>();
	}

	private Group(Group parent, String groupName, List<Positionned> shapes) {
		this.parent = parent;
		this.groupName = groupName;
		this.shapes = shapes;
		this.children = null;
	}

	public Group createChild(String childName) {
		final Group result = new Group(this, childName);
		this.children.put(childName, result);
		return result;
	}

	public void drawU(UGraphic ug) {
		for (Positionned shape : shapes) {
			shape.drawU(ug);
		}
	}

	public void add(Positionned shape) {
		shapes.add(shape);
	}

	public String getName() {
		return groupName;
	}

	public Positionned rotateZoom(RotationZoom rotationZoom) {
		if (rotationZoom.isNone()) {
			return this;
		}
		final List<Positionned> result = new ArrayList<Positionned>();
		for (Positionned shape : shapes) {
			result.add(shape.rotateZoom(rotationZoom));
		}
		return new Group(parent, groupName + "->" + rotationZoom, result);
	}

	public Positionned translate(UTranslate translation) {
		throw new UnsupportedOperationException();
	}

	public Group getParent() {
		return parent;
	}

	public Map<String, Group> getChildren() {
		return Collections.unmodifiableMap(children);
	}

}
