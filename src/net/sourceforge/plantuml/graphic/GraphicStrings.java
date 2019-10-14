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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.Margins;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GraphicStrings extends AbstractTextBlock implements IEntityImage {

	private final double margin = 5;

	private final HtmlColor background;

	private final static HtmlColor hyperlinkColor = HtmlColorUtils.BLUE;

	private final static boolean useUnderlineForHyperlink = true;

	private final List<String> strings;

	private final BufferedImage image;
	private final double imagePadding = 30;

	private final GraphicPosition position;

	private final FontConfiguration fontConfiguration;

	public static IEntityImage createForError(List<String> strings, boolean useRed) {
		return new GraphicStrings(strings, sansSerif14(getForeColor(useRed)).bold(), getBackColor(useRed), null, null,
				CreoleMode.NO_CREOLE);
	}

	private static HtmlColor getForeColor(boolean useRed) {
		if (useRed) {
			return HtmlColorUtils.BLACK;
		}
		return HtmlColorUtils.MY_GREEN;
	}

	private static HtmlColor getBackColor(boolean useRed) {
		if (useRed) {
			return HtmlColorUtils.RED_LIGHT;
		}
		return HtmlColorUtils.BLACK;
	}

	public static TextBlockBackcolored createGreenOnBlackMonospaced(List<String> strings) {
		return new GraphicStrings(strings, monospaced14(HtmlColorUtils.GREEN), HtmlColorUtils.BLACK, null, null,
				CreoleMode.SIMPLE_LINE);
	}

	public static TextBlockBackcolored createBlackOnWhite(List<String> strings) {
		return new GraphicStrings(strings, sansSerif12(HtmlColorUtils.BLACK), HtmlColorUtils.WHITE, null, null,
				CreoleMode.FULL);
	}

	public static TextBlockBackcolored createBlackOnWhiteMonospaced(List<String> strings) {
		return new GraphicStrings(strings, monospaced14(HtmlColorUtils.BLACK), HtmlColorUtils.WHITE, null, null,
				CreoleMode.FULL);
	}

	public static TextBlockBackcolored createBlackOnWhite(List<String> strings, BufferedImage image,
			GraphicPosition position) {
		return new GraphicStrings(strings, sansSerif12(HtmlColorUtils.BLACK), HtmlColorUtils.WHITE, image, position,
				CreoleMode.FULL);
	}

	private static FontConfiguration sansSerif12(HtmlColor color) {
		return new FontConfiguration(UFont.sansSerif(12), color, hyperlinkColor, useUnderlineForHyperlink);
	}

	public static FontConfiguration sansSerif14(HtmlColor color) {
		return new FontConfiguration(UFont.sansSerif(14), color, hyperlinkColor, useUnderlineForHyperlink);
	}

	private static FontConfiguration monospaced14(HtmlColor color) {
		return new FontConfiguration(UFont.monospaced(14), color, hyperlinkColor, useUnderlineForHyperlink);
	}

	private final CreoleMode mode;

	private GraphicStrings(List<String> strings, FontConfiguration fontConfiguration, HtmlColor background,
			BufferedImage image, GraphicPosition position, CreoleMode mode) {
		this.strings = strings;
		this.background = background;
		this.image = image;
		this.position = position;
		this.mode = mode;
		this.fontConfiguration = fontConfiguration;

	}

	private TextBlock getTextBlock() {
		final Display display = Display.create(strings);
		if (mode == CreoleMode.NO_CREOLE) {
			return new TextBlockRaw(strings, fontConfiguration);

		} else {
			return display.create(fontConfiguration, HorizontalAlignment.LEFT, new SpriteContainerEmpty(), mode);
		}
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UTranslate(margin, margin));
		final Dimension2D size = calculateDimensionInternal(ug.getStringBounder());
		getTextBlock().drawU(ug.apply(new UChangeColor(fontConfiguration.getColor())));

		if (image != null) {
			if (position == GraphicPosition.BOTTOM) {
				ug.apply(new UTranslate((size.getWidth() - image.getWidth()) / 2, size.getHeight() - image.getHeight()))
						.draw(new UImage(image));
			} else if (position == GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT) {
				ug.apply(new UTranslate(size.getWidth() - image.getWidth(), size.getHeight() - image.getHeight()))
						.draw(new UImage(image));
			} else if (position == GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT) {
				ug.apply(new UTranslate(size.getWidth() - image.getWidth() - 1, 1)).draw(new UImage(image));
			}
		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.delta(calculateDimensionInternal(stringBounder), 2 * margin);
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D dim = getTextBlock().calculateDimension(stringBounder);
		if (image != null) {
			if (position == GraphicPosition.BOTTOM) {
				dim = new Dimension2DDouble(dim.getWidth(), dim.getHeight() + image.getHeight());
			} else if (position == GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT) {
				dim = new Dimension2DDouble(dim.getWidth() + imagePadding + image.getWidth(), dim.getHeight());
			} else if (position == GraphicPosition.BACKGROUND_CORNER_TOP_RIGHT) {
				dim = new Dimension2DDouble(dim.getWidth() + imagePadding + image.getWidth(), dim.getHeight());
			}
		}
		return dim;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public HtmlColor getBackcolor() {
		return background;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public boolean isHidden() {
		return false;
	}

	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}