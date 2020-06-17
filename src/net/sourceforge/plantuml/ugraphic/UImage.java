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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class UImage implements UShape {

	private final MutableImage image;
	private final String formula;
	private final String rawFileName;

	public UImage(MutableImage image) {
		this(image, null, null);
	}

	private UImage(MutableImage image, String rawFileName, String formula) {
		this.image = image;
		this.formula = formula;
		this.rawFileName = rawFileName;
	}

	public final UImage withRawFileName(String rawFileName) {
		return new UImage(image, rawFileName, formula);
	}

	public final UImage withFormula(String formula) {
		return new UImage(image, rawFileName, formula);
	}

	public final String getRawFileName() {
		return rawFileName;
	}

	public final String getFormula() {
		return formula;
	}

	public UImage scale(double scale) {
		return new UImage(image.withScale(scale), rawFileName, formula);
	}

	public final BufferedImage getImage(double withScale) {
		return image.withScale(withScale).getImage();
		// return bufferedImage.getImage();
	}

	public int getWidth() {
		return image.getImage().getWidth() - 1;
	}

	public int getHeight() {
		return image.getImage().getHeight() - 1;
	}

	public UImage muteColor(Color newColor) {
		return new UImage(image.muteColor(newColor), rawFileName, formula);
	}

	public UImage muteTransparentColor(Color newColor) {
		return new UImage(image.muteTransparentColor(newColor), rawFileName, formula);
	}

}
