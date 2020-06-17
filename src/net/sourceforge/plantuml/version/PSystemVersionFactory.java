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
package net.sourceforge.plantuml.version;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;
import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class PSystemVersionFactory extends PSystemSingleLineFactory {

	@Override
	protected AbstractPSystem executeLine(String line) {
		try {
			if (line.matches("(?i)^(authors?|about)\\s*$")) {
				return PSystemVersion.createShowAuthors();
			}
			if (line.matches("(?i)^version\\s*$")) {
				return PSystemVersion.createShowVersion();
			}
			if (line.matches("(?i)^stdlib\\s*$")) {
				return PSystemVersion.createStdLib();
			}
			if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE && line.matches("(?i)^path\\s*$")) {
				return PSystemVersion.createPath();
			}
			if (line.matches("(?i)^testdot\\s*$")) {
				return PSystemVersion.createTestDot();
			}
			if (SecurityUtils.getSecurityProfile() == SecurityProfile.UNSECURE
					&& line.matches("(?i)^dumpstacktrace\\s*$")) {
				return PSystemVersion.createDumpStackTrace();
			}
			if (line.matches("(?i)^keydistributor\\s*$")) {
				return PSystemVersion.createKeyDistributor();
			}
			if (line.matches("(?i)^keygen\\s*$")) {
				line = line.trim();
				return new PSystemKeygen("");
			}
			if (line.matches("(?i)^keyimport(\\s+[0-9a-z]+)?\\s*$")) {
				line = line.trim();
				final String key = line.substring("keyimport".length()).trim();
				return new PSystemKeygen(key);
			}
			if (line.matches("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$")) {
				final Pattern p = Pattern.compile("(?i)^keycheck\\s+([0-9a-z]+)\\s+([0-9a-z]+)\\s*$");
				final Matcher m = p.matcher(line);
				if (m.find()) {
					return new PSystemKeycheck(m.group(1), m.group(2));
				}
			}
		} catch (IOException e) {
			Log.error("Error " + e);

		}
		return null;
	}
}
