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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;

public class ProcessState {

	private final String name;
	private final IOException cause;

	private ProcessState(String name, IOException cause) {
		this.name = name;
		this.cause = cause;
	}

	@Override
	public String toString() {
		if (cause == null) {
			return name;
		}
		return name + " " + cause.toString();
	}

	private final static ProcessState INIT = new ProcessState("INIT", null);
	private final static ProcessState RUNNING = new ProcessState("RUNNING", null);
	private final static ProcessState TERMINATED_OK = new ProcessState("TERMINATED_OK", null);
	private final static ProcessState TIMEOUT = new ProcessState("TIMEOUT", null);

	// INIT, RUNNING, TERMINATED_OK, TIMEOUT, IO_EXCEPTION1, IO_EXCEPTION2;

	public static ProcessState INIT() {
		return INIT;
	}

	public static ProcessState RUNNING() {
		return RUNNING;
	}

	public static ProcessState TERMINATED_OK() {
		return TERMINATED_OK;
	}

	public static ProcessState TIMEOUT() {
		return TIMEOUT;
	}

	public static ProcessState IO_EXCEPTION1(IOException e) {
		return new ProcessState("IO_EXCEPTION1", e);
	}

	public static ProcessState IO_EXCEPTION2(IOException e) {
		return new ProcessState("IO_EXCEPTION2", e);
	}

	public boolean differs(ProcessState other) {
		return name.equals(other.name) == false;
	}

	@Override
	public boolean equals(Object o) {
		final ProcessState other = (ProcessState) o;
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public Throwable getCause() {
		return cause;
	}
}
