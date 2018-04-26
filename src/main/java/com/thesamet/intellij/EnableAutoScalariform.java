package com.thesamet.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.IconLoader;

public class EnableAutoScalariform extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        if(e.getProject() != null) {
            ScalariformState scalariformState = ServiceManager.getService(e.getProject(), ScalariformState.class);
            if (scalariformState.isAutoFormatEnabled()) {
                e.getPresentation().setIcon(IconLoader.getIcon("/icon-scalariform.png"));
            } else {
                e.getPresentation().setIcon(IconLoader.getIcon("/icon-scalariform-disabled.png"));
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if(e.getProject() != null) {
            ScalariformState scalariformState = ServiceManager.getService(e.getProject(), ScalariformState.class);
            scalariformState.setAutoFormatEnabled(!scalariformState.isAutoFormatEnabled());
        }
    }
}
