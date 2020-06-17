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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMargedRight;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileFactoryDelegatorAssembly extends FtileFactoryDelegator {

	public FtileFactoryDelegatorAssembly(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile assembly(final Ftile tile1, final Ftile tile2) {
		double height = 35;
		final TextBlock textBlock = getTextBlock(getInLinkRenderingDisplay(tile2));
		final StringBounder stringBounder = getStringBounder();
		if (textBlock != null) {
			height += textBlock.calculateDimension(stringBounder).getHeight();
		}
		// final Ftile space = new FtileEmpty(getFactory().shadowing(), 1, height);
		final Ftile tile1andSpace = FtileUtils.addBottom(tile1, height);
		Ftile result = super.assembly(tile1andSpace, tile2);
		final FtileGeometry geo = tile1.calculateDimension(stringBounder);
		if (geo.hasPointOut() == false) {
			return result;
		}
		final UTranslate translate1 = result.getTranslateFor(tile1andSpace, stringBounder);
		final Point2D p1 = geo.translate(translate1).getPointOut();
		final UTranslate translate2 = result.getTranslateFor(tile2, stringBounder);
		final Point2D p2 = tile2.calculateDimension(stringBounder).translate(translate2).getPointIn();

		final Rainbow color = getInLinkRenderingColor(tile2);

		final ConnectionVerticalDown connection = new ConnectionVerticalDown(tile1, tile2, p1, p2, color, textBlock);
		result = FtileUtils.addConnection(result, connection);
		if (textBlock != null) {
			final FtileGeometry dim = result.calculateDimension(stringBounder);
			final double width = dim.getWidth();
			// System.err.println("width=" + width);
			// System.err.println("p1=" + p1);
			// System.err.println("p2=" + p2);
			final double maxX = connection.getMaxX(stringBounder);
			// System.err.println("FtileFactoryDelegatorAssembly dim=" + dim);
			// System.err.println("maxX=" + maxX);
			final double needed = (maxX - width) * 2;
			// result = new FtileMinWidth(result, needed);
			if (width < maxX) {
				result = new FtileMargedRight(result, maxX);
			}
			// System.err.println("FtileFactoryDelegatorAssembly result=" + result.calculateDimension(stringBounder));
		}
		return result;
	}

	private final Rose rose = new Rose();

}
