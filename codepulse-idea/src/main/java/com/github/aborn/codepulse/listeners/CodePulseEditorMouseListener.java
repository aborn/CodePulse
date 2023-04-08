package com.github.aborn.codepulse.listeners;

import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;

import java.util.Date;

/**
 * @author aborn
 * @date 2021/02/08 4:09 PM
 */
public class CodePulseEditorMouseListener extends UserActionBaseListener implements EditorMouseListener {
    public CodePulseEditorMouseListener() {}

    private static Date lastRecord = null;

    @Override
    public void mousePressed(EditorMouseEvent editorMouseEvent) {
        if (isNeedRecord()) {
            info("mouse pressed.");
            record();
        }

        /** 先不用处理
        FileDocumentManager instance = FileDocumentManager.getInstance();
        VirtualFile file = instance.getFile(editorMouseEvent.getEditor().getDocument());
        Project project = editorMouseEvent.getEditor().getProject();
        info("mouse pressed. projectName:" + project.getName());
         */
    }

    @Override
    public void mouseEntered(EditorMouseEvent editorMouseEvent) {
        if (isNeedRecord()) {
            info("mouse entered.");
            record();
        }
    }

    /**
     * 每固定时间（5s）记录一次，免得事件太过频繁
     * @return
     */
    private boolean isNeedRecord() {
        if (lastRecord == null) {
            lastRecord = new Date();
            return true;
        } else {
            Date currentDate = new Date();
            if ((currentDate.getTime() - lastRecord.getTime()) / 1000 > 5) {
                lastRecord = new Date();
                return true;
            } else {
                return false;
            }
        }
    }
}
