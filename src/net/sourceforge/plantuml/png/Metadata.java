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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Metadata {

	public static void main(String[] args) throws IOException {
		final Metadata meta = new Metadata();
		final int length = args.length;
		for (int i = 0; i < length; i++) {
			meta.readAndDisplayMetadata(new File(args[i]));
		}
	}

	public void readAndDisplayMetadata(File file) throws IOException {
		final ImageInputStream iis = ImageIO.createImageInputStream(file);
		final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

		if (readers.hasNext()) {
			// pick the first available ImageReader
			final ImageReader reader = readers.next();

			// attach source to the reader
			reader.setInput(iis, true);

			// read metadata of first image
			final IIOMetadata metadata = reader.getImageMetadata(0);

			final String[] names = metadata.getMetadataFormatNames();
			final int length = names.length;
			for (int i = 0; i < length; i++) {
				displayMetadata(metadata.getAsTree(names[i]));
			}
		}
	}

	private void displayMetadata(Node root) {
		displayMetadata(root, 0);
	}

	private void indent(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("    ");
		}
	}

	private void displayMetadata(Node node, int level) {
		// print open tag of element
		indent(level);
		System.out.print("<" + node.getNodeName());
		final NamedNodeMap map = node.getAttributes();
		if (map != null) {

			// print attribute values
			final int length = map.getLength();
			for (int i = 0; i < length; i++) {
				final Node attr = map.item(i);
				System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
			}
		}

		Node child = node.getFirstChild();
		if (child == null) {
			// no children, so close element and return
			System.out.println("/>");
			return;
		}

		// children, so close current tag
		System.out.println(">");
		while (child != null) {
			// print children recursively
			displayMetadata(child, level + 1);
			child = child.getNextSibling();
		}

		// print close tag of element
		indent(level);
		System.out.println("</" + node.getNodeName() + ">");
	}

}
