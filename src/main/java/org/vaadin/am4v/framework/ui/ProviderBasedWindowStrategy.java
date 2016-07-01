package org.vaadin.am4v.framework.ui;

import java.util.HashSet;
import java.util.Set;

import org.vaadin.am4v.framework.model.Parameters;
import org.vaadin.am4v.framework.model.WindowStrategy;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * An implementation of {@link WindowStrategy} that uses {@link WindowProvider}s to create {@link com.vaadin.ui.Window}
 * instances. The window instances are then added to the current {@link UI}.
 */
public class ProviderBasedWindowStrategy implements WindowStrategy {

    private final Set<WindowProvider> windowProviders = new HashSet<>();

    @Override
    public void showWindow(String window, Parameters parameters) {
        UI ui = UI.getCurrent();
        if (ui == null) {
            throw new IllegalStateException("No UI bound to current thread");
        }
        if (parameters == null) {
            parameters = new Parameters();
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
     * Utility method that creates, registers and returns a new {@link SingleWindowProvider}. This method is provided
     * for developer convenience only.
     * 
     * @see SingleWindowProvider#SingleWindowProvider(String, Class)
     * @param windowName the name of the window.
     * @param windowClass the type of the window.
     * @return the window provider.
     */
    public <W extends Window & ParameterizedWindow> WindowProvider addWindow(String windowName, Class<W> windowClass) {
        WindowProvider windowProvider = new SingleWindowProvider(windowName, windowClass);
        addProvider(windowProvider);
        return windowProvider;
    }

    /**
     * Utility method that creates, registers and returns a new {@link SingleWindowProvider}. This method is provided
     * for developer convenience only.
     *
     * @see SingleWindowProvider#SingleWindowProvider(Class)
     * @see WindowName
     * @param windowClass the type of the window.
     * @return the window provider.
     */
    public <W extends Window & ParameterizedWindow> WindowProvider addWindow(Class<W> windowClass) {
        WindowProvider windowProvider = new SingleWindowProvider(windowClass);
        addProvider(windowProvider);
        return windowProvider;
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
