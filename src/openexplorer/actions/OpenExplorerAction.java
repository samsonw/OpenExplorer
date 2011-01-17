package openexplorer.actions;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OpenExplorerAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;
    private ISelection currentSelection;

    private String os;
    public static final String WINDOWS = "win32";
    public static final String LINUX = "linux";
    public static final String MACOSX = "macosx";

    private String systemBrowser = "explorer";

    public OpenExplorerAction() {
	this.os = System.getProperty("osgi.os");
	if (WINDOWS.equalsIgnoreCase(this.os)) {
	    this.systemBrowser = "explorer";
	} else if (LINUX.equalsIgnoreCase(this.os)) {
	    this.systemBrowser = "nautilus";
	} else if (MACOSX.equalsIgnoreCase(this.os)) {
	    this.systemBrowser = "open";
	}
    }

    public void run(IAction action) {
	if (this.currentSelection == null || this.currentSelection.isEmpty()) {
	    return;
	}
	if (this.currentSelection instanceof TreeSelection) {
	    TreeSelection treeSelection = (TreeSelection) this.currentSelection;

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
	} else if (this.currentSelection instanceof TextSelection) {
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

    private void openInBrowser(String browser, String location) {
	try {
	    if (MACOSX.equalsIgnoreCase(this.os)) {
		Runtime.getRuntime().exec(new String[] { browser, location });
	    } else {
		Runtime.getRuntime().exec(browser + " \"" + location + "\"");
	    }

	} catch (IOException e) {
	    MessageDialog.openError(this.window.getShell(),
		    "OpenExploer Error", "Can't open \"" + location + "\"");
	    e.printStackTrace();
	}
    }

    public void selectionChanged(IAction action, ISelection selection) {
	this.currentSelection = selection;
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
	this.window = window;
    }

}
