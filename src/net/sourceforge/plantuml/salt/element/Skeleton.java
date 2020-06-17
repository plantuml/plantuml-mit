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
package net.sourceforge.plantuml.salt.element;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Skeleton {

	private final List<Entry> entries = new ArrayList<Entry>();

	static class Entry {
		private final double xpos;
		private final double ypos;

		public Entry(double x, double y) {
			this.xpos = x;
			this.ypos = y;
		}

		public void drawRectangle(UGraphic ug) {
			ug.apply(new UTranslate(xpos, ypos)).draw(new URectangle(2, 2));
		}
	}

	public void add(double x, double y) {
		entries.add(new Entry(x, y));
	}

	public void draw(UGraphic ug, double x, double y) {
		for (int i = 0; i < entries.size(); i++) {
			final Entry en = entries.get(i);
			if (i + 1 < entries.size() && entries.get(i + 1).xpos > en.xpos) {
				en.drawRectangle(ug);
			}
			Entry parent = null;
			for (int j = 0; j < i; j++) {
				final Entry en0 = entries.get(j);
				if (en0.xpos < en.xpos) {
					parent = en0;
				}
			}
			if (parent != null) {
				drawChild(ug, parent, en);
			}
		}
	}

	private void drawChild(UGraphic ug, Entry parent, Entry child) {
		final double dy = child.ypos - parent.ypos - 2;
		ug.apply(new UTranslate(parent.xpos + 1, parent.ypos + 3)).draw(ULine.vline(dy));

		final double dx = child.xpos - parent.xpos - 2;
		ug.apply(new UTranslate(parent.xpos + 1, child.ypos + 1)).draw(ULine.hline(dx));

	}

}
