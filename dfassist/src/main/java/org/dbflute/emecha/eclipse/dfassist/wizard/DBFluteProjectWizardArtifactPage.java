/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.emecha.eclipse.dfassist.wizard;

import org.dbflute.emecha.eclipse.dfassist.nls.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Maven Artifact Info of New DBFlute Project
 */
public class DBFluteProjectWizardArtifactPage extends AbstractNewProjectWizardPage implements IWizardPage {

    private static final String[] PACKAGING_OPTIONS = { "jar", "war" };

    private Text groupIdText;
    private Text artifactIdText;
    private Text versionText;
    private Combo packagingCombo;
    private Text nameText;
    private Text descriptionText;
    private Text parentGroupIdText;
    private Text parentArtifactIdText;
    private Text parentVersionText;

    protected DBFluteProjectWizardArtifactPage() {
        super("DBFluteProjectWizardArtifactPage");
        setTitle(Messages.NewProjectWizardArtifactTitle);
        setDescription(Messages.NewProjectWizardArtifactDescription);
        setPageComplete(false);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(3, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        container.setLayout(layout);
        WidthGroup widthGroup = new WidthGroup();
        container.addControlListener(widthGroup);

        createArtifactGroupControl(container, widthGroup);
        createParentArtifactGroupControl(container, widthGroup);

        container.layout();

        validate();

        setControl(container);
    }

    private void createArtifactGroupControl(Composite container, WidthGroup widthGroup) {
        ModifyListener modifyingListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                DBFluteProjectWizardArtifactPage.this.validate();
            }
        };

        Group artifactGroup = new Group(container, SWT.NORMAL);
        artifactGroup.setText(Messages.NewProjectWizardArtifact_artifactGroupTitle);
        artifactGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
        artifactGroup.setLayout(new GridLayout(2, false));

        Label groupIdlabel = new Label(artifactGroup, SWT.NORMAL);
        groupIdlabel.setText(Messages.NewProjectWizardArtifact_groupId);
        widthGroup.addControl(groupIdlabel);

        this.groupIdText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.groupIdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL_WINDING, true, false));
        this.groupIdText.addModifyListener(modifyingListener);

        Label artifactIdLabel = new Label(artifactGroup, SWT.NORMAL);
        artifactIdLabel.setText(Messages.NewProjectWizardArtifact_artifactId);
        widthGroup.addControl(artifactIdLabel);

        this.artifactIdText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.artifactIdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL_WINDING, false, false));
        this.artifactIdText.addModifyListener(modifyingListener);

        Label versionLabel = new Label(artifactGroup, SWT.NORMAL);
        versionLabel.setText(Messages.NewProjectWizardArtifact_version);
        widthGroup.addControl(versionLabel);

        this.versionText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.versionText.setLayoutData(new GridData(150, SWT.DEFAULT));
        this.versionText.setText("0.0.1-SNAPSHOT");
        this.versionText.addModifyListener(modifyingListener);

        Label packagingLabel = new Label(artifactGroup, SWT.NORMAL);
        packagingLabel.setText(Messages.NewProjectWizardArtifact_packaging);
        widthGroup.addControl(packagingLabel);

        this.packagingCombo = new Combo(artifactGroup, SWT.NORMAL);
        this.packagingCombo.setItems(PACKAGING_OPTIONS);
        this.packagingCombo.setText("jar");
        this.packagingCombo.setLayoutData(new GridData(135, SWT.DEFAULT));
        this.packagingCombo.addModifyListener(modifyingListener);

        Label nameLabel = new Label(artifactGroup, SWT.NORMAL);
        nameLabel.setText(Messages.NewProjectWizardArtifact_artifactName);
        widthGroup.addControl(nameLabel);

        this.nameText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Label descriptionLabel = new Label(artifactGroup, SWT.NORMAL);
        descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        descriptionLabel.setText(Messages.NewProjectWizardArtifact_artifactDescription);
        widthGroup.addControl(descriptionLabel);

        this.descriptionText = new Text(artifactGroup, SWT.MULTI | SWT.BORDER);
        GridData gd_descriptionText = new GridData(4, 4, false, true);
        gd_descriptionText.minimumHeight = 20;
        this.descriptionText.setLayoutData(gd_descriptionText);
    }

    private void createParentArtifactGroupControl(Composite container, WidthGroup widthGroup) {
        ModifyListener modifyingListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                DBFluteProjectWizardArtifactPage.this.validate();
            }
        };

        Group artifactGroup = new Group(container, SWT.NORMAL);
        artifactGroup.setText(Messages.NewProjectWizardArtifact_parentArtifactGroupTitle);

        GridData gd_artifactGroup = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);
        artifactGroup.setLayoutData(gd_artifactGroup);
        artifactGroup.setLayout(new GridLayout(2, false));

        Label groupIdlabel = new Label(artifactGroup, SWT.NORMAL);
        groupIdlabel.setText(Messages.NewProjectWizardArtifact_groupId);
        widthGroup.addControl(groupIdlabel);

        this.parentGroupIdText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.parentGroupIdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL_WINDING, true, false));
        this.parentGroupIdText.addModifyListener(modifyingListener);

        Label artifactIdLabel = new Label(artifactGroup, SWT.NORMAL);
        artifactIdLabel.setText(Messages.NewProjectWizardArtifact_artifactId);
        widthGroup.addControl(artifactIdLabel);

        this.parentArtifactIdText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.parentArtifactIdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL_WINDING, false, false));
        this.parentArtifactIdText.addModifyListener(modifyingListener);

        Label versionLabel = new Label(artifactGroup, SWT.NORMAL);
        versionLabel.setText(Messages.NewProjectWizardArtifact_version);
        widthGroup.addControl(versionLabel);

        this.parentVersionText = new Text(artifactGroup, SWT.SINGLE | SWT.BORDER);
        this.parentVersionText.setLayoutData(new GridData(150, SWT.DEFAULT));
        this.parentVersionText.addModifyListener(modifyingListener);

    }

    @Override
    protected String validateInput() {

        String message = validateInput(groupIdText.getText(), Messages.NewProjectWizardArtifact_groupId, true);
        if (message != null) {
            return message;
        }
        message = validateInput(artifactIdText.getText(), Messages.NewProjectWizardArtifact_artifactId, true);
        if (message != null) {
            return message;
        }
        message = validateInput(versionText.getText(), Messages.NewProjectWizardArtifact_version, true);
        if (message != null) {
            return message;
        }
        if (packagingCombo.getText().trim().isEmpty()) {
            return NLS.bind(Messages.NewProjectWizardPage_validatorRequired, Messages.NewProjectWizardArtifact_packaging);
        }
        message = validateInput(parentGroupIdText.getText(), Messages.NewProjectWizardArtifact_groupId, false);
        if (message != null) {
            return message;
        }
        message = validateInput(parentArtifactIdText.getText(), Messages.NewProjectWizardArtifact_artifactId, false);
        if (message != null) {
            return message;
        }
        message = validateInput(parentVersionText.getText(), Messages.NewProjectWizardArtifact_version, false);
        if (message != null) {
            return message;
        }

        if (hasParent()) {
            if (parentGroupIdText.getText().trim().isEmpty()) {
                return NLS.bind(Messages.NewProjectWizardPage_validatorRequired, Messages.NewProjectWizardArtifact_groupId);
            }
            if (parentArtifactIdText.getText().trim().isEmpty()) {
                return NLS.bind(Messages.NewProjectWizardPage_validatorRequired, Messages.NewProjectWizardArtifact_artifactId);
            }
            if (parentVersionText.getText().trim().isEmpty()) {
                return NLS.bind(Messages.NewProjectWizardPage_validatorRequired, Messages.NewProjectWizardArtifact_version);
            }
        }

        if (existProject(artifactIdText.getText().trim())) {
            return Messages.NewProjectWizard_existsProjectMessage;
        }

        return null;
    }

    protected boolean existProject(String artifactId) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(artifactId);
        return project.exists();
    }

    public boolean hasParent() {
        return !parentGroupIdText.getText().trim().isEmpty() || !parentArtifactIdText.getText().trim().isEmpty()
                || !parentVersionText.getText().trim().isEmpty();
    }

    public String getArtifactId() {
        return this.artifactIdText.getText().trim();
    }

    public String getPackaging() {
        return this.packagingCombo.getText();
    }

    public String getArtifactName() {
        return this.nameText.getText().trim();
    }

    public String getDescription() {
        return this.descriptionText.getText().trim();
    }

    public String getGroupId() {
        return this.groupIdText.getText().trim();
    }

    public String getVersion() {
        return this.versionText.getText().trim();
    }

    public String getParentGroupId() {
        return this.parentGroupIdText.getText().trim();
    }

    public String getParentArtifactId() {
        return this.parentArtifactIdText.getText().trim();
    }

    public String getParentVersion() {
        return this.parentVersionText.getText().trim();
    }

}
