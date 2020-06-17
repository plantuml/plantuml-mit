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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class TimingNote {

	private final TimeTick when;
	private final Player player;
	private final Display note;
	private final Position position;
	private final ISkinParam skinParam;

	public TimingNote(TimeTick when, Player player, Display note, Position position, ISkinParam skinParam) {
		this.note = note;
		this.player = player;
		this.when = when;
		this.skinParam = skinParam;
		this.position = position;
	}

	public void drawU(UGraphic ug) {
		if (position == Position.BOTTOM) {
			ug = ug.apply(UTranslate.dy(getMarginY() / 2));
		}
		createOpale().drawU(ug);

	}

	private Opale createOpale() {
		final FontConfiguration fc = new FontConfiguration(skinParam, FontParam.NOTE, null);
		final Rose rose = new Rose();

		final HColor noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		final HColor borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);

		final Sheet sheet = Parser.build(fc, skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), skinParam, CreoleMode.FULL)
				.createSheet(note);
		final SheetBlock1 sheet1 = new SheetBlock1(sheet, LineBreakStrategy.NONE, skinParam.getPadding());
		final double shadowing;
		shadowing = skinParam.shadowing(null) ? 4 : 0;
		final Opale opale = new Opale(shadowing, borderColor, noteBackgroundColor, sheet1, false);
		return opale;
	}

	public double getHeight(StringBounder stringBounder) {
		return createOpale().calculateDimension(stringBounder).getHeight() + getMarginY();
	}

	private double getMarginY() {
		return 10;
	}

	public TimeTick getWhen() {
		return when;
	}

	public final Position getPosition() {
		return position;
	}

}
