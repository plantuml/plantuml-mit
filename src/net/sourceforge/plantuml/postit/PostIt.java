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
package net.sourceforge.plantuml.postit;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.ComponentRoseNote;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class PostIt {

	private final String id;
	private final Display text;

	private Dimension2D minimumDimension;

	public PostIt(String id, Display text) {
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public Display getText() {
		return text;
	}

	public Dimension2D getMinimumDimension() {
		return minimumDimension;
	}

	public void setMinimumDimension(Dimension2D minimumDimension) {
		this.minimumDimension = minimumDimension;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		double width = getComponent().getPreferredWidth(stringBounder);
		double height = getComponent().getPreferredHeight(stringBounder);

		if (minimumDimension != null && width < minimumDimension.getWidth()) {
			width = minimumDimension.getWidth();
		}
		if (minimumDimension != null && height < minimumDimension.getHeight()) {
			height = minimumDimension.getHeight();
		}

		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {

		final Component note = getComponent();
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimensionToUse = getDimension(stringBounder);

		note.drawU(ug, new Area(dimensionToUse), new SimpleContext2D(false));

	}

	private Component getComponent() {
		final HtmlColor noteBackgroundColor = new HtmlColorSetSimple().getColorIfValid("#FBFB77");
		final HtmlColor borderColor = HtmlColorUtils.MY_RED;

		final SkinParam param = SkinParam.noShadowing(null);
		final UFont fontNote = param.getFont(null, false, FontParam.NOTE);
		final FontConfiguration font2 = fontNote.toFont2(HtmlColorUtils.BLACK, true, HtmlColorUtils.BLUE, 8);
		final ComponentRoseNote note = new ComponentRoseNote(
				null, new SymbolContext(noteBackgroundColor, borderColor).withStroke(new UStroke()), font2, text, 0,
				0, new SpriteContainerEmpty(), 0, HorizontalAlignment.LEFT);
		return note;
	}
}
