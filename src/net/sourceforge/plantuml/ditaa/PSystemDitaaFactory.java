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
package net.sourceforge.plantuml.ditaa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.PSystemBasicFactory;
import net.sourceforge.plantuml.core.DiagramType;

public class PSystemDitaaFactory extends PSystemBasicFactory<PSystemDitaa> {

	// private StringBuilder data;
	// // -E,--no-separation
	// private boolean performSeparationOfCommonEdges;
	//
	// // -S,--no-shadows
	// private boolean dropShadows;

	public PSystemDitaaFactory(DiagramType diagramType) {
		super(diagramType);
	}

	public PSystemDitaa init(String startLine) {
		boolean performSeparationOfCommonEdges = true;
		if (startLine != null && (startLine.contains("-E") || startLine.contains("--no-separation"))) {
			performSeparationOfCommonEdges = false;
		}
		boolean dropShadows = true;
		if (startLine != null && (startLine.contains("-S") || startLine.contains("--no-shadows"))) {
			dropShadows = false;
		}
		final float scale = extractScale(startLine);
		if (getDiagramType() == DiagramType.UML) {
			return null;
		} else if (getDiagramType() == DiagramType.DITAA) {
			return new PSystemDitaa("", performSeparationOfCommonEdges, dropShadows, scale);
		} else {
			throw new IllegalStateException(getDiagramType().name());
		}
	}

	@Override
	public PSystemDitaa executeLine(PSystemDitaa system, String line) {
		if (system == null && (line.equals("ditaa") || line.startsWith("ditaa("))) {
			boolean performSeparationOfCommonEdges = true;
			if (line.contains("-E") || line.contains("--no-separation")) {
				performSeparationOfCommonEdges = false;
			}
			boolean dropShadows = true;
			if (line.contains("-S") || line.contains("--no-shadows")) {
				dropShadows = false;
			}
			final float scale = extractScale(line);
			return new PSystemDitaa("", performSeparationOfCommonEdges, dropShadows, scale);
		}
		if (system == null) {
			return null;
		}
		return system.add(line);
	}

	private float extractScale(String line) {
		if (line == null) {
			return 1;
		}
		final Pattern p = Pattern.compile("scale=([\\d.]+)");
		final Matcher m = p.matcher(line);
		if (m.find()) {
			final String number = m.group(1);
			return Float.parseFloat(number);
		}
		return 1;
	}
}
