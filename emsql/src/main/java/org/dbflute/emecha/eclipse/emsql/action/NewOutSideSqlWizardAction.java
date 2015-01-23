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
package org.dbflute.emecha.eclipse.emsql.action;

import org.dbflute.emecha.eclipse.emsql.wizard.NewOutSideSqlWizard;
import org.dbflute.emecha.eclipse.util.WorkbenchUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * @author schatten
 */
public class NewOutSideSqlWizardAction implements IActionDelegate {

    private IStructuredSelection _selection;

    /**
     * Construct
     */
    public NewOutSideSqlWizardAction() {
        super();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        Object obj = this._selection.getFirstElement();
        if (obj instanceof IFile) {
            IFile file = (IFile) obj;
            IProject project = file.getProject();
            IJavaProject javap = JavaCore.create(project);
            if (javap.exists() && javap.isOpen()) {
                NewOutSideSqlWizard wizard = new NewOutSideSqlWizard();
                wizard.init(PlatformUI.getWorkbench(), this._selection);
                WorkbenchUtil.startWizard(wizard);
                return;
            }
        }
        // TODO Message Resource
        WorkbenchUtil.showMessage("EMecha-EMSql", "Invalid path.");
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection iss = (IStructuredSelection) selection;
            this._selection = iss;
        }
    }

}
