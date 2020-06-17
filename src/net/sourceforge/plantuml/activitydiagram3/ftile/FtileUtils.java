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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.Collection;

public class FtileUtils {

	public static Ftile addConnection(Ftile ftile, Connection connection) {
		return new FtileWithConnection(ftile, connection);
	}

	public static Ftile addConnection(Ftile ftile, Collection<Connection> connections) {
		return new FtileWithConnection(ftile, connections);
	}

	public static Ftile withSwimlaneOut(Ftile ftile, Swimlane out) {
		return new FtileWithSwimlanes(ftile, ftile.getSwimlaneIn(), out);
	}

	public static Ftile addBottom(Ftile ftile, double marginBottom) {
		return new FtileMargedVertically(ftile, 0, marginBottom);
	}

	public static Ftile addVerticalMargin(Ftile ftile, double marginTop, double marginBottom) {
		if (marginTop == 0 && marginBottom == 0) {
			return ftile;
		}
		return new FtileMargedVertically(ftile, marginTop, marginBottom);
	}

	public static Ftile addHorizontalMargin(Ftile ftile, double margin1, double margin2) {
		if (margin1 == 0 && margin2 == 0) {
			return ftile;
		}
		return new FtileMarged(ftile, margin1, margin2);
	}

	public static Ftile addHorizontalMargin(Ftile ftile, double margin) {
		if (margin == 0) {
			return ftile;
		}
		return new FtileMarged(ftile, margin, margin);
	}

	// public static Ftile addHorizontalMargin(Ftile ftile, double margin) {
	// return new FtileMarged(ftile, margin);
	// }

	// private static Ftile neverNull(Ftile ftile, ISkinParam skinParam) {
	// if (ftile == null) {
	// return new FtileEmpty(skinParam);
	// }
	// return ftile;
	// }

}
