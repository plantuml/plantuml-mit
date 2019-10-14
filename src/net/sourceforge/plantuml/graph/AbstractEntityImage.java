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
package net.sourceforge.plantuml.graph;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UFont;

abstract class AbstractEntityImage {

	private final IEntity entity;

	final private HtmlColor red = HtmlColorUtils.MY_RED;

	final private HtmlColor yellow = HtmlColorUtils.MY_YELLOW;
	private final HtmlColor yellowNote = HtmlColorUtils.COL_FBFB77;

	final private UFont font14 = UFont.sansSerif(14);
	final private UFont font17 = UFont.courier(17).bold();
	final private HtmlColor green = HtmlColorUtils.COL_ADD1B2;
	final private HtmlColor violet = HtmlColorUtils.COL_B4A7E5;
	final private HtmlColor blue = HtmlColorUtils.COL_A9DCDF;
	final private HtmlColor rose = HtmlColorUtils.COL_EB937F;

	public AbstractEntityImage(IEntity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("entity null");
		}
		this.entity = entity;
	}

	public abstract Dimension2D getDimension(StringBounder stringBounder);

	public abstract void draw(ColorMapper colorMapper, Graphics2D g2d);

	protected final IEntity getEntity() {
		return entity;
	}

	protected final HtmlColor getRed() {
		return red;
	}

	protected final HtmlColor getYellow() {
		return yellow;
	}

	protected final UFont getFont17() {
		return font17;
	}

	protected final UFont getFont14() {
		return font14;
	}

	protected final HtmlColor getGreen() {
		return green;
	}

	protected final HtmlColor getViolet() {
		return violet;
	}

	protected final HtmlColor getBlue() {
		return blue;
	}

	protected final HtmlColor getRose() {
		return rose;
	}

	protected final HtmlColor getYellowNote() {
		return yellowNote;
	}
}
