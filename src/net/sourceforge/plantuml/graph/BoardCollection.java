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
package net.sourceforge.plantuml.graph;

import java.util.ArrayList;
import java.util.List;

public class BoardCollection {

	static class Entry implements Comparable<Entry> {
		final private Board board;
		final private double cost;
		private boolean explored;

		public Entry(Board b, CostComputer costComputer) {
			this.board = b;
			if (costComputer == null) {
				this.cost = 0;
			} else {
				this.cost = costComputer.getCost(b);
			}
		}

		public int compareTo(Entry other) {
			return (int) Math.signum(this.cost - other.cost);
		}

		@Override
		public boolean equals(Object obj) {
			final Entry other = (Entry) obj;
			return board.equals(other.board);
		}

		@Override
		public int hashCode() {
			return board.hashCode();
		}

	}

	private final SortedCollection<Entry> all = new SortedCollectionArrayList<Entry>();

	private final CostComputer costComputer;

	public BoardCollection(CostComputer costComputer) {
		this.costComputer = costComputer;
	}

	public int size() {
		return all.size();
	}

	public Board getAndSetExploredSmallest() {
		for (Entry ent : all) {
			if (ent.explored == false) {
				ent.explored = true;
				assert costComputer.getCost(ent.board) == ent.cost;
				// Log.println("Peeking " + ent.cost);
				return ent.board;
			}
		}
		return null;
	}

	public double getBestCost() {
		for (Entry ent : all) {
			return ent.cost;
		}
		return 0;
	}

	public Board getBestBoard() {
		for (Entry ent : all) {
			return ent.board;
		}
		return null;
	}

	public List<Double> getCosts() {
		final List<Double> result = new ArrayList<Double>();
		for (Entry ent : all) {
			result.add(costComputer.getCost(ent.board));
		}
		return result;
	}

	public void add(Board b) {
		all.add(new Entry(b, costComputer));
	}

	public boolean contains(Board b) {
		return all.contains(new Entry(b, null));
	}

}
