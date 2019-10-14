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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ConnectionVerticalDown extends AbstractConnection implements ConnectionTranslatable {

	private final Point2D p1;
	private final Point2D p2;
	private final Rainbow color;
	private final TextBlock textBlock;

	public ConnectionVerticalDown(Ftile ftile1, Ftile ftile2, Point2D p1, Point2D p2, Rainbow color, TextBlock textBlock) {
		super(ftile1, ftile2);
		if (color.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
		this.textBlock = textBlock;
	}

	public void drawU(UGraphic ug) {
		ug.draw(getSimpleSnake());
	}

	public double getMaxX(StringBounder stringBounder) {
		return getSimpleSnake().getMaxX(stringBounder);
	}

	private Snake getSimpleSnake() {
		final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToDown());
		snake.setLabel(textBlock);
		snake.addPoint(p1);
		snake.addPoint(p2);
		return snake;
	}

	public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
		final Snake snake = new Snake(arrowHorizontalAlignment(), color, Arrows.asToDown());
		snake.setLabel(textBlock);
		final Point2D mp1a = translate1.getTranslated(p1);
		final Point2D mp2b = translate2.getTranslated(p2);
		final double middle = (mp1a.getY() + mp2b.getY()) / 2.0;
		snake.addPoint(mp1a);
		snake.addPoint(mp1a.getX(), middle);
		snake.addPoint(mp2b.getX(), middle);
		snake.addPoint(mp2b);
		ug.draw(snake);

		// final Snake small = new Snake(color, Arrows.asToDown());
		// small.addPoint(mp2b.getX(), middle);
		// small.addPoint(mp2b);
		// ug.draw(small);

	}

}
