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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;

public abstract class TitledDiagram extends AbstractPSystem implements Diagram, Annotated {

	private DisplayPositionned title = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.TOP);

	private DisplayPositionned caption = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private DisplayPositionned legend = DisplayPositionned.none(HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	private final DisplaySection header = DisplaySection.none();
	private final DisplaySection footer = DisplaySection.none();
	private Display mainFrame;

	
	final public void setTitle(DisplayPositionned title) {
		if (title.isNull() || title.getDisplay().isWhite()) {
			return;
		}
		this.title = title;
	}

	@Override
	final public DisplayPositionned getTitle() {
		return title;
	}
	
	final public void setMainFrame(Display mainFrame) {
		this.mainFrame = mainFrame;
	}

	final public void setCaption(DisplayPositionned caption) {
		this.caption = caption;
	}

	final public DisplayPositionned getCaption() {
		return caption;
	}
	
	final public DisplaySection getHeader() {
		return header;
	}

	final public DisplaySection getFooter() {
		return footer;
	}
	
	final public DisplayPositionned getLegend() {
		return legend;
	}

	public void setLegend(DisplayPositionned legend) {
		this.legend = legend;
	}

	final public Display getMainFrame() {
		return mainFrame;
	}







}
