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
package net.sourceforge.plantuml.graph;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.geom.Point2DInt;
import net.sourceforge.plantuml.geom.PolylineBreakeable;
import net.sourceforge.plantuml.geom.SpiderWeb;

public class Galaxy4 {

	final private Board board;

	final private Map<ALink, PolylineBreakeable> lines = new LinkedHashMap<ALink, PolylineBreakeable>();
	final private SpiderWeb spiderWeb;

	public Galaxy4(Board board, int widthCell, int heightCell) {
		this.spiderWeb = new SpiderWeb(widthCell, heightCell);
		this.board = board;
	}

	public Point2DInt getMainPoint(int row, int col) {
		return spiderWeb.getMainPoint(row, col);
	}

	public PolylineBreakeable getPolyline(ALink link) {
		return lines.get(link);

	}

	public void addLink(ALink link) {
		final int rowStart = link.getNode1().getRow();
		final int rowEnd = link.getNode2().getRow();
		final int colStart = board.getCol(link.getNode1());
		final int colEnd = board.getCol(link.getNode2());

		final PolylineBreakeable polyline = spiderWeb.addPolyline(rowStart, colStart, rowEnd, colEnd);

		Log.info("link=" + link + " polyline=" + polyline);

		if (polyline == null) {
			Log.info("PENDING " + link + " " + polyline);
		} else {
			lines.put(link, polyline);
		}

	}

	public final Board getBoard() {
		return board;
	}

	public final Map<ALink, PolylineBreakeable> getLines() {
		return Collections.unmodifiableMap(lines);
	}

}
