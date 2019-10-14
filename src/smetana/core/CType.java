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

import h.ST_Ppoly_t;
import h.ST_pointf;
import h.htmllabel_t;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CType {

	final private String type;

	CType(String type) {
		this.type = type;
		if (type.equals("void")) {
			throw new IllegalArgumentException();
		}
	}

	public boolean isInteger() {
		return type.equals("int") || type.equals("packval_t");
	}

	public boolean isChar() {
		return type.equals("char");
	}

	public boolean isBoolean() {
		return type.equals("boolean");
	}

	public boolean isDoubleOrFloat() {
		return type.equals("double") || type.equals("float");
	}

	public boolean isShort() {
		return type.equals("short");
	}

	public boolean isLong() {
		return type.equals("long");
	}

	public boolean isEnum() {
		JUtils.LOG("IS ENUM " + this);
		if (functionPointer() || isVoidStar() || isInteger() || isChar() || isCString() || isBoolean()
				|| isDoubleOrFloat() || isShort() || isLong()) {
			return false;
		}
		if (getType().contains("*")) {
			return false;
		}
		if (getType().contains("[")) {
			return false;
		}
		final Class cl = getTypeClass();
		if (cl == null) {
			return false;
		}
		List<String> def = CType.getDefinition(cl);
		JUtils.LOG("def3=" + def);
		if (def.get(0).equals("typedef enum")) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "CTYPE:" + type;
	}

	public Class getTypeClass() {
		if (isPrimitive()) {
			return null;
		}
		if (type.equals("ST_Ppolyline_t")) {
			return ST_Ppoly_t.class;
		}
		if (type.equals("Ppolyline_t")) {
			return ST_Ppoly_t.class;
		}
		return getClassFrom(type);
	}

	public static Class getClassFrom(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		if (type.equals("h.pointf_s")) {
			return ST_pointf.class;
		}
		if (type.equals("pointf_s")) {
			return ST_pointf.class;
		}
		if (type.endsWith("htmllabel_t")) {
			return htmllabel_t.class;
		}
		try {
			final Class result = Class.forName("h." + type);
			final List<String> definition = getDefinition(result);
			if (definition.size() != 1) {
				return result;
			}
			final String single = definition.get(0);
			final Pattern p1 = Pattern.compile("^typedef\\s+struct\\s+(\\w+)\\s+(\\w+)$");
			final Matcher m1 = p1.matcher(single);
			if (m1.find() == false) {
				// return null;
				throw new IllegalStateException(single);
			}
			if (m1.group(2).equals(type) == false) {
				throw new IllegalStateException(single);
			}
			return getClassFrom(m1.group(1));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> getDefinition() {
		return getDefinition(getTypeClass());
	}

	static List<String> getDefinition(Class inter) {
		try {
			// JUtils.LOG("TR1=" + inter);
			if (inter == null) {
				return null;
			}
			final Field field = inter.getField("DEFINITION");
			return (List<String>) field.get(null);
		} catch (NoSuchFieldException e) {
			System.err.println("inter=" + inter);
			e.printStackTrace();
			throw new UnsupportedOperationException();
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
	}

	public boolean functionPointer() {
		// int (*foo)()
		if (isPrimitive()) {
			return false;
		}
		boolean result = type.matches("^.*(\\(\\*(\\w+)\\)).*$");
		if (result == true) {
			return true;
		}
		try {
			final List<String> definition = getDefinition(Class.forName("h." + type));
			JUtils.LOG("def=" + definition);
			if (definition.size() != 1) {
				return false;
			}
			assert definition.size() == 1;
			result = definition.get(0).matches("^.*(\\(\\*(\\w+)\\)).*$");
			JUtils.LOG("checking " + type + " " + result);
			return result;
		} catch (ClassNotFoundException e) {
			JUtils.LOG("typeXXX=" + type);
			return false;
		}

	}

	public static boolean isPrimitive(String type) {
		if (type.equals("char") || type.equals("long") || type.equals("int") || type.equals("packval_t")
				|| type.equals("boolean") || type.equals("double") || type.equals("float") || type.equals("short")) {
			return true;
		}
		return false;
	}

	public boolean isPrimitive() {
		return isPrimitive(type);
	}

	public Class getClassFrom() {
		return getClassFrom(type);
	}

	public int getArrayLength() {
		final Pattern p = Pattern.compile(".*\\[(\\d+)\\]$");
		final Matcher m = p.matcher(type);
		if (m.find() == false) {
			return 0;
		}
		return Integer.parseInt(m.group(1));
	}

	public boolean isIntStar() {
		return "int*".equals(type);
	}

	public boolean isDoubleStar() {
		return "double*".equals(type);
	}

	public boolean isVoidStar() {
		return "void*".equals(type);
	}

	public String getType() {
		return type;
	}

	public boolean isCString() {
		return type.equals("CString");
	}

	public boolean isArrayOfCString() {
		return type.equals("CString[]");
	}

	public boolean containsStar() {
		return type.contains("*");
	}

}
