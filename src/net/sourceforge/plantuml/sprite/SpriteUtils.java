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
package net.sourceforge.plantuml.sprite;

import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;

public class SpriteUtils {

	public static final String SPRITE_NAME = "[-\\p{L}0-9_/]+";

	private SpriteUtils() {
	}

	public static String encodeColor(BufferedImage img, String name) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sprite $" + name + " [" + img.getWidth() + "x" + img.getHeight() + "/color] {\n");
		final List<String> result = SpriteColorBuilder4096.encodeColor(img);
		for (String s : result) {
			sb.append(s);
			sb.append(BackSlash.NEWLINE);
		}
		sb.append("}\n");
		return sb.toString();
	}

	public static String encode(BufferedImage img, String name, SpriteGrayLevel level) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sprite $" + name + " [" + img.getWidth() + "x" + img.getHeight() + "/" + level.getNbColor()
				+ "] {\n");
		final List<String> result = level.encode(img);
		for (String s : result) {
			sb.append(s);
			sb.append(BackSlash.NEWLINE);
		}
		sb.append("}\n");
		return sb.toString();
	}

	public static String encodeCompressed(BufferedImage img, String name, SpriteGrayLevel level) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sprite $" + name + " [" + img.getWidth() + "x" + img.getHeight() + "/" + level.getNbColor() + "z] ");
		final List<String> list = level.encodeZ(img);
		if (list.size() == 1) {
			sb.append(list.get(0));
			sb.append(BackSlash.NEWLINE);
		} else {
			sb.append("{\n");
			for (String s : list) {
				sb.append(s);
				sb.append(BackSlash.NEWLINE);
			}
			sb.append("}\n");
		}
		return sb.toString();
	}

}
