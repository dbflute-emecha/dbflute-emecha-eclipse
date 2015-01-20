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
package org.dbflute.emecha.eclipse.emsql.dialog;

import org.dbflute.emecha.eclipse.emsql.EMSqlPlugin;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * @author schatten
 *
 */
public class SourceContainerDialog extends ElementTreeSelectionDialog {

    protected static class PackageRootSelectionValidator implements ISelectionStatusValidator {

        public IStatus validate(Object[] selection) {
            if (selection.length != 1) {
                return new Status(IStatus.ERROR, EMSqlPlugin.PLUGIN_ID, "");
            }
            return typeValid(selection[0]) ? new Status(IStatus.OK, EMSqlPlugin.PLUGIN_ID, "") : new Status(IStatus.ERROR,
                    EMSqlPlugin.PLUGIN_ID, "");
        }

        private boolean typeValid(Object element) {
            try {
                if (element instanceof IJavaProject) {
                    IJavaProject jproject = (IJavaProject) element;
                    IPath path = jproject.getProject().getFullPath();
                    return (jproject.findPackageFragmentRoot(path) != null);
                } else if (element instanceof IPackageFragmentRoot) {
                    return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
                }
            } catch (JavaModelException e) {
                // fall through returning false
            }
            return false;

        }
    }

    protected static class PackageAndProjectViewerFilter extends ViewerFilter {
        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (element instanceof IJavaProject) {
                return true;
            }
            if (element instanceof IPackageFragmentRoot) {
                IPackageFragmentRoot fragmentRoot = (IPackageFragmentRoot) element;
                try {
                    return (fragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE);
                } catch (JavaModelException e) {
                    return false;
                }
            }
            return false;
        }

    }

    protected SourceContainerDialog(Shell parent) {
        super(parent, new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT), new StandardJavaElementContentProvider());
        this.setAllowMultiple(false);
        this.setDoubleClickSelects(true);
        setValidator(new PackageRootSelectionValidator());
        setComparator(new JavaElementComparator());
        setTitle(getDialogTitle());
        setMessage(getInitialDialogMessage());
        addFilter(new PackageAndProjectViewerFilter());
    }

    public static IPackageFragmentRoot getSourceContainer(Shell shell, IWorkspaceRoot workspaceRoot) {
        SourceContainerDialog dialog = new SourceContainerDialog(shell);
        dialog.setInput(JavaCore.create(workspaceRoot));
        if (dialog.open() == Window.OK) {
            Object element = dialog.getFirstResult();
            if (element instanceof IJavaProject) {
                IJavaProject jproject = (IJavaProject) element;
                return jproject.getPackageFragmentRoot(jproject.getProject());
            } else if (element instanceof IPackageFragmentRoot) {
                return (IPackageFragmentRoot) element;
            }
            return null;
        }
        return null;
    }

    protected String getInitialDialogMessage() {
        return "& Choose an output folder :";
    }

    protected String getDialogTitle() {
        return "SQL directory Selection";
    }

}
