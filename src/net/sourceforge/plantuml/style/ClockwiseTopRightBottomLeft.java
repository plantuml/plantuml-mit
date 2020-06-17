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

public class ClockwiseTopRightBottomLeft {

	private final double top;
	private final double right;
	private final double bottom;
	private final double left;

	public static ClockwiseTopRightBottomLeft same(double value) {
		return new ClockwiseTopRightBottomLeft(value, value, value, value);
	}

	public static ClockwiseTopRightBottomLeft none() {
		return new ClockwiseTopRightBottomLeft(0, 0, 0, 0);
	}

	public static ClockwiseTopRightBottomLeft read(String value) {
		if (value.matches("[0-9 ]+")) {
			final String split[] = value.split(" +");
			if (split.length == 1) {
				final double first = Integer.parseInt(split[0]);
				return new ClockwiseTopRightBottomLeft(first, first, first, first);
			}
			if (split.length == 2) {
				final double first = Integer.parseInt(split[0]);
				final double second = Integer.parseInt(split[1]);
				return new ClockwiseTopRightBottomLeft(first, second, first, second);
			}
			if (split.length == 3) {
				final double first = Integer.parseInt(split[0]);
				final double second = Integer.parseInt(split[1]);
				final double third = Integer.parseInt(split[2]);
				return new ClockwiseTopRightBottomLeft(first, second, third, second);
			}
			if (split.length == 4) {
				final double first = Integer.parseInt(split[0]);
				final double second = Integer.parseInt(split[1]);
				final double third = Integer.parseInt(split[2]);
				final double forth = Integer.parseInt(split[3]);
				return new ClockwiseTopRightBottomLeft(first, second, third, forth);
			}
		}
		return none();
	}

	public static ClockwiseTopRightBottomLeft margin1margin2(double margin1, double margin2) {
		return new ClockwiseTopRightBottomLeft(margin1, margin2, margin2, margin1);
	}

	private ClockwiseTopRightBottomLeft(double top, double right, double bottom, double left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	@Override
	public String toString() {
		return "" + top + ":" + right + ":" + bottom + ":" + left;
	}

	public final double getTop() {
		return top;
	}

	public final double getRight() {
		return right;
	}

	public final double getBottom() {
		return bottom;
	}

	public final double getLeft() {
		return left;
	}

	public static ClockwiseTopRightBottomLeft marginForDocument(StyleBuilder styleBuilder) {
		final Style style = StyleSignature.of(SName.root, SName.document).getMergedStyle(styleBuilder);
		return style.getMargin();
	}

}
