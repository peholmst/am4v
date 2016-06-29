package org.vaadin.am4v.framework.model;

import com.vaadin.data.util.ObjectProperty;

/**
 * Base class for application models who drive a Vaadin {@link com.vaadin.ui.Window}. This is still an
 * <b>experimental</b> class since some common use cases, such as preventing the window from being closed if there are
 * unsaved changes, are not supported yet. Also I'm not sure whether this is actually the best way of handling windows.
 *
 * @see org.vaadin.am4v.framework.ui.WindowFactory
 */
public abstract class WindowApplicationModel extends ApplicationModel {

    private final ObjectProperty<Boolean> windowVisible = new ObjectProperty<>(false);

    /**
     * Creates a new root model (no parent) with the specified strategies.
     *
     * @param navigatorStrategy the navigator strategy to use, or {@code null} to use the
     *        {@link NavigatorStrategy#getDefault() default}.
     * @param pushStrategy the push strategy to use, or {@code null} to use the {@link PushStrategy#getDefault()
     *        default}.
     * @param notificationStrategy the notification strategy to use, or {@code null} to use the
     *        {@link NotificationStrategy#getDefault() default}.
     */
    public WindowApplicationModel(NavigatorStrategy navigatorStrategy, PushStrategy pushStrategy,
        NotificationStrategy notificationStrategy) {
        super(navigatorStrategy, pushStrategy, notificationStrategy);
    }

    /**
     * Creates a new root model (no parent) with the default strategies.
     *
     * @see NavigatorStrategy#getDefault()
     * @see PushStrategy#getDefault()
     * @see NotificationStrategy#getDefault()
     */
    public WindowApplicationModel() {
    }

    /**
     * Creates a new child model with the specified parent. The strategies are inherited from the parent.
     *
     * @see #detachFromParent()
     * @param parent the parent model.
     */
    public WindowApplicationModel(ApplicationModel parent) {
        super(parent);
    }

    /**
     * Returns the property that controls whether the window is visible or hidden. Exposing this as a property directly
     * is problematic because you can't perform any pre-show or pre-hide checks that might change the outcome of the
     * operation.
     * 
     * @return the property (never {@code null}).
     */
    public final ObjectProperty<Boolean> windowVisible() {
        return windowVisible;
    }

    /**
     * Shows the window.
     */
    protected final void show() {
        windowVisible.setValue(true);
    }

    /**
     * Hides the window.
     */
    protected final void hide() {
        windowVisible.setValue(false);
    }
}
