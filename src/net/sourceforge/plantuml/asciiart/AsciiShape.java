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
package net.sourceforge.plantuml.asciiart;

public enum AsciiShape {

	// int STICKMAN_HEIGHT = 5;
	// int STICKMAN_UNICODE_HEIGHT = 6;

	STICKMAN(3, 5), STICKMAN_UNICODE(3, 6), BOUNDARY(8, 3), DATABASE(10, 6);

	private final int width;
	private final int height;

	private AsciiShape(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void draw(BasicCharArea area, int x, int y) {
		if (this == STICKMAN) {
			drawStickMan(area, x, y);
		} else if (this == STICKMAN_UNICODE) {
			drawStickManUnicode(area, x, y);
		} else if (this == BOUNDARY) {
			drawBoundary(area, x, y);
		} else if (this == DATABASE) {
			drawDatabase(area, x, y);
		}
	}

	private void drawDatabase(BasicCharArea area, int x, int y) {
		area.drawStringLR(" ,.-^^-._", x, y++);
		area.drawStringLR("|-.____.-|", x, y++);
		area.drawStringLR("|        |", x, y++);
		area.drawStringLR("|        |", x, y++);
		area.drawStringLR("|        |", x, y++);
		area.drawStringLR("'-.____.-'", x, y++);
	}

	private void drawDatabaseSmall(BasicCharArea area, int x, int y) {
		area.drawStringLR(" ,.-\"-._ ", x, y++);
		area.drawStringLR("|-.___.-|", x, y++);
		area.drawStringLR("|       |", x, y++);
		area.drawStringLR("|       |", x, y++);
		area.drawStringLR("|       |", x, y++);
		area.drawStringLR("'-.___.-'", x, y++);
	}

	private void drawBoundary(BasicCharArea area, int x, int y) {
		area.drawStringLR("|   ,-.", x, y++);
		area.drawStringLR("+--{   )", x, y++);
		area.drawStringLR("|   `-'", x, y++);
	}

	private void drawStickMan(BasicCharArea area, int x, int y) {
		area.drawStringLR(",-.", x, y++);
		area.drawStringLR("`-'", x, y++);
		area.drawStringLR("/|\\", x, y++);
		area.drawStringLR(" | ", x, y++);
		area.drawStringLR("/ \\", x, y++);
	}

	private void drawStickManUnicode(BasicCharArea area, int x, int y) {
		area.drawStringLR("\u250c\u2500\u2510", x, y++);
		area.drawStringLR("\u2551\"\u2502", x, y++);
		area.drawStringLR("\u2514\u252c\u2518", x, y++);
		area.drawStringLR("\u250c\u253c\u2510", x, y++);
		area.drawStringLR(" \u2502 ", x, y++);
		area.drawStringLR("\u250c\u2534\u2510", x, y++);
	}

	public final int getHeight() {
		return height;
	}

	public final int getWidth() {
		return width;
	}

}
