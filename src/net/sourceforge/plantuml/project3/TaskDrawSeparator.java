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
package net.sourceforge.plantuml.project3;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TaskDrawSeparator implements TaskDraw {

	private final TimeScale timeScale;
	private final double y;
	private final Instant min;
	private final Instant max;
	private final String name;

	public TaskDrawSeparator(TaskSeparator task, TimeScale timeScale, double y, Instant min, Instant max) {
		this.name = task.getName();
		this.y = y;
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
	}

	public void drawTitle(UGraphic ug) {
		getTitle().drawU(ug.apply(new UTranslate(MARGIN1, 0)));
	}

	private TextBlock getTitle() {
		if (name == null) {
			return TextBlockUtils.empty(0, 0);
		}
		return Display.getWithNewlines(this.name).create(getFontConfiguration(), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HtmlColorUtils.BLACK, HtmlColorUtils.BLACK, false);
	}

	private final static double MARGIN1 = 10;
	private final static double MARGIN2 = 2;

	public void drawU(UGraphic ug) {
		final double widthTitle = getTitle().calculateDimension(ug.getStringBounder()).getWidth();
		final double start = timeScale.getStartingPosition(min) + widthTitle;
		final double end = timeScale.getEndingPosition(max);

		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
		ug = ug.apply(new UTranslate(0, getHeight() / 2));

		if (widthTitle == 0) {
			final ULine line = new ULine(end - start, 0);
			ug.draw(line);
		} else {
			final ULine line1 = new ULine(MARGIN1 - MARGIN2, 0);
			final ULine line2 = new ULine(end - start - MARGIN1 - MARGIN2, 0);
			ug.draw(line1);
			ug.apply(new UTranslate(widthTitle + MARGIN1 + MARGIN2, 0)).draw(line2);
		}
	}

	public double getHeight() {
		return 16;
	}

	public double getY() {
		return y;
	}

	public double getY(Direction direction) {
		if (direction == Direction.UP) {
			return y;
		}
		if (direction == Direction.DOWN) {
			return y + getHeight();
		}
		return y + getHeight() / 2;
	}

	public void setColors(ComplementColors colors) {
	}

}
