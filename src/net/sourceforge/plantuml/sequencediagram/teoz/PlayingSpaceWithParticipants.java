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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PlayingSpaceWithParticipants extends AbstractTextBlock implements TextBlock {

	private final PlayingSpace playingSpace;
	private Dimension2D cacheDimension;
	private double ymin;
	private double ymax;

	public PlayingSpaceWithParticipants(PlayingSpace playingSpace) {
		if (playingSpace == null) {
			throw new IllegalArgumentException();
		}
		this.playingSpace = playingSpace;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		if (cacheDimension == null) {
			final double width = playingSpace.getMaxX(stringBounder).getCurrentValue()
					- playingSpace.getMinX(stringBounder).getCurrentValue();

			final int factor = playingSpace.isShowFootbox() ? 2 : 1;
			final double height = playingSpace.getPreferredHeight(stringBounder) + factor
					* playingSpace.getLivingSpaces().getHeadHeight(stringBounder);

			cacheDimension = new Dimension2DDouble(width, height);
		}
		return cacheDimension;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		final double height = playingSpace.getPreferredHeight(stringBounder);
		final LivingSpaces livingSpaces = playingSpace.getLivingSpaces();

		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		if (ymax == 0) {
			playingSpace.drawBackground(ug.apply(new UTranslate(0, headHeight)));
		} else {
			final UClip clip = new UClip(-1000, ymin, Double.MAX_VALUE, ymax - ymin + 1);
			playingSpace.drawBackground(ug.apply(new UTranslate(0, headHeight)).apply(clip));
		}

		livingSpaces.drawLifeLines(ug.apply(new UTranslate(0, headHeight)), height, context);

		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);
		if (playingSpace.isShowFootbox()) {
			livingSpaces.drawHeads(ug.apply(new UTranslate(0, height + headHeight)), context, VerticalAlignment.TOP);
		}
		if (ymax == 0) {
			playingSpace.drawForeground(ug.apply(new UTranslate(0, headHeight)));
		} else {
			final UClip clip = new UClip(-1000, ymin, Double.MAX_VALUE, ymax - ymin + 1);
			// playingSpace.drawForeground(new UGraphicNewpages(ug.apply(new UTranslate(0, headHeight)), ymin, ymax));
			playingSpace.drawForeground(ug.apply(new UTranslate(0, headHeight)).apply(clip));
		}
		// drawNewPages(ug.apply(new UTranslate(0, headHeight)));
	}

	public Real getMinX(StringBounder stringBounder) {
		return playingSpace.getMinX(stringBounder);
	}

	public int getNbPages() {
		return playingSpace.getNbPages();
	}

	public void setIndex(int index) {
		final List<Double> yNewPages = playingSpace.yNewPages();
		this.ymin = yNewPages.get(index);
		this.ymax = yNewPages.get(index + 1);
	}

	private List<Double> yNewPages() {
		return playingSpace.yNewPages();
	}

	private void drawNewPages(UGraphic ug) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLUE));
		for (Double change : yNewPages()) {
			if (change == 0 || change == Double.MAX_VALUE) {
				continue;
			}
			ug.apply(new UTranslate(0, change)).draw(new ULine(100, 0));
		}
	}

}
