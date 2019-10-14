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

package smetana.core;

import java.util.ArrayList;
import java.util.List;

public class CEnumInterpretor {

	private final List<String> keys = new ArrayList<String>();
	private String keyRef;
	private int valueRef;

	public CEnumInterpretor(Class enumClass) {
		List<String> def = CType.getDefinition(enumClass);
		JUtils.LOG("def1=" + def);
		if (def.get(0).equals("typedef enum") == false) {
			throw new IllegalArgumentException();
		}
		if (def.get(1).equals("{") == false) {
			throw new IllegalArgumentException();
		}
		if (def.get(def.size() - 2).equals("}") == false) {
			throw new IllegalArgumentException();
		}
		def = def.subList(2, def.size() - 2);
		JUtils.LOG("def2=" + def);

		for (String s1 : def) {
			for (String s2 : s1.split(",")) {
				s2 = s2.trim();
				final int idx = s2.indexOf('=');
				final String k = idx == -1 ? s2 : s2.substring(0, idx);
				keys.add(k.trim());
				if (idx == -1) {
					continue;
				}
				if (keyRef != null) {
					throw new IllegalStateException();
				}
				keyRef = k.trim();
				valueRef = Integer.parseInt(s2.substring(idx + 1).trim());
			}
		}
		JUtils.LOG("keys=" + keys);
	}

	public int valueOf(String name) {
		JUtils.LOG("keys=" + keys);
		final int idx = keys.indexOf(name);
		if (idx == -1) {
			throw new IllegalArgumentException(name + " is no enum value");
		}
		if (keyRef == null) {
			return idx;
		}
		final int keyRefIndex = keys.indexOf(keyRef);
		if (keyRefIndex == -1) {
			throw new IllegalStateException();
		}
		return idx - keyRefIndex + valueRef;
	}
}
