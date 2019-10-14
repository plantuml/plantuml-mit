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

import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;

public class DisplayPositionned {

	private final Display display;
	private final HorizontalAlignment horizontalAlignment;
	private final VerticalAlignment verticalAlignment;

	private DisplayPositionned(Display display, HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment) {
		this.display = display;
		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
	}

	public static DisplayPositionned single(Display display, HorizontalAlignment horizontalAlignment,
			VerticalAlignment verticalAlignment) {
		return new DisplayPositionned(display, horizontalAlignment, verticalAlignment);
	}

	public static DisplayPositionned none(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		return new DisplayPositionned(Display.NULL, horizontalAlignment, verticalAlignment);
	}

	public final Display getDisplay() {
		return display;
	}

	public final HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public final VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public boolean isNull() {
		return Display.isNull(display);
	}

	public boolean hasUrl() {
		return display.hasUrl();
	}

}
