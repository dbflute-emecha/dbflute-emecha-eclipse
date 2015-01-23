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
package org.dbflute.emecha.eclipse.emsql.wizard;

import java.lang.reflect.InvocationTargetException;

import org.dbflute.emecha.eclipse.emsql.EMSqlPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.ide.IDE;

/**
 * @author Schatten
 *
 */
public class NewOutSideSqlWizard extends Wizard implements INewWizard {

    private IStructuredSelection _selection;

    private NewOutSideSqlWizardPage mainPage;
    private IFile createFile = null;

    /**
     * Construct
     */
    public NewOutSideSqlWizard() {
        super();
        setWindowTitle("Create new outside sql.");
        setNeedsProgressMonitor(true);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (!mainPage.isPageComplete()) {
            return false;
        }
        IRunnableWithProgress progress = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    if (monitor == null) {
                        monitor = new NullProgressMonitor();
                    }
                    monitor.beginTask("Create SQL file. File name is " + mainPage.getSQLFileName() + ".", 5);
                    createFile = mainPage.createSQLFile(new SubProgressMonitor(monitor, 5));
                } catch (Exception e) {
                    EMSqlPlugin.log().error(e);
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            if (finishPage(progress)) {
                if (createFile != null) {
                    IWorkbenchWindow dw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    try {
                        if (dw != null) {
                            IWorkbenchPage page = dw.getActivePage();
                            if (page != null) {
                                IDE.openEditor(page, createFile, true);
                            }
                        }
                    } catch (PartInitException e) {
                        EMSqlPlugin.log().error(e);
                    }

                }
                return true;
            }
        } catch (Exception e) {
            EMSqlPlugin.log().error(e);
        }
        return false;
    }

    /**
     * finishPage.
     * @param runnable execute task.
     * @return true:complite false:error
     */
    protected boolean finishPage(IRunnableWithProgress runnable) {
        IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(runnable);
        try {
            PlatformUI.getWorkbench().getProgressService().runInUI(getContainer(), op, ResourcesPlugin.getWorkspace().getRoot());
        } catch (InvocationTargetException e) {
            EMSqlPlugin.log().error(e);
            return false;
        } catch (InterruptedException e) {
            EMSqlPlugin.log().error(e);
            return false;
        }
        return true;
    }

    /**
     * Initialize Wizard.
     * @param workbench
     * @param selection2
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this._selection = selection;
    }

    /**
     * Initialize Wizard.
     * @param file
     */
    public void init(IFile file) {
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new NewOutSideSqlWizardPage();
        mainPage.setTitle("DBFlute OutSideSql");
        mainPage.setDescription("Create new OutSideSql file.");
        addPage(mainPage);
        mainPage.init(_selection);

    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.wizard.Wizard#canFinish()
     */
    @Override
    public boolean canFinish() {
        String typeName = mainPage.getTypeName();
        if (typeName == null || typeName.trim().length() == 0) {
            return false;
        }
        if (!mainPage.validateSqlFileName(typeName)) {
            return false;
        }
        return super.canFinish();
    }

}
