package com.thesamet.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.IconLoader;

public class EnableAutoScalariform extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        if(e.getProject() != null) {
            AutoScalariformState autoScalariformState = ServiceManager.getService(e.getProject(), AutoScalariformState.class);
            if (autoScalariformState != null && autoScalariformState.isAutoFormatEnabled()) {
                e.getPresentation().setIcon(IconLoader.getIcon("/icon-scalariform.png"));
            } else {
                e.getPresentation().setIcon(IconLoader.getIcon("/icon-scalariform-disabled.png"));
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if(e.getProject() != null) {
            AutoScalariformState autoScalariformState = ServiceManager.getService(e.getProject(), AutoScalariformState.class);
            if (autoScalariformState != null)
                autoScalariformState.setAutoFormatEnabled(!autoScalariformState.isAutoFormatEnabled());
        }
    }
}
