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
package net.sourceforge.plantuml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.Defines;

public class Pipe {

	private final Option option;
	private final InputStream is;
	private final PrintStream ps;
	private boolean closed = false;
	private final String charset;
	private final Stdrpt stdrpt;

	public Pipe(Option option, PrintStream ps, InputStream is, String charset) {
		this.option = option;
		this.is = is;
		this.ps = ps;
		this.charset = charset;
		this.stdrpt = option.getStdrpt();
	}

	public void managePipe(ErrorStatus error) throws IOException {
		final boolean noStdErr = option.isPipeNoStdErr();
		int nb = 0;
		do {
			final String source = readOneDiagram();
			if (source == null) {
				ps.flush();
				if (nb == 0) {
					// error.goNoData();
				}
				return;
			}
			nb++;
			final Defines defines = option.getDefaultDefines();
			final SourceStringReader sourceStringReader = new SourceStringReader(defines, source, option.getConfig());
			if (option.isComputeurl()) {
				for (BlockUml s : sourceStringReader.getBlocks()) {
					ps.println(s.getEncodedUrl());
				}
			} else if (option.isSyntax()) {
				final Diagram system = sourceStringReader.getBlocks().get(0).getDiagram();
				if (system instanceof UmlDiagram) {
					error.goOk();
					ps.println(((UmlDiagram) system).getUmlDiagramType().name());
					ps.println(system.getDescription());
				} else if (system instanceof PSystemError) {
					error.goWithError();
					stdrpt.printInfo(ps, (PSystemError) system);
				} else {
					error.goOk();
					ps.println("OTHER");
					ps.println(system.getDescription());
				}
			} else if (option.isPipeMap()) {
				final String result = sourceStringReader.getCMapData(option.getImageIndex(),
						option.getFileFormatOption());
				// https://forum.plantuml.net/10049/2019-pipemap-diagrams-containing-links-give-zero-exit-code
				// We don't check errors
				error.goOk();
				if (result == null) {
					ps.println();
				} else {
					ps.println(result);
				}
			} else {
				final OutputStream os = noStdErr ? new ByteArrayOutputStream() : ps;
				final DiagramDescription result = sourceStringReader.outputImage(os, option.getImageIndex(),
						option.getFileFormatOption());
				printInfo(noStdErr ? ps : System.err, sourceStringReader);
				if (result != null && "(error)".equalsIgnoreCase(result.getDescription())) {
					error.goWithError();
				} else {
					error.goOk();
					if (noStdErr) {
						final ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
						baos.close();
						ps.write(baos.toByteArray());
					}
				}
				if (option.getPipeDelimitor() != null) {
					ps.println(option.getPipeDelimitor());
				}
			}
			ps.flush();
		} while (closed == false);
		if (nb == 0) {
			// error.goNoData();
		}
	}

	private void printInfo(final PrintStream output, final SourceStringReader sourceStringReader) {
		final List<BlockUml> blocks = sourceStringReader.getBlocks();
		if (blocks.size() == 0) {
			stdrpt.printInfo(output, null);
		} else {
			stdrpt.printInfo(output, blocks.get(0).getDiagram());
		}
	}

	private boolean isFinished(String s) {
		return s == null || s.startsWith("@end");
	}

	private String readOneDiagram() throws IOException {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final String s = readOneLine();
			if (s == null) {
				closed = true;
			} else {
				sb.append(s);
				sb.append(BackSlash.NEWLINE);
			}
			if (isFinished(s)) {
				break;
			}
		}
		if (sb.length() == 0) {
			return null;
		}
		String source = sb.toString();
		if (source.startsWith("@start") == false) {
			source = "@startuml\n" + source + "\n@enduml";
		}
		return source;
	}

	private String readOneLine() throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			final int read = is.read();
			if (read == -1) {
				if (baos.size() == 0) {
					return null;
				}
				break;
			}
			if (read != '\r' && read != '\n') {
				baos.write(read);
			}
			if (read == '\n') {
				break;
			}
		}
		if (charset == null) {
			return new String(baos.toByteArray());
		}
		return new String(baos.toByteArray(), charset);

	}
}
