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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.DefinesGet;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.IfManagerFilter;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineNumbered;

public class Preprocessor implements ReadLineNumbered {

	private final ReadLine source;
	private final PreprocessorInclude include;
	private final PreprocessorModeSet mode;
	private final ReadLine sourceV2;

	public Preprocessor(List<String> config, ReadLine reader, String charset, Defines defines,
			DefinitionsContainer definitionsContainer, ImportedFiles importedFiles) throws IOException {
		this(config, reader, charset, new DefinesGet(defines), definitionsContainer, new HashSet<FileWithSuffix>(),
				importedFiles, true);
	}

	Preprocessor(List<String> config, ReadLine reader, String charset, DefinesGet defines,
			DefinitionsContainer definitionsContainer, Set<FileWithSuffix> filesUsedGlobal,
			ImportedFiles importedFiles, boolean doSaveState) throws IOException {
		this.mode = definitionsContainer;
		if (doSaveState) {
			defines.saveState();
		}
		final ReadFilterAnd filtersV2 = new ReadFilterAnd();
		filtersV2.add(new ReadLineQuoteComment(true));
		filtersV2.add(new ReadLineAddConfig(config));
		this.sourceV2 = filtersV2.applyFilter(reader);

		final ReadFilterAnd filters = new ReadFilterAnd();
		filters.add(new ReadLineQuoteComment(false));
		include = new PreprocessorInclude(config, charset, defines, definitionsContainer, importedFiles,
				filesUsedGlobal);
		filters.add(new ReadLineAddConfig(config));
		filters.add(new IfManagerFilter(defines));
		filters.add(new PreprocessorDefineApply(defines));
		filters.add(new SubPreprocessor(charset, definitionsContainer));
		filters.add(new PreprocessorDefineLearner(defines, importedFiles.getCurrentDir()));
		filters.add(include);

		this.source = filters.applyFilter(reader);
	}

	private boolean isV2() {
		return mode != null && mode.getPreprocessorMode() == PreprocessorMode.V2_NEW_TIM;
	}

	public StringLocated readLine() throws IOException {
		if (isV2()) {
			return sourceV2.readLine();
		}
		return source.readLine();
	}

	public void close() throws IOException {
		this.source.close();
	}

	public Set<FileWithSuffix> getFilesUsed() {
		// System.err.println("************************** WARNING **************************");
		// return Collections.emptySet();
		return Collections.unmodifiableSet(include.getFilesUsedGlobal());
	}
}
