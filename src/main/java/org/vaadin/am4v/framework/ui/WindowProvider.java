package org.vaadin.am4v.framework.ui;

import java.io.Serializable;
import java.util.Map;

import com.vaadin.ui.Window;

/**
 * A window provider is used by the {@link ProviderBasedWindowStrategy} to create and initialize {@link Window}s, a
 * bit in the same way as the Navigation APIs {@link com.vaadin.navigator.ViewProvider}s.
 *
 * @see SingleWindowProvider
 */
public interface WindowProvider extends Serializable {

    /**
     * Checks whether this provider can provide windows with the specified {@code name}.
     * 
     * @param name the name of the window.
     * @return true if the provider supports it, false otherwise.
     */
    boolean hasWindow(String name);

    /**
     * Gets a window with the specified {@code name}, using the specified {@code parameters}. If the window
     * name is not supported, an implementation specific runtime exception is thrown.
     * 
     * @param name the name of the window.
     * @param parameters the parameters to pass to the window (never {@code null} but may be empty).
     * @return the window.
     */
    Window getWindow(String name, Map<String, Object> parameters);
}
