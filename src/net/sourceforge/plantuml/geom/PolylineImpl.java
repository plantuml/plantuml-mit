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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PolylineImpl extends AbstractPolyline implements Polyline {

	final private List<Point2DInt> intermediates = new ArrayList<Point2DInt>();

	public PolylineImpl(Pointable start, Pointable end) {
		super(start, end);
	}

	public int nbSegments() {
		return intermediates.size() + 1;
	}

	public List<LineSegmentInt> segments() {
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		Point2DInt cur = getStart().getPosition();
		for (Point2DInt intermediate : intermediates) {
			result.add(new LineSegmentInt(cur, intermediate));
			cur = intermediate;
		}
		result.add(new LineSegmentInt(cur, getEnd().getPosition()));
		return Collections.unmodifiableList(result);
	}

	public void addIntermediate(Point2DInt intermediate) {
		assert intermediates.contains(intermediate) == false;
		intermediates.add(intermediate);
	}

	public void inflate(InflationTransform transform) {
		// final List<LineSegment> segments = segments();
		// if (segments.size() == 1) {
		// return;
		// }
		// intermediates.clear();
		// if (segments.size() == 2) {
		// final Point2DInt p = segments.get(0).getP2();
		// intermediates.add(transform.inflatePoint2DInt(p));
		// } else {
		// final List<LineSegment> segmentsT = transform.inflate(segments);
		// for (int i = 0; i < segmentsT.size() - 2; i++) {
		// intermediates.add(segmentsT.get(i).getP2());
		// }
		//
		// }

		final List<LineSegmentInt> segments = transform.inflate(this.segments());
		// Log.println("segments="+segments);
		intermediates.clear();
		for (int i = 1; i < segments.size() - 1; i++) {
			addIntermediate(segments.get(i).getP1());
		}
	}

	public final Collection<Point2DInt> getIntermediates() {
		return Collections.unmodifiableCollection(intermediates);
	}

}
