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
/* Copyright 2015 Google Inc. All Rights Reserved.

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package net.sourceforge.plantuml.brotli;

/**
 * Common context lookup table for all context modes.
 */
final class Context {

  static final int[] LOOKUP = new int[2048];

  private static final String UTF_MAP = "         !!  !                  \"#$##%#$&'##(#)#+++++++++"
      + "+((&*'##,---,---,-----,-----,-----&#'###.///.///./////./////./////&#'# ";
  private static final String UTF_RLE = "A/*  ':  & : $  \u0081 @";

  private static void unpackLookupTable(int[] lookup, String map, String rle) {
    // LSB6, MSB6, SIGNED
    for (int i = 0; i < 256; ++i) {
      lookup[i] = i & 0x3F;
      lookup[512 + i] = i >> 2;
      lookup[1792 + i] = 2 + (i >> 6);
    }
    // UTF8
    for (int i = 0; i < 128; ++i) {
      lookup[1024 + i] = 4 * (map.charAt(i) - 32);
    }
    for (int i = 0; i < 64; ++i) {
      lookup[1152 + i] = i & 1;
      lookup[1216 + i] = 2 + (i & 1);
    }
    int offset = 1280;
    for (int k = 0; k < 19; ++k) {
      int value = k & 3;
      int rep = rle.charAt(k) - 32;
      for (int i = 0; i < rep; ++i) {
        lookup[offset++] = value;
      }
    }
    // SIGNED
    for (int i = 0; i < 16; ++i) {
      lookup[1792 + i] = 1;
      lookup[2032 + i] = 6;
    }
    lookup[1792] = 0;
    lookup[2047] = 7;
    for (int i = 0; i < 256; ++i) {
      lookup[1536 + i] = lookup[1792 + i] << 3;
    }
  }

  static {
    unpackLookupTable(LOOKUP, UTF_MAP, UTF_RLE);
  }
}
