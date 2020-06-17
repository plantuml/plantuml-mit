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
package net.sourceforge.plantuml.project.draw;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UPath;

public class PathUtils {

	private final static double round = 4;

	public static UPath UtoRight(double width, double height) {
		final UPath result = new UPath();
		result.moveTo(0, 0);
		result.lineTo(width - round, 0);
		result.arcTo(new Point2D.Double(width, round), round, 0, 1);
		result.lineTo(width, height - round);
		result.arcTo(new Point2D.Double(width - round, height), round, 0, 1);
		result.lineTo(0, height);
		return result;
	}

	public static UPath UtoLeft(double width, double height) {
		final UPath result = new UPath();
		result.moveTo(width, height);
		result.lineTo(round, height);
		result.arcTo(new Point2D.Double(0, height - round), round, 0, 1);
		result.lineTo(0, round);
		result.arcTo(new Point2D.Double(round, 0), round, 0, 1);
		result.lineTo(width, 0);
		return result;
	}

}
