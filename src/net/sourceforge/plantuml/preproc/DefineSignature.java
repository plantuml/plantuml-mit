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
package net.sourceforge.plantuml.preproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class DefineSignature {

	private final String key;
	private final String fonctionName;
	private final List<Variables> variables = new ArrayList<Variables>();
	private final boolean isMethod;

	public DefineSignature(String key, String definitionQuoted) {
		this.key = key;
		this.isMethod = key.contains("(");

		final StringTokenizer st = new StringTokenizer(key, "(),");
		this.fonctionName = st.nextToken().trim();
		final Variables master = new Variables(fonctionName, definitionQuoted);

		while (st.hasMoreTokens()) {
			final String var1 = st.nextToken().trim();
			master.add(new DefineVariable(var1));
		}

		final int count = master.countDefaultValue();
		for (int i = 0; i <= count; i++) {
			variables.add(master.removeSomeDefaultValues(i));
		}
	}

	@Override
	public String toString() {
		return key + "/" + fonctionName;
	}

	public boolean isMethod() {
		return isMethod;
	}

	public String getKey() {
		return key;
	}

	public List<Variables> getVariationVariables() {
		return Collections.unmodifiableList(variables);
	}

	public final String getFonctionName() {
		return fonctionName;
	}

}
