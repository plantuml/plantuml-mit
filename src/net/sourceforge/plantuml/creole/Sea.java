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
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.MinMax;

public class Sea {

	private double currentX;
	private final Map<Atom, Position> positions = new HashMap<Atom, Position>();
	private final StringBounder stringBounder;

	public Sea(StringBounder stringBounder) {
		if (stringBounder == null) {
			throw new IllegalArgumentException();
		}
		this.stringBounder = stringBounder;
	}

	public void add(Atom atom) {
		final Dimension2D dim = atom.calculateDimension(stringBounder);
		final double y = 0;
		final Position position = new Position(currentX, y, dim);
		positions.put(atom, position);
		currentX += dim.getWidth();
	}

	public Position getPosition(Atom atom) {
		return positions.get(atom);
	}

	public void doAlign() {
		for (Map.Entry<Atom, Position> ent : new HashMap<Atom, Position>(positions).entrySet()) {
			final Position pos = ent.getValue();
			final Atom atom = ent.getKey();
			final double height = atom.calculateDimension(stringBounder).getHeight();
			final Position newPos = pos.translateY(-height + atom.getStartingAltitude(stringBounder));
			positions.put(atom, newPos);
		}
	}

	public void translateMinYto(double newValue) {
		final double delta = newValue - getMinY();
		for (Map.Entry<Atom, Position> ent : new HashMap<Atom, Position>(positions).entrySet()) {
			final Position pos = ent.getValue();
			final Atom atom = ent.getKey();
			positions.put(atom, pos.translateY(delta));
		}
	}

	public void exportAllPositions(Map<Atom, Position> destination) {
		destination.putAll(positions);
	}

	public double getMinY() {
		if (positions.size() == 0) {
			throw new IllegalStateException();
		}
		double result = Double.MAX_VALUE;
		for (Position pos : positions.values()) {
			if (result > pos.getMinY()) {
				result = pos.getMinY();
			}
		}
		return result;
	}

	public double getMaxY() {
		if (positions.size() == 0) {
			throw new IllegalStateException();
		}
		double result = -Double.MAX_VALUE;
		for (Position pos : positions.values()) {
			if (result < pos.getMaxY()) {
				result = pos.getMaxY();
			}
		}
		return result;
	}

	public double getHeight() {
		return getMaxY() - getMinY();
	}

	public MinMax update(MinMax minMax) {
		for (Position position : positions.values()) {
			minMax = position.update(minMax);
		}
		return minMax;
	}

	public final double getWidth() {
		return currentX;
	}

}
