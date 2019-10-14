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
package net.sourceforge.plantuml.classdiagram.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.cucadiagram.LeafType;

class JavaFile {

	private static final Pattern2 classDefinition = MyPattern
			.cmpile("^(?:public[%s]+|abstract[%s]+|final[%s]+)*(class|interface|enum|annotation)[%s]+(\\w+)(?:.*\\b(extends|implements)[%s]+([\\w%s,]+))?");

	private static final Pattern2 packageDefinition = MyPattern.cmpile("^package[%s]+([\\w+.]+)[%s]*;");

	private final List<JavaClass> all = new ArrayList<JavaClass>();

	public JavaFile(File f) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			initFromReader(br);
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	private void initFromReader(BufferedReader br) throws IOException {
		String javaPackage = null;
		String s;
		while ((s = br.readLine()) != null) {
			s = StringUtils.trin(s);
			final Matcher2 matchPackage = packageDefinition.matcher(s);
			if (matchPackage.find()) {
				javaPackage = matchPackage.group(1);
			} else {
				final Matcher2 matchClassDefinition = classDefinition.matcher(s);
				if (matchClassDefinition.find()) {
					final String n = matchClassDefinition.group(2);
					final String p = matchClassDefinition.group(4);
					final LeafType type = LeafType.valueOf(StringUtils.goUpperCase(matchClassDefinition.group(1)));
					final LeafType parentType = getParentType(type, matchClassDefinition.group(3));
					all.add(new JavaClass(javaPackage, n, p, type, parentType));
				}
			}
		}
	}

	static LeafType getParentType(LeafType type, String extendsOrImplements) {
		if (extendsOrImplements == null) {
			return null;
		}
		if (type == LeafType.CLASS) {
			if (extendsOrImplements.equals("extends")) {
				return LeafType.CLASS;
			}
			return LeafType.INTERFACE;
		}
		return LeafType.INTERFACE;
	}

	public List<JavaClass> getJavaClasses() {
		return Collections.unmodifiableList(all);

	}

}
