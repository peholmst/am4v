package org.vaadin.am4v.framework.model;

import java.io.Serializable;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

public interface NavigatorStrategy extends Serializable {

    void navigateTo(String view);

    static NavigatorStrategy getDefault() {
        return (NavigatorStrategy) view -> {
            UI ui = UI.getCurrent();
            if (ui == null) {
                throw new IllegalStateException("No UI bound to current thread");
            }

            Navigator navigator = ui.getNavigator();
            if (navigator == null) {
                throw new IllegalStateException("The current UI has no navigator");
            }

            navigator.navigateTo(view);
        };
    }
}
