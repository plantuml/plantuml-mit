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
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public abstract class ReallyAbstractPlayer implements Player {
	
	protected final ISkinParam skinParam;
	protected final TimingRuler ruler;
	protected final TitleStrategy titleStrategy;
	private final Display title;

	public ReallyAbstractPlayer(TitleStrategy titleStrategy, String title, ISkinParam skinParam, TimingRuler ruler) {
		this.skinParam = skinParam;
		this.ruler = ruler;
		this.titleStrategy = titleStrategy;
		this.title = Display.getWithNewlines(title);
	}
	
	public double getFirstColumnWidth(StringBounder stringBounder) {
		return 40;
	}



	final protected FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.TIMING, null);
	}

	final protected TextBlock getTitle() {
		return title.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}
	
	private TextBlock createTextBlock(String value) {
		final Display display = Display.getWithNewlines(value);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}



}
