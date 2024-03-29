package com.github.aborn.codepulse.listeners;

import com.github.aborn.codepulse.tc.TimeTrace;
import com.github.aborn.codepulse.utils.ToolUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author aborn
 * @date 2021/02/08 4:11 PM
 */
public class CodePulseDocumentListener extends UserActionBaseListener implements DocumentListener {
    public CodePulseDocumentListener() {}

    @Override
    public void documentChanged(DocumentEvent documentEvent) {
        Document document = documentEvent.getDocument();
        FileDocumentManager instance = FileDocumentManager.getInstance();
        VirtualFile file = instance.getFile(document);
        Editor[] editors = EditorFactory.getInstance().getEditors(document);
        if (editors.length > 0) {
            Project project = editors[0].getProject();
            if (project != null) {
                info("document changed. projectName:" + project.getName());
            }
        }

        if (file != null) {
            TimeTrace.appendActionPoint(file, ToolUtils.getProject(document), true);
        }
        record();
    }
}
