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
package net.sourceforge.plantuml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public abstract class SourceFileReaderAbstract {

	protected File file;
	protected File outputDirectory;
	protected File outputFile;

	protected BlockUmlBuilder builder;
	protected FileFormatOption fileFormatOption;
	private boolean checkMetadata;

	public void setCheckMetadata(boolean checkMetadata) {
		this.checkMetadata = checkMetadata;
	}

	public boolean hasError() {
		for (final BlockUml b : builder.getBlockUmls()) {
			if (b.getDiagram() instanceof PSystemError) {
				return true;
			}
		}
		return false;
	}

	public List<BlockUml> getBlocks() {
		return builder.getBlockUmls();
	}

	protected Reader getReader(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		if (charset == null) {
			Log.info("Using default charset");
			return new InputStreamReader(new BufferedInputStream(new FileInputStream(file)));
		}
		Log.info("Using charset " + charset);
		return new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), charset);
	}

	public final Set<FileWithSuffix> getIncludedFiles() {
		return builder.getIncludedFiles();
	}

	public final void setFileFormatOption(FileFormatOption fileFormatOption) {
		this.fileFormatOption = fileFormatOption;
	}

	protected boolean endsWithSlashOrAntislash(String newName) {
		return newName.endsWith("/") || newName.endsWith("\\");
	}

	private List<GeneratedImage> getCrashedImage(BlockUml blockUml, Throwable t, SFile outputFile) throws IOException {
		final GeneratedImage image = new GeneratedImageImpl(outputFile, "Crash Error", blockUml, FileImageData.CRASH);
		OutputStream os = null;
		try {
			os = outputFile.createBufferedOutputStream();
			UmlDiagram.exportDiagramError(os, t, fileFormatOption, 42, null, blockUml.getFlashData(),
					UmlDiagram.getFailureText2(t, blockUml.getFlashData()));
		} finally {
			if (os != null) {
				os.close();
			}
		}

		return Collections.singletonList(image);
	}

	protected void exportWarnOrErrIfWord(final SFile f, final Diagram system) throws FileNotFoundException {
		if (OptionFlags.getInstance().isWord()) {
			final String warnOrError = system.getWarningOrError();
			if (warnOrError != null) {
				final String name = f.getName().substring(0, f.getName().length() - 4) + ".err";
				final SFile errorFile = f.getParentFile().file(name);
				final PrintStream ps = SecurityUtils.createPrintStream(errorFile.createFileOutputStream());
				ps.print(warnOrError);
				ps.close();
			}
		}
	}

	protected int cpt;

	final public List<GeneratedImage> getGeneratedImages() throws IOException {
		Log.info("Reading file: " + file);

		cpt = 0;
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();

		for (BlockUml blockUml : builder.getBlockUmls()) {
			final SuggestedFile suggested = getSuggestedFile(blockUml);

			final Diagram system;
			try {
				system = blockUml.getDiagram();
			} catch (Throwable t) {
				t.printStackTrace();
				return getCrashedImage(blockUml, t, suggested.getFile(0));
			}

			if (OptionFlags.getInstance().isSilentlyCompletelyIgnoreErrors() && system instanceof PSystemError) {
				continue;
			}

			OptionFlags.getInstance().logData(SFile.fromFile(file), system);
			final List<FileImageData> exportDiagrams = PSystemUtils.exportDiagrams(system, suggested, fileFormatOption,
					checkMetadata);
			if (exportDiagrams.size() > 1) {
				cpt += exportDiagrams.size() - 1;
			}

			for (FileImageData fdata : exportDiagrams) {
				final String desc = "[" + file.getName() + "] " + system.getDescription();
				final SFile f = fdata.getFile();
				exportWarnOrErrIfWord(f, system);
				final GeneratedImage generatedImage = new GeneratedImageImpl(f, desc, blockUml, fdata.getStatus());
				result.add(generatedImage);
			}

		}

		Log.info("Number of image(s): " + result.size());

		return Collections.unmodifiableList(result);
	}

	abstract protected SuggestedFile getSuggestedFile(BlockUml blockUml) throws FileNotFoundException;

}
