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

public class UnsupportedC implements __ptr__ {

	public int getInt() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setInt(int value) {
		throw new UnsupportedOperationException(getClass().toString());
	}
	

	public int minus(__ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public double getDouble() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setDouble(double value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ getPtr() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setPtr(__ptr__ value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __struct__ getStruct() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setStruct(__struct__ value) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ setPtr(String fieldName, __ptr__ data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setStruct(String fieldName, __struct__ data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setInt(String fieldName, int data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void setDouble(String fieldName, double data) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ castTo(Class dest) {
		throw new UnsupportedOperationException(getClass().toString());
	}


	public Object addVirtualBytes(int bytes) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	final public __ptr__ unsupported() {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public __ptr__ plus(int pointerMove) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public int comparePointer(__ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public String getDebug(String fieldName) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void copyDataFrom(__ptr__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

	public void copyDataFrom(__struct__ other) {
		throw new UnsupportedOperationException(getClass().toString());
	}

}
