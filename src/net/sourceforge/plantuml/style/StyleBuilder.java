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
package net.sourceforge.plantuml.style;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SkinParam;

public class StyleBuilder implements AutomaticCounter {

	private final Map<StyleSignature, Style> styles = new LinkedHashMap<StyleSignature, Style>();
	private final Set<StyleSignature> printedForLog;
	private final SkinParam skinParam;
	private int counter;

	private StyleBuilder(SkinParam skinParam, Set<StyleSignature> printedForLog) {
		this.skinParam = skinParam;
		this.printedForLog = new LinkedHashSet<StyleSignature>();
	}

	public StyleBuilder(SkinParam skinParam) {
		this(skinParam, new LinkedHashSet<StyleSignature>());
	}

	public final SkinParam getSkinParam() {
		return skinParam;
	}

	// public Collection<StyleSignature> getAllStyleSignatures() {
	// return Collections.unmodifiableCollection(styles.keySet());
	// }

	public Style createStyle(String name) {
		name = name.toLowerCase();
		final StyleSignature signature = new StyleSignature(name);
		final Style result = styles.get(signature);
		if (result == null) {
			return new Style(signature, new EnumMap<PName, Value>(PName.class));
		}
		return result;
	}

	public StyleBuilder muteStyle(Style modifiedStyle) {
		final Map<StyleSignature, Style> copy = new LinkedHashMap<StyleSignature, Style>(styles);
		final StyleSignature signature = modifiedStyle.getSignature();
		final Style orig = copy.get(signature);
		if (orig == null) {
			copy.put(signature, modifiedStyle);
		} else {
			final Style newStyle = orig.mergeWith(modifiedStyle);
			copy.put(signature, newStyle);
		}
		final StyleBuilder result = new StyleBuilder(skinParam, this.printedForLog);
		result.styles.putAll(copy);
		result.counter = this.counter;
		return result;
	}

	public void put(StyleSignature styleName, Style newStyle) {
		this.styles.put(styleName, newStyle);
	}

	public int getNextInt() {
		return ++counter;
	}

	public Style getMergedStyle(StyleSignature signature) {
		boolean added = this.printedForLog.add(signature);
		if (added) {
			Log.info("Using style " + signature);
		}
		// if (signature.isStarred()) {
		// throw new IllegalArgumentException();
		// }
		Style result = null;
		for (Entry<StyleSignature, Style> ent : styles.entrySet()) {
			final StyleSignature key = ent.getKey();
			if (key.matchAll(signature) == false) {
				continue;
			}
			if (result == null) {
				result = ent.getValue();
			} else {
				result = result.mergeWith(ent.getValue());
			}

		}
		return result;
	}

}
