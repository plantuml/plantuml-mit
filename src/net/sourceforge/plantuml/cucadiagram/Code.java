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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.StringUtils;

public class Code implements Comparable<Code> {

	private final String fullName;
	private final String separator;

	private Code(String fullName, String separator) {
		if (fullName == null) {
			throw new IllegalArgumentException();
		}
		this.fullName = fullName;
		this.separator = separator;
	}

	public Code removeMemberPart() {
		final int x = fullName.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		return new Code(fullName.substring(0, x), separator);
	}

	public String getPortMember() {
		final int x = fullName.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		return fullName.substring(x + 2);
	}

	public Code withSeparator(String separator) {
		if (separator == null) {
			throw new IllegalArgumentException();
		}
		if (this.separator != null && this.separator.equals(separator) == false) {
			throw new IllegalStateException();
		}
		return new Code(fullName, separator);
	}

	public static Code of(String code) {
		return of(code, null);
	}

	public static Code of(String code, String separator) {
		if (code == null) {
			return null;
		}
		return new Code(code, separator);
	}

	public final String getFullName() {
		return fullName;
	}

	@Override
	public String toString() {
		return fullName + "(" + separator + ")";
	}

	@Override
	public int hashCode() {
		return fullName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Code other = (Code) obj;
		return this.fullName.equals(other.fullName);
	}

	public Code addSuffix(String suffix) {
		return new Code(fullName + suffix, separator);
	}

	public int compareTo(Code other) {
		return this.fullName.compareTo(other.fullName);
	}

	public Code eventuallyRemoveStartingAndEndingDoubleQuote(String format) {
		return Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(fullName, format), separator);
	}

	public final String getSeparator() {
		return separator;
	}

	public String getLastPart() {
		final int x = this.fullName.lastIndexOf(separator);
		return this.fullName.substring(x + separator.length());
	}

}
