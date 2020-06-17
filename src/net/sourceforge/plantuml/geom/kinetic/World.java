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
package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class World implements MoveObserver {

	private final List<Path> paths = new ArrayList<Path>();
	private final Map<Point2DCharge, Path> pathOfPoints = new HashMap<Point2DCharge, Path>();
	private final List<Frame> frames = new ArrayList<Frame>();

	public void addFrame(Frame frame) {
		this.frames.add(frame);
	}

	public void addPath(Path path) {
		this.paths.add(path);
	}

	public VectorForce getElectricForce(Point2DCharge point) {
		VectorForce result = new VectorForce(0, 0);

		final Quadrant quadrant = new Quadrant(point);

		for (Quadrant q : quadrant.neighbourhood()) {
			for (Point2DCharge pc2 : quadrantMapper.getAllPoints(q)) {
				final Path path = pathOfPoints.get(pc2);
				if (path.containsPoint2DCharge(point)) {
					continue;
				}
				result = result.plus(getElectricForce(point, pc2));
			}
		}
		return result;
	}

	private VectorForce getElectricForceSlow(Point2DCharge point) {
		VectorForce result = new VectorForce(0, 0);

		for (Path path : paths) {
			if (path.containsPoint2DCharge(point)) {
				continue;
			}
			for (Point2DCharge pc2 : path.getPoints()) {
				result = result.plus(getElectricForce(point, pc2));
			}
		}
		return result;
	}

	static private VectorForce getElectricForce(Point2DCharge onThis, Point2DCharge byThis) {
		final double dist = onThis.distance(byThis);
		if (dist == 0) {
			return new VectorForce(0, 0);
		}
		final VectorForce result = new VectorForce(byThis.getX() - onThis.getX(), byThis.getY() - onThis.getY());
		final double v = 100.0 * onThis.getCharge() * byThis.getCharge() / dist / dist;
		return result.normaliseTo(v);
	}

	static private VectorForce getAtomicForce(Point2DCharge onThis, Point2DCharge byThis) {
		final double dist = onThis.distance(byThis);
		if (dist == 0) {
			return new VectorForce(0, 0);
		}
		final VectorForce result = new VectorForce(byThis.getX() - onThis.getX(), byThis.getY() - onThis.getY());
		double v = 1000 / dist / dist / dist;
		if (v > 5) {
			v = 5;
		}
		return result.normaliseTo(v);
	}

	Map<Point2DCharge, VectorForce> getForces() {
		final Map<Point2DCharge, VectorForce> result = new LinkedHashMap<Point2DCharge, VectorForce>();
		for (Path path : paths) {
			for (Point2DCharge pt : path.getPoints()) {
				// final VectorForce elastic = new VectorForce(0, 0);
				// final VectorForce elect = new VectorForce(0, 0);
				final VectorForce elastic = path.getElasticForce(pt);
				final VectorForce elect = getElectricForce(pt);
				VectorForce force = elastic.plus(elect);
				for (Frame f : frames) {
					final Point2D inter = f.getFrontierPointViewBy(pt);
					if (inter != null) {
						final Point2DCharge pchar = new Point2DCharge(inter, 1);
						force = force.plus(getAtomicForce(pt, pchar));
					}
				}
				result.put(pt, force);
			}
		}

		return result;
	}

	public double onePass() {
		double result = 0;
		final Map<Point2DCharge, VectorForce> forces = getForces();
		for (Map.Entry<Point2DCharge, VectorForce> ent : forces.entrySet()) {
			final VectorForce force = ent.getValue();
			result += force.getLength();
			ent.getKey().apply(force);
		}
		return result;
	}

	public final Collection<Path> getPaths() {
		return Collections.unmodifiableCollection(paths);
	}

	private QuadrantMapper quadrantMapper;

	public void renderContinue() {
		quadrantMapper = new QuadrantMapper();
		pathOfPoints.clear();
		for (Path path : paths) {
			path.renderContinue();
		}
		for (Path path : paths) {
			for (Point2DCharge pt : path.getPoints()) {
				pt.setMoveObserver(this);
				quadrantMapper.addPoint(pt);
				pathOfPoints.put(pt, path);
			}
		}
	}

	public void pointMoved(Point2DCharge point) {
		quadrantMapper.updatePoint(point);
	}
}
