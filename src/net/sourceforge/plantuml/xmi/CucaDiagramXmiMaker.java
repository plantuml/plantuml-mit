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
package net.sourceforge.plantuml.xmi;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public final class CucaDiagramXmiMaker {

	private final CucaDiagram diagram;
	private final FileFormat fileFormat;

	public CucaDiagramXmiMaker(CucaDiagram diagram, FileFormat fileFormat) throws IOException {
		this.diagram = diagram;
		this.fileFormat = fileFormat;
	}

	public static String getModel(UmlDiagram classDiagram) {
		return "model1";
	}

	public void createFiles(OutputStream fos) throws IOException {
		try {
			final IXmiClassDiagram xmi;
			if (diagram instanceof StateDiagram) {
				xmi = new XmiStateDiagram((StateDiagram) diagram);
			} else if (diagram instanceof DescriptionDiagram) {
				xmi = new XmiDescriptionDiagram((DescriptionDiagram) diagram);
			} else if (fileFormat == FileFormat.XMI_STANDARD) {
				xmi = new XmiClassDiagramStandard((ClassDiagram) diagram);
			} else if (fileFormat == FileFormat.XMI_ARGO) {
				xmi = new XmiClassDiagramArgo((ClassDiagram) diagram);
			} else if (fileFormat == FileFormat.XMI_STAR) {
				xmi = new XmiClassDiagramStar((ClassDiagram) diagram);
			} else {
				throw new UnsupportedOperationException();
			}
			xmi.transformerXml(fos);
		} catch (ParserConfigurationException e) {
			Log.error(e.toString());
			e.printStackTrace();
			throw new IOException(e.toString());
		} catch (TransformerException e) {
			Log.error(e.toString());
			e.printStackTrace();
			throw new IOException(e.toString());
		}
	}

}
