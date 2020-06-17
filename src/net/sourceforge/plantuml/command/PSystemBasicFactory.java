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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

public abstract class PSystemBasicFactory<P extends AbstractPSystem> extends PSystemAbstractFactory {

	public PSystemBasicFactory(DiagramType diagramType) {
		super(diagramType);
	}

	public PSystemBasicFactory() {
		this(DiagramType.UML);
	}

	public abstract P executeLine(P system, String line);

	public P init(String startLine) {
		return null;
	}

	private boolean isEmptyLine(StringLocated result) {
		return result.getTrimmed().getString().length() == 0;
	}

	final public Diagram createSystem(UmlSource source) {
		source = source.removeInitialSkinparam();
		final IteratorCounter2 it = source.iterator2();
		final StringLocated startLine = it.next();
		P system = init(startLine.getString());
		boolean first = true;
		while (it.hasNext()) {
			final StringLocated s = it.next();
			if (first && s != null && isEmptyLine(s)) {
				continue;
			}
			first = false;
			if (StartUtils.isArobaseEndDiagram(s.getString())) {
				if (source.getTotalLineCount() == 2 && source.isStartDef() == false) {
					return buildEmptyError(source, s.getLocation(), it.getTrace());
				}
				if (system != null) {
					system.setSource(source);
				}
				return system;
			}
			system = executeLine(system, s.getString());
			if (system == null) {
				final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", s.getLocation());
				// return PSystemErrorUtils.buildV1(source, err, null);
				return PSystemErrorUtils.buildV2(source, err, null, it.getTrace());
			}
		}
		if (system != null) {
			system.setSource(source);
		}
		return system;
	}

}
