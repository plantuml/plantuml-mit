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
package net.sourceforge.plantuml.graph;

import net.sourceforge.plantuml.geom.LineSegmentInt;

public class KenavoCostComputer implements CostComputer {

	public double getCost(Board board) {
		double result = 0;
		for (ALink link1 : board.getLinks()) {
			for (ALink link2 : board.getLinks()) {
				result += getCost(board, link1, link2);
			}
		}
		return result;
	}

	LineSegmentInt getLineSegment(Board board, ALink link) {
		final ANode n1 = link.getNode1();
		final ANode n2 = link.getNode2();
		return new LineSegmentInt(board.getCol(n1), n1.getRow(), board.getCol(n2), n2.getRow());
	}

	private double getCost(Board board, ALink link1, ALink link2) {
		final LineSegmentInt seg1 = getLineSegment(board, link1);
		final LineSegmentInt seg2 = getLineSegment(board, link2);

		final double len1 = getLength(link1, seg1, board);
		final double len2 = getLength(link2, seg2, board);

		return len1 * len2 * Math.exp(-seg1.getDistance(seg2));
	}

	private double getLength(ALink link, final LineSegmentInt seg, Board board) {
		double coef = 1;
		if (link.getNode1().getRow() == link.getNode2().getRow()
				&& board.getDirection(link) != board.getInitialDirection(link)) {
			coef = 1.1;
		}

		return seg.getLength() * coef;
	}

}
