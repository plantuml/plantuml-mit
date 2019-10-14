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
package net.sourceforge.plantuml.preproc2;

import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc.DefinesGet;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineList;

public class PreprocessorDefineApply implements ReadFilter {

	private final DefinesGet defines;

	public PreprocessorDefineApply(DefinesGet defines) throws IOException {
		this.defines = defines;
	}

	public ReadLine applyFilter(final ReadLine source) {
		return new Inner(source);
	}

	class Inner extends ReadLineInsertable {

		final ReadLine source;

		Inner(ReadLine source) {
			this.source = source;
		}

		@Override
		void closeInternal() throws IOException {
			source.close();
		}

		@Override
		StringLocated readLineInternal() throws IOException {
			final StringLocated s = this.source.readLine();
			if (s == null || s.getPreprocessorError() != null) {
				return s;
			}
			if (PreprocessorDefineLearner.isLearningLine(s)) {
				return s;
			}
			final List<String> result = defines.get().applyDefines(s.getString());
			if (result.size() > 1) {
				insert(new ReadLineList(result, s.getLocation()));
				return readLine();
			}
			String tmp = result.get(0);
			return new StringLocated(tmp, s.getLocation(), s.getPreprocessorError());
		}

	}

}
