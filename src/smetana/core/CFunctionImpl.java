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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import smetana.core.amiga.Area;

public class CFunctionImpl extends UnsupportedC implements CFunction, Area {

	private final Class codingClass;
	private final String name;
	private final Method method;

	public static CFunctionImpl create(Class codingClass, String name) {
		return new CFunctionImpl(codingClass, name);
	}

	private CFunctionImpl(Class codingClass, String name) {
		this.codingClass = codingClass;
		this.name = name;
		for (Method m : codingClass.getMethods()) {
			if (m.getName().equals(name)) {
				this.method = m;
				return;
			}
		}
		JUtils.LOG("CANNOT FIND METHOD " + name + " IN " + codingClass);
		throw new IllegalStateException("codingClass=" + codingClass + " name=" + name);
	}

	@Override
	public String toString() {
		return codingClass.getName() + "::" + name;
	}

	public Object exe(Object... args) {
		JUtils.LOG("-------");
		for (Object arg : args) {
			JUtils.LOG("arg=" + arg);
		}
		JUtils.LOG("method="+method);
		try {
			return this.method.invoke(null, args);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(toString());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(toString());
		}
	}

	public String getName() {
		return name;
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException();
	}
	
}
