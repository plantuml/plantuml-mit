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

import java.io.InputStream;

final class State {
  byte[] ringBuffer;
  byte[] contextModes;
  byte[] contextMap;
  byte[] distContextMap;
  byte[] output;
  byte[] byteBuffer;  // BitReader

  short[] shortBuffer; // BitReader

  int[] intBuffer;  // BitReader
  int[] rings;
  int[] blockTrees;
  int[] hGroup0;
  int[] hGroup1;
  int[] hGroup2;

  long accumulator64;  // BitReader: pre-fetched bits.

  int runningState;  // Default value is 0 == Decode.UNINITIALIZED
  int nextRunningState;
  int accumulator32;  // BitReader: pre-fetched bits.
  int bitOffset;  // BitReader: bit-reading position in accumulator.
  int halfOffset;  // BitReader: offset of next item in intBuffer/shortBuffer.
  int tailBytes;  // BitReader: number of bytes in unfinished half.
  int endOfStreamReached;  // BitReader: input stream is finished.
  int metaBlockLength;
  int inputEnd;
  int isUncompressed;
  int isMetadata;
  int literalBlockLength;
  int numLiteralBlockTypes;
  int commandBlockLength;
  int numCommandBlockTypes;
  int distanceBlockLength;
  int numDistanceBlockTypes;
  int pos;
  int maxDistance;
  int distRbIdx;
  int trivialLiteralContext;
  int literalTreeIndex;
  int literalTree;
  int j;
  int insertLength;
  int contextMapSlice;
  int distContextMapSlice;
  int contextLookupOffset1;
  int contextLookupOffset2;
  int treeCommandOffset;
  int distanceCode;
  int numDirectDistanceCodes;
  int distancePostfixMask;
  int distancePostfixBits;
  int distance;
  int copyLength;
  int copyDst;
  int maxBackwardDistance;
  int maxRingBufferSize;
  int ringBufferSize;
  int expectedTotalSize;
  int outputOffset;
  int outputLength;
  int outputUsed;
  int bytesWritten;
  int bytesToWrite;

  InputStream input; // BitReader

  State() {
    this.ringBuffer = new byte[0];
    this.rings = new int[10];
    this.rings[0] = 16;
    this.rings[1] = 15;
    this.rings[2] = 11;
    this.rings[3] = 4;
  }
}
