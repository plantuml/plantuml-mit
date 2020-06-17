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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicNo;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UParamNull;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Footprint {

	private final StringBounder stringBounder;

	public Footprint(StringBounder stringBounder) {
		this.stringBounder = stringBounder;

	}

	class MyUGraphic extends UGraphicNo implements UGraphic {

		private final UTranslate translate;
		private final List<Point2D.Double> all;

		public double dpiFactor() {
			return 1;
		}

		private MyUGraphic(List<Point2D.Double> all, UTranslate translate) {
			this.all = all;
			this.translate = translate;
		}

		public boolean matchesProperty(String propertyName) {
			return false;
		}

		public MyUGraphic() {
			this(new ArrayList<Point2D.Double>(), new UTranslate());
		}

		public UGraphic apply(UChange change) {
			if (change instanceof UTranslate) {
				return new MyUGraphic(all, translate.compose((UTranslate) change));
			} else if (change instanceof UStroke || change instanceof HColor) {
				return new MyUGraphic(all, translate);
			}
			throw new UnsupportedOperationException();
		}

		public StringBounder getStringBounder() {
			return stringBounder;
		}

		public UParam getParam() {
			return new UParamNull();
		}

		public void draw(UShape shape) {
			final double x = translate.getDx();
			final double y = translate.getDy();
			if (shape instanceof UText) {
				drawText(x, y, (UText) shape);
			} else if (shape instanceof UHorizontalLine) {
				// Definitively a Horizontal line
//				line.drawTitleInternalForFootprint(this, x, y);
			} else if (shape instanceof ULine) {
				// Probably a Horizontal line
			} else if (shape instanceof UImage) {
				drawImage(x, y, (UImage) shape);
			} else if (shape instanceof UPath) {
				drawPath(x, y, (UPath) shape);
			} else {
				throw new UnsupportedOperationException(shape.getClass().toString());
			}
		}

		public ColorMapper getColorMapper() {
			return new ColorMapperIdentity();
		}

		private void addPoint(double x, double y) {
			all.add(new Point2D.Double(x, y));
		}

		private void drawText(double x, double y, UText text) {
			final Dimension2D dim = stringBounder.calculateDimension(text.getFontConfiguration().getFont(),
					text.getText());
			y -= dim.getHeight() - 1.5;
			addPoint(x, y);
			addPoint(x, y + dim.getHeight());
			addPoint(x + dim.getWidth(), y);
			addPoint(x + dim.getWidth(), y + dim.getHeight());
		}

		private void drawImage(double x, double y, UImage image) {
			addPoint(x, y);
			addPoint(x, y + image.getHeight());
			addPoint(x + image.getWidth(), y);
			addPoint(x + image.getWidth(), y + image.getHeight());
		}

		private void drawPath(double x, double y, UPath path) {
			addPoint(x + path.getMinX(), y + path.getMinY());
			addPoint(x + path.getMaxX(), y + path.getMaxY());
		}

		public void flushUg() {
		}

	}

	public ContainingEllipse getEllipse(UDrawable drawable, double alpha) {
		final MyUGraphic ug = new MyUGraphic();
		drawable.drawU(ug);
		final List<Point2D.Double> all = ug.all;
		final ContainingEllipse circle = new ContainingEllipse(alpha);
		for (Point2D pt : all) {
			circle.append(pt);
		}
		return circle;
	}

	// public void drawDebug(UGraphic ug, double dx, double dy, TextBlock text) {
	// final MyUGraphic mug = new MyUGraphic();
	// text.drawU(mug, dx, dy);
	// for (Point2D pt : mug.all) {
	// ug.draw(pt.getX(), pt.getY(), new URectangle(1, 1));
	// }
	//
	// }

}
