package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Strategy for showing {@link com.vaadin.ui.Window}s from within application models. By abstracting this away,
 * different strategies can be plugged in for e.g. testing. This is one of the model strategies whose
 * {@link #getDefault() default implementation} actually does nothing.
 */
public interface WindowStrategy extends Serializable {

    /**
     * Shows the specified window.
     * 
     * @param window the name of the window to show.
     * @param parameters any parameters to pass to the window.
     */
    void showWindow(String window, Map<String, Object> parameters);

    /**
     * Shows the specified window.
     * 
     * @param window the name of the window to show.
     * @param paramName the name of the single parameter to pass to the window.
     * @param paramValue the value of the single parameter to pass to the window.
     */
    default void showWindow(String window, String paramName, Object paramValue) {
        showWindow(window, Collections.singletonMap(paramName, paramValue));
    }

    /**
     * Shows the specified window without passing any parameters to it.
     * 
     * @param window the name of the window to show.
     */
    default void showWindow(String window) {
        showWindow(window, Collections.emptyMap());
    }

    /**
     * Returns the default window strategy, which simply throws an exception. If you need to use windows, you have
     * to use a concrete window strategy.
     * 
     * @see org.vaadin.am4v.framework.ui.ProviderBasedWindowStrategy
     * @return the default window strategy.
     */
    static WindowStrategy getDefault() {
        return (WindowStrategy) (window, parameters) -> {
            throw new IllegalStateException("Please specify a WindowStrategy for your application model");
        };
    }
}
