package org.vaadin.am4v.framework.ui;

import java.io.Serializable;
import java.util.Objects;

import org.vaadin.am4v.framework.model.Parameters;

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

    /**
     * Creates a new {@code SingleWindowProvider} that creates new instances of the given {@code windowClass}.
     * 
     * @param windowName the name of the window to show.
     * @param windowClass the class of the window to create.
     */
    public <W extends Window & ParameterizedWindow> SingleWindowProvider(String windowName, Class<W> windowClass) {
        this.windowName = Objects.requireNonNull(windowName);
        this.windowFactory = createWindowFactoryForParameterizedWindow(Objects.requireNonNull(windowClass));
    }

    /**
     * Creates a new {@code SingleWindowProvider} that creates new instances of the given {@code windowClass}. The
     * name of the window is taken from the {@link WindowName} annotation which must be present on the window class.
     * 
     * @param windowClass the class of the window to create.
     */
    public <W extends Window & ParameterizedWindow> SingleWindowProvider(Class<W> windowClass) {
        Objects.requireNonNull(windowClass);
        WindowName annotation = windowClass.getAnnotation(WindowName.class);
        if (annotation == null) {
            throw new IllegalArgumentException(
                "The class " + windowClass.getSimpleName() + " does not have a WindowName annotation");
        }
        this.windowName = annotation.value();
        this.windowFactory = createWindowFactoryForParameterizedWindow(windowClass);
    }

    private <W extends Window & ParameterizedWindow> WindowFactory createWindowFactoryForParameterizedWindow(
        Class<W> windowClass) {
        return params -> {
            try {
                W window = windowClass.newInstance();
                window.setParameters(params);
                return window;
            } catch (Exception ex) {
                throw new RuntimeException("Error creating window instance", ex);
            }
        };
    }

    @Override
    public boolean hasWindow(String name) {
        return windowName.equals(name);
    }

    @Override
    public Window getWindow(String name, Parameters parameters) {
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
        Window createWindow(Parameters parameters);
    }
}
