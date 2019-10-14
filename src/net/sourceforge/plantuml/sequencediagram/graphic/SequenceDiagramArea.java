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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.utils.MathUtils;

public class SequenceDiagramArea {

	private final double sequenceWidth;
	private final double sequenceHeight;

	private double headerWidth;
	private double headerHeight;
	private double headerMargin;

	private double titleWidth;
	private double titleHeight;

	private double captionWidth;
	private double captionHeight;

	private double footerWidth;
	private double footerHeight;
	private double footerMargin;

	public SequenceDiagramArea(double width, double height) {
		this.sequenceWidth = width;
		this.sequenceHeight = height;
	}

	public void setTitleArea(double width, double height) {
		this.titleWidth = width;
		this.titleHeight = height;
	}

	private void setCaptionArea(double width, double height) {
		this.captionWidth = width;
		this.captionHeight = height;
	}

	public void setCaptionArea(Dimension2D dim) {
		setCaptionArea(dim.getWidth(), dim.getHeight());
	}

	public void setHeaderArea(double headerWidth, double headerHeight, double headerMargin) {
		this.headerWidth = headerWidth;
		this.headerHeight = headerHeight;
		this.headerMargin = headerMargin;
	}

	public void setFooterArea(double footerWidth, double footerHeight, double footerMargin) {
		this.footerWidth = footerWidth;
		this.footerHeight = footerHeight;
		this.footerMargin = footerMargin;
	}

	public double getWidth() {
		return MathUtils.max(sequenceWidth, headerWidth, titleWidth, footerWidth, captionWidth);
	}

	public double getHeight() {
		return sequenceHeight + headerHeight + headerMargin + titleHeight + footerMargin + footerHeight + captionHeight;
	}

	public double getTitleX() {
		return (getWidth() - titleWidth) / 2;
	}

	public double getTitleY() {
		return headerHeight + headerMargin;
	}

	public double getHeaderHeightMargin() {
		return headerHeight + headerMargin;
	}

	public double getCaptionX() {
		return (getWidth() - captionWidth) / 2;
	}

	public double getCaptionY() {
		return sequenceHeight + headerHeight + headerMargin + titleHeight;
	}

	public double getSequenceAreaX() {
		return (getWidth() - sequenceWidth) / 2;
	}

	public double getSequenceAreaY() {
		return getTitleY() + titleHeight;
	}

	public double getHeaderY() {
		return 0;
	}

	public double getFooterY() {
		return sequenceHeight + headerHeight + headerMargin + titleHeight + footerMargin + captionHeight;
	}

	public double getFooterX(HorizontalAlignment align) {
		if (align == HorizontalAlignment.LEFT) {
			return 0;
		}
		if (align == HorizontalAlignment.RIGHT) {
			return getWidth() - footerWidth;
		}
		if (align == HorizontalAlignment.CENTER) {
			return (getWidth() - footerWidth) / 2;
		}
		throw new IllegalStateException();
	}

	public double getHeaderX(HorizontalAlignment align) {
		if (align == HorizontalAlignment.LEFT) {
			return 0;
		}
		if (align == HorizontalAlignment.RIGHT) {
			return getWidth() - headerWidth;
		}
		if (align == HorizontalAlignment.CENTER) {
			return (getWidth() - headerWidth) / 2;
		}
		throw new IllegalStateException();
	}

	public void initFooter(PngTitler pngTitler, StringBounder stringBounder) {
		final Dimension2D dim = pngTitler.getTextDimension(stringBounder);
		if (dim != null) {
			if (SkinParam.USE_STYLES())
				setFooterArea(dim.getWidth(), dim.getHeight(), 0);
			else
				setFooterArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	public void initHeader(PngTitler pngTitler, StringBounder stringBounder) {
		final Dimension2D dim = pngTitler.getTextDimension(stringBounder);
		if (dim != null) {
			if (SkinParam.USE_STYLES())
				setHeaderArea(dim.getWidth(), dim.getHeight(), 0);
			else
				setHeaderArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

}
