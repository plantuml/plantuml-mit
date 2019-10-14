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
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.HtmlColor;

public abstract class AbstractCommonUGraphic implements UGraphic {

	private UStroke stroke = new UStroke();
	private UPattern pattern = UPattern.FULL;
	private boolean hidden = false;
	private HtmlColor backColor = null;
	private HtmlColor color = null;

	private UTranslate translate = new UTranslate();

	private final ColorMapper colorMapper;
	private UClip clip;
	private double scale = 1;

	public double dpiFactor() {
		return 1;
	}

	public UGraphic apply(UChange change) {
		final AbstractCommonUGraphic copy = copyUGraphic();
		if (change instanceof UTranslate) {
			copy.translate = ((UTranslate) change).scaled(scale).compose(copy.translate);
		} else if (change instanceof UClip) {
			copy.clip = (UClip) change;
			copy.clip = copy.clip.translate(getTranslateX(), getTranslateY());
		} else if (change instanceof UStroke) {
			copy.stroke = (UStroke) change;
		} else if (change instanceof UPattern) {
			copy.pattern = (UPattern) change;
		} else if (change instanceof UHidden) {
			copy.hidden = change == UHidden.HIDDEN;
		} else if (change instanceof UChangeBackColor) {
			copy.backColor = ((UChangeBackColor) change).getBackColor();
		} else if (change instanceof UChangeColor) {
			copy.color = ((UChangeColor) change).getColor();
		} else if (change instanceof UScale) {
			final double factor = ((UScale) change).getScale();
			copy.scale = scale * factor;
		}
		return copy;
	}

	final public UClip getClip() {
		return clip;
	}

	public AbstractCommonUGraphic(ColorMapper colorMapper) {
		this.colorMapper = colorMapper;
	}

	protected AbstractCommonUGraphic(AbstractCommonUGraphic other) {
		this.colorMapper = other.colorMapper;
		this.translate = other.translate;
		this.clip = other.clip;

		this.stroke = other.stroke;
		this.pattern = other.pattern;
		this.hidden = other.hidden;
		this.color = other.color;
		this.backColor = other.backColor;
		this.scale = other.scale;
	}

	protected abstract AbstractCommonUGraphic copyUGraphic();

	final public UParam getParam() {
		return new UParam() {

			public boolean isHidden() {
				return hidden;
			}

			public UStroke getStroke() {
				return stroke;
			}

			public HtmlColor getColor() {
				return color;
			}

			public HtmlColor getBackcolor() {
				return backColor;
			}

			public UPattern getPattern() {
				return pattern;
			}

			public double getScale() {
				return scale;
			}
		};
	}

	final protected double getTranslateX() {
		return translate.getDx();
	}

	final protected double getTranslateY() {
		return translate.getDy();
	}

	final public ColorMapper getColorMapper() {
		return new ColorMapperTransparentWrapper(colorMapper);
	}

	public void flushUg() {

	}

	public boolean matchesProperty(String propertyName) {
		return false;
	}

}
