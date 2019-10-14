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
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;

public abstract class AbstractEntityDiagram extends CucaDiagram {

	public AbstractEntityDiagram(ISkinSimple orig) {
		super(orig);
	}

	final protected List<String> getDotStrings() {
		final List<String> def = Arrays.asList("nodesep=.35;", "ranksep=0.8;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11,height=.35,width=.55];");
		if (getPragma().isDefine("graphattributes") == false) {
			return def;
		}
		final String attribute = getPragma().getValue("graphattributes");
		final List<String> result = new ArrayList<String>(def);
		result.add(attribute);
		return Collections.unmodifiableList(result);
	}

	final public DiagramDescription getDescription() {
		final StringBuilder result = new StringBuilder("(" + getLeafssize() + " entities");
		if (getSource() != null) {
			final String id = getSource().getId();
			if (id != null) {
				result.append(", ");
				result.append(id);
			}
		}
		result.append(")");
		return new DiagramDescription(result.toString());
	}

}
