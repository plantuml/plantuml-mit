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
package net.sourceforge.plantuml.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SourceStringReader;

class AcceptTelnetClient extends Thread {
	final private Socket clientSocket;
	final private BufferedReader br;
	final private OutputStream os;

	AcceptTelnetClient(Socket socket) throws Exception {
		clientSocket = socket;
		System.out.println("Client Connected ...");
		br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		os = clientSocket.getOutputStream();

		start();
	}

	public String runInternal() throws IOException {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final String s = br.readLine();
			if (s == null) {
				return sb.toString();
			}
			Log.println("S=" + s);
			sb.append(s);
			sb.append('\n');
			if (s.equalsIgnoreCase("@enduml")) {
				return sb.toString();
			}
		}
	}

	public void run() {
		try {
			final String uml = runInternal();
			Log.println("UML=" + uml);
			final SourceStringReader s = new SourceStringReader(uml);
			s.outputImage(os, new FileFormatOption(FileFormat.ATXT));
			os.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
