package org.vaadin.am4v.framework.model;

import com.vaadin.data.util.ObjectProperty;

public abstract class WindowApplicationModel extends ApplicationModel {

    private final ObjectProperty<Boolean> windowVisible = new ObjectProperty<>(false);

    public WindowApplicationModel(NavigatorStrategy navigatorStrategy, PushStrategy pushStrategy,
        NotificationStrategy notificationStrategy) {
        super(navigatorStrategy, pushStrategy, notificationStrategy);
    }

    public WindowApplicationModel() {
    }

    public WindowApplicationModel(ApplicationModel parent) {
        super(parent);
    }

    public final ObjectProperty<Boolean> windowVisible() {
        return windowVisible;
    }

    protected final void show() {
        windowVisible.setValue(true);
    }

    protected final void hide() {
        windowVisible.setValue(false);
    }
}
