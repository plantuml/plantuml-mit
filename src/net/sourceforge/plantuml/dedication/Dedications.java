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
package net.sourceforge.plantuml.dedication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.SignatureUtils;

public class Dedications {

	private static final Map<String, Dedication> normal = new HashMap<String, Dedication>();
	private static final Map<String, Dedication> crypted = new HashMap<String, Dedication>();

	static {
		addNormal("Write your own dedication!", "dedication");
		addNormal("linux_china", "linux_china");
		addNormal("ARKBAN", "arkban");
		addNormal("Boundaries allow discipline to create true strength", "boundaries");
		addCrypted("0", "pOhci6rKgPXw32AeYXhOpSY0suoauHq5VUSwFqHLHsLYgSO6WaJ7BW5vtHBAoU6ePbcW7d8Flx99MWjPSKQTDm00");
		addCrypted("1", "LTxN3hdnhSJ515qcA7IQ841axt4GXfUd3n2wgNirYCdLnyX2360Gv1OEOnJ1-gwFzRW5B3HAqLBkR6Ge0WW_Z000");
	}

	private static void addNormal(String sentence, String name) {
		normal.put(keepLetter(sentence), new Dedication(name));
	}

	private static void addCrypted(String name, String contentKey) {
		crypted.put(contentKey, new Dedication(name));
	}

	private Dedications() {
	}

	public static Dedication get(String line) {
		final String keepLetter = keepLetter(line);
		final Dedication result = normal.get(keepLetter);
		if (result != null) {
			return result;
		}
		for (Map.Entry<String, Dedication> ent : crypted.entrySet()) {
			final Dedication dedication = ent.getValue();
			InputStream is = null;
			try {
				is = dedication.getInputStream(keepLetter);
				final String signature = SignatureUtils.getSignatureSha512(is);
				if (signature.equals(ent.getKey())) {
					return dedication;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	public static String keepLetter(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
