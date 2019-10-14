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
package net.sourceforge.plantuml.classdiagram.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.LeafType;

class JavaClass {

	private final String name;
	private final String javaPackage;
	private final List<String> parents = new ArrayList<String>();
	private final LeafType type;
	private final LeafType parentType;

	public JavaClass(String javaPackage, String name, String p, LeafType type, LeafType parentType) {
		this.name = name;
		this.javaPackage = javaPackage;
		if (p == null) {
			p = "";
		}
		final StringTokenizer st = new StringTokenizer(StringUtils.trin(p), ",");
		while (st.hasMoreTokens()) {
			this.parents.add(StringUtils.trin(st.nextToken()).replaceAll("\\<.*", ""));
		}
		this.type = type;
		this.parentType = parentType;
	}

	public final String getName() {
		return name;
	}

	public final LeafType getType() {
		return type;
	}

	public final List<String> getParents() {
		return Collections.unmodifiableList(parents);
	}

	public final LeafType getParentType() {
		return parentType;
	}

	public final String getJavaPackage() {
		return javaPackage;
	}

}
