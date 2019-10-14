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
package net.sourceforge.plantuml.version;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PSystemLicense extends AbstractPSystem implements UDrawable {

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, null, getMetadata(), null,
				0, 0, null, false);
		imageBuilder.setUDrawable(this);
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public static PSystemLicense create() throws IOException {
		return new PSystemLicense();
	}

	private TextBlockBackcolored getGraphicStrings(List<String> strings) {
		return GraphicStrings.createBlackOnWhite(strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(License)");
	}

	public void drawU(UGraphic ug) {

		final LicenseInfo licenseInfo = LicenseInfo.retrieveQuick();
		final BufferedImage logo = LicenseInfo.retrieveDistributorImage(licenseInfo);

		if (logo == null) {
			final List<String> strings = new ArrayList<String>();
			strings.addAll(License.getCurrent().getText1(licenseInfo));
			strings.addAll(License.getCurrent().getText2(licenseInfo));
			getGraphicStrings(strings).drawU(ug);
		} else {
			final List<String> strings1 = new ArrayList<String>();
			final List<String> strings2 = new ArrayList<String>();

			strings1.addAll(License.getCurrent().getText1(licenseInfo));
			strings2.addAll(License.getCurrent().getText2(licenseInfo));

			final TextBlockBackcolored result1 = getGraphicStrings(strings1);
			result1.drawU(ug);
			ug = ug.apply(new UTranslate(0, 4 + result1.calculateDimension(ug.getStringBounder()).getHeight()));
			UImage im = new UImage(logo);
			ug.apply(new UTranslate(20, 0)).draw(im);

			ug = ug.apply(new UTranslate(0, im.getHeight()));
			final TextBlockBackcolored result2 = getGraphicStrings(strings2);
			result2.drawU(ug);
		}
	}
}
