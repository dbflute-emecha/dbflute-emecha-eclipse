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
package org.dbflute.emecha.eclipse.dfeditor.preferences;

import org.dbflute.emecha.eclipse.dfeditor.DFEditorActivator;
import org.dbflute.emecha.eclipse.dfeditor.DfColor;
import org.dbflute.emecha.eclipse.text.TextAttributeDefinition;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

/**
 * Class used to initialize default preference values.
 * @author schatten
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /**
     * 設定情報の初期値を設定する。
     *
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        // 起動時設定読み出し
        IPreferenceStore store = DFEditorActivator.getDefault().getPreferenceStore();

        store.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH, 4);
        store.setValue("errorIndicationInVerticalRuler", true);
        store.setValue("warningIndicationInVerticalRuler", true);

        for (TextAttributeDefinition color : DfColor.values()) {
            String foreground;
            if (color.getForeground() != null) {
                foreground = StringConverter.asString(color.getForeground());
            } else {
                foreground = IPreferenceStore.STRING_DEFAULT_DEFAULT;
            }
            store.setDefault(color.getForegroundKey(), foreground);

            String background;
            if (color.getBackground() != null) {
                background = StringConverter.asString(color.getBackground());
            } else {
                background = IPreferenceStore.STRING_DEFAULT_DEFAULT;
            }
            store.setDefault(color.getBackgroundKey(), background);

            store.setDefault(color.getStyleKey(), color.getStyle());
        }

    }

}
