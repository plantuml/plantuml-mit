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
package net.sourceforge.plantuml.creole;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PSystemCreole extends AbstractPSystem {

	private final List<String> lines = new ArrayList<String>();

	public PSystemCreole() {
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Creole)");
	}

	public void doCommandLine(String line) {
		lines.add(line);
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final Display display = Display.create(lines);
		final UFont font = UFont.serif(14);
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(font);
		final Sheet sheet = new CreoleParser(fontConfiguration, HorizontalAlignment.LEFT, SkinParam.create(UmlDiagramType.SEQUENCE), CreoleMode.FULL)
				.createSheet(display);
		final SheetBlock1 sheetBlock = new SheetBlock1(sheet, LineBreakStrategy.NONE, 0);

		final ImageBuilder builder = new ImageBuilder(new ColorMapperIdentity(), 1.0, null, null, null, 0, 0, null,
				false);
		builder.setUDrawable(sheetBlock);
		return builder.writeImageTOBEMOVED(fileFormat, seed, os);

		// final Dimension2D dim = TextBlockUtils.getDimension(sheetBlock);
		// final UGraphic2 ug = fileFormat.createUGraphic(new ColorMapperIdentity(), 1, dim, null, false);
		// // sheetBlock.drawU(ug.apply(new UTranslate(0, 10)));
		// sheetBlock.drawU(ug);
		// ug.writeImageTOBEMOVED(os, null, 96);
		// return new ImageDataSimple(dim);
	}
}
