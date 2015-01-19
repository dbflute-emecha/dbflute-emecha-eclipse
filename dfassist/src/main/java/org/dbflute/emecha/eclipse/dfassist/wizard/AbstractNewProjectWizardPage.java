package org.dbflute.emecha.eclipse.dfassist.wizard;

import java.util.HashSet;

import org.dbflute.emecha.eclipse.dfassist.nls.Messages;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractNewProjectWizardPage extends WizardPage {

    protected static class WidthGroup extends ControlAdapter {
        private final HashSet<Control> controls = new HashSet<>();

        public WidthGroup() {
        }

        public void controlResized(ControlEvent e) {
            int maxWidth = 0;
            for (Control c : this.controls) {
                int width = c.getSize().x;
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
            if (maxWidth > 0) {
                for (Control c : this.controls) {
                    GridData gd = (GridData) c.getLayoutData();
                    gd.widthHint = maxWidth;
                    c.getParent().layout();
                }
            }
        }

        public void addControl(Control control) {
            this.controls.add(control);
            control.getParent().layout();
        }
    }

    public AbstractNewProjectWizardPage(String pageName) {
        super(pageName);
    }

    public AbstractNewProjectWizardPage(String pageName, String title, ImageDescriptor titleImage) {
        super(pageName, title, titleImage);
    }

    protected void validate() {
        String error = validateInput();
        setErrorMessage(error);
        setPageComplete(error == null);
    }

    protected abstract String validateInput();

    protected String validateInput(String text, String fieldName, boolean required) {
        if (text == null || text.isEmpty()) {
            return required ? NLS.bind(Messages.NewProjectWizardPage_validatorRequired, fieldName) : null;
        }
        if (text.contains(" ")) {
            return NLS.bind(Messages.NewProjectWizardPage_validatorNospace, fieldName);
        }
        IStatus nameStatus = ResourcesPlugin.getWorkspace().validateName(text, IResource.PROJECT);
        if (!nameStatus.isOK()) {
            return NLS.bind(Messages.NewProjectWizardPage_validatorInvalid, fieldName, nameStatus.getMessage());
        }
        if (!text.matches("[A-Za-z0-9_\\-.]+")) {
            return NLS.bind(Messages.NewProjectWizardPage_validatorInvalid, fieldName, text);
        }

        return null;
    }

}