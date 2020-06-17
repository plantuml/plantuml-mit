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
package net.sourceforge.plantuml.openiconic;

import java.awt.geom.Dimension2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.openiconic.data.DummyIcon;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class OpenIcon {

	private SvgPath svgPath;
	private List<String> rawData = new ArrayList<String>();
	private final String id;

	public static OpenIcon retrieve(String name) {
		final InputStream is = getResource(name);
		if (is == null) {
			return null;
		}
		try {
			return new OpenIcon(is, name);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	OpenIcon(String name) throws IOException {
		this(getResource(name), name);
	}

	private static InputStream getResource(String name) {
		// System.err.println("OPENING " + name);
		return DummyIcon.class.getResourceAsStream(name + ".svg");
	}

	private OpenIcon(InputStream is, String id) throws IOException {
		this.id = id;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s = null;
		while ((s = br.readLine()) != null) {
			rawData.add(s);
			if (s.contains("<path")) {
				final int x1 = s.indexOf('"');
				final int x2 = s.indexOf('"', x1 + 1);
				svgPath = new SvgPath(s.substring(x1 + 1, x2));
			}
		}
		br.close();
		is.close();
		if (rawData.size() != 3 && rawData.size() != 4) {
			throw new IllegalStateException();
		}
	}

	void saveCopy(SFile fnew) throws IOException {
		final PrintWriter pw = fnew.createPrintWriter();
		pw.println(rawData.get(0));
		pw.println(svgPath.toSvg());
		pw.println(rawData.get(rawData.size() - 1));
		pw.close();

	}

	private Dimension2D getDimension(double factor) {
		final String width = getNumber(rawData.get(0), "width");
		final String height = getNumber(rawData.get(0), "height");
		return new Dimension2DDouble(Integer.parseInt(width) * factor, Integer.parseInt(height) * factor);
	}

	private String getNumber(String s, String arg) {
		int x1 = s.indexOf(arg);
		if (x1 == -1) {
			throw new IllegalArgumentException();
		}
		x1 = s.indexOf("\"", x1);
		if (x1 == -1) {
			throw new IllegalArgumentException();
		}
		final int x2 = s.indexOf("\"", x1 + 1);
		if (x2 == -1) {
			throw new IllegalArgumentException();
		}
		return s.substring(x1 + 1, x2);
	}

	public TextBlock asTextBlock(final HColor color, final double factor) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
				svgPath.drawMe(ug.apply(color), factor);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return getDimension(factor);
			}
		};
	}

}
