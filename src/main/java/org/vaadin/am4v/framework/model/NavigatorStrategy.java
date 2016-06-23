package org.vaadin.am4v.framework.model;

import java.io.Serializable;

import com.vaadin.ui.UI;

public interface NavigatorStrategy extends Serializable {

    void navigateTo(String view);

    static NavigatorStrategy getDefault() {
        return (NavigatorStrategy) view -> UI.getCurrent().getNavigator().navigateTo(view);
    }
}
