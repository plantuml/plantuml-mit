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
package net.sourceforge.plantuml.cute;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;

public class CuteShapeFactory {

	private final Map<String, Group> groups;

	public CuteShapeFactory(Map<String, Group> groups) {
		this.groups = groups;

	}

	public Positionned createCuteShapePositionned(String data) {
		final VarArgs varArgs = new VarArgs(data);
		return new PositionnedImpl(createCuteShape(data), varArgs);
	}

	private CuteShape createCuteShape(String data) {
		data = StringUtils.trin(data.toLowerCase());
		final VarArgs varArgs = new VarArgs(data);
		if (data.startsWith("circle ")) {
			return new Circle(varArgs);
		}
		if (data.startsWith("cheese ")) {
			return new Cheese(varArgs);
		}
		if (data.startsWith("stick ")) {
			return new Stick(varArgs);
		}
		if (data.startsWith("rectangle ") || data.startsWith("rect ")) {
			return new Rectangle(varArgs);
		}
		if (data.startsWith("triangle ")) {
			return new Triangle(varArgs);
		}
		final String first = data.split(" ")[0];
		// System.err.println("Looking for group " + first + " in " + groups.keySet());
		final Group group = groups.get(first);
		if (group == null) {
			throw new IllegalArgumentException("Cannot find group " + first + " in " + groups.keySet());
		}
		// System.err.println("Found group " + first + " in " + groups.keySet());
		return group;
	}

}
