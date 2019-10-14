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
package net.sourceforge.plantuml.tim;

public enum TLineType {

	PLAIN, AFFECTATION_DEFINE, AFFECTATION, ASSERT, IF, IFDEF, UNDEF, IFNDEF, ELSE, ELSEIF, ENDIF, DECLARE_FUNCTION, END_FUNCTION, RETURN, LEGACY_DEFINE, LEGACY_DEFINELONG, INCLUDE, INCLUDE_DEF, IMPORT, STARTSUB, ENDSUB, INCLUDESUB, LOG, DUMP_MEMORY, COMMENT_SIMPLE, COMMENT_LONG_START;

	public static TLineType getFromLine(String s) {
		if (s.matches("^\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\(.*")) {
			return LEGACY_DEFINE;
		}
		if (s.matches("^\\s*!definelong\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*")) {
			return LEGACY_DEFINELONG;
		}
		if (s.matches("^\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*")) {
			return AFFECTATION_DEFINE;
		}
		if (s.matches("^\\s*!\\s*(local|global)?\\s*\\$?[\\p{L}_][\\p{L}_0-9]*\\s*=.*")) {
			return AFFECTATION;
		}
		if (s.matches("^\\s*'.*")) {
			return COMMENT_SIMPLE;
		}
		if (s.matches("^\\s*/'.*'/\\s*$")) {
			return COMMENT_SIMPLE;
		}
		if (s.matches("^\\s*/'.*") && s.contains("'/") == false) {
			return COMMENT_LONG_START;
		}
		if (s.matches("^\\s*!ifdef\\s+.*")) {
			return IFDEF;
		}
		if (s.matches("^\\s*!undef\\s+.*")) {
			return UNDEF;
		}
		if (s.matches("^\\s*!ifndef\\s+.*")) {
			return IFNDEF;
		}
		if (s.matches("^\\s*!assert\\s+.*")) {
			return ASSERT;
		}
		if (s.matches("^\\s*!if\\s+.*")) {
			return IF;
		}
		if (s.matches("^\\s*!(unquoted\\s|final\\s)*function\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*")) {
			return DECLARE_FUNCTION;
		}
		if (s.matches("^\\s*!else\\b.*")) {
			return ELSE;
		}
		if (s.matches("^\\s*!elseif\\b.*")) {
			return ELSEIF;
		}
		if (s.matches("^\\s*!endif\\b.*")) {
			return ENDIF;
		}
		if (s.matches("^\\s*!(endfunction|enddefinelong)\\b.*")) {
			return END_FUNCTION;
		}
		if (s.matches("^\\s*!return\\b.*")) {
			return RETURN;
		}
		if (s.matches("^\\s*!(include|includeurl|include_many|include_once)\\b.*")) {
			return INCLUDE;
		}
		if (s.matches("^\\s*!(includedef)\\b.*")) {
			return INCLUDE_DEF;
		}
		if (s.matches("^\\s*!(import)\\b.*")) {
			return IMPORT;
		}
		if (s.matches("^\\s*!startsub\\s+.*")) {
			return STARTSUB;
		}
		if (s.matches("^\\s*!endsub\\b.*")) {
			return ENDSUB;
		}
		if (s.matches("^\\s*!includesub\\b.*")) {
			return INCLUDESUB;
		}
		if (s.matches("^\\s*!(log)\\b.*")) {
			return LOG;
		}
		if (s.matches("^\\s*!(dump_memory)\\b.*")) {
			return DUMP_MEMORY;
		}
		return PLAIN;
	}

	public static boolean isQuote(char ch) {
		return ch == '\"' || ch == '\'';
	}

	public static boolean isLetterOrUnderscoreOrDigit(char ch) {
		return isLetterOrUnderscore(ch) || isLatinDigit(ch);
	}

	public static boolean isLetterOrUnderscore(char ch) {
		return isLetter(ch) || ch == '_';
	}

	public static boolean isLetterOrUnderscoreOrDollar(char ch) {
		return isLetterOrUnderscore(ch) || ch == '$';
	}

	public static boolean isLetterOrDigit(char ch) {
		return isLetter(ch) || isLatinDigit(ch);
	}

	public static boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

	public static boolean isSpaceChar(char ch) {
		return Character.isSpaceChar(ch);
	}

	public static boolean isLatinDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

}
