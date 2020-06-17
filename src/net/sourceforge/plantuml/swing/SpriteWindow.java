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
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.sprite.SpriteGrayLevel;
import net.sourceforge.plantuml.sprite.SpriteUtils;
import net.sourceforge.plantuml.version.PSystemVersion;

public class SpriteWindow extends JFrame {

	// private final JButton encode = new JButton("Encode");
	private final JTextArea area = new JTextArea();

	public SpriteWindow() {
		super("SpriteWindows");
		setIconImage(PSystemVersion.getPlantumlSmallIcon2());
		// encode.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent ae) {
		// encode();
		// }
		// });

		area.setFont(new Font("Courier", Font.PLAIN, 14));
		area.setText("Copy an image to the clipboard.\nIt will be converted inside this window.\n");

		final JScrollPane scroll = new JScrollPane(area);

		// getContentPane().add(encode, BorderLayout.SOUTH);
		getContentPane().add(scroll, BorderLayout.CENTER);
		setSize(400, 320);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		startTimer();
	}

	private void startTimer() {
		Log.info("Init done");
		final Timer timer = new Timer(10000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.setInitialDelay(0);
		timer.start();
		Log.info("Timer started");
	}

	private void tick() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				encode();
			}
		});
	}

	private void encode() {
		final BufferedImage img = getClipboard();
		if (img == null) {
			return;
		}
		final StringBuilder sb = new StringBuilder();
		encodeColor(img, sb);
		encode(img, SpriteGrayLevel.GRAY_16, sb);
		encodeCompressed(img, SpriteGrayLevel.GRAY_16, sb);
		encode(img, SpriteGrayLevel.GRAY_8, sb);
		encodeCompressed(img, SpriteGrayLevel.GRAY_8, sb);
		encode(img, SpriteGrayLevel.GRAY_4, sb);
		encodeCompressed(img, SpriteGrayLevel.GRAY_4, sb);
		printData(sb.toString());
	}

	private void encodeColor(BufferedImage img, StringBuilder sb) {
		sb.append("\n");
		sb.append(SpriteUtils.encodeColor(img, "demo"));
		
	}

	private void encodeCompressed(BufferedImage img, SpriteGrayLevel level, StringBuilder sb) {
		sb.append("\n");
		sb.append(SpriteUtils.encodeCompressed(img, "demo", level));

	}

	private void encode(BufferedImage img, SpriteGrayLevel level, StringBuilder sb) {
		sb.append("\n");
		sb.append(SpriteUtils.encode(img, "demo", level));
	}

	private String last;

	private void printData(final String s) {
		if (s.equals(last) == false) {
			area.setText(s);
			last = s;
		}
	}

	public static BufferedImage getClipboard() {
		final Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

		try {
			if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				final BufferedImage text = (BufferedImage) t.getTransferData(DataFlavor.imageFlavor);
				return text;
			}

		} catch (UnsupportedFlavorException e) {
			Log.error(e.toString());
		} catch (IOException e) {
			Log.error(e.toString());
		}
		return null;
	}

	public static void main(String[] args) {
		new SpriteWindow();

	}

}
