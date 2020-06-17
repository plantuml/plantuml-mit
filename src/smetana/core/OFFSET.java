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

package smetana.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OFFSET {

	private static int CPT = 10000;
	private static Map<Integer, OFFSET> byID = new HashMap<Integer, OFFSET>();
	private static Map<Object, OFFSET> primaryKey = new HashMap<Object, OFFSET>();

	private final Class cl;
	private final String field;
	private final int id;

	private OFFSET(Class cl, String field) {
		this.cl = cl;
		this.field = field;
		this.id = CPT++;
		JUtils.LOG("REAL CREATING OF " + this);
	}

	@Override
	public String toString() {
		return cl.getName() + "::" + field;
	}

	public static OFFSET create(Class cl, String field) {
		final Object key = Arrays.asList(cl, field);
		JUtils.LOG("getting OFFSET " + key);
		OFFSET result = primaryKey.get(key);
		if (result != null) {
			JUtils.LOG("FOUND!");
			return result;
		}
		result = new OFFSET(cl, field);
		byID.put(result.id, result);
		primaryKey.put(key, result);
		return result;
	}

	public int toInt() {
		return id;
	}

	public static OFFSET fromInt(int value) {
		final OFFSET result = byID.get(value);
		if (result == null) {
			throw new IllegalArgumentException("value=" + value);
		}
		return result;
	}

	public final Class getTheClass() {
		return cl;
	}

	public final String getField() {
		return field;
	}

}
