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
package net.sourceforge.plantuml.hector2.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.hector2.MinMax;
import net.sourceforge.plantuml.hector2.layering.Layer;
import net.sourceforge.plantuml.hector2.mpos.Distribution;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Foo2 extends AbstractTextBlock implements TextBlock {

	private final Distribution distribution;
	private final CucaDiagram diagram;

	public Foo2(Distribution distribution, CucaDiagram diagram) {
		this.distribution = distribution;
		this.diagram = diagram;
	}

	public Dimension2D getMaxCellDimension(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (Layer layer : distribution.getLayers()) {
			final Dimension2D dim = Foo1.getMaxCellDimension(stringBounder, layer, diagram);
			result = Dimension2DDouble.max(result, dim);
		}
		return result;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D cell = getMaxCellDimension(stringBounder);
		final MinMax longitudes = distribution.getMinMaxLongitudes();
		final double width = (longitudes.getDiff() + 2) * cell.getWidth() / 2;
		final double height = cell.getHeight() * distribution.getNbLayers();
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D cell = getMaxCellDimension(stringBounder);
		for (Layer layer : distribution.getLayers()) {
			drawLayer(ug, layer, cell.getWidth(), cell.getHeight());
			ug = ug.apply(UTranslate.dy(cell.getHeight()));
		}
	}

	private void drawLayer(UGraphic ug, Layer layer, double w, double h) {
		for (IEntity ent : layer.entities()) {
			final IEntityImage image = computeImage((ILeaf) ent);
			final int longitude = layer.getLongitude(ent);
			final Dimension2D dimImage = image.calculateDimension(ug.getStringBounder());
			final double diffx = w - dimImage.getWidth();
			final double diffy = h - dimImage.getHeight();
			image.drawU(ug.apply(new UTranslate(w * longitude / 2 + diffx / 2, diffy / 2)));
		}
	}

	private IEntityImage computeImage(final ILeaf leaf) {
		final IEntityImage image = GeneralImageBuilder.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null, null, null, diagram.getLinks());
		return image;
	}

}
