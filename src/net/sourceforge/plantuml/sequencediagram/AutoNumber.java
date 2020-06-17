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
package net.sourceforge.plantuml.sequencediagram;

import java.text.DecimalFormat;

public class AutoNumber {

	private boolean running = false;
	private DottedNumber current;
	private int increment;

	private DecimalFormat format;
	private String last = "";

	public final void go(DottedNumber startingNumber, int increment, DecimalFormat format) {
		this.running = true;
		this.current = startingNumber;
		this.increment = increment;
		this.format = format;
	}

	public final void stop() {
		this.running = false;
	}

	public final void resume(DecimalFormat format) {
		this.running = true;
		if (format != null) {
			this.format = format;
		}
	}

	public final void resume(int increment, DecimalFormat format) {
		this.running = true;
		this.increment = increment;
		if (format != null) {
			this.format = format;
		}
	}

	public void incrementIntermediate() {
		current.incrementIntermediate();
	}

	public void incrementIntermediate(int position) {
		current.incrementIntermediate(position);
	}

	public String getNextMessageNumber() {
		if (running == false) {
			return null;
		}
		last = current.format(format);
		current.incrementMinor(increment);
		return last;
	}

	public String getCurrentMessageNumber(boolean formatted) {
		if (formatted) {
			return last;
		}
		return last.replace("<b>", "").replace("</b>", "");
	}
}
