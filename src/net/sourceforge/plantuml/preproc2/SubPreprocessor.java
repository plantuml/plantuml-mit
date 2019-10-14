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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.ReadLineSimple;
import net.sourceforge.plantuml.preproc.Sub;

public class SubPreprocessor implements ReadFilter {

	private static final String ID = "[A-Za-z_][A-Za-z_0-9]*";

	private static final Pattern2 includeSubPattern = MyPattern.cmpile("^[%s]*!includesub[%s]+[%g]?([^%g]+)[%g]?$");

	private static final Pattern2 startsub = MyPattern.cmpile("^[%s]*!startsub[%s]+(" + ID + ")");
	private static final Pattern2 endsub = MyPattern.cmpile("^[%s]*!endsub[%s]*");

	private final DefinitionsContainer definitionsContainer;
	private final String charset;

	public SubPreprocessor(String charset, DefinitionsContainer definitionsContainer) {
		this.charset = charset;
		this.definitionsContainer = definitionsContainer;
	}

	private final Map<String, Sub> subs = new HashMap<String, Sub>();
	private Sub learningSub;
	private ReadLine includedSub;

	public ReadLine applyFilter(ReadLine source) {
		return new InnerReadLine(source);
	}

	class InnerReadLine implements ReadLine {
		final ReadLine source;

		public InnerReadLine(ReadLine source) {
			this.source = source;
		}

		private StringLocated manageStartsub(Matcher2 m) throws IOException {
			final String name = m.group(1);
			learningSub = getSub(name);
			return this.readLine();
		}

		private StringLocated manageEndsub(Matcher2 m) throws IOException {
			learningSub = null;
			return this.readLine();
		}

		public void close() throws IOException {
			source.close();
		}

		private StringLocated manageIncludeSub(StringLocated s, Matcher2 m) throws IOException {
			final String name = m.group(1);
			final int idx = name.indexOf('!');
			if (idx != -1) {
				final String filename = name.substring(0, idx);
				final String blocname = name.substring(idx + 1);
				final File f = FileSystem.getInstance().getFile(PreprocessorInclude.withEnvironmentVariable(filename));
				if (f.exists() == false || f.isDirectory()) {
					Log.error("Cannot include " + FileWithSuffix.getAbsolutePath(f));
					return s.withErrorPreprocessor("Cannot include " + FileWithSuffix.getFileName(f));
				}
				final SubPreprocessor data = new SubPreprocessor(charset, definitionsContainer);
				InnerReadLine tmp = (InnerReadLine) data.applyFilter(getReaderIncludeWithoutComment(s, f));
				while (tmp.readLine() != null) {
					// Read file
				}
				tmp.close();
				includedSub = tmp.getSub(blocname).getReadLine(s.getLocation());
			} else {
				includedSub = getSub(name).getReadLine(s.getLocation());
			}
			return this.readLine();
		}

		public StringLocated readLine() throws IOException {
			if (includedSub != null) {
				final StringLocated s = includedSub.readLine();
				if (s != null) {
					eventuallyLearn(s);
					return s;
				}
				includedSub = null;
			}

			final StringLocated s = source.readLine();
			if (s == null) {
				return null;
			}

			final Matcher2 m1 = includeSubPattern.matcher(s.getString());
			if (m1.find()) {
				return manageIncludeSub(s, m1);
			}

			Matcher2 m = startsub.matcher(s.getString());
			if (m.find()) {
				return manageStartsub(m);
			}

			m = endsub.matcher(s.getString());
			if (m.find()) {
				return manageEndsub(m);
			}
			eventuallyLearn(s);
			return s;
		}

		private void eventuallyLearn(final StringLocated s) {
			if (learningSub != null) {
				learningSub.add(s);
			}
		}

		Sub getSub(String name) {
			Sub result = subs.get(name);
			if (result == null) {
				result = new Sub(name);
				subs.put(name, result);
			}
			return result;
		}

	}

	private ReadLine getReaderIncludeWithoutComment(StringLocated s, final File f) {
		return new ReadLineQuoteComment(false).applyFilter(getReaderIncludeRaw(s, f));
	}

	private ReadLine getReaderIncludeRaw(StringLocated s, final File f) {
		try {
			if (charset == null) {
				Log.info("Using default charset");
				return ReadLineReader.create(new FileReader(f), FileWithSuffix.getFileName(f), s.getLocation());
			}
			Log.info("Using charset " + charset);
			return ReadLineReader.create(new InputStreamReader(new FileInputStream(f), charset), FileWithSuffix.getFileName(f),
					s.getLocation());
		} catch (IOException e) {
			return new ReadLineSimple(s, e.toString());
		}

	}

}
