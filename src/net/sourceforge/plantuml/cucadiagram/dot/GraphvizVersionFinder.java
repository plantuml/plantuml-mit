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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;

public class GraphvizVersionFinder {

	final private File dotExe;
	final public static GraphvizVersion DEFAULT = new GraphvizVersion() {
		public boolean useShield() {
			return true;
		}

		public boolean useProtectionWhenThereALinkFromOrToGroup() {
			return true;
		}

		public boolean useXLabelInsteadOfLabel() {
			return false;
		}

		public boolean isVizjs() {
			return false;
		}

		public boolean ignoreHorizontalLinks() {
			return false;
		}
	};

	public GraphvizVersionFinder(File dotExe) {
		this.dotExe = dotExe;
	}

	public GraphvizVersion getVersion() {
		final String dotVersion = dotVersion();
		final Pattern p = Pattern.compile("\\d\\.\\d\\d");
		final Matcher m = p.matcher(dotVersion);
		final boolean find = m.find();
		if (find == false) {
			return DEFAULT;
		}
		final String vv = m.group(0);
		final int v = Integer.parseInt(vv.replaceAll("\\.", ""));
		return new GraphvizVersion() {
			public boolean useShield() {
				return v <= 228;
			}

			public boolean useProtectionWhenThereALinkFromOrToGroup() {
				if (v == 239 || v == 240) {
					return false;
				}
				// return v < 238;
				return true;
			}

			public boolean useXLabelInsteadOfLabel() {
				return false;
			}

			public boolean isVizjs() {
				return false;
			}

			public boolean ignoreHorizontalLinks() {
				if (v == 230) {
					return true;
				}
				return false;
			}

		};
	}

	public String dotVersion() {
		final String cmd[] = getCommandLine();

		final ProcessRunner p = new ProcessRunner(cmd);
		final ProcessState state = p.run(null, null);
		if (state.differs(ProcessState.TERMINATED_OK())) {
			return "?";
		}
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotEmpty(p.getOut())) {
			sb.append(p.getOut());
		}
		if (StringUtils.isNotEmpty(p.getError())) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(p.getError());
		}
		return StringUtils.trin(sb.toString().replace('\n', ' '));
	}

	private String[] getCommandLine() {
		return new String[] { dotExe.getAbsolutePath(), "-V" };
	}

}
