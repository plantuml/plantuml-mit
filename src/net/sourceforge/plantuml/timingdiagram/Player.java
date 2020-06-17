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
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;

public abstract class Player implements TimeProjected {

	protected final ISkinParam skinParam;
	protected final TimingRuler ruler;
	private final boolean compact;
	private final Display title;
	protected int suggestedHeight;

	public Player(String title, ISkinParam skinParam, TimingRuler ruler, boolean compact) {
		this.skinParam = skinParam;
		this.compact = compact;
		this.ruler = ruler;
		this.title = Display.getWithNewlines(title);
	}

	public boolean isCompact() {
		return compact;
	}

	final protected FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.TIMING, null);
	}

	final protected TextBlock getTitle() {
		return title.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public abstract void addNote(TimeTick now, Display note, Position position);

	public abstract void defineState(String stateCode, String label);

	public abstract void setState(TimeTick now, String comment, Colors color, String... states);

	public abstract void createConstraint(TimeTick tick1, TimeTick tick2, String message);

	public abstract TextBlock getPart1(double fullAvailableWidth, double specialVSpace);

	public abstract UDrawable getPart2();

	public abstract double getFullHeight(StringBounder stringBounder);

	public final void setHeight(int height) {
		this.suggestedHeight = height;
	}

}
