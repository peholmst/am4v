package org.vaadin.am4v.framework;

import java.io.Serializable;

import com.vaadin.ui.UI;

public interface PushStrategy extends Serializable {

    void execute(Runnable job);

    static PushStrategy getDefault() {
        return UI.getCurrent()::access;
    }
}
