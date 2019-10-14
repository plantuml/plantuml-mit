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
package net.sourceforge.plantuml.png;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SplitParam;
import net.sourceforge.plantuml.SuggestedFile;

public class PngSplitter {

	private final List<File> files = new ArrayList<File>();

	public PngSplitter(SuggestedFile pngFile, int horizontalPages, int verticalPages, String source, int dpi,
			boolean isWithMetadata, SplitParam splitParam) throws IOException {
		if (horizontalPages == 1 && verticalPages == 1) {
			this.files.add(pngFile.getFile(0));
			return;
		}

		Log.info("Splitting " + horizontalPages + " x " + verticalPages);
		final File full = pngFile.getTmpFile(); // new File(pngFile.getParentFile(), pngFile.getName() + ".tmp");
		// Thread.yield();
		full.delete();
		// Thread.yield();
		final boolean ok = pngFile.getFile(0).renameTo(full);
		// Thread.yield();
		if (ok == false) {
			throw new IOException("Cannot rename");
		}

		// Thread.yield();
		final BufferedImage im = ImageIO.read(full);
		// Thread.yield();
		final PngSegment horizontalSegment = new PngSegment(im.getWidth(), horizontalPages);
		final PngSegment verticalSegment = new PngSegment(im.getHeight(), verticalPages);

		int x = 0;
		for (int i = 0; i < horizontalPages; i++) {
			for (int j = 0; j < verticalPages; j++) {
				final File f = pngFile.getFile(x++);
				this.files.add(f);
				final int width = horizontalSegment.getLen(i);
				final int height = verticalSegment.getLen(j);
				BufferedImage piece = im.getSubimage(horizontalSegment.getStart(i), verticalSegment.getStart(j), width,
						height);
				if (splitParam.isSet()) {
					BufferedImage withMargin = new BufferedImage(width + 2 * splitParam.getExternalMargin(), height + 2
							* splitParam.getExternalMargin(), BufferedImage.TYPE_INT_ARGB);
					final Graphics2D g2d = withMargin.createGraphics();
					if (splitParam.getExternalColor() != null) {
						g2d.setColor(splitParam.getExternalColor());
						g2d.fillRect(0, 0, withMargin.getWidth(), withMargin.getHeight());
					}
					g2d.drawImage(piece, splitParam.getExternalMargin(), splitParam.getExternalMargin(), null);

					if (splitParam.getBorderColor() != null) {
						g2d.setColor(splitParam.getBorderColor());
						g2d.drawRect(splitParam.getExternalMargin() - 1, splitParam.getExternalMargin() - 1,
								piece.getWidth() + 1, piece.getHeight() + 1);
					}

					piece = withMargin;

					g2d.dispose();
				}
				// Thread.yield();
				PngIO.write(piece, f, isWithMetadata ? source : null, dpi);
				// Thread.yield();
			}
		}

		full.delete();
		Log.info("End of splitting");
	}

	public List<File> getFiles() {
		return Collections.unmodifiableList(files);
	}

}
