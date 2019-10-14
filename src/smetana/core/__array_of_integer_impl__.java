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

import smetana.core.amiga.Area;
import smetana.core.amiga.AreaInt;
import smetana.core.amiga.BuilderArea;
import smetana.core.amiga.Counter;

public class __array_of_integer_impl__ implements __array_of_integer__ {

	private final List<Area> data;
	private final int currentPos;

	private final int UID = Counter.CPT++;

	public String getUID36() {
		return Integer.toString(UID, 36);
	}

	public void swap(int i, int j) {
		Area e1 = data.get(i);
		Area e2 = data.get(j);
		data.set(i, e2);
		data.set(j, e1);
	}

	@Override
	public String toString() {
		if (data.get(0) != null) {
			return "__array__ " + getUID36() + " " + currentPos + "/" + data.size() + " " + data.get(0).toString();
		}
		return "__array__ " + getUID36() + " " + currentPos + "/" + data.size();
	}

	public void realloc(int nb) {
		while (data.size() < nb + currentPos) {
			data.add(new AreaInt());
		}
	}

	// public __ptr__ asPtr() {
	// return new StarArray(this);
	// }

	public int comparePointerInternal(__array_of_integer__ other2) {
		__array_of_integer_impl__ other = (__array_of_integer_impl__) other2;
		if (this.data != other.data) {
			throw new IllegalArgumentException();
		}
		return this.currentPos - other.currentPos;
	}

	public static __array_of_integer_impl__ mallocInteger(int nb) {
		return new __array_of_integer_impl__(nb, new BuilderArea() {
			public Area createArea() {
				return new AreaInt();
			}
		});
	}

	private __array_of_integer_impl__(List<Area> data, int currentPos) {
		this.data = data;
		this.currentPos = currentPos;
		check();
	}

	private __array_of_integer_impl__(int size, BuilderArea builder) {
		this.data = new ArrayList<Area>();
		this.currentPos = 0;
		for (int i = 0; i < size; i++) {
			final Area tmp = builder.createArea();
			data.add(tmp);
		}
		check();
	}

	private void check() {
		if (getUID36().equals("194")) {
			JUtils.LOG("It's me");
		}
	}

	public __array_of_integer_impl__ move(int delta) {
		return new __array_of_integer_impl__(data, currentPos + delta);
	}

	public __array_of_integer_impl__ plus(int delta) {
		return move(delta);
	}

	public Area getInternal(int idx) {
		return data.get(idx + currentPos);
	}

	public void memcopyFrom(Area source) {
		throw new UnsupportedOperationException();
	}

	//

	public int getInt() {
		return ((AreaInt) getInternal(0)).getInternal();
	}

	public void setInt(int value) {
		((AreaInt) getInternal(0)).setInternal(value);
	}

}
