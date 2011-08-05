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

import org.eclipse.osgi.util.NLS;

/**
 * @author <a href="mailto:samson959@gmail.com">Samson Wu</a>
 * @version 1.5.0
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "openexplorer.util.messages";//$NON-NLS-1$
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String MAC_OS_X;
	public static String WINDOWS;
	public static String LINUX;

	public static String Finder;
	public static String Windows_Explorer;

	public static String Open_Explorer_Version;
	public static String Expand_Instruction;

	public static String Nautilus;
	public static String Dolphin;
	public static String Thunar;
	public static String PCManFM;
	public static String ROX;
	public static String Xdg_open;
	public static String Other;

	public static String Full_Path_Label_Text;
	public static String Browse_Button_Text;

	public static String Error_Path_Cant_be_blank;
	public static String Erorr_Path_Not_Exist_or_Not_a_File;

	public static String OpenExploer_Error;
	public static String Cant_Open;

	public static String Preference_note;
	public static String FileManager_need_to_be_installed;

	public static String System_File_Manager_Preferences;

}
