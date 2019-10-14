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
package net.sourceforge.plantuml.geom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Kingdom extends AbstractFigure {

	private Set<ClosedArea> buildClosedArea(ClosedArea area) {
		if (area.isClosed()) {
			throw new IllegalArgumentException();
		}
		final Set<ClosedArea> result = new HashSet<ClosedArea>();
		for (LineSegmentInt seg : getSegmentsWithExtremity(area.getFreePoint(), area.getSegments())) {
			final ClosedArea newArea = area.append(seg);
			if (newArea != null) {
				result.add(newArea);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	private void grow(Set<ClosedArea> areas) {
		for (ClosedArea area : new HashSet<ClosedArea>(areas)) {
			if (area.isClosed() == false) {
				areas.addAll(buildClosedArea(area));
			}
		}
	}

	public Set<ClosedArea> getAllClosedArea() {
		final Set<ClosedArea> result = new HashSet<ClosedArea>();
		for (LineSegmentInt seg : getSegments()) {
			result.add(new ClosedArea().append(seg));
		}
		int lastSize;
		do {
			lastSize = result.size();
			grow(result);
		} while (result.size() != lastSize);
		for (final Iterator<ClosedArea> it = result.iterator(); it.hasNext();) {
			final ClosedArea area = it.next();
			if (area.isClosed() == false) {
				it.remove();
			}
		}
		return Collections.unmodifiableSet(result);
	}

	// public Set<ClosedArea> getAllSmallClosedArea() {
	// final Set<ClosedArea> all = getAllClosedArea();
	// final Set<ClosedArea> result = new HashSet<ClosedArea>(all);
	//
	// for (final Iterator<ClosedArea> it = result.iterator(); it.hasNext();) {
	// final ClosedArea area = it.next();
	// if (containsAnotherArea(area, all)) {
	// it.remove();
	// }
	// }
	//
	// return Collections.unmodifiableSet(result);
	// }

	// static private boolean containsAnotherArea(ClosedArea area,
	// Set<ClosedArea> all) {
	// for (ClosedArea another : all) {
	// if (another == area) {
	// continue;
	// }
	// if (area.contains(another)) {
	// return true;
	// }
	// }
	// return false;
	// }

	@Override
	public boolean arePointsConnectable(Point2DInt p1, Point2DInt p2) {
		for (ClosedArea area : getAllClosedArea()) {
			if (area.arePointsConnectable(p1, p2) == false) {
				return false;
			}
		}
		return true;
	}

}
