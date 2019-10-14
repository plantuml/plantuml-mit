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
package net.sourceforge.plantuml.ugraphic.html5;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TikzFontDistortion;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicHtml5 extends AbstractUGraphic<Html5Drawer> implements ClipContainer, UGraphic2 {

	private final StringBounder stringBounder;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		return this;
	}

	public UGraphicHtml5(ColorMapper colorMapper) {
		super(colorMapper, new Html5Drawer());
		stringBounder = FileFormat.PNG.getDefaultStringBounder(TikzFontDistortion.getDefault());
		registerDriver(URectangle.class, new DriverRectangleHtml5(this));
		// registerDriver(UText.class, new DriverTextEps(imDummy, this, strategy));
		registerDriver(UText.class, new DriverNopHtml5());
		registerDriver(ULine.class, new DriverLineHtml5(this));
		// registerDriver(UPolygon.class, new DriverPolygonEps(this));
		registerDriver(UPolygon.class, new DriverNopHtml5());
		// registerDriver(UEllipse.class, new DriverEllipseEps());
		// registerDriver(UImage.class, new DriverImageEps());
		// registerDriver(UPath.class, new DriverPathEps());
		// registerDriver(DotPath.class, new DriverDotPathEps());
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void startUrl(Url url) {
		// throw new UnsupportedOperationException();

	}

	public void closeAction() {
		// throw new UnsupportedOperationException();

	}

	// public void close() {
	// getEpsGraphics().close();
	// }

	public String generateHtmlCode() {
		return getGraphicObject().generateHtmlCode();
	}

	public void writeImageTOBEMOVED(OutputStream os, String metadata, int dpi) throws IOException {
		os.write(generateHtmlCode().getBytes());
	}

	// public void centerChar(double x, double y, char c, UFont font) {
	// final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);
	//
	// final double xpos = x - unusedSpace.getCenterX() - 0.5;
	// final double ypos = y - unusedSpace.getCenterY() - 0.5;
	//
	// final TextLayout t = new TextLayout("" + c, font.getFont(), imDummy.getFontRenderContext());
	// getGraphicObject().setStrokeColor(getColorMapper().getMappedColor(getParam().getColor()));
	// DriverTextEps.drawPathIterator(getGraphicObject(), xpos + getTranslateX(), ypos + getTranslateY(), t
	// .getOutline(null).getPathIterator(null));
	//
	// }
	//
	// static public String getEpsString(ColorMapper colorMapper, EpsStrategy epsStrategy, UDrawable udrawable)
	// throws IOException {
	// final UGraphicHtml5 ug = new UGraphicHtml5(colorMapper, epsStrategy);
	// udrawable.drawU(ug);
	// return ug.getEPSCode();
	// }
	//
	// static public void copyEpsToFile(ColorMapper colorMapper, UDrawable udrawable, File f) throws IOException {
	// final PrintWriter pw = new PrintWriter(f);
	// final EpsStrategy epsStrategy = EpsStrategy.getDefault2();
	// pw.print(UGraphicHtml5.getEpsString(colorMapper, epsStrategy, udrawable));
	// pw.close();
	// }
	//
	// public void setAntiAliasing(boolean trueForOn) {
	// }
	//
	// public void startUrl(String url, String tooltip) {
	// getGraphicObject().openLink(url);
	// }
	//
	// public void closeAction() {
	// getGraphicObject().closeLink();
	// }

}
