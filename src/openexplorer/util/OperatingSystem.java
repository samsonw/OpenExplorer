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

import openexplorer.Activator;
import openexplorer.preferences.IPreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * OperatingSystem singleton
 * @author <a href="mailto:samson959@gmail.com">Samson Wu</a>
 * @version 1.5.0
 */
public enum OperatingSystem {
	INSTANCE;

	public static final String WINDOWS = "win32";
	public static final String LINUX = "linux";
	public static final String MACOSX = "macosx";

	private String os;

	private OperatingSystem() {
		os = System.getProperty("osgi.os");
	}

	/**
     * @return the systemBrowser
     */
    public String getSystemBrowser() {
    	String systemBrowser = null;
    	if (isWindows()) {
			systemBrowser = IFileManagerExecutables.EXPLORER;
		} else if (isLinux()) {
			IPreferenceStore store = Activator.getDefault()
			        .getPreferenceStore();
			systemBrowser = store
			        .getString(IPreferenceConstants.LINUX_FILE_MANAGER);
			if (systemBrowser.equals(IFileManagerExecutables.OTHER)) {
				systemBrowser = store
				        .getString(IPreferenceConstants.LINUX_FILE_MANAGER_PATH);
			}
		} else if (isMacOSX()) {
			systemBrowser = IFileManagerExecutables.FINDER;
		}
    	return systemBrowser;
    }
    
    public String getOS() {
    	return os;
    }

    public boolean isWindows() {
    	return WINDOWS.equalsIgnoreCase(os);
    }
    
    public boolean isMacOSX() {
    	return MACOSX.equalsIgnoreCase(this.os);
    }
    
    public boolean isLinux() {
    	return LINUX.equalsIgnoreCase(os);
    }
}
