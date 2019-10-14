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
/* Copyright 2015 Google Inc. All Rights Reserved.

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package net.sourceforge.plantuml.brotli;

import java.io.IOException;
import java.io.InputStream;

/**
 * A set of utility methods.
 */
final class Utils {

  private static final byte[] BYTE_ZEROES = new byte[1024];

  private static final int[] INT_ZEROES = new int[1024];

  /**
   * Fills byte array with zeroes.
   *
   * <p> Current implementation uses {@link System#arraycopy}, so it should be used for length not
   * less than 16.
   *
   * @param dest array to fill with zeroes
   * @param offset the first byte to fill
   * @param length number of bytes to change
   */
  static void fillBytesWithZeroes(byte[] dest, int start, int end) {
    int cursor = start;
    while (cursor < end) {
      int step = Math.min(cursor + 1024, end) - cursor;
      System.arraycopy(BYTE_ZEROES, 0, dest, cursor, step);
      cursor += step;
    }
  }

  /**
   * Fills int array with zeroes.
   *
   * <p> Current implementation uses {@link System#arraycopy}, so it should be used for length not
   * less than 16.
   *
   * @param dest array to fill with zeroes
   * @param offset the first item to fill
   * @param length number of item to change
   */
  static void fillIntsWithZeroes(int[] dest, int start, int end) {
    int cursor = start;
    while (cursor < end) {
      int step = Math.min(cursor + 1024, end) - cursor;
      System.arraycopy(INT_ZEROES, 0, dest, cursor, step);
      cursor += step;
    }
  }

  static void copyBytesWithin(byte[] bytes, int target, int start, int end) {
    System.arraycopy(bytes, start, bytes, target, end - start);
  }

  static int readInput(InputStream src, byte[] dst, int offset, int length) {
    try {
      return src.read(dst, offset, length);
    } catch (IOException e) {
      throw new BrotliRuntimeException("Failed to read input", e);
    }
  }

  static void closeInput(InputStream src) throws IOException {
    src.close();
  }
}
