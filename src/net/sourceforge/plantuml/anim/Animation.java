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
package net.sourceforge.plantuml.anim;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.MinMax;

public class Animation {

	private final List<AffineTransformation> all;

	private Animation(List<AffineTransformation> all) {
		if (all.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.all = all;
	}

	public static Animation singleton(AffineTransformation affineTransformation) {
		if (affineTransformation == null) {
			return null;
		}
		return new Animation(Collections.singletonList(affineTransformation));
	}

	public static Animation create(List<String> descriptions) {
		final List<AffineTransformation> all = new ArrayList<AffineTransformation>();
		for (String s : descriptions) {
			final AffineTransformation tmp = AffineTransformation.create(s);
			if (tmp != null) {
				all.add(tmp);
			}
		}
		return new Animation(all);
	}

	public Collection<AffineTransformation> getAll() {
		return Collections.unmodifiableCollection(all);
	}

	public void setDimension(Dimension2D dim) {
		for (AffineTransformation affineTransform : all) {
			affineTransform.setDimension(dim);
		}

	}

	public AffineTransformation getFirst() {
		return all.get(0);
	}

	public MinMax getMinMax(Dimension2D dim) {
		MinMax result = MinMax.getEmpty(false);
		for (AffineTransformation affineTransform : all) {
			final MinMax m = affineTransform.getMinMax(dim);
			result = result.addMinMax(m);
		}
		return result;
	}

}
