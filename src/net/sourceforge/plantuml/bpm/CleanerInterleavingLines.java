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
package net.sourceforge.plantuml.bpm;

import net.sourceforge.plantuml.bpm.ConnectorPuzzle.Where;

public class CleanerInterleavingLines implements GridCleaner {

	public boolean clean(Grid grid) {
		// System.err.println("running CleanerInterleavingLines");
		boolean result = false;
		Line previous = null;
		for (Line line : grid.lines().toList()) {
			if (previous != null) {
				if (mergeable(grid, previous, line)) {
					// System.err.println("MERGEABLE! " + previous + " " + line);
					mergeLines(grid, previous, line);
					return true;
				}
			}
			previous = line;
		}
		return result;
	}

	private void mergeLines(Grid grid, Line line1, Line line2) {
		for (Col col : grid.cols().toList()) {
			final Cell cell1 = grid.getCell(line1, col);
			final Cell cell2 = grid.getCell(line2, col);
			cell1.setData(merge(cell1.getData(), cell2.getData()));
			cell2.setData(null);
		}
		grid.removeLine(line2);

	}

	private boolean mergeable(Grid grid, Line line1, Line line2) {
		// int c = 0;
		for (Col col : grid.cols().toList()) {
			// System.err.println("c=" + c);
			// c++;
			final Placeable cell1 = grid.getCell(line1, col).getData();
			final Placeable cell2 = grid.getCell(line2, col).getData();
			// System.err.println("cells=" + cell1 + " " + cell2 + " " + mergeable(cell1, cell2));
			if (mergeable(cell1, cell2) == false) {
				return false;
			}
		}
		return true;
	}

	private Placeable merge(Placeable data1, Placeable data2) {
		if (data1 == null) {
			return data2;
		}
		if (data2 == null) {
			return data1;
		}
		assert data1 != null && data2 != null;
		if (data1 instanceof BpmElement) {
			return data1;
		}
		if (data2 instanceof BpmElement) {
			final ConnectorPuzzleEmpty puz1 = (ConnectorPuzzleEmpty) data1;
			if (puz1.checkDirections("SW")) {
				((BpmElement) data2).remove(Where.NORTH);
				((BpmElement) data2).append(Where.WEST);
			}
			return data2;
		}
		assert data1 instanceof ConnectorPuzzleEmpty && data2 instanceof ConnectorPuzzleEmpty;
		final ConnectorPuzzleEmpty puz1 = (ConnectorPuzzleEmpty) data1;
		final ConnectorPuzzleEmpty puz2 = (ConnectorPuzzleEmpty) data2;
		return puz2;
	}

	private boolean mergeable(Placeable data1, Placeable data2) {
		if (data1 == null || data2 == null) {
			return true;
		}
		assert data1 != null && data2 != null;
		if (data1 instanceof ConnectorPuzzleEmpty && data2 instanceof ConnectorPuzzleEmpty) {
			return mergeableCC((ConnectorPuzzleEmpty) data1, (ConnectorPuzzleEmpty) data2);
		}
		if (data1 instanceof ConnectorPuzzleEmpty && data2 instanceof BpmElement) {
			final boolean result = mergeablePuzzleSingle((ConnectorPuzzleEmpty) data1, (BpmElement) data2);
			// System.err.println("OTHER2=" + data2 + " " + data1 + " " + result);
			return result;
		}
		if (data2 instanceof ConnectorPuzzleEmpty && data1 instanceof BpmElement) {
			final boolean result = mergeablePuzzleSingle((BpmElement) data1, (ConnectorPuzzleEmpty) data2);
			// System.err.println("OTHER1=" + data1 + " " + data2 + " " + result);
			return result;
		}
		return false;
	}

	private boolean mergeablePuzzleSingle(ConnectorPuzzleEmpty data1, BpmElement data2) {
		if (data1.checkDirections("NS")) {
			return true;
		}
		if (data1.checkDirections("SW")) {
			return true;
		}
		return false;
	}

	private boolean mergeablePuzzleSingle(BpmElement data1, ConnectorPuzzleEmpty data2) {
		if (data2.checkDirections("NS")) {
			return true;
		}
		return false;
	}

	private boolean mergeableCC(ConnectorPuzzleEmpty puz1, ConnectorPuzzleEmpty puz2) {
		if (puz1.checkDirections("NS") && puz2.checkDirections("NS")) {
			return true;
		}
		if (puz1.checkDirections("NS") && puz2.checkDirections("NE")) {
			return true;
		}
		if (puz1.checkDirections("NS") && puz2.checkDirections("NW")) {
			return true;
		}
		return false;
	}

}
