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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.TextBlock;

public class BodierMap implements Bodier {

	private final List<String> rawBody = new ArrayList<String>();
	private final Map<String, String> map = new LinkedHashMap<String, String>();
	private ILeaf leaf;

	public void muteClassToObject() {
		throw new UnsupportedOperationException();
	}

	public BodierMap() {
	}

	public void setLeaf(ILeaf leaf) {
		if (leaf == null) {
			throw new IllegalArgumentException();
		}
		this.leaf = leaf;

	}

	public static String getLinkedEntry(String s) {
		final Pattern p = Pattern.compile("(\\*-+\\>)");
		final Matcher m = p.matcher(s);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	public void addFieldOrMethod(String s) {
		if (s.contains("=>")) {
			final int x = s.indexOf("=>");
			map.put(s.substring(0, x).trim(), s.substring(x + 2).trim());
		} else if (getLinkedEntry(s) != null) {
			final String link = getLinkedEntry(s);
			final int x = s.indexOf(link);
			map.put(s.substring(0, x).trim(), "\0");
		}
	}

	public List<Member> getMethodsToDisplay() {
		throw new UnsupportedOperationException();
	}

	public List<Member> getFieldsToDisplay() {
		throw new UnsupportedOperationException();
	}

	public boolean hasUrl() {
		return false;
	}

	public TextBlock getBody(FontParam fontParam, ISkinParam skinParam, final boolean showMethods,
			final boolean showFields, Stereotype stereotype) {
		return new TextBlockMap(fontParam, skinParam, map);
	}

	public List<String> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

}
