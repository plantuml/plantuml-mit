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
package net.sourceforge.plantuml.cucadiagram;

public class HideOrShow2 {

	private final String what;
	private final boolean show;

	private boolean isApplyable(ILeaf leaf) {
		if (what.startsWith("$")) {
			return isApplyableTag(leaf, what.substring(1));
		}
		if (what.startsWith("<<") && what.endsWith(">>")) {
			return isApplyableStereotype(leaf, what.substring(2, what.length() - 2).trim());
		}
		final String fullName = leaf.getCode().getFullName();
		// System.err.println("fullName=" + fullName);
		return match(fullName, what);
	}

	private boolean isApplyableStereotype(ILeaf leaf, String pattern) {
		final Stereotype stereotype = leaf.getStereotype();
		if (stereotype == null) {
			return false;
		}
		for (String label : stereotype.getMultipleLabels()) {
			if (match(label, pattern)) {
				return true;
			}

		}
		return false;
	}

	private boolean isApplyableTag(ILeaf leaf, String pattern) {
		for (Stereotag tag : leaf.stereotags()) {
			if (match(tag.getName(), pattern)) {
				return true;
			}
		}
		return false;
	}

	private boolean match(String s, String pattern) {
		if (pattern.contains("*")) {
			// System.err.println("f1=" + pattern);
			// System.err.println("f2=" + Pattern.quote(pattern));
			// System.err.println("f3=" + Matcher.quoteReplacement(pattern));
			String reg = "^" + pattern.replace("*", ".*") + "$";
			return s.matches(reg);

		}
		return s.equals(pattern);
	}

	public HideOrShow2(String what, boolean show) {
		this.what = what;
		this.show = show;
	}

	public boolean apply(boolean hidden, ILeaf leaf) {
		if (isApplyable(leaf)) {
			return !show;
		}
		return hidden;
	}

}
