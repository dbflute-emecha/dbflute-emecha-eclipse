package org.dbflute.emecha.eclipse.dfassist.wizard;

import java.lang.reflect.InvocationTargetException;

import org.dbflute.emecha.eclipse.dfassist.DfAssistPlugin;
import org.dbflute.emecha.eclipse.dfassist.nls.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Create a new DBFlute project wizard
 */
public class DBFluteProjectWizard extends Wizard implements INewWizard {
    protected DBFluteProjectWizardLocationPage locationPage;
    protected DBFluteProjectWizardArtifactPage artifactPage;
    protected DBFluteProjectWizardClientPage clientPage;
    protected IStructuredSelection selection;
    protected IWorkbench workbench;

    /**
     *
     */
    public DBFluteProjectWizard() {
        setWindowTitle(Messages.NewProjectWizardTitle);
        setDefaultPageImageDescriptor(DfAssistPlugin.getImageDescriptor("icons/wizard_logo.png"));
        setNeedsProgressMonitor(true);
    }

    /* (非 Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
    }

    @Override
    public void addPages() {

        locationPage = new DBFluteProjectWizardLocationPage(this.selection);
        artifactPage = new DBFluteProjectWizardArtifactPage() {
            @Override
            protected boolean existProject(String artifactId) {
                if (super.existProject(artifactId)) {
                    return true;
                }
                IPath location = DBFluteProjectWizard.this.locationPage.getLocationPath();
                if (location == null) {
                    location = ResourcesPlugin.getWorkspace().getRoot().getLocation();
                }
                IPath projectDir = location.addTrailingSeparator().append(artifactId);
                return projectDir.toFile().exists();
            }
        };
        clientPage = new DBFluteProjectWizardClientPage();
        addPage(locationPage);
        addPage(artifactPage);
        addPage(clientPage);
    }

    /* (非 Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        IPath location = this.locationPage.getLocationPath();
        String artifactId = this.artifactPage.getArtifactId();

        if (artifactId == null || artifactId.isEmpty()) {
            MessageDialog.openError(getShell(), NLS.bind(Messages.NewProjectWizard_existsProjectTitle, artifactId),
                    Messages.NewProjectWizard_existsProjectMessage);
            return false;
        }

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(artifactId);
        if (project.exists()) {
            MessageDialog.openError(getShell(), NLS.bind(Messages.NewProjectWizard_existsProjectTitle, artifactId),
                    Messages.NewProjectWizard_existsProjectMessage);
            return false;
        }

        if (!this.locationPage.isInWorkspace()) {
            IPath projectDir = location.addTrailingSeparator().append(artifactId);
            if (projectDir.toFile().exists()) {
                MessageDialog.openError(getShell(), NLS.bind(Messages.NewProjectWizard_existsProjectTitle, artifactId),
                        Messages.NewProjectWizard_existsProjectMessage);
                return false;
            }
            location = projectDir;
        }

        DBFluteProjectCreationOperation operation = new DBFluteProjectCreationOperation(getShell());
        operation.setWorkbench(this.workbench);
        operation.setWorkingSets(this.locationPage.getSelectedWorkingSets());
        operation.setProject(project);
        operation.setLocation(location);
        operation.setPackaging(this.artifactPage.getPackaging());
        operation.setArtifactName(this.artifactPage.getArtifactName());
        operation.setDescription(this.artifactPage.getDescription());
        operation.setArtifactInfo(this.artifactPage.getGroupId(), this.artifactPage.getArtifactId(), this.artifactPage.getVersion());
        if (this.artifactPage.hasParent()) {
            operation.setParentArtifactInfo(this.artifactPage.getParentGroupId(), this.artifactPage.getParentArtifactId(),
                    this.artifactPage.getParentVersion());
        }
        operation.setJreVersion(this.clientPage.getJreVersion());
        operation.setDbfluteVersion(this.clientPage.getDbfluteVersion());
        operation.setClientName(this.clientPage.getClientName());
        operation.setPackageBase(this.clientPage.getPackageBase());

        try {
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
            dialog.run(true, true, operation);
        } catch (InvocationTargetException e) {
            // 処理中に何らかの例外が発生したときの処理
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        } catch (InterruptedException e) {
            // キャンセルされたときの処理
            IProject createdProject = ResourcesPlugin.getWorkspace().getRoot().getProject(artifactId);
            if (createdProject.exists()) {
                try {
                    createdProject.delete(true, true, null);
                } catch (CoreException e1) {}
            }
            return false;
        }
        return true;
    }

}
