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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimeConstraint {

	private final TimeTick tick1;
	private final TimeTick tick2;
	private final Display label;

	public TimeConstraint(TimeTick tick1, TimeTick tick2, String label) {
		this.tick1 = tick1;
		this.tick2 = tick2;
		this.label = Display.getWithNewlines(label);
	}

	public final TimeTick getTick1() {
		return tick1;
	}

	public final TimeTick getTick2() {
		return tick2;
	}

	public final Display getLabel() {
		return label;
	}

	private TextBlock getTextBlock(Display display, ISkinParam skinParam) {
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(14);
		return new FontConfiguration(font, HtmlColorUtils.BLACK, HtmlColorUtils.BLUE, false);
	}

	public void drawU(UGraphic ug, TimingRuler ruler, ISkinParam skinParam) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.RED)).apply(new UChangeBackColor(HtmlColorUtils.RED));
		final double x1 = ruler.getPosInPixel(tick1);
		final double x2 = ruler.getPosInPixel(tick2);
		final ULine line = new ULine(x2 - x1, 0);

		ug = ug.apply(new UTranslate(x1, 0));
		ug.draw(line);

		ug.draw(getPolygon(-Math.PI / 2, new Point2D.Double(0, 0)));
		ug.draw(getPolygon(Math.PI / 2, new Point2D.Double(x2 - x1, 0)));

		final TextBlock text = getTextBlock(label, skinParam);
		final Dimension2D dimText = text.calculateDimension(ug.getStringBounder());
		final double x = (x2 - x1 - dimText.getWidth()) / 2;
		final UTranslate tr = new UTranslate(x, -5 - dimText.getHeight());
		text.drawU(ug.apply(tr));

	}

	private UPolygon getPolygon(final double angle, final Point2D end) {
		final double delta = 20.0 * Math.PI / 180.0;
		final Point2D pt1 = TimeArrow.onCircle(end, angle + delta);
		final Point2D pt2 = TimeArrow.onCircle(end, angle - delta);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(pt1.getX(), pt1.getY());
		polygon.addPoint(pt2.getX(), pt2.getY());
		polygon.addPoint(end.getX(), end.getY());

		return polygon;
	}

}
