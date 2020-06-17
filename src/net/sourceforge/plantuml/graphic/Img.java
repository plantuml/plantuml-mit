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
package net.sourceforge.plantuml.graphic;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;

public class Img implements HtmlCommand {

	final static private Pattern2 srcPattern = MyPattern.cmpile("(?i)src[%s]*=[%s]*[\"%q]?([^%s\">]+)[\"%q]?");
	final static private Pattern2 vspacePattern = MyPattern.cmpile("(?i)vspace[%s]*=[%s]*[\"%q]?(\\d+)[\"%q]?");
	final static private Pattern2 valignPattern = MyPattern
			.cmpile("(?i)valign[%s]*=[%s]*[\"%q]?(top|bottom|middle)[\"%q]?");
	final static private Pattern2 noSrcColonPattern = MyPattern.cmpile("(?i)" + Splitter.imgPatternNoSrcColon);

	private final TextBlock tileImage;

	private Img(TextBlock image) {
		this.tileImage = image;
	}

	static int getVspace(String html) {
		final Matcher2 m = vspacePattern.matcher(html);
		if (m.find() == false) {
			return 0;
		}
		return Integer.parseInt(m.group(1));
	}

	static ImgValign getValign(String html) {
		final Matcher2 m = valignPattern.matcher(html);
		if (m.find() == false) {
			return ImgValign.TOP;
		}
		return ImgValign.valueOf(StringUtils.goUpperCase(m.group(1)));
	}

	static HtmlCommand getInstance(String html, boolean withSrc) {
		if (withSrc) {
			final Matcher2 m = srcPattern.matcher(html);
			final int vspace = getVspace(html);
			final ImgValign valign = getValign(html);
			return build(m, valign, vspace);
		}
		final Matcher2 m = noSrcColonPattern.matcher(html);
		return build(m, ImgValign.TOP, 0);
	}

	private static HtmlCommand build(final Matcher2 m, final ImgValign valign, final int vspace) {
		if (m.find() == false) {
			return new Text("(SYNTAX ERROR)");
		}
		final String src = m.group(1);
		try {
			final SFile f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				// Check if valid URL
				if (src.startsWith("http:") || src.startsWith("https:")) {
					final SURL tmp = SURL.create(src);
					if (tmp == null) {
						return new Text("(Cannot decode: " + src + ")");
					}
					final BufferedImage read = tmp.readRasterImageFromURL();
					if (read == null) {
						return new Text("(Cannot decode: " + src + ")");
					}
					return new Img(new TileImage(read, valign, vspace));
				}
				return new Text("(Cannot decode: " + f + ")");
			}
			if (f.getName().endsWith(".svg")) {
				final String tmp = FileUtils.readSvg(f);
				if (tmp == null) {
					return new Text("(Cannot decode: " + f + ")");
				}
				return new Img(new TileImageSvg(tmp));
			}
			final BufferedImage read = f.readRasterImageFromFile();
			if (read == null) {
				return new Text("(Cannot decode: " + f + ")");
			}
			return new Img(new TileImage(f.readRasterImageFromFile(), valign, vspace));
		} catch (IOException e) {
			e.printStackTrace();
			return new Text("ERROR " + e.toString());
		}
	}

	public TextBlock createMonoImage() {
		return tileImage;
	}

}
