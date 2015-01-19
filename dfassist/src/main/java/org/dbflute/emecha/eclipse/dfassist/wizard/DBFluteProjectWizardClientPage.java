/*
 *
 */
package org.dbflute.emecha.eclipse.dfassist.wizard;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.dbflute.emecha.eclipse.dfassist.nls.Messages;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * DBFlute Client Info Page
 */
public class DBFluteProjectWizardClientPage extends AbstractNewProjectWizardPage implements IWizardPage {

    private Text versionText;
    private Text clientProjectText;
    private Text packageBaseText;


    public DBFluteProjectWizardClientPage() {
        super("DBFluteProjectWizardClientPage");
        setTitle(Messages.NewProjectWizardClientTitle);
        setDescription(Messages.NewProjectWizardClientDescription);
        setPageComplete(false);
    }
    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(3,false);
        container.setLayout(layout);

        WidthGroup widthGroup = new WidthGroup();

        ModifyListener modifyingListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                DBFluteProjectWizardClientPage.this.validate();
            }
        };

        Group clientGroup = new Group(container, SWT.NORMAL);
        clientGroup.setText(Messages.NewProjectWizardClient_clientGroupTitle);
        clientGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
        clientGroup.setLayout(new GridLayout(2, false));

        Label clientProjectLabel = new Label(clientGroup, SWT.NORMAL);
        clientProjectLabel.setText(Messages.NewProjectWizardClient_clientProject);
        widthGroup.addControl(clientProjectLabel);

        this.clientProjectText= new Text(clientGroup, SWT.SINGLE | SWT.BORDER);
        this.clientProjectText.setLayoutData(new GridData(SWT.FILL, SWT.FILL_WINDING, true, false));
        this.clientProjectText.addModifyListener(modifyingListener);

        Label packageBaseLabel = new Label(clientGroup, SWT.NORMAL);
        packageBaseLabel.setText(Messages.NewProjectWizardClient_packageBase);
        widthGroup.addControl(packageBaseLabel);

        this.packageBaseText= new Text(clientGroup, SWT.SINGLE | SWT.BORDER);
        this.packageBaseText.setLayoutData(new GridData(SWT.FILL, SWT.FILL_WINDING, true, false));
        this.packageBaseText.addModifyListener(modifyingListener);

        Label versionLabel = new Label(container, SWT.NORMAL);
        versionLabel.setText(Messages.NewProjectWizardClient_version);
        widthGroup.addControl(versionLabel);

        this.versionText = new Text(container, SWT.SINGLE | SWT.BORDER);
        this.versionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        setLatestDBFluteVersion();
        this.versionText.addModifyListener(modifyingListener);

        Button latestVersionBtn = new Button(container, SWT.PUSH);
        latestVersionBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        latestVersionBtn.setText(Messages.NewProjectWizardClient_latestVersion);
        latestVersionBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DBFluteProjectWizardClientPage.this.setLatestDBFluteVersion();
            }
        });

        container.layout();

        validate();

        setControl(container);
    }

    private String getVersionMetaURL() {
        return "http://dbflute.org/meta/public.properties";
    }
    private String getLatestReleaseVersionKey() {
        return "dbflute.latest.release.version";
    }

    protected void setLatestDBFluteVersion() {
        Properties prop = new Properties();
        try (InputStream is = new URL(getVersionMetaURL()).openStream()) {
            prop.load(is);
        } catch (IOException e) {
        }
        String dbfluteVersion = prop.getProperty(getLatestReleaseVersionKey(), "1.1.0");
        this.versionText.setText(dbfluteVersion);
    }

    @Override
    protected String validateInput() {
        String message = validateInput(this.versionText.getText(), Messages.NewProjectWizardClient_version, true);
        if (message != null) {
            return message;
        }
        message = validateInput(this.clientProjectText.getText(), Messages.NewProjectWizardClient_clientProject, true);
        if (message != null) {
            return message;
        }
        message = validateInput(this.packageBaseText.getText(), Messages.NewProjectWizardClient_packageBase, true);
        if (message != null) {
            return message;
        }
        return null;
    }

    public String getDbfluteVersion() {
        return this.versionText.getText().trim();
    }

    public String getClientName() {
        return this.clientProjectText.getText().trim();
    }

    public String getPackageBase() {
        return this.packageBaseText.getText().trim();
    }
}
