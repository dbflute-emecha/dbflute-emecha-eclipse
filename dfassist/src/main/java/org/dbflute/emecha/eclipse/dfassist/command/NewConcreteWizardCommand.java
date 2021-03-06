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
package org.dbflute.emecha.eclipse.dfassist.command;

import org.dbflute.emecha.eclipse.dfassist.wizard.NewConcreteClassWizard;
import org.dbflute.emecha.eclipse.util.WorkbenchUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Command handler to create a new concrete class.
 * @author schatten
 */
@SuppressWarnings("restriction")
public class NewConcreteWizardCommand extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection objParam = HandlerUtil.getActiveMenuEditorInput(event);
        boolean execute = executeSelection(objParam);
        if (execute)
            return null;

        ISelection menuParam = HandlerUtil.getActiveMenuSelection(event);
        execute = executeSelection(menuParam);

        return null;
    }

    protected boolean executeSelection(ISelection selection) {
        if (selection instanceof StructuredSelection) {
            Object firstElement = ((StructuredSelection) selection).getFirstElement();
            if (firstElement instanceof IFile) {
                NewConcreteClassWizard wizard = new NewConcreteClassWizard();
                wizard.init(PlatformUI.getWorkbench(), (StructuredSelection) selection);
                WorkbenchUtil.startWizard(wizard);
                return true;
            }
            if (firstElement instanceof FileEditorInput) {
                final IFile file = ((FileEditorInput) firstElement).getFile();
                NewConcreteClassWizard wizard = new NewConcreteClassWizard();
                wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(file));
                WorkbenchUtil.startWizard(wizard);
                return true;
            }
            if (firstElement instanceof CompilationUnit) {
                IJavaElement javaElement = ((CompilationUnit) firstElement).getPrimaryElement();
                if (javaElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
                    final IFile file = javaElement.getJavaProject().getProject().getWorkspace().getRoot().getFile(javaElement.getPath());
                    NewConcreteClassWizard wizard = new NewConcreteClassWizard();
                    wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(file));
                    WorkbenchUtil.startWizard(wizard);
                    return true;
                }
            }
            if (firstElement instanceof SourceType) {
                IJavaElement primaryElement = ((SourceType) firstElement).getPrimaryElement();
                if (primaryElement.getElementType() == IJavaElement.TYPE) {
                    final IFile file =
                            primaryElement.getJavaProject().getProject().getWorkspace().getRoot().getFile(primaryElement.getPath());
                    NewConcreteClassWizard wizard = new NewConcreteClassWizard();
                    wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(file));
                    WorkbenchUtil.startWizard(wizard);
                    return true;
                }
            }
        }
        return false;
    }

}
