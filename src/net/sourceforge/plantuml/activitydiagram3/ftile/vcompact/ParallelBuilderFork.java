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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.ConnectionTranslatable;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBlackBlock;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ParallelBuilderFork extends AbstractParallelFtilesBuilder {

	private final String label;
	private final Swimlane in;
	private final Swimlane out;

	public ParallelBuilderFork(ISkinParam skinParam, StringBounder stringBounder, final List<Ftile> list, Ftile inner,
			String label, Swimlane in, Swimlane out) {
		super(skinParam, stringBounder, list, inner);
		this.label = label;
		this.in = in;
		this.out = out;
	}

	@Override
	protected Swimlane swimlaneOutForStep2() {
		return out;
	}

	@Override
	protected Ftile doStep1() {
		Ftile result = getMiddle();
		final List<Connection> conns = new ArrayList<Connection>();
		final Swimlane swimlaneBlack = in;
		final Ftile black = new FtileBlackBlock(skinParam(), getRose()
				.getHtmlColor(skinParam(), ColorParam.activityBar), swimlaneBlack);
		double x = 0;
		for (Ftile tmp : getList()) {
			final Dimension2D dim = tmp.calculateDimension(getStringBounder());
			final Rainbow def;
			if (SkinParam.USE_STYLES()) {
				Style style = getDefaultStyleDefinition().getMergedStyle(skinParam().getCurrentStyleBuilder());
				def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			} else {
				def = Rainbow.build(skinParam());
			}
			final Rainbow rainbow = tmp.getInLinkRendering().getRainbow(def);
			conns.add(new ConnectionIn(black, tmp, x, rainbow));
			x += dim.getWidth();
		}

		result = FtileUtils.addConnection(result, conns);
		((FtileBlackBlock) black).setBlackBlockDimension(result.calculateDimension(getStringBounder()).getWidth(),
				barHeight);

		return new FtileAssemblySimple(black, result);
	}

	@Override
	protected Ftile doStep2(Ftile result) {
		final Swimlane swimlaneBlack = out;
		final Ftile out = new FtileBlackBlock(skinParam(), getRose().getHtmlColor(skinParam(), ColorParam.activityBar),
				swimlaneBlack);
		((FtileBlackBlock) out).setBlackBlockDimension(result.calculateDimension(getStringBounder()).getWidth(),
				barHeight);
		if (label != null) {
			((FtileBlackBlock) out).setLabel(getTextBlock(Display.getWithNewlines(label)));
		}
		result = new FtileAssemblySimple(result, out);
		final List<Connection> conns = new ArrayList<Connection>();
		double x = 0;
		for (Ftile tmp : getList()) {
			final UTranslate translate0 = new UTranslate(0, barHeight);
			final Dimension2D dim = tmp.calculateDimension(getStringBounder());
			final Rainbow def;
			if (SkinParam.USE_STYLES()) {
				Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam().getCurrentStyleBuilder());
				def = Rainbow.build(style, skinParam().getIHtmlColorSet());
			} else {
				def = Rainbow.build(skinParam());
			}
			final Rainbow rainbow = tmp.getOutLinkRendering().getRainbow(def);
			conns.add(new ConnectionOut(translate0, tmp, out, x, rainbow, getHeightOfMiddle()));
			x += dim.getWidth();
		}
		result = FtileUtils.addConnection(result, conns);
		return result;
	}

	class ConnectionIn extends AbstractConnection implements ConnectionTranslatable {

		private final double x;
		private final Rainbow arrowColor;
		private final Display label;

		public ConnectionIn(Ftile ftile1, Ftile ftile2, double x, Rainbow arrowColor) {
			super(ftile1, ftile2);
			label = ftile2.getInLinkRendering().getDisplay();
			this.x = x;
			this.arrowColor = arrowColor;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final FtileGeometry geo = getFtile2().calculateDimension(getStringBounder());
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			snake.addPoint(geo.getLeft(), 0);
			snake.addPoint(geo.getLeft(), geo.getInY());
			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(new UTranslate(x, 0));
			final FtileGeometry geo = getFtile2().calculateDimension(getStringBounder());
			final Point2D p1 = new Point2D.Double(geo.getLeft(), 0);
			final Point2D p2 = new Point2D.Double(geo.getLeft(), geo.getInY());

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = mp1a.getY() + 4;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			snake.setIgnoreForCompression(true);
			ug.draw(snake);
		}
	}

	class ConnectionOut extends AbstractConnection implements ConnectionTranslatable {

		private final double x;
		private final Rainbow arrowColor;
		private final double height;
		private final Display label;
		private final UTranslate translate0;

		public ConnectionOut(UTranslate translate0, Ftile ftile1, Ftile ftile2, double x, Rainbow arrowColor,
				double height) {
			super(ftile1, ftile2);
			this.translate0 = translate0;
			this.label = ftile1.getOutLinkRendering().getDisplay();
			this.x = x;
			this.arrowColor = arrowColor;
			this.height = height;
		}

		public void drawU(UGraphic ug) {
			ug = ug.apply(new UTranslate(x, 0));
			final FtileGeometry geo = getFtile1().calculateDimension(getStringBounder());
			if (geo.hasPointOut() == false) {
				return;
			}
			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D p1 = translate0.getTranslated(new Point2D.Double(geo.getLeft(), geo.getOutY()));
			final Point2D p2 = translate0.getTranslated(new Point2D.Double(geo.getLeft(), height));
			snake.addPoint(p1);
			snake.addPoint(p2);
			ug.draw(snake);
		}

		public void drawTranslate(UGraphic ug, UTranslate translate1, UTranslate translate2) {
			ug = ug.apply(new UTranslate(x, 0));
			final FtileGeometry geo = getFtile1().calculateDimension(getStringBounder());
			if (geo.hasPointOut() == false) {
				return;
			}
			final Point2D p1 = translate0.getTranslated(new Point2D.Double(geo.getLeft(), geo.getOutY()));
			final Point2D p2 = translate0.getTranslated(new Point2D.Double(geo.getLeft(), height));

			final Snake snake = new Snake(arrowHorizontalAlignment(), arrowColor, Arrows.asToDown());
			if (Display.isNull(label) == false) {
				snake.setLabel(getTextBlock(label));
			}
			final Point2D mp1a = translate1.getTranslated(p1);
			final Point2D mp2b = translate2.getTranslated(p2);
			final double middle = mp2b.getY() - 14;
			snake.addPoint(mp1a);
			snake.addPoint(mp1a.getX(), middle);
			snake.addPoint(mp2b.getX(), middle);
			snake.addPoint(mp2b);
			snake.setIgnoreForCompression(true);
			ug.draw(snake);
		}

	}

}
