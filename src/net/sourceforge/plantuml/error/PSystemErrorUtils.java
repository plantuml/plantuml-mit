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
package net.sourceforge.plantuml.error;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;

public class PSystemErrorUtils {

	// public static AbstractPSystemError buildV1(UmlSource source, ErrorUml singleError, List<String> debugLines) {
	// return new PSystemErrorV1(source, singleError, debugLines);
	// }

	public static PSystemError buildV2(UmlSource source, ErrorUml singleError, List<String> debugLines,
			List<StringLocated> list) {
		// return new PSystemErrorV1(source, singleError, debugLines);
		return new PSystemErrorV2(source, list, singleError);
	}

	public static PSystemError merge(Collection<PSystemError> ps) {
		UmlSource source = null;
		final List<ErrorUml> errors = new ArrayList<ErrorUml>();
		// final List<String> debugs = new ArrayList<String>();
		final List<PSystemErrorV2> errorsV2 = new ArrayList<PSystemErrorV2>();
		for (PSystemError system : ps) {
			if (system == null) {
				continue;
			}
			if (system.getSource() != null && source == null) {
				source = system.getSource();
			}
			errors.addAll(system.getErrorsUml());
			// if (system instanceof PSystemErrorV1) {
			// debugs.addAll(((PSystemErrorV1) system).debugLines);
			// if (((PSystemErrorV1) system).debugLines.size() > 0) {
			// debugs.add("-");
			// }
			// }
			if (system instanceof PSystemErrorV2) {
				errorsV2.add((PSystemErrorV2) system);
			}
		}
		if (source == null) {
			throw new IllegalStateException();
		}
		if (errorsV2.size() > 0) {
			return mergeV2(errorsV2);
		}
		throw new IllegalStateException();
		// return new PSystemErrorV1(source, errors, debugs);
	}

	private static PSystemErrorV2 mergeV2(List<PSystemErrorV2> errorsV2) {
		PSystemErrorV2 result = null;
		for (PSystemErrorV2 err : errorsV2) {
			if (result == null || result.size() < err.size()) {
				result = err;
			}
		}
		return result;
	}

	public static boolean isDiagramError(Class<? extends Diagram> type) {
		return PSystemError.class.isAssignableFrom(type);
		// return type == PSystemErrorV1.class;
	}

}
