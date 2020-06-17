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
package net.sourceforge.plantuml.creole.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.creole.CreoleContext;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.Stripe;
import net.sourceforge.plantuml.creole.StripeStyle;
import net.sourceforge.plantuml.creole.StripeStyleType;
import net.sourceforge.plantuml.creole.atom.Atom;
import net.sourceforge.plantuml.creole.atom.AtomTable;
import net.sourceforge.plantuml.creole.atom.AtomWithMargin;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class StripeTable implements Stripe {

	static enum Mode {
		HEADER, NORMAL
	};

	final private FontConfiguration fontConfiguration;
	final private ISkinSimple skinParam;
	final private AtomTable table;
	final private Atom marged;
	final private StripeStyle stripeStyle = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	public StripeTable(FontConfiguration fontConfiguration, ISkinSimple skinParam, String line) {
		this.skinParam = skinParam;
		this.fontConfiguration = fontConfiguration;
		HColor lineColor = getBackOrFrontColor(line, 1);
		if (lineColor == null) {
			lineColor = fontConfiguration.getColor();
		}
		this.table = new AtomTable(lineColor);
		this.marged = new AtomWithMargin(table, 2, 2);
		analyzeAndAddInternal(line);
	}

	public List<Atom> getAtoms() {
		return Collections.<Atom>singletonList(marged);
	}

	public Atom getLHeader() {
		return null;
	}

	static Atom asAtom(List<StripeSimple> cells, double padding) {
		final Sheet sheet = new Sheet(HorizontalAlignment.LEFT);
		for (StripeSimple cell : cells) {
			sheet.add(cell);
		}
		return new SheetBlock1(sheet, LineBreakStrategy.NONE, padding);
	}

	private HColor getBackOrFrontColor(String line, int idx) {
		if (CreoleParser.doesStartByColor(line)) {
			final int idx1 = line.indexOf('#');
			final int idx2 = line.indexOf('>');
			if (idx2 == -1) {
				throw new IllegalStateException();
			}
			final String[] color = line.substring(idx1, idx2).split(",");
			if (idx < color.length) {
				return skinParam.getIHtmlColorSet().getColorIfValid(color[idx]);
			}
		}
		return null;
	}

	private String withouBackColor(String line) {
		final int idx2 = line.indexOf('>');
		if (idx2 == -1) {
			throw new IllegalStateException();
		}
		return line.substring(idx2 + 1);
	}

	private static final String hiddenBar = "\uE000";

	private void analyzeAndAddInternal(String line) {
		line = line.replace("\\|", hiddenBar);
		HColor lineBackColor = getBackOrFrontColor(line, 0);
		if (lineBackColor != null) {
			line = withouBackColor(line);
		}
		table.newLine(lineBackColor);
		for (final StringTokenizer st = new StringTokenizer(line, "|"); st.hasMoreTokens();) {
			Mode mode = Mode.NORMAL;
			String v = st.nextToken().replace(hiddenBar.charAt(0), '|');
			if (v.startsWith("=")) {
				v = v.substring(1);
				mode = Mode.HEADER;
			}
			HColor cellBackColor = getBackOrFrontColor(v, 0);
			if (cellBackColor != null) {
				v = withouBackColor(v);
			}
			final List<String> lines = getWithNewlinesInternal(v);
			final List<StripeSimple> cells = new ArrayList<StripeSimple>();
			for (String s : lines) {
				final StripeSimple cell = new StripeSimple(getFontConfiguration(mode), stripeStyle, new CreoleContext(),
						skinParam, CreoleMode.FULL);
				if (s.startsWith("<r>")) {
					cell.setCellAlignment(HorizontalAlignment.RIGHT);
					s = s.substring("<r>".length());
				}
				cell.analyzeAndAdd(s);
				cells.add(cell);
			}
			table.addCell(asAtom(cells, skinParam.getPadding()), cellBackColor);
		}
	}

	static List<String> getWithNewlinesInternal(String s) {
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == '\\') {
					current.append(c2);
				} else {
					current.append(c);
					current.append(c2);
				}
			} else if (c == BackSlash.hiddenNewLine()) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return result;
	}

	private FontConfiguration getFontConfiguration(Mode mode) {
		if (mode == Mode.NORMAL) {
			return fontConfiguration;
		}
		return fontConfiguration.bold();
	}

	public void analyzeAndAddLine(String line) {
		analyzeAndAddInternal(line);
	}

}
