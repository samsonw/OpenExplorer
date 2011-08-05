package openexplorer.preferences;

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
import java.util.ArrayList;

import openexplorer.Activator;
import openexplorer.util.IFileManagerExecutables;
import openexplorer.util.Messages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author <a href="mailto:samson959@gmail.com">Samson Wu</a>
 * @version 1.4.0
 */
public class FMPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {

	private ArrayList<Button> fileManagerButtons = new ArrayList<Button>();

	private Label fullPathLabel;

	private Text fileManagerPath;

	private Button browseButton;

	private String selectedFileManager;

	private String fileManagerPathString;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		IPreferenceStore store = getPreferenceStore();
		selectedFileManager = store
		        .getString(IPreferenceConstants.LINUX_FILE_MANAGER);
		fileManagerPathString = store
		        .getString(IPreferenceConstants.LINUX_FILE_MANAGER_PATH);
		setDescription(Messages.System_File_Manager_Preferences);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = createComposite(parent);
		createMacOSXFMGroup(composite);
		createWindowsFMGroup(composite);
		createLinuxFMGroup(composite);
		return composite;
	}

	protected Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
		        | GridData.HORIZONTAL_ALIGN_FILL));
		return composite;
	}

	private Group createGroup(Composite composite, String title) {
		Group groupComposite = new Group(composite, SWT.LEFT);
		GridLayout layout = new GridLayout();
		groupComposite.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
		        | GridData.GRAB_HORIZONTAL);
		groupComposite.setLayoutData(data);
		groupComposite.setText(title);
		return groupComposite;
	}

	protected void createMacOSXFMGroup(Composite composite) {
		Group groupComposite = createGroup(composite, Messages.MAC_OS_X);
		Button macOSXFMButton = createRadioButton(groupComposite,
		        Messages.Finder, IFileManagerExecutables.FINDER);
		macOSXFMButton.setSelection(true);
	}

	protected void createWindowsFMGroup(Composite composite) {
		Group groupComposite = createGroup(composite, Messages.WINDOWS);
		Button windowsFMButton = createRadioButton(groupComposite,
		        Messages.Windows_Explorer, IFileManagerExecutables.EXPLORER);
		windowsFMButton.setSelection(true);
	}

	protected void createLinuxFMGroup(Composite composite) {
		Group groupComposite = createGroup(composite, Messages.LINUX);

		String label = Messages.Nautilus;
		String value = IFileManagerExecutables.NAUTILUS;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        false);

		label = Messages.Dolphin;
		value = IFileManagerExecutables.DOLPHIN;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        false);

		label = Messages.Thunar;
		value = IFileManagerExecutables.THUNAR;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        false);

		label = Messages.PCManFM;
		value = IFileManagerExecutables.PCMANFM;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        false);

		label = Messages.ROX;
		value = IFileManagerExecutables.ROX;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        false);

		label = Messages.Xdg_open;
		value = IFileManagerExecutables.XDG_OPEN;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        false);

		label = Messages.Other;
		value = IFileManagerExecutables.OTHER;
		createRadioButtonWithSelectionListener(groupComposite, label, value,
		        true);

		Composite c = new Composite(groupComposite, SWT.NONE);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		c.setLayout(layout);

		fullPathLabel = new Label(c, SWT.NONE);
		fullPathLabel.setText(Messages.Full_Path_Label_Text);
		fileManagerPath = new Text(c, SWT.BORDER);
		if (fileManagerPathString != null) {
			fileManagerPath.setText(fileManagerPathString);
		}
		fileManagerPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fileManagerPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (fileManagerPath.isEnabled()) {
					String path = fileManagerPath.getText();
					if (path == null || path.equals("")) {
						setValid(false);
						setErrorMessage(Messages.Error_Path_Cant_be_blank);
						return;
					}
					File f = new File(path);
					if (!f.exists() || !f.isFile()) {
						setValid(false);
						setErrorMessage(Messages.Erorr_Path_Not_Exist_or_Not_a_File);
						return;
					}
					setErrorMessage(null);
					setValid(true);
				}
			}
		});

		browseButton = new Button(c, SWT.PUSH);
		browseButton.setText(Messages.Browse_Button_Text);
		browseButton.setFont(composite.getFont());
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				String newValue = browsePressed();
				if (newValue != null) {
					setFileManagerPath(newValue);
				}
			}
		});
		browseButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				browseButton = null;
			}
		});

		if (!IFileManagerExecutables.OTHER.equals(selectedFileManager)) {
			fullPathLabel.setEnabled(false);
			fileManagerPath.setEnabled(false);
			browseButton.setEnabled(false);
		}

		createNoteComposite(composite.getFont(), groupComposite,
		        Messages.Preference_note,
		        Messages.FileManager_need_to_be_installed);
	}

	private void setFileManagerPath(String value) {
		if (fileManagerPath != null) {
			if (value == null) {
				value = "";
			}
			fileManagerPath.setText(value);
		}
	}

	private String browsePressed() {
		File f = new File(fileManagerPath.getText());
		if (!f.exists()) {
			f = null;
		}
		File filePath = getFilePath(f);
		if (filePath == null) {
			return null;
		}

		return filePath.getAbsolutePath();
	}

	private File getFilePath(File startingDirectory) {
		FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);
		if (startingDirectory != null) {
			fileDialog.setFilterPath(startingDirectory.getPath());
		}
		String filePath = fileDialog.open();
		if (filePath != null) {
			filePath = filePath.trim();
			if (filePath.length() > 0) {
				return new File(filePath);
			}
		}

		return null;
	}

	private void toggleOtherTextField(boolean enabled) {
		fullPathLabel.setEnabled(enabled);
		fileManagerPath.setEnabled(enabled);
		browseButton.setEnabled(enabled);
	}

	private Button createRadioButton(Composite parent, String label,
	        String value) {
		Button button = new Button(parent, SWT.RADIO | SWT.LEFT);
		button.setText(label);
		button.setData(value);
		return button;
	}

	private Button createRadioButtonWithSelectionListener(Composite parent,
	        String label, final String value,
	        final boolean enableOtherTextFieldIfClick) {
		Button button = createRadioButton(parent, label, value);
		if (value != null && value.equals(selectedFileManager)) {
			button.setSelection(true);
		}
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedFileManager = (String) e.widget.getData();
				toggleOtherTextField(enableOtherTextFieldIfClick);
				if (IFileManagerExecutables.OTHER.equals(value)) {
					fileManagerPath.notifyListeners(SWT.Modify, new Event());
				} else {
					setValid(true);
					setErrorMessage(null);
				}
			}
		});
		fileManagerButtons.add(button);
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		IPreferenceStore store = getPreferenceStore();
		for (Button button : fileManagerButtons) {
			if (store.getDefaultString(IPreferenceConstants.LINUX_FILE_MANAGER)
			        .equals((String) button.getData())) {
				button.setSelection(true);
				selectedFileManager = (String) button.getData();
			} else {
				button.setSelection(false);
			}
		}
		fileManagerPath
		        .setText(store
		                .getDefaultString(IPreferenceConstants.LINUX_FILE_MANAGER_PATH));
		super.performDefaults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(IPreferenceConstants.LINUX_FILE_MANAGER,
		        selectedFileManager);
		store.setValue(IPreferenceConstants.LINUX_FILE_MANAGER_PATH,
		        fileManagerPath.getText());
		return super.performOk();
	}

}
