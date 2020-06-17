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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorBackground;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class SymbolContext {

	private final HColor backColor;
	private final HColor foreColor;
	private final UStroke stroke;
	private final double deltaShadow;
	private final double roundCorner;
	private final double diagonalCorner;

	private SymbolContext(HColor backColor, HColor foreColor, UStroke stroke, double deltaShadow, double roundCorner,
			double diagonalCorner) {
		this.backColor = backColor;
		this.foreColor = foreColor;
		this.stroke = stroke;
		this.deltaShadow = deltaShadow;
		this.roundCorner = roundCorner;
		this.diagonalCorner = diagonalCorner;
	}

	@Override
	public String toString() {
		return super.toString() + " backColor=" + backColor + " foreColor=" + foreColor;
	}

	final public UGraphic apply(UGraphic ug) {
		ug = applyColors(ug);
		ug = applyStroke(ug);
		return ug;
	}

	public UGraphic applyColors(UGraphic ug) {
		ug = ug.apply(foreColor);
		if (backColor == null) {
			ug = ug.apply(new HColorNone().bg());
		} else {
			ug = ug.apply(backColor.bg());
		}
		return ug;
	}

	public UGraphic applyStroke(UGraphic ug) {
		return ug.apply(stroke);
	}

	public SymbolContext transparentBackColorToNull() {
		if (backColor instanceof HColorBackground) {
			return new SymbolContext(((HColorBackground) backColor).getNull(), foreColor, stroke, deltaShadow,
					roundCorner, diagonalCorner);
		}
		return this;
	}

	public SymbolContext(HColor backColor, HColor foreColor) {
		this(backColor, foreColor, new UStroke(), 0, 0, 0);
	}

	public SymbolContext withShadow(double deltaShadow2) {
		return new SymbolContext(backColor, foreColor, stroke, deltaShadow2, roundCorner, diagonalCorner);
	}

	public SymbolContext withDeltaShadow(double deltaShadow2) {
		return new SymbolContext(backColor, foreColor, stroke, deltaShadow2, roundCorner, diagonalCorner);
	}

	public SymbolContext withStroke(UStroke newStroke) {
		return new SymbolContext(backColor, foreColor, newStroke, deltaShadow, roundCorner, diagonalCorner);
	}

	public SymbolContext withBackColor(HColor backColor) {
		return new SymbolContext(backColor, foreColor, stroke, deltaShadow, roundCorner, diagonalCorner);
	}

	public SymbolContext withForeColor(HColor foreColor) {
		return new SymbolContext(backColor, foreColor, stroke, deltaShadow, roundCorner, diagonalCorner);
	}

	public SymbolContext withCorner(double roundCorner, double diagonalCorner) {
		return new SymbolContext(backColor, foreColor, stroke, deltaShadow, roundCorner, diagonalCorner);
	}

	public HColor getBackColor() {
		return backColor;
	}

	public HColor getForeColor() {
		return foreColor;
	}

	public UStroke getStroke() {
		return stroke;
	}

	public boolean isShadowing() {
		return deltaShadow > 0;
	}

	public double getDeltaShadow() {
		return deltaShadow;
	}

	public double getRoundCorner() {
		return roundCorner;
	}

	public double getDiagonalCorner() {
		return diagonalCorner;
	}

}
