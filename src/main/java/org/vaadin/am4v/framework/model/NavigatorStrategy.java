package org.vaadin.am4v.framework.model;

import java.io.Serializable;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

/**
 * Strategy for navigating to views using the Vaadin {@link Navigator} from within application models. By abstracting
 * this away, different strategies can be plugged in for e.g. testing.
 */
public interface NavigatorStrategy extends Serializable {

    /**
     * Navigates to the specified view.
     * 
     * @param view the name of the view to navigate to.
     */
    void navigateTo(String view);

    /**
     * Returns the default navigation strategy, which is to use the {@link Navigator} of the current {@link UI}.
     * 
     * @see UI#getCurrent()
     * @see UI#getNavigator()
     * @return the default navigation strategy.
     */
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
