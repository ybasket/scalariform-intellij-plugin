package com.thesamet.intellij;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * Created by gil308 on 12/10/2016.
 */
@State(name = "AutoScalariformSettings", storages = {@Storage("autoscalariform.xml")})
public class AutoScalariformState implements PersistentStateComponent<AutoScalariformState> {

    private boolean autoFormatEnabled = true;

    @Override
    public void loadState(AutoScalariformState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public AutoScalariformState getState() {
        return this;
    }

    public boolean isAutoFormatEnabled() {
        return autoFormatEnabled;
    }

    public void setAutoFormatEnabled(boolean autoFormatEnabled) {
        this.autoFormatEnabled = autoFormatEnabled;
    }
}
