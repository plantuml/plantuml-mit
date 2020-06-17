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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SvgString;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.security.ImageIO;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.MutableImage;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;

public class ScientificEquationSafe {

	private final ScientificEquation equation;
	private final String formula;

	private ScientificEquationSafe(String formula, ScientificEquation equation) {
		this.formula = formula;
		this.equation = equation;
	}

	public static ScientificEquationSafe fromAsciiMath(String formula) {
		try {
			return new ScientificEquationSafe(formula, new AsciiMath(formula));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	public static ScientificEquationSafe fromLatex(String formula) {
		try {
			return new ScientificEquationSafe(formula, new LatexBuilder(formula));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	private ImageData dimSvg;

	public SvgString getSvg(double scale, Color foregroundColor, Color backgroundColor) {
		try {
			final SvgString svg = equation.getSvg(scale, foregroundColor, backgroundColor);
			dimSvg = new ImageDataSimple(equation.getDimension());
			return svg;
		} catch (Exception e) {
			printTrace(e);
			final ImageBuilder imageBuilder = getRollback();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				dimSvg = imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.SVG), 42, baos);
			} catch (IOException e1) {
				return null;
			}
			return new SvgString(new String(baos.toByteArray()), scale);
		}
	}

	public MutableImage getImage(Color foregroundColor, Color backgroundColor) {
		try {
			return equation.getImage(foregroundColor, backgroundColor);
		} catch (Exception e) {
			printTrace(e);
			final ImageBuilder imageBuilder = getRollback();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.PNG), 42, baos);
				return new PixelImage(ImageIO.read(new ByteArrayInputStream(baos.toByteArray())),
						AffineTransformType.TYPE_BILINEAR);
			} catch (IOException e1) {
				return null;
			}
		}
	}

	private void printTrace(Exception e) {
		System.err.println("formula=" + formula);
		if (equation != null) {
			System.err.println("Latex=" + equation.getSource());
		}
		e.printStackTrace();
	}

	private ImageBuilder getRollback() {
		final TextBlock block = GraphicStrings.createBlackOnWhiteMonospaced(Arrays.asList(formula));
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, null, null, 1.0,
				null);
		imageBuilder.setUDrawable(block);
		return imageBuilder;
	}

	public ImageData export(OutputStream os, FileFormatOption fileFormat, float scale, Color foregroundColor,
			Color backgroundColor) throws IOException {
		if (fileFormat.getFileFormat() == FileFormat.PNG) {
			final BufferedImage image = getImage(foregroundColor, backgroundColor).withScale(scale).getImage();
			ImageIO.write(image, "png", os);
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		}
		if (fileFormat.getFileFormat() == FileFormat.SVG) {
			os.write(getSvg(1, foregroundColor, backgroundColor).getSvg(true).getBytes());
			return dimSvg;
		}
		if (fileFormat.getFileFormat() == FileFormat.EPS) {
			final BufferedImage image = getImage(foregroundColor, backgroundColor).withScale(scale).getImage();
			final EpsGraphics out = new EpsGraphics();
			out.drawImage(image, 0, 0);
			out.close();
			os.write(out.getEPSCode().getBytes());
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		}
		throw new UnsupportedOperationException();
	}

	public final String getFormula() {
		return formula;
	}

}
