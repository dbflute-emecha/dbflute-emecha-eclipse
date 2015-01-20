/*
 *
 */
package org.dbflute.emecha.eclipse.dfassist.wizard;

import org.dbflute.emecha.eclipse.dfassist.nls.Messages;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.dialogs.WorkingSetConfigurationBlock;

/**
 * Project location info
 */
public class DBFluteProjectWizardLocationPage extends AbstractNewProjectWizardPage implements IWizardPage {

    private Button useDefaultWorkspaceLocationButton;
    private Label locationLabel;
    private Text locationText;
    private Button locationBrowseButton;
    private IStructuredSelection selection;

    public DBFluteProjectWizardLocationPage(IStructuredSelection selection) {
        super("DBFluteProjectWizardLocationPage");
        this.selection = selection;
        setTitle(Messages.NewProjectWizardLocationTitle);
        setDescription(Messages.NewProjectWizardLocationDescription);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        container.setLayout(new GridLayout(3, false));

        this.useDefaultWorkspaceLocationButton = new Button(container, SWT.CHECK);
        GridData useDefaultWorkspaceLocationButtonData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        this.useDefaultWorkspaceLocationButton.setLayoutData(useDefaultWorkspaceLocationButtonData);
        this.useDefaultWorkspaceLocationButton.setText(Messages.NewProjectWizardLocationPage_useDefaultLocation);
        this.useDefaultWorkspaceLocationButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean inWorkspace = DBFluteProjectWizardLocationPage.this.useDefaultWorkspaceLocationButton.getSelection();
                DBFluteProjectWizardLocationPage.this.locationLabel.setEnabled(!inWorkspace);
                DBFluteProjectWizardLocationPage.this.locationText.setEnabled(!inWorkspace);
                DBFluteProjectWizardLocationPage.this.locationBrowseButton.setEnabled(!inWorkspace);
                DBFluteProjectWizardLocationPage.this.validate();
            }
        });
        this.useDefaultWorkspaceLocationButton.setSelection(true);

        this.locationLabel = new Label(container, SWT.NONE);
        GridData locationLabelData = new GridData();
        locationLabelData.horizontalIndent = 10;
        this.locationLabel.setLayoutData(locationLabelData);
        this.locationLabel.setText(Messages.NewProjectWizardLocationPage_location);
        this.locationLabel.setEnabled(false);

        this.locationText = new Text(container, SWT.SINGLE | SWT.BORDER);
        GridData locationComboData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        this.locationText.setLayoutData(locationComboData);
        this.locationText.setText(ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString());
        this.locationText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                DBFluteProjectWizardLocationPage.this.validate();
            }
        });
        this.locationText.setEnabled(false);

        this.locationBrowseButton = new Button(container, SWT.PUSH);
        this.locationBrowseButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.locationBrowseButton.setText(Messages.NewProjectWizardLocationPage_locationBrowse);
        this.locationBrowseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell());
                dialog.setText(Messages.NewProjectWizardLocationPage_dialog_location);

                String path = DBFluteProjectWizardLocationPage.this.locationText.getText();
                if (path.isEmpty()) {
                    path = ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString();
                }
                dialog.setFilterPath(path);

                String selectedDir = dialog.open();

                if (selectedDir != null) {
                    DBFluteProjectWizardLocationPage.this.locationText.setText(selectedDir);
                    DBFluteProjectWizardLocationPage.this.useDefaultWorkspaceLocationButton.setSelection(false);
                    DBFluteProjectWizardLocationPage.this.validate();
                }
            }
        });
        this.locationBrowseButton.setEnabled(false);

        // WorkingSetGroup
        createWorkingSetGroup(container, this.selection);

        setControl(container);
    }

    private WorkingSetConfigurationBlock workingSetBlock;

    /**
     * @see org.eclipse.ui.dialogs.WorkingSetGroup#getSelectedWorkingSets()
     */
    public IWorkingSet[] getSelectedWorkingSets() {
        return workingSetBlock.getSelectedWorkingSets();
    }

    /**
     * @see org.eclipse.ui.dialogs.WorkingSetGroup#WorkingSetGroup(Composite, IStructuredSelection, String[])
     */
    protected void createWorkingSetGroup(Composite composite, IStructuredSelection selection) {
        Group workingSetGroup = new Group(composite, SWT.NONE);
        workingSetGroup.setFont(composite.getFont());
        workingSetGroup.setText(Messages.WorkingSetGroup_WorkingSets_group);
        workingSetGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
        workingSetGroup.setLayout(new GridLayout(1, false));

        String[] workingSetIds = { "org.eclipse.jdt.ui.JavaWorkingSetPage", "org.eclipse.ui.resourceWorkingSetPage" };
        workingSetBlock = new WorkingSetConfigurationBlock(workingSetIds, new DialogSettings("Workbench"));
        workingSetBlock.setWorkingSets(workingSetBlock.findApplicableWorkingSets(selection));
        workingSetBlock.createContent(workingSetGroup);
    }

    public boolean isInWorkspace() {
        return this.useDefaultWorkspaceLocationButton.getSelection();
    }

    public IPath getLocationPath() {
        if (isInWorkspace()) {
            return null;
        }
        return Path.fromOSString(this.locationText.getText().trim());
    }

    @Override
    protected String validateInput() {
        if (!this.useDefaultWorkspaceLocationButton.getSelection()) {
            String location = this.locationText.getText().trim();
            if (location.isEmpty()) {
                return Messages.NewProjectWizardLocationPage_validatorProjectLocation;
            }
            if (!Path.ROOT.isValidPath(location)) {
                return Messages.NewProjectWizardLocationPage_validatorInvalidLocation;
            }
        }
        return null;
    }

}
