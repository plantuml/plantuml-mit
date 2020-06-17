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
package net.sourceforge.plantuml.version;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SignatureUtils;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.flashcode.FlashCodeFactory;
import net.sourceforge.plantuml.flashcode.FlashCodeUtils;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.AffineTransformType;
import net.sourceforge.plantuml.ugraphic.PixelImage;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PSystemKeycheck extends AbstractPSystem {

	final private String key;
	final private String sig;

	public PSystemKeycheck(String sig, String key) {
		this.sig = sig;
		this.key = key;
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final ImageBuilder imageBuilder = ImageBuilder.buildA(new ColorMapperIdentity(), false, null, getMetadata(),
				null, 1.0, HColorUtils.WHITE);

		imageBuilder.setUDrawable(new UDrawable() {
			public void drawU(UGraphic ug) {
				try {
					drawInternal(ug);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Key)");
	}

	private void drawInternal(UGraphic ug) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		final List<String> strings = header();
		try {
			final LicenseInfo info = PLSSignature.retrieveNamed(sig, key, false);
			strings.add("<u>Provided license information</u>:");
			License.addLicenseInfo(strings, info);
			strings.add(" ");
		} catch (Exception e) {
			e.printStackTrace();
			strings.add("<i>Error:</i> " + e);
		}

		final TextBlock disp = GraphicStrings.createBlackOnWhite(strings);
		disp.drawU(ug);
	}

	private ArrayList<String> header() {
		final ArrayList<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.versionString() + "</b> (" + Version.compileTimeString() + ")");
		strings.add("(" + License.getCurrent() + " source distribution)");
//		if (OptionFlags.ALLOW_INCLUDE) {
//			strings.add("Loaded from " + Version.getJarPath());
//		}
		strings.add(" ");
		return strings;
	}

	private void drawFlash(UGraphic ug, LicenseInfo info) throws IOException {
		final List<String> strings = header();
		strings.add("To get your <i>Professional Edition License</i>,");
		strings.add("please send this qrcode to <b>plantuml@gmail.com</b> :");

		TextBlock disp = GraphicStrings.createBlackOnWhite(strings);
		disp.drawU(ug);

		ug = ug.apply(UTranslate.dy(disp.calculateDimension(ug.getStringBounder()).getHeight()));
		final FlashCodeUtils utils = FlashCodeFactory.getFlashCodeUtils();
		final BufferedImage im = utils.exportFlashcode(
				Version.versionString() + "\n" + SignatureUtils.toHexString(PLSSignature.signature()), Color.BLACK,
				Color.WHITE);
		if (im != null) {
			final UImage flash = new UImage(new PixelImage(im, AffineTransformType.TYPE_NEAREST_NEIGHBOR))
					.scale(4);
			ug.draw(flash);
			ug = ug.apply(UTranslate.dy(flash.getHeight()));
		}

		if (info.isNone() == false) {
			strings.clear();
			strings.add("<u>Installed license</u>:");
			License.addLicenseInfo(strings, info);
			strings.add(" ");
			disp = GraphicStrings.createBlackOnWhite(strings);
			disp.drawU(ug);
		}

	}
}
