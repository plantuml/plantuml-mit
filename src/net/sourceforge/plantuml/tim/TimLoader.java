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
package net.sourceforge.plantuml.tim;

import java.util.List;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.ImportedFiles;

public class TimLoader {

	private final TContext context;
	private final TMemory global = new TMemoryGlobal();
	private boolean preprocessorError;
	private List<StringLocated> resultList;

	public TimLoader(ImportedFiles importedFiles, Defines defines, String charset, DefinitionsContainer definitionsContainer) {
		this.context = new TContext(importedFiles, defines, charset, definitionsContainer);
	}

	public void load(List<StringLocated> input) {
		for (StringLocated s : input) {
			final TLineType type = TLineType.getFromLine(s.getTrimmed().getString());
			try {
				context.executeOneLine(global, type, s, null);
			} catch (EaterException e) {
				context.getResultList().add(s.withErrorPreprocessor(e.getMessage()));
				this.resultList = context.getResultList();
				changeLastLine(context.getDebug(), e.getMessage());
				this.preprocessorError = true;
				return;
			}
		}
		this.resultList = context.getResultList();
	}

	private void changeLastLine(List<StringLocated> list, String message) {
		final int num = list.size() - 1;
		final StringLocated last = list.get(num);
		list.set(num, last.withErrorPreprocessor(message));
	}

	public final List<StringLocated> getResultList() {
		return resultList;
	}

	public final List<StringLocated> getDebug() {
		return context.getDebug();
	}

	public final boolean isPreprocessorError() {
		return preprocessorError;
	}

}
