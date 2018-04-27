package com.thesamet.intellij;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

public class AutoScalariform {
    public AutoScalariform(Project project) {
        registerListener(project);
    }

    private void registerListener(Project project) {
        VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileContentsChangedAdapter() {
            @Override
            protected void onFileChange(@NotNull VirtualFile file) {
                //nada
            }

            @Override
            protected void onBeforeFileChange(@NotNull VirtualFile file) {
                AutoScalariformState autoScalariformState = ServiceManager.getService(project, AutoScalariformState.class);
                if(autoScalariformState != null && autoScalariformState.isAutoFormatEnabled()) {
                    if (file.getName().toLowerCase().endsWith(".scala")) {
                        AnAction action = ActionManager.getInstance().getAction("ScalariformIdea.com.thesamet.intellij.ScalariformFormatAction");
                        DataContext dataContext = dataId -> {
                            if (dataId.equals(CommonDataKeys.PROJECT.getName())) {
                                return project;
                            } else if (dataId.equals(CommonDataKeys.VIRTUAL_FILE_ARRAY.getName())) {
                                return new VirtualFile[]{file};
                            } else {
                                return null;
                            }
                        };
                        AnActionEvent event = AnActionEvent.createFromAnAction(action, null, ActionPlaces.UNKNOWN, dataContext);
                        action.actionPerformed(event);
                    }
                }
            }
        });
    }

    public static AutoScalariform getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, AutoScalariform.class);
    }
}
