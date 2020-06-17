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
package net.sourceforge.plantuml.graph;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

class EntityImageCircleInterface extends AbstractEntityImage {

	final private TextBlock name;
	final private CircleInterface circleInterface;

	public EntityImageCircleInterface(IEntity entity) {
		super(entity);
		this.name = entity.getDisplay().create(FontConfiguration.blackBlueTrue(getFont14()),
				HorizontalAlignment.CENTER, new SpriteContainerEmpty());
		this.circleInterface = new CircleInterface(getYellow(), getRed());
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final double manWidth = circleInterface.getPreferredWidth(stringBounder);
		final double manHeight = circleInterface.getPreferredHeight(stringBounder);
		return new Dimension2DDouble(Math.max(manWidth, nameDim.getWidth()), manHeight + nameDim.getHeight());
	}

	@Override
	public void draw(ColorMapper colorMapper, Graphics2D g2d) {
		throw new UnsupportedOperationException();
		// final Dimension2D dimTotal = getDimension(StringBounderUtils.asStringBounder(g2d));
		// final Dimension2D nameDim = name.calculateDimension(StringBounderUtils.asStringBounder(g2d));
		//
		// final double manWidth = circleInterface.getPreferredWidth(StringBounderUtils.asStringBounder(g2d));
		// final double manHeight = circleInterface.getPreferredHeight(StringBounderUtils.asStringBounder(g2d));
		//
		// final double manX = (dimTotal.getWidth() - manWidth) / 2;
		//
		// g2d.setColor(Color.WHITE);
		// g2d.fill(new Rectangle2D.Double(0, 0, dimTotal.getWidth(), dimTotal.getHeight()));
		//
		// g2d.translate(manX, 0);
		// circleInterface.draw(g2d);
		// g2d.translate(-manX, 0);
		//
		// g2d.setColor(Color.BLACK);
		// name.drawTOBEREMOVED(g2d, (dimTotal.getWidth() - nameDim.getWidth()) / 2, manHeight);
	}
}
