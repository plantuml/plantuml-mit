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
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SheetBlock1 extends AbstractTextBlock implements TextBlock, Atom, Stencil {

	public List<Atom> splitInTwo(StringBounder stringBounder, double width) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	private final Sheet sheet;
	private List<Stripe> stripes;
	private Map<Stripe, Double> heights;
	private Map<Stripe, Double> widths;
	private Map<Atom, Position> positions;
	private MinMax minMax;
	private final LineBreakStrategy maxWidth;
	private final double padding;

	public SheetBlock1(Sheet sheet, LineBreakStrategy maxWidth, double padding) {
		this.sheet = sheet;
		this.maxWidth = maxWidth;
		this.padding = padding;
		if (maxWidth == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		return sheet.toString();
	}

	public HorizontalAlignment getCellAlignment() {
		if (stripes.size() != 1) {
			return HorizontalAlignment.LEFT;
		}
		final Stripe simple = stripes.get(0);
		if (simple instanceof StripeSimple) {
			return ((StripeSimple) simple).getCellAlignment();
		}
		return HorizontalAlignment.LEFT;
	}

	private void initMap(StringBounder stringBounder) {
		if (positions != null) {
			return;
		}
		stripes = new ArrayList<Stripe>();
		for (Stripe stripe : sheet) {
			stripes.addAll(new Fission(stripe, maxWidth).getSplitted(stringBounder));
		}
		positions = new LinkedHashMap<Atom, Position>();
		widths = new LinkedHashMap<Stripe, Double>();
		heights = new LinkedHashMap<Stripe, Double>();
		minMax = MinMax.getEmpty(true);
		double y = 0;
		for (Stripe stripe : stripes) {
			if (stripe.getAtoms().size() == 0) {
				continue;
			}
			final Sea sea = new Sea(stringBounder);
			for (Atom atom : stripe.getAtoms()) {
				sea.add(atom);
			}
			sea.doAlign();
			sea.translateMinYto(y);
			sea.exportAllPositions(positions);
			final double width = sea.getWidth();
			widths.put(stripe, width);
			minMax = sea.update(minMax);
			final double height = sea.getHeight();
			heights.put(stripe, height);
			y += height;
		}
		final int coef;
		if (sheet.getHorizontalAlignment() == HorizontalAlignment.CENTER) {
			coef = 2;
		} else if (sheet.getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
			coef = 1;
		} else {
			coef = 0;
		}
		if (coef != 0) {
			double maxWidth = 0;
			for (Double v : widths.values()) {
				if (v > maxWidth) {
					maxWidth = v;
				}
			}
			for (Map.Entry<Stripe, Double> ent : widths.entrySet()) {
				final double diff = maxWidth - ent.getValue();
				if (diff > 0) {
					for (Atom atom : ent.getKey().getAtoms()) {
						final Position pos = positions.get(atom);
						positions.put(atom, pos.translateX(diff / coef));
					}
				}

			}

		}
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		initMap(stringBounder);
		return Dimension2DDouble.delta(minMax.getDimension(), 2 * padding);
	}

	@Override
	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

	public void drawU(UGraphic ug) {
		initMap(ug.getStringBounder());
		if (padding > 0) {
			ug = ug.apply(new UTranslate(padding, padding));
		}
		for (Stripe stripe : stripes) {
			for (Atom atom : stripe.getAtoms()) {
				final Position position = positions.get(atom);
				atom.drawU(position.translate(ug));
				// position.drawDebug(ug);
			}
		}
	}

	public double getStartingAltitude(StringBounder stringBounder) {
		return 0;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

}
