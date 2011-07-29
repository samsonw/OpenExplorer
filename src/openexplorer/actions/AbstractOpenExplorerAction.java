package openexplorer.actions;

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

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author <a href="mailto:samson959@gmail.com">Samson Wu</a>
 * @version 1.4.0
 */
public abstract class AbstractOpenExplorerAction implements IActionDelegate {
    protected IWorkbenchWindow window = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow();
    protected Shell shell;
    protected ISelection currentSelection;

    protected String os;
    public static final String WINDOWS = "win32";
    public static final String LINUX = "linux";
    public static final String MACOSX = "macosx";

    protected String systemBrowser = "explorer";

    public AbstractOpenExplorerAction() {
        this.os = System.getProperty("osgi.os");
        if (WINDOWS.equalsIgnoreCase(this.os)) {
            this.systemBrowser = "explorer";
        } else if (LINUX.equalsIgnoreCase(this.os)) {
            this.systemBrowser = detachLinuxBrowser();
        } else if (MACOSX.equalsIgnoreCase(this.os)) {
            this.systemBrowser = "open";
        }
    }
    
    protected String detachLinuxBrowser() {
    	String result = executeCommand("which dolphin");
    	if (StringUtils.isBlank(result)) {
    		result = executeCommand("which nautilus");
    	}
    	if (StringUtils.isBlank(result)) {
    		result = executeCommand("which thunar");
    	}
    	if (StringUtils.isBlank(result)) {
    		result = executeCommand("which pcmanfm");
    	}
    	if (StringUtils.isBlank(result)) {
    		result = executeCommand("which rox");
    	}
    	if (StringUtils.isBlank(result)) {
    		result =  "xdg-open";
    	}
    	return result;
    }

    /**
	 * @param command
	 * @return
	 */
	private String executeCommand(String command) {
		String stdout = null;
		try {
			Process process = Runtime.getRuntime().exec(command);
			stdout = IOUtils.toString(process.getInputStream());
			stdout = StringUtils.remove(StringUtils.trim(stdout), "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stdout;
	}

	public void run(IAction action) {
        if (this.currentSelection == null || this.currentSelection.isEmpty()) {
            return;
        }
        if (this.currentSelection instanceof ITreeSelection) {
            ITreeSelection treeSelection = (ITreeSelection) this.currentSelection;

            TreePath[] paths = treeSelection.getPaths();

            for (int i = 0; i < paths.length; i++) {
                TreePath path = paths[i];
                IResource resource = null;
                Object segment = path.getLastSegment();
                if ((segment instanceof IResource))
                    resource = (IResource) segment;
                else if ((segment instanceof IJavaElement)) {
                    resource = ((IJavaElement) segment).getResource();
                }
                if (resource == null) {
                    continue;
                }
                String browser = this.systemBrowser;
                String location = resource.getLocation().toOSString();
                if ((resource instanceof IFile)) {
                    location = ((IFile) resource).getParent().getLocation()
                            .toOSString();
                    if (WINDOWS.equalsIgnoreCase(this.os)) {
                        browser = this.systemBrowser + " /select,";
                        location = ((IFile) resource).getLocation()
                                .toOSString();
                    }
                }
                openInBrowser(browser, location);
            }
        } else if (this.currentSelection instanceof ITextSelection
                || this.currentSelection instanceof IStructuredSelection) {
            // open current editing file
            IEditorPart editor = window.getActivePage().getActiveEditor();
            if (editor != null) {
                IFile current_editing_file = (IFile) editor.getEditorInput()
                        .getAdapter(IFile.class);
                String browser = this.systemBrowser;
                String location = current_editing_file.getParent()
                        .getLocation().toOSString();
                if (WINDOWS.equalsIgnoreCase(this.os)) {
                    browser = this.systemBrowser + " /select,";
                    location = current_editing_file.getLocation().toOSString();
                }
                openInBrowser(browser, location);
            }
        }
    }

    protected void openInBrowser(String browser, String location) {
        try {
            if (WINDOWS.equalsIgnoreCase(this.os)) {
                Runtime.getRuntime().exec(browser + " \"" + location + "\"");
            } else {
                Runtime.getRuntime().exec(new String[] { browser, location });
            }
        } catch (IOException e) {
            MessageDialog.openError(shell, "OpenExploer Error", "Can't open \""
                    + location + "\"");
            e.printStackTrace();
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.currentSelection = selection;
    }
}
