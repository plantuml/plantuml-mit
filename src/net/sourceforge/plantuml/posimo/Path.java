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
package net.sourceforge.plantuml.posimo;

public class Path {

	private final Label label;
	private final Block start;
	private final Block end;
	private final int length;
	private DotPath dotPath;

	public Path(Block start, Block end, Label label) {
		this(start, end, label, 2);
	}

	public Path(Block start, Block end, Label label, int length) {
		if (start == null || end == null) {
			throw new IllegalArgumentException();
		}
		if (length < 1) {
			throw new IllegalArgumentException("length=" + length);
		}
		this.start = start;
		this.end = end;
		this.label = label;
		this.length = length;
	}

	public final Label getLabel() {
		return label;
	}

	public final Block getStart() {
		return start;
	}

	public final Block getEnd() {
		return end;
	}

	public void setLabelPositionCenter(double labelX, double labelY) {
		label.setCenterX(labelX);
		label.setCenterY(labelY);
	}

	public void setLabelPosition(double x, double y) {
		label.setX(x);
		label.setY(y);
	}

	public void setDotPath(DotPath dotPath) {
		this.dotPath = dotPath;

	}

	public final DotPath getDotPath() {
		return dotPath;
	}

	public int getLength() {
		return length;
	}

}
