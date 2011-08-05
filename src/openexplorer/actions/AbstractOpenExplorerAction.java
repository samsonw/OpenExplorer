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

import openexplorer.Activator;
import openexplorer.util.Messages;
import openexplorer.util.OperatingSystem;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
public abstract class AbstractOpenExplorerAction implements IActionDelegate,
        IPropertyChangeListener {
	protected IWorkbenchWindow window = PlatformUI.getWorkbench()
	        .getActiveWorkbenchWindow();
	protected Shell shell;
	protected ISelection currentSelection;

	protected String systemBrowser;

	public AbstractOpenExplorerAction() {
		this.systemBrowser = OperatingSystem.INSTANCE.getSystemBrowser();
		Activator.getDefault().getPreferenceStore()
		        .addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse
	 * .jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (OperatingSystem.INSTANCE.isLinux()) {
			this.systemBrowser = OperatingSystem.INSTANCE.getSystemBrowser();
		}
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
					if (OperatingSystem.INSTANCE.isWindows()) {
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
				if (OperatingSystem.INSTANCE.isWindows()) {
					browser = this.systemBrowser + " /select,";
					location = current_editing_file.getLocation().toOSString();
				}
				openInBrowser(browser, location);
			}
		}
	}

	protected void openInBrowser(String browser, String location) {
		try {
			if (OperatingSystem.INSTANCE.isWindows()) {
				Runtime.getRuntime().exec(browser + " \"" + location + "\"");
			} else {
				Runtime.getRuntime().exec(new String[] { browser, location });
			}
		} catch (IOException e) {
			MessageDialog.openError(shell, Messages.OpenExploer_Error,
			        Messages.Cant_Open + " \"" + location + "\"");
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.currentSelection = selection;
	}
}
