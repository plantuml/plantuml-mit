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
package net.sourceforge.plantuml.postit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public class PostItDiagram extends UmlDiagram {

	private final Area defaultArea = new Area('\0', null);

	private final Map<String, PostIt> postIts = new HashMap<String, PostIt>();

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return null;
	}

	@Override
	final protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final UGraphic ug = createImage(fileFormatOption);
		drawU(ug);
		if (ug instanceof UGraphicG2d) {
			final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
			PngIO.write(im, os, fileFormatOption.isWithMetadata() ? getMetadata() : null, 96);
		} else if (ug instanceof UGraphicSvg) {
			final UGraphicSvg svg = (UGraphicSvg) ug;
			svg.createXml(os, fileFormatOption.isWithMetadata() ? getMetadata() : null);
		} else if (ug instanceof UGraphicEps) {
			final UGraphicEps eps = (UGraphicEps) ug;
			os.write(eps.getEPSCode().getBytes());
		}
		return ImageDataSimple.ok();
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("Board of post-it");
	}

	public Area getDefaultArea() {
		return defaultArea;
	}

	public Area createArea(char id) {
		throw new UnsupportedOperationException();
	}

	public PostIt createPostIt(String id, Display text) {
		if (postIts.containsKey(id)) {
			throw new IllegalArgumentException();
		}
		final PostIt postIt = new PostIt(id, text);
		postIts.put(id, postIt);
		getDefaultArea().add(postIt);
		return postIt;
	}

	void drawU(UGraphic ug) {
		getDefaultArea().drawU(ug, width);
	}

	private UGraphic createImage(FileFormatOption fileFormatOption) {
		final Color backColor = getSkinParam().getColorMapper()
				.toColor(this.getSkinParam().getBackgroundColor(false));
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			final double height = getDefaultArea().heightWhenWidthIs(width, fileFormatOption.getDefaultStringBounder());
			final EmptyImageBuilder builder = new EmptyImageBuilder(fileFormatOption.getWatermark(), width, height, backColor);

			final Graphics2D graphics2D = builder.getGraphics2D();
			final double dpiFactor = this.getScaleCoef(fileFormatOption);
			final UGraphicG2d result = new UGraphicG2d(new ColorMapperIdentity(), graphics2D, dpiFactor);
			result.setBufferedImage(builder.getBufferedImage());
			return result;
		}
		throw new UnsupportedOperationException();
	}

	private int width = 800;

	public void setWidth(int width) {
		this.width = width;
	}

}
