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
package net.sourceforge.plantuml.oregon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.sourceforge.plantuml.Log;

public class MagicTable {

	static enum Oc {
		USED, NEAR
	}

	private final Oc number[] = new Oc[10000];

	private static ArrayList<int[]> neighbours;

	static {
		neighbours = new ArrayList<int[]>();
		for (int i = 0; i < 10000; i++) {
			neighbours.add(null);
		}
	}

	public static int[] getNeighbours(final int nb) {
		if (neighbours.get(nb) == null) {
			neighbours.set(nb, getNeighboursSlow(nb));
		}
		return neighbours.get(nb);
	}

	public static int[] getNeighboursSlow(final int nb) {
		final int result[] = new int[36];

		final int v1 = nb % 10;
		int root = nb - v1;
		int cpt = 0;
		for (int i = 0; i < 10; i++) {
			final int candidate = root + i;
			if (candidate == nb) {
				continue;
			}
			result[cpt++] = candidate;
		}
		final int v2 = (nb / 10) % 10;
		root = nb - v2 * 10;
		for (int i = 0; i < 10; i++) {
			final int candidate = root + i * 10;
			if (candidate == nb) {
				continue;
			}
			result[cpt++] = candidate;
		}
		final int v3 = (nb / 100) % 10;
		root = nb - v3 * 100;
		for (int i = 0; i < 10; i++) {
			final int candidate = root + i * 100;
			if (candidate == nb) {
				continue;
			}
			result[cpt++] = candidate;
		}
		final int v4 = nb / 1000;
		root = nb - v4 * 1000;
		for (int i = 0; i < 10; i++) {
			final int candidate = root + i * 1000;
			if (candidate == nb) {
				continue;
			}
			result[cpt++] = candidate;
		}
		return result;
	}

	public List<Integer> getAllFree() {
		final List<Integer> result = new ArrayList<Integer>(10000);
		for (int i = 0; i < number.length; i++) {
			if (number[i] == null) {
				result.add(i);
			}
		}
		return result;
	}

	public List<Integer> getAllUsed() {
		final List<Integer> result = new ArrayList<Integer>(10000);
		for (int i = 0; i < number.length; i++) {
			if (number[i] == Oc.USED) {
				result.add(i);
			}
		}
		return result;
	}

	public boolean isUsuable(int nb) {
		if (number[nb] != null) {
			return false;
		}
		for (int near : getNeighbours(nb)) {
			if (number[near] != null) {
				return false;
			}
		}
		return true;
	}

	public void burnNumber(int nb) {
		if (number[nb] != null) {
			throw new IllegalArgumentException();
		}
		number[nb] = Oc.USED;
		for (int near : getNeighbours(nb)) {
			number[near] = Oc.NEAR;
		}
	}

	public int getRandomFree(Random rnd) {
		final List<Integer> frees = getAllFree();
		// final int size = frees.size();
		// for (int i = 0; i < size; i++) {
		// final int pos = rnd.nextInt(frees.size());
		// final int nb = frees.get(pos);
		// frees.remove(pos);
		// if (isUsuable(nb)) {
		// return nb;
		// }
		// }
		Collections.shuffle(frees, rnd);
		for (int nb : frees) {
			if (isUsuable(nb)) {
				return nb;
			}
		}
		return -1;

	}

	public static int size(Random rnd, MagicTable mt) {
		int i = 0;
		while (true) {
			final int candidate = mt.getRandomFree(rnd);
			if (candidate == -1) {
				break;
			}
			mt.burnNumber(candidate);
			i++;
		}
		return i;
	}

	public static void main(String[] args) {
		int max = 0;
		final long start = System.currentTimeMillis();
		final Random rnd = new Random(49);
		final int nb = 200000;
		for (int i = 0; i < nb; i++) {
			if (i == 100) {
				long dur = (System.currentTimeMillis() - start) / 1000L;
				dur = dur * nb / 100;
				dur = dur / 3600;
				Log.println("Estimated duration = " + dur + " h");
			}
			final MagicTable mt = new MagicTable();
			final int v = MagicTable.size(rnd, mt);
			if (v > max) {
				Log.println("v="+v);
				Log.println("mt="+mt.getAllUsed());
				max = v;
			}
		}
		final long duration = System.currentTimeMillis() - start;
		Log.println("Duration = " + duration / 1000L / 60);

	}

	public static void main2(String[] args) {
		int max = 0;
		final long start = System.currentTimeMillis();
		for (int j = 1; j < 100; j++) {
			final Random rnd = new Random(j);
			for (int i = 0; i < 1000; i++) {
				final MagicTable mt = new MagicTable();
				final int v = MagicTable.size(rnd, mt);
				if (v > max) {
					Log.println("v="+v);
					Log.println("mt="+mt.getAllUsed());
					max = v;
				}
			}
		}
		final long duration = System.currentTimeMillis() - start;
		Log.println("Duration = " + duration / 1000L / 60);

	}

}
