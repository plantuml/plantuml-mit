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
package net.sourceforge.plantuml.creole.legacy;

import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.creole.CreoleContext;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Stripe;
import net.sourceforge.plantuml.creole.StripeStyle;
import net.sourceforge.plantuml.creole.StripeStyleType;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.creole.atom.AtomTree;
import net.sourceforge.plantuml.creole.atom.AtomWithMargin;
import net.sourceforge.plantuml.graphic.FontConfiguration;

public class StripeTree implements Stripe {

	private FontConfiguration fontConfiguration;
	final private ISkinSimple skinParam;
	final private AtomTree tree;
	final private Atom marged;
	final private StripeStyle stripeStyle = new StripeStyle(StripeStyleType.TREE, 0, '\0');

	public StripeTree(FontConfiguration fontConfiguration, ISkinSimple skinParam, String line) {
		this.fontConfiguration = fontConfiguration;
		this.skinParam = skinParam;
		this.tree = new AtomTree(fontConfiguration.getColor());
		this.marged = new AtomWithMargin(tree, 2, 2);
		analyzeAndAdd(line);
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom>singletonList(marged);
	}

	public Atom getLHeader() {
		return null;
	}

	public void analyzeAndAdd(String line) {
		final List<String> lines = StripeTable.getWithNewlinesInternal(line);
		for (String s : lines) {
			final StripeSimple cell = new StripeSimple(fontConfiguration, stripeStyle, new CreoleContext(), skinParam,
					CreoleMode.FULL);
			final String text = s.replaceFirst("^\\s*\\|_", "");
			final int level = computeLevel(s);
			cell.analyzeAndAdd(text);
			this.tree.addCell(StripeTable.asAtom(Collections.singletonList(cell), 0), level);
		}

	}

	private int computeLevel(String s) {
		int result = 1;
		while (s.length() > 0) {
			if (s.startsWith("  ")) {
				result++;
				s = s.substring(2);
				continue;
			} else if (s.startsWith("\t")) {
				result++;
				s = s.substring(1);
				continue;
			}
			return result;
		}
		return result;
	}

}
