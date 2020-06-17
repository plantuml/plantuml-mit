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
package net.sourceforge.plantuml.asciiart;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public class TranslatedCharArea implements UmlCharArea {

	private final int dx;
	private final int dy;
	private final UmlCharArea charArea;

	public TranslatedCharArea(UmlCharArea charArea, int dx, int dy) {
		this.charArea = charArea;
		this.dx = dx;
		this.dy = dy;
	}

	public void drawBoxSimple(int x, int y, int width, int height) {
		charArea.drawBoxSimple(x + dx, y + dy, width, height);

	}

	public void drawBoxSimpleUnicode(int x, int y, int width, int height) {
		charArea.drawBoxSimpleUnicode(x + dx, y + dy, width, height);
	}
	
	public void drawNoteSimple(int x, int y, int width, int height) {
		charArea.drawNoteSimple(x + dx, y + dy, width, height);
	}

	public void drawNoteSimpleUnicode(int x, int y, int width, int height) {
		charArea.drawNoteSimpleUnicode(x + dx, y + dy, width, height);
	}


	public void drawShape(AsciiShape shape, int x, int y) {
		charArea.drawShape(shape, x + dx, y + dy);
	}
	
	public void drawChar(char c, int x, int y) {
		charArea.drawChar(c, x + dx, y + dy);
	}

	public void drawHLine(char c, int line, int col1, int col2) {
		charArea.drawHLine(c, line + dy, col1 + dx, col2 + dx);
	}
	public void drawHLine(char c, int line, int col1, int col2, char ifFound, char thenUse) {
		charArea.drawHLine(c, line + dy, col1 + dx, col2 + dx, ifFound, thenUse);
	}

	public void drawStringLR(String string, int x, int y) {
		charArea.drawStringLR(string, x + dx, y + dy);
	}

	public void drawStringTB(String string, int x, int y) {
		charArea.drawStringTB(string, x + dx, y + dy);
	}

	public void drawVLine(char c, int col, int line1, int line2) {
		charArea.drawVLine(c, col + dx, line1 + dy, line2 + dy);
	}

	public int getHeight() {
		return charArea.getHeight();
	}

	public int getWidth() {
		return charArea.getWidth();
	}

	public String getLine(int line) {
		return charArea.getLine(line);
	}

	public List<String> getLines() {
		return charArea.getLines();
	}

	public void print(PrintStream ps) {
		charArea.print(ps);
	}

	public void drawStringsLR(Collection<? extends CharSequence> strings, int x, int y) {
		charArea.drawStringsLR(strings, x + dx, y + dy);
	}

	public void fillRect(char c, int x, int y, int width, int height) {
		charArea.fillRect(c, x + dx, y + dy, width, height);
	}



}
