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

import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;

public class DisplaySection {

	private final Map<HorizontalAlignment, Display> map = new EnumMap<HorizontalAlignment, Display>(
			HorizontalAlignment.class);

	private DisplaySection() {
	}

	public DisplaySection withPage(int page, int lastpage) {
		final DisplaySection result = new DisplaySection();
		for (Map.Entry<HorizontalAlignment, Display> ent : this.map.entrySet()) {
			result.map.put(ent.getKey(), ent.getValue().withPage(page, lastpage));
		}
		return result;
	}

	public Display getDisplay() {
		if (map.size() == 0) {
			return null;
		}
		return map.values().iterator().next();
	}

	public static DisplaySection none() {
		return new DisplaySection();
	}

	public final HorizontalAlignment getHorizontalAlignment() {
		if (map.size() == 0) {
			return HorizontalAlignment.CENTER;
		}
		return map.keySet().iterator().next();
	}

	public boolean isNull() {
		if (map.size() == 0) {
			return true;
		}
		final Display display = map.values().iterator().next();
		return Display.isNull(display);
	}

	public TextBlock createRibbon(FontConfiguration fontConfiguration, ISkinSimple spriteContainer) {
		if (map.size() == 0) {
			return null;
		}
		final Display display = map.values().iterator().next();
		if (Display.isNull(display) || display.size() == 0) {
			return null;
		}
		// if (SkinParam.USE_STYLES()) {
		// throw new UnsupportedOperationException();
		// }
		return display.create(fontConfiguration, getHorizontalAlignment(), spriteContainer);
	}

	public void putDisplay(Display display, HorizontalAlignment horizontalAlignment) {
		this.map.put(horizontalAlignment, display);

	}

}
