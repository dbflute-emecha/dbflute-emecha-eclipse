/*
 * Copyright 2015 the original author or authors.
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
package org.dbflute.emecha.eclipse.pmeditor;

import org.dbflute.emecha.eclipse.pmeditor.scanner.PMPartitionScanner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * ドキュメントプロバイダー
 * @author schatten
 */
public class PMDocumentProvider extends FileDocumentProvider implements PMPartitions {

    private static final String[] LEGAL_CONTENT_TYPES = {PM_HEADER, PM_PARAMETER, PM_SEPARATE};

    public PMDocumentProvider() {
        super();
    }

    @Override
    protected IDocument createDocument(Object element) throws CoreException {
        IDocument document = super.createDocument(element);
        if (document != null) {
            IPartitionTokenScanner scanner = new PMPartitionScanner();
            IDocumentPartitioner partitioner = new FastPartitioner(scanner, LEGAL_CONTENT_TYPES);
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
        return document;
    }

}
