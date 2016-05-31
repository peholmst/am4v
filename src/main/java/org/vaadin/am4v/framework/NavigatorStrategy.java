package org.vaadin.am4v.framework;

import java.io.Serializable;

import com.vaadin.ui.UI;

public interface NavigatorStrategy extends Serializable {

    void navigateTo(String view);

    static NavigatorStrategy getDefault() {
        return UI.getCurrent().getNavigator()::navigateTo;
    }
}
