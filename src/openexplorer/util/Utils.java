package openexplorer.util;

/**
 * Copyright (c) 2011 Samson Wu
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:samson959@gmail.com">Samson Wu</a>
 * @version 1.5.0
 */
public class Utils {

	/**
	 * Use {@code which} command to found the modern file manager, if not found
	 * use the xdg-open.
	 * 
	 * @return the file manager
	 */
	public static String detectLinuxSystemBrowser() {
		String result = executeCommand("which nautilus");
		if (result == null || result.trim().equals("")) {
			result = executeCommand("which dolphin");
		}
		if (result == null || result.trim().equals("")) {
			result = executeCommand("which thunar");
		}
		if (result == null || result.trim().equals("")) {
			result = executeCommand("which pcmanfm");
		}
		if (result == null || result.trim().equals("")) {
			result = executeCommand("which rox");
		}
		if (result == null || result.trim().equals("")) {
			result = executeCommand("xdg-open");
		}
		if (result == null || result.trim().equals("")) {
			result = IFileManagerExecutables.OTHER;
		}
		String[] pathnames = result.split(File.separator);
		return pathnames[pathnames.length - 1];
	}

	/**
	 * execute the command and return the result.
	 * 
	 * @param command
	 * @return
	 */
	public static String executeCommand(String command) {
		String stdout = null;
		try {
			Process process = Runtime.getRuntime().exec(command);
			stdout = IOUtils.toString(process.getInputStream());
			stdout = stdout.trim();
			stdout = stdout.replace("\n", "");
			stdout = stdout.replace("\r", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stdout;
	}

}
