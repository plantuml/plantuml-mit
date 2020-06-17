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


public class CleanerMoveBlock implements GridCleaner {

	public boolean clean(Grid grid) {
		// System.err.println("CleanerMoveBlock");
		for (Line line : grid.lines().toList()) {
			tryGrid(grid, line);
		}
		return false;
	}

	private void tryGrid(Grid grid, Line line) {
		// System.err.println("TRYING LINE " + line);
		for (Col col1 : grid.cols().toList()) {
			final Placeable cell1 = grid.getCell(line, col1).getData();
			if (cell1 instanceof ConnectorPuzzleEmpty == false) {
				continue;
			}
			final ConnectorPuzzleEmpty puzzle1 = (ConnectorPuzzleEmpty) cell1;
			if (puzzle1.checkDirections("NS") == false) {
				continue;
			}
			final Navigator<Col> it2 = grid.cols().navigator(col1);
			int cpt = 0;
			while (true) {
				final Col col2 = it2.next();
				cpt++;
				if (col2 == null) {
					break;
				}
				if (col1 == col2) {
					continue;
				}
				final Placeable cell2 = grid.getCell(line, col2).getData();
				if (cell2 == null) {
					continue;
				}
				if (cell2 instanceof ConnectorPuzzleEmpty == false) {
					break;
				}
				final ConnectorPuzzleEmpty puzzle2 = (ConnectorPuzzleEmpty) cell2;
				if (puzzle2.checkDirections("NS") == false) {
					continue;
				}
				if (cpt > 1) {
					tryBridge(line, col1, col2);
				}
				break;
			}
		}

	}

	private void tryBridge(Line line, Col col1, final Col col2) {
		// System.err.println("LINE=" + line + " " + col1 + " " + col2 + " ");
	}
}
