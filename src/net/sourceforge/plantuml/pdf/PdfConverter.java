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
package net.sourceforge.plantuml.pdf;

import java.lang.reflect.Method;

import net.sourceforge.plantuml.security.SFile;

public class PdfConverter {

	public static void convert(SFile svgFile, SFile pdfFile) {

		if (svgFile.exists() == false) {
			throw new IllegalArgumentException();
		}
		pdfFile.delete();
		if (pdfFile.exists()) {
			throw new IllegalStateException();
		}

		try {
			// https://stackoverflow.com/questions/12579468/how-to-set-log4j-property-file
			System.setProperty("log4j.debug", "false");

			final Class<?> clSVGConverter = Class.forName("org.apache.batik.apps.rasterizer.SVGConverter");

			final Object converter = clSVGConverter.newInstance();

			final Class<?> clDestinationType = Class.forName("org.apache.batik.apps.rasterizer.DestinationType");
			final Object pdf = clDestinationType.getField("PDF").get(null);
			final Method setDestinationType = clSVGConverter.getMethod("setDestinationType", clDestinationType);

			setDestinationType.invoke(converter, pdf);

			final String[] path = new String[] { svgFile.getAbsolutePath() };
			final Method setSources = clSVGConverter.getMethod("setSources", path.getClass());
			setSources.invoke(converter, new Object[] { path });
			final Method setDst = clSVGConverter.getMethod("setDst", pdfFile.getClass());
			setDst.invoke(converter, new Object[] { pdfFile });
			final Method execute = clSVGConverter.getMethod("execute");
			execute.invoke(converter);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
		if (pdfFile.exists() == false) {
			throw new IllegalStateException();
		}
	}
}
