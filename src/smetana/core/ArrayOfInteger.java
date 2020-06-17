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

import smetana.core.amiga.Area;

public class ArrayOfInteger implements __array_of_integer__ {

	private final int[] data;
	private final int position;

	public ArrayOfInteger(int[] data, int position) {
		this.data = data;
		this.position = position;

	}

	public String getUID36() {
		throw new UnsupportedOperationException();
	}

	public void swap(int i, int j) {
		throw new UnsupportedOperationException();
	}

	public void realloc(int nb) {
		throw new UnsupportedOperationException();
	}

	public int comparePointerInternal(__array_of_integer__ other) {
		throw new UnsupportedOperationException();
	}

	public final __array_of_integer__ move(int delta) {
		return plus(delta);
	}

	public __array_of_integer__ plus(int delta) {
		return new ArrayOfInteger(data, position + delta);
	}

	public Area getInternal(int idx) {
		throw new UnsupportedOperationException();
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException();
	}

	public int getInt() {
		return data[position];
	}

	public void setInt(int value) {
		data[position] = value;
	}

}
