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

import java.io.File;

public enum ExeState {

	NULL_UNDEFINED, OK, DOES_NOT_EXIST, IS_A_DIRECTORY, NOT_A_FILE, CANNOT_BE_READ;

	public static ExeState checkFile(File dotExe) {
		if (dotExe == null) {
			return NULL_UNDEFINED;
		} else if (dotExe.exists() == false) {
			return DOES_NOT_EXIST;
		} else if (dotExe.isDirectory()) {
			return IS_A_DIRECTORY;
		} else if (dotExe.isFile() == false) {
			return NOT_A_FILE;
		} else if (dotExe.canRead() == false) {
			return CANNOT_BE_READ;
		}
		return OK;
	}

	public String getTextMessage() {
		switch (this) {
		case OK:
			return "File OK";
		case NULL_UNDEFINED:
			return "No dot executable found";
		case DOES_NOT_EXIST:
			return "File does not exist";
		case IS_A_DIRECTORY:
			return "It should be an executable, not a directory";
		case NOT_A_FILE:
			return "Not a valid file";
		case CANNOT_BE_READ:
			return "File cannot be read";
		}
		throw new IllegalStateException();
	}

	public String getTextMessage(File exe) {
		switch (this) {
		case OK:
			return "File " + exe.getAbsolutePath() + " OK";
		case NULL_UNDEFINED:
			return NULL_UNDEFINED.getTextMessage();
		case DOES_NOT_EXIST:
			return "File " + exe.getAbsolutePath() + " does not exist";
		case IS_A_DIRECTORY:
			return "File " + exe.getAbsolutePath() + " should be an executable, not a directory";
		case NOT_A_FILE:
			return "File " + exe.getAbsolutePath() + " is not a valid file";
		case CANNOT_BE_READ:
			return "File " + exe.getAbsolutePath() + " cannot be read";
		}
		throw new IllegalStateException();
	}

}
