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
package net.sourceforge.plantuml.graph2;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RectanglesCollection implements Iterable<Rectangle2D.Double> {

	private final List<Rectangle2D.Double> areas = new ArrayList<Rectangle2D.Double>();
	private final SortedListImpl<Rectangle2D.Double> sortedX1;
	private final SortedListImpl<Rectangle2D.Double> sortedX2;
	private final SortedListImpl<Rectangle2D.Double> sortedY1;
	private final SortedListImpl<Rectangle2D.Double> sortedY2;

	private Rectangle2D.Double max = null;

	public RectanglesCollection() {
		sortedX1 = new SortedListImpl<Rectangle2D.Double>(new Measurer<Rectangle2D.Double>() {
			public int getMeasure(Rectangle2D.Double data) {
				return (int) data.x;
			}
		});
		sortedX2 = new SortedListImpl<Rectangle2D.Double>(new Measurer<Rectangle2D.Double>() {
			public int getMeasure(Rectangle2D.Double data) {
				return (int) (data.x + data.width);
			}
		});
		sortedY1 = new SortedListImpl<Rectangle2D.Double>(new Measurer<Rectangle2D.Double>() {
			public int getMeasure(Rectangle2D.Double data) {
				return (int) data.y;
			}
		});
		sortedY2 = new SortedListImpl<Rectangle2D.Double>(new Measurer<Rectangle2D.Double>() {
			public int getMeasure(Rectangle2D.Double data) {
				return (int) (data.y + data.height);
			}
		});
	}

	public RectanglesCollection(Rectangle2D.Double rect) {
		this();
		add(rect);
	}

	public double getSurf() {
		if (max == null) {
			return 0;
		}
		return max.getWidth() * max.getHeight();
	}

	public void add(Rectangle2D.Double rect) {
		areas.add(rect);
		// sortedX1.add(rect);
		// sortedX2.add(rect);
		// sortedY1.add(rect);
		// sortedY2.add(rect);
		if (max == null) {
			max = rect;
		} else {
			max = (Rectangle2D.Double) max.createUnion(rect);
		}
	}

	public Iterator<Rectangle2D.Double> iterator() {
		return areas.iterator();
	}

	public boolean intersect(RectanglesCollection other) {
		if (this.size() > other.size()) {
			return intersectSeveral(this, other);
		}
		return intersectSeveral(other, this);
	}

	static private long TPS1;
	static private long TPS2;

	private static boolean intersectSeveral(RectanglesCollection large, RectanglesCollection compact) {
		assert large.size() >= compact.size();
		final long start = System.currentTimeMillis();
		try {
			for (Rectangle2D.Double r : compact) {
				if (large.intersectSimple(r)) {
					return true;
				}
			}
			return false;
		} finally {
			TPS2 += System.currentTimeMillis() - start;
		}
	}

	private boolean intersectSimple(Rectangle2D.Double rect) {
		final long start = System.currentTimeMillis();
		try {
			if (max == null || max.intersects(rect) == false) {
				return false;
			}
			for (Rectangle2D.Double r : areas) {
				if (rect.intersects(r)) {
					return true;
				}
			}
			return false;
		} finally {
			TPS1 += System.currentTimeMillis() - start;
		}
	}

	private boolean intersectSimpleOld(Rectangle2D.Double rect) {
		final long start = System.currentTimeMillis();
		try {
			if (max == null || max.intersects(rect) == false) {
				return false;
			}
			final List<Rectangle2D.Double> lX1 = sortedX1.lesserOrEquals((int) (rect.x + rect.width));
			List<Rectangle2D.Double> lmin = lX1;
			if (lX1.size() == 0) {
				return false;
			}
			final List<Rectangle2D.Double> lX2 = sortedX2.biggerOrEquals((int) rect.x);
			if (lX2.size() == 0) {
				return false;
			}
			if (lX2.size() < lmin.size()) {
				lmin = lX2;
			}
			final List<Rectangle2D.Double> lY1 = sortedY1.lesserOrEquals((int) (rect.y + rect.height));
			if (lY1.size() == 0) {
				return false;
			}
			if (lY1.size() < lmin.size()) {
				lmin = lY1;
			}
			final List<Rectangle2D.Double> lY2 = sortedY2.biggerOrEquals((int) rect.y);
			if (lY2.size() == 0) {
				return false;
			}
			if (lY2.size() < lmin.size()) {
				lmin = lY2;
			}
			for (Rectangle2D.Double r : lmin) {
				if (rect.intersects(r)) {
					return true;
				}
			}
			return false;
		} finally {
			TPS1 += System.currentTimeMillis() - start;
		}
	}

	public int size() {
		return areas.size();
	}

	public void addAll(RectanglesCollection other) {
		for (Rectangle2D.Double r : other.areas) {
			this.add(r);
		}
	}

}
