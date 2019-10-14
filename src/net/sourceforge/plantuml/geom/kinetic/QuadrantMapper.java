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
package net.sourceforge.plantuml.geom.kinetic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QuadrantMapper {

	private final Map<Point2DCharge, Quadrant> quadrants = new HashMap<Point2DCharge, Quadrant>();
	private final Map<Quadrant, HashSet<Point2DCharge>> setOfPoints = new HashMap<Quadrant, HashSet<Point2DCharge>>();

	public void addPoint(Point2DCharge pt) {
		if (quadrants.containsKey(pt)) {
			throw new IllegalArgumentException();
		}
		final Quadrant q = new Quadrant(pt);
		quadrants.put(pt, q);
		getSetOfPoints(q).add(pt);
		assert getSetOfPoints(q).contains(pt);
		assert getSetOfPoints(new Quadrant(pt)).contains(pt);
	}

	public Set<Point2DCharge> getAllPoints(Quadrant qt) {
		return Collections.unmodifiableSet(getSetOfPoints(qt));
	}

	public Set<Point2DCharge> getAllPoints() {
		assert quadrants.keySet().equals(mergeOfSetOfPoints());
		return Collections.unmodifiableSet(quadrants.keySet());
	}

	private Set<Point2DCharge> mergeOfSetOfPoints() {
		final Set<Point2DCharge> result = new HashSet<Point2DCharge>();
		for (Set<Point2DCharge> set : setOfPoints.values()) {
			assert Collections.disjoint(set, result);
			result.addAll(set);
		}
		return result;
	}

	public void updatePoint(Point2DCharge pt) {
		final Quadrant newQ = new Quadrant(pt);
		final Quadrant old = quadrants.get(pt);
		assert getSetOfPoints(old).contains(pt);
		if (old.equals(newQ) == false) {
			assert getSetOfPoints(newQ).contains(pt) == false;
			assert getSetOfPoints(old).contains(pt);
			final boolean remove = getSetOfPoints(old).remove(pt);
			assert remove;
			final boolean add = getSetOfPoints(newQ).add(pt);
			assert add;
			assert getSetOfPoints(newQ).contains(pt);
			assert getSetOfPoints(old).contains(pt) == false;
			quadrants.put(pt, newQ);
		}
		assert getSetOfPoints(new Quadrant(pt)).contains(pt);
	}

	private HashSet<Point2DCharge> getSetOfPoints(Quadrant q) {
		HashSet<Point2DCharge> result = setOfPoints.get(q);
		if (result == null) {
			result = new HashSet<Point2DCharge>();
			setOfPoints.put(q, result);
		}
		return result;

	}

}
