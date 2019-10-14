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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;

public class PSystemMath extends AbstractPSystem {

	private String math = "";
	private float scale = 1;
	private Color color = Color.BLACK;
	private Color backColor = Color.WHITE;

	public PSystemMath() {
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Math)");
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final ScientificEquationSafe asciiMath = ScientificEquationSafe.fromAsciiMath(math);
		return asciiMath.export(os, fileFormat, scale, color, backColor);
	}

	public void doCommandLine(String line) {
		final String lineLower = StringUtils.trin(StringUtils.goLowerCase(line));
		final String colorParam = "color ";
		final String backParam = "backgroundcolor ";
		if (lineLower.startsWith(colorParam)) {
			final Color col3 = getColor(line.substring(colorParam.length()));
			if (col3 != null) {
				color = col3;
			}
		} else if (lineLower.startsWith(backParam)) {
			final Color col3 = getColor(line.substring(backParam.length()));
			if (col3 != null) {
				backColor = col3;
			}
		} else if (lineLower.startsWith("scale ")) {
			final String value = line.substring("scale ".length());
			try {
				final float scale1 = Float.parseFloat(value);
				if (scale1 > 0) {
					scale = scale1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (lineLower.startsWith("dpi ")) {
			final String value = line.substring("dpi ".length());
			try {
				final float dpi1 = Float.parseFloat(value);
				if (dpi1 > 0) {
					scale = dpi1 / 96;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.math = line;

		}
	}

	private Color getColor(final String col) {
		final HtmlColor col2 = new HtmlColorSetSimple().getColorIfValid(col);
		final Color col3 = new ColorMapperIdentity().getMappedColor(col2);
		return col3;
	}

}
