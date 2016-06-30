package org.vaadin.am4v.framework.ui;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.vaadin.ui.Window;

/**
 * Class for window providers that serve a single {@link Window}. A new instance of the window is created
 * every time the window is shown.
 */
public class SingleWindowProvider implements WindowProvider {

    private final String windowName;
    private final WindowFactory windowFactory;

    /**
     * Creates a new {@code SingleWindowProvider}.
     * 
     * @param windowName the name of the window to show.
     * @param windowFactory the factory to use when creating windows.
     */
    public SingleWindowProvider(String windowName, WindowFactory windowFactory) {
        this.windowName = Objects.requireNonNull(windowName);
        this.windowFactory = Objects.requireNonNull(windowFactory);
    }

    @Override
    public boolean hasWindow(String name) {
        return windowName.equals(name);
    }

    @Override
    public Window getWindow(String name, Map<String, Object> parameters) {
        if (windowName.equals(name)) {
            return windowFactory.createWindow(Objects.requireNonNull(parameters));
        } else {
            throw new IllegalArgumentException("Unsupported window name: " + name);
        }
    }

    /**
     * Interface for the factory that creates new windows.
     */
    @FunctionalInterface
    public interface WindowFactory extends Serializable {

        /**
         * Creates a new {@link Window} instance.
         *
         * @param parameters the parameters passed from the {@link org.vaadin.am4v.framework.model.WindowStrategy}
         *        (never {@code null}).
         * @return the new window instance.
         */
        Window createWindow(Map<String, Object> parameters);
    }
}
