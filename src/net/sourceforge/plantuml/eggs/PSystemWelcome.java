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
package net.sourceforge.plantuml.eggs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.version.PSystemVersion;

public class PSystemWelcome extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private final GraphicPosition position;

	public PSystemWelcome(GraphicPosition position) {
		this.position = position;
		strings.add("<b>Welcome to PlantUML!");
		strings.add(" ");
		strings.add("If you use this software, you accept its license.");
		strings.add("(details by typing \"\"license\"\" keyword)");
		strings.add(" ");
		strings.add("You can start with a simple UML Diagram like:");
		strings.add(" ");
		strings.add("\"\"Bob->Alice: Hello\"\"");
		strings.add(" ");
		strings.add("Or");
		strings.add(" ");
		strings.add("\"\"class Example\"\"");
		strings.add(" ");
		strings.add("You will find more information about PlantUML syntax on <u>http://plantuml.com</u>");
		if (position == GraphicPosition.BACKGROUND_CORNER_BOTTOM_RIGHT) {
			strings.add(" ");
			strings.add(" ");
			strings.add(" ");
			strings.add(" ");
		}
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int num, FileFormatOption fileFormat, long seed)
			throws IOException {
		final TextBlockBackcolored result = getGraphicStrings();
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, result.getBackcolor(),
				getMetadata(), null, 0, 0, null, false);
		imageBuilder.setUDrawable(result);
		// imageBuilder.setUDrawable(TextBlockUtils.withMargin(result, 4, 4));
		return imageBuilder.writeImageTOBEMOVED(fileFormat, seed, os);
	}

	public TextBlockBackcolored getGraphicStrings() throws IOException {
		if (position != null) {
			return GraphicStrings.createBlackOnWhite(strings, PSystemVersion.getPlantumlImage(), position);
		}
		return GraphicStrings.createBlackOnWhite(strings);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Empty)");
	}

}
