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
package net.sourceforge.plantuml.ant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class CheckZipTask extends Task {

	private String zipfile = null;
	private List<FileSet> filesets = new ArrayList<FileSet>();
	private List<FileList> filelists = new ArrayList<FileList>();

	/**
	 * Add a set of files to touch
	 */
	public void addFileset(FileSet set) {
		filesets.add(set);
	}

	/**
	 * Add a filelist to touch
	 */
	public void addFilelist(FileList list) {
		filelists.add(list);
	}

	// The method executing the task
	@Override
	public void execute() throws BuildException {

		myLog("Check " + zipfile);

		try {
			loadZipFile(new SFile(zipfile));
			for (FileList fileList : filelists) {
				manageFileList(fileList);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		}
	}

	private void manageFileList(FileList fileList) {
		boolean error = false;
		final String[] srcFiles = fileList.getFiles(getProject());
		for (String s : srcFiles) {
			if (isPresentInFile(s) == false) {
				myLog("Missing " + s);
				error = true;
			}
		}
		if (error) {
			throw new BuildException("Some entries are missing in the zipfile");
		}
	}

	private boolean isPresentInFile(String s) {
		return entries.contains(s);
	}

	private final List<String> entries = new ArrayList<String>();

	private void loadZipFile(SFile file) throws IOException {

		this.entries.clear();
		final PrintWriter pw = SecurityUtils.createPrintWriter("tmp.txt");
		final InputStream tmp = file.openFile();
		if (tmp == null) {
			throw new FileNotFoundException();
		}
		final ZipInputStream zis = new ZipInputStream(tmp);
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			final String fileName = ze.getName();
			this.entries.add(fileName);
			if (fileName.endsWith("/") == false) {
				pw.println("<file name=\"" + fileName + "\" />");
			}
			ze = zis.getNextEntry();
		}
		pw.close();
		zis.close();
	}

	private synchronized void myLog(String s) {
		this.log(s);
	}

	public void setZipfile(String s) {
		this.zipfile = s;
	}

}
