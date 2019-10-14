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

import java.util.Collection;

public class UmlCharAreaImpl extends BasicCharAreaImpl implements UmlCharArea {

	public void drawBoxSimple(int x, int y, int width, int height) {
		this.drawHLine('-', y, x + 1, x + width - 1);
		this.drawHLine('-', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('|', x, y + 1, y + height - 1);
		this.drawVLine('|', x + width - 1, y + 1, y + height - 1);

		this.drawChar(',', x, y);
		this.drawChar('.', x + width - 1, y);
		this.drawChar('`', x, y + height - 1);
		this.drawChar('\'', x + width - 1, y + height - 1);
	}

	public void drawBoxSimpleUnicode(int x, int y, int width, int height) {
		this.drawHLine('\u2500', y, x + 1, x + width - 1);
		this.drawHLine('\u2500', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('\u2502', x, y + 1, y + height - 1);
		this.drawVLine('\u2502', x + width - 1, y + 1, y + height - 1);

		this.drawChar('\u250c', x, y);
		this.drawChar('\u2510', x + width - 1, y);
		this.drawChar('\u2514', x, y + height - 1);
		this.drawChar('\u2518', x + width - 1, y + height - 1);
	}

	public void drawShape(AsciiShape shape, int x, int y) {
		shape.draw(this, x, y);
	}

	public void drawStringsLR(Collection<? extends CharSequence> strings, int x, int y) {
		int i = 0;
		if (x < 0) {
			x = 0;
		}
		for (CharSequence s : strings) {
			this.drawStringLR(s.toString(), x, y + i);
			i++;
		}

	}

	public void drawNoteSimple(int x, int y, int width, int height) {
		this.drawHLine('-', y, x + 1, x + width - 1);
		this.drawHLine('-', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('|', x, y + 1, y + height - 1);
		this.drawVLine('|', x + width - 1, y + 1, y + height - 1);

		this.drawChar(',', x, y);

		this.drawStringLR("!. ", x + width - 3, y);
		this.drawStringLR("|_\\", x + width - 3, y + 1);

		this.drawChar('`', x, y + height - 1);
		this.drawChar('\'', x + width - 1, y + height - 1);
	}

	public void drawNoteSimpleUnicode(int x, int y, int width, int height) {
		this.drawChar('\u2591', x + width - 2, y + 1);

		this.drawHLine('\u2550', y, x + 1, x + width - 1, '\u2502', '\u2567');
		this.drawHLine('\u2550', y + height - 1, x + 1, x + width - 1, '\u2502', '\u2564');

		this.drawVLine('\u2551', x, y + 1, y + height - 1);
		this.drawVLine('\u2551', x + width - 1, y + 1, y + height - 1);

		this.drawChar('\u2554', x, y);
		this.drawChar('\u2557', x + width - 1, y);
		this.drawChar('\u255a', x, y + height - 1);
		this.drawChar('\u255d', x + width - 1, y + height - 1);
	}
}
