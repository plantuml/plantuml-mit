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

import java.nio.ByteBuffer;

/**
 * Collection of static dictionary words.
 *
 * <p>Dictionary content is loaded from binary resource when {@link #getData()} is executed for the
 * first time. Consequently, it saves memory and CPU in case dictionary is not required.
 *
 * <p>One possible drawback is that multiple threads that need dictionary data may be blocked (only
 * once in each classworld). To avoid this, it is enough to call {@link #getData()} proactively.
 */
public final class Dictionary {
  private static volatile ByteBuffer data;

  private static class DataLoader {
    static final boolean OK;

    static {
      boolean ok = true;
      try {
        Class.forName(Dictionary.class.getPackage().getName() + ".DictionaryData");
      } catch (Throwable ex) {
        ok = false;
      }
      OK = ok;
    }
  }

  public static void setData(ByteBuffer data) {
    if (!data.isDirect() || !data.isReadOnly()) {
      throw new BrotliRuntimeException("data must be a direct read-only byte buffer");
    }
    Dictionary.data = data;
  }

  public static ByteBuffer getData() {
    if (data != null) {
      return data;
    }
    if (!DataLoader.OK) {
      throw new BrotliRuntimeException("brotli dictionary is not set");
    }
    /* Might have been set when {@link DictionaryData} was loaded.*/
    return data;
  }
}
