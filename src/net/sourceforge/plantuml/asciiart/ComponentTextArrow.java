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
package net.sourceforge.plantuml.asciiart;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class ComponentTextArrow extends AbstractComponentText implements ArrowComponent {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;
	private final ArrowConfiguration config;
	private final int maxAsciiMessageLength;

	public ComponentTextArrow(ComponentType type, ArrowConfiguration config, Display stringsToDisplay,
			FileFormat fileFormat, int maxAsciiMessageLength) {
		this.maxAsciiMessageLength = maxAsciiMessageLength;
		this.type = type;
		this.config = config;
		this.stringsToDisplay = clean(stringsToDisplay);
		this.fileFormat = fileFormat;
	}

	private static Display clean(Display orig) {
		if (orig.size() == 0 || orig.get(0) instanceof MessageNumber == false) {
			return orig;
		}
		Display result = Display.empty();
		for (int i = 0; i < orig.size(); i++) {
			CharSequence element = orig.get(i);
			if (i == 1) {
				element = removeTag(orig.get(0).toString()) + " " + element;
			}
			if (i != 0) {
				result = result.add(element);
			}
		}
		return result;
	}

	private static String removeTag(String s) {
		return s.replaceAll("\\<[^<>]+\\>", "");
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		if (config.isHidden()) {
			return;
		}
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight();
		final int textWidth = StringUtils.getWcWidth(stringsToDisplay);

		final int yarrow = height - 2;
		charArea.drawHLine(fileFormat == FileFormat.UTXT ? '\u2500' : '-', yarrow, 1, width);
		if (config.isDotted()) {
			for (int i = 1; i < width; i += 2) {
				charArea.drawChar(' ', i, yarrow);
			}
		}

		if (config.getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			charArea.drawChar('>', width - 1, yarrow);
		} else if (config.getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
			charArea.drawChar('<', 1, yarrow);
		} else if (config.getArrowDirection() == ArrowDirection.BOTH_DIRECTION) {
			charArea.drawChar('>', width - 1, yarrow);
			charArea.drawChar('<', 1, yarrow);
		} else {
			throw new UnsupportedOperationException();
		}
		// final int position = Math.max(0, (width - textWidth) / 2);
		charArea.drawStringsLR(stringsToDisplay.as(), (width - textWidth) / 2, 0);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + 2;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		final int width = StringUtils.getWcWidth(stringsToDisplay) + 2;
		if (maxAsciiMessageLength > 0) {
			return Math.min(maxAsciiMessageLength, width);
		}
		return width;
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		return new Point2D.Double(0, 0);
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		return new Point2D.Double(0, 0);
	}

	public double getPaddingY() {
		throw new UnsupportedOperationException();
	}

	public double getYPoint(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
