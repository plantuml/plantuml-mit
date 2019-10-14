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

import java.util.Random;

public class Oven {

	final private double temp;
	final private CostComputer costComputer;

	public Oven(double temp, CostComputer costComputer) {
		this.temp = temp;
		this.costComputer = costComputer;
	}

	public Board longTic(int nbTic, Board board, Random rnd) {
		double best = costComputer.getCost(board);
		Board bestBoard = board.copy();
		for (int i = 0; i < nbTic; i++) {
			final double current = tic(board, rnd);
			// Log.println("current=" + current + " best=" + best);
			if (current < best) {
				best = current;
				bestBoard = board.copy();
			}

		}
		return bestBoard;
	}

	public double tic(Board board, Random rnd) {
		// Log.println("Oven::tic");
		final double costBefore = costComputer.getCost(board);
		final Move move = null; // board.getRandomMove(rnd);
		board.applyMove(move);
		final double costAfter = costComputer.getCost(board);
		final double delta = costAfter - costBefore;
		// Log.println("delta=" + delta);
		if (delta <= 0) {
			return costAfter;
		}
		assert delta > 0;
		assert costAfter > costBefore;
		// Log.println("temp=" + temp);
		if (temp > 0) {
			final double probability = Math.exp(-delta / temp);
			final double dice = rnd.nextDouble();
			// Log.println("probability=" + probability + " dice=" +
			// dice);
			if (dice < probability) {
				// Log.println("We keep it");
				return costAfter;
			}
		}
		// Log.println("Roolback");
		board.applyMove(move.getBackMove());
		assert costBefore == costComputer.getCost(board);
		return costBefore;

	}
}
