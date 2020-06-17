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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Genealogy {

	private Map<Ftile, Ftile> myFatherIs = new HashMap<Ftile, Ftile>();
	private final Ftile root;

	public Genealogy(Ftile root) {
		this.root = root;
		process(root);
		// System.err.println("myFatherIs=" + myFatherIs);
	}

	private void process(Ftile current) {
		final Collection<Ftile> children = current.getMyChildren();
		// System.err.println("current=" + current);
		// System.err.println("children=" + children);
		for (Ftile child : children) {
			setMyFather(child, current);
			process(child);
		}
	}

	public Ftile getMyFather(Ftile me) {
		return myFatherIs.get(me);
	}

	private void setMyFather(Ftile child, Ftile father) {
		if (myFatherIs.containsKey(child)) {
			throw new IllegalArgumentException();
		}
		myFatherIs.put(child, father);
	}

	public UTranslate getTranslate(Ftile child, StringBounder stringBounder) {
		Ftile current = child;
		UTranslate result = new UTranslate();
		while (current != root) {
			final Ftile father = getMyFather(current);
			final UTranslate tr = father.getTranslateFor(current, stringBounder);
//			System.err.println("Father=" + father);
//			System.err.println("current=" + current);
//			System.err.println("TR=" + tr);
			result = tr.compose(result);
			current = father;
		}
		return result;
	}

}
