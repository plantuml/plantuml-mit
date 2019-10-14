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
package net.sourceforge.plantuml.core;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.FileFormatOption;

/**
 * Represents a single diagram. A Diagram could be a UML (sequence diagram, class diagram...) or an non-UML diagram.
 * 
 * @author Arnaud Roques
 */
public interface Diagram {

	/**
	 * Export the diagram as an image to some format. Note that a diagram could be drawn as several images (think about
	 * <code>new page</code> for sequence diagram for example).
	 * 
	 * @param os
	 *            where to write the image
	 * @param num
	 *            usually 0 (index of the image to be exported for this diagram).
	 * @param fileFormat
	 *            file format to use
	 * 
	 * @return a description of the generated image
	 * 
	 * @throws IOException
	 */
	ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException;

	/**
	 * Number of images in this diagram (usually, 1)
	 * 
	 * @return usually 1
	 */
	int getNbImages();

	DiagramDescription getDescription();

	String getMetadata();

	String getWarningOrError();

	/**
	 * The original source of the diagram
	 * 
	 * @return
	 */
	UmlSource getSource();

	/**
	 * Check if the Diagram have some links.
	 * 
	 * @return
	 */
	public boolean hasUrl();

}
