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
package net.sourceforge.plantuml.style;

import java.awt.Font;

import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ValueImpl implements Value {

	private final String value;
	private final int priority;

	public ValueImpl(String value, AutomaticCounter counter) {
		this.value = value;
		this.priority = counter.getNextInt();
	}

	@Override
	public String toString() {
		return value + " (" + priority + ")";
	}

	public String asString() {
		return value;
	}

	public HColor asColor(HColorSet set) {
		if ("none".equalsIgnoreCase(value)) {
			return null;
		}
		if ("transparent".equalsIgnoreCase(value)) {
			return HColorUtils.transparent();
		}
		return set.getColorIfValid(value);
	}

	public boolean asBoolean() {
		return "true".equalsIgnoreCase(value);
	}

	public int asInt() {
		return Integer.parseInt(value);
	}

	public double asDouble() {
		return Double.parseDouble(value);
	}

	public int asFontStyle() {
		if (value.equalsIgnoreCase("bold")) {
			return Font.BOLD;
		}
		if (value.equalsIgnoreCase("italic")) {
			return Font.ITALIC;
		}
		return Font.PLAIN;
	}

	public HorizontalAlignment asHorizontalAlignment() {
		return HorizontalAlignment.fromString(asString());
	}

	public int getPriority() {
		return priority;
	}

}
