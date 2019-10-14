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
package net.sourceforge.plantuml.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.error.PSystemError;

public class SyntaxResult {

	private UmlDiagramType umlDiagramType;
	private boolean isError;
	private String description;
	private Collection<String> errors = new TreeSet<String>();
	private boolean hasCmapData;
	private PSystemError systemError;
	private LineLocation lineLocation;

	public UmlDiagramType getUmlDiagramType() {
		return umlDiagramType;
	}

	public boolean isError() {
		return isError;
	}

	public String getDescription() {
		return description;
	}

	public Collection<String> getErrors() {
		return Collections.unmodifiableCollection(errors);
	}

	public void setUmlDiagramType(UmlDiagramType umlDiagramType) {
		this.umlDiagramType = umlDiagramType;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addErrorText(String error) {
		this.errors.add(error);
	}

	public final boolean hasCmapData() {
		return hasCmapData;
	}

	public final void setCmapData(boolean hasCmapData) {
		this.hasCmapData = hasCmapData;
	}

	public void setSystemError(PSystemError systemError) {
		this.systemError = systemError;
	}

	public void generateDiagramDescriptionForError(OutputStream os, FileFormatOption fileFormatOption)
			throws IOException {
		if (systemError == null) {
			throw new IllegalStateException();
		}
		systemError.exportDiagram(os, 0, fileFormatOption);
	}

	public void setLineLocation(LineLocation lineLocation) {
		this.lineLocation = lineLocation;
	}

	public LineLocation getLineLocation() {
		return lineLocation;
	}

}
