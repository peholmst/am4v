package org.vaadin.am4v.framework.ui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vaadin.am4v.framework.model.WindowStrategy;

import com.vaadin.ui.UI;

/**
 * An implementation of {@link WindowStrategy} that uses {@link WindowProvider}s to create {@link com.vaadin.ui.Window}
 * instances. The window instances are then added to the current {@link UI}.
 */
public class ProviderBasedWindowStrategy implements WindowStrategy {

    private final Set<WindowProvider> windowProviders = new HashSet<>();

    @Override
    public void showWindow(String window, Map<String, Object> parameters) {
        UI ui = UI.getCurrent();
        if (ui == null) {
            throw new IllegalStateException("No UI bound to current thread");
        }
        if (parameters == null) {
            parameters = Collections.emptyMap();
        }
        WindowProvider windowProvider = windowProviders.stream().filter(p -> p.hasWindow(window)).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such window: " + window));
        ui.addWindow(windowProvider.getWindow(window, parameters));
    }

    /**
     * Adds the specified window provider to the strategy.
     * 
     * @param windowProvider the window provider to add.
     * @return {@code this}, to make method chaining possible.
     */
    public ProviderBasedWindowStrategy addProvider(WindowProvider windowProvider) {
        if (windowProvider != null) {
            windowProviders.add(windowProvider);
        }
        return this;
    }

    /**
     * Removes the specified window provider from this strategy. If it had not been added in the first place, nothing
     * happens.
     * 
     * @param windowProvider the window provider to remove.
     * @return {@code this}, to make method chaining possible.
     */
    public ProviderBasedWindowStrategy removeProvider(WindowProvider windowProvider) {
        if (windowProvider != null) {
            windowProviders.remove(windowProvider);
        }
        return this;
    }
}
