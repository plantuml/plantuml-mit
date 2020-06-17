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

import net.sourceforge.plantuml.ugraphic.UStroke;

public class LinkStyle {

	static enum Type {
		NORMAL, DASHED, DOTTED, BOLD, INVISIBLE;
	}

	private LinkStyle(Type type, Double thickness) {
		this.type = type;
		this.thickness = thickness;

	}

	private final Type type;
	private final Double thickness;

	@Override
	public String toString() {
		return type.toString() + "(" + thickness + ")";
	}

	// DOUBLE_tobedone, __toremove_INTERFACE_PROVIDER, __toremove_INTERFACE_USER;

	// private UStroke getStroke2() {
	// return getStroke2(1);
	// }

	public boolean isNormal() {
		return type == Type.NORMAL;
	}

	public boolean isInvisible() {
		return type == Type.INVISIBLE;
	}

	public static LinkStyle NORMAL() {
		return new LinkStyle(Type.NORMAL, null);
	}

	public static LinkStyle INVISIBLE() {
		return new LinkStyle(Type.INVISIBLE, null);
	}

	public static LinkStyle BOLD() {
		return new LinkStyle(Type.BOLD, null);
	}

	public static LinkStyle DOTTED() {
		return new LinkStyle(Type.DOTTED, null);
	}

	public static LinkStyle DASHED() {
		return new LinkStyle(Type.DASHED, null);
	}

	public LinkStyle goThickness(double thickness) {
		return new LinkStyle(type, thickness);
	}

	public UStroke getStroke3() {
		if (type == Type.DASHED) {
			return new UStroke(7, 7, nonZeroThickness());
		}
		if (type == Type.DOTTED) {
			return new UStroke(1, 3, nonZeroThickness());
		}
		if (type == Type.BOLD) {
			return new UStroke(2);
		}
		return new UStroke(nonZeroThickness());
	}

	public UStroke muteStroke(UStroke stroke) {
		if (type == Type.DASHED || type == Type.DOTTED || type == Type.BOLD) {
			return getStroke3();
		}
		return stroke;
	}

	private double nonZeroThickness() {
		if (thickness == null) {
			return 1;
		}
		return thickness;
	}

	public static LinkStyle fromString1(String s) {
		final LinkStyle result = fromString2(s);
		if (result == null) {
			return LinkStyle.NORMAL();
		}
		return result;
	}

	public static LinkStyle fromString2(String s) {
		if ("dashed".equalsIgnoreCase(s)) {
			return DASHED();
		}
		if ("dotted".equalsIgnoreCase(s)) {
			return DOTTED();
		}
		if ("bold".equalsIgnoreCase(s)) {
			return BOLD();
		}
		if ("hidden".equalsIgnoreCase(s)) {
			return INVISIBLE();
		}
		return null;
	}

	public boolean isThicknessOverrided() {
		return thickness != null;
	}

}
