package org.vaadin.am4v.framework;

import com.vaadin.data.util.ObjectProperty;
import org.vaadin.am4v.framework.model.ApplicationModel;

public class WindowApplicationModel extends ApplicationModel {

    private final ObjectProperty<Boolean> windowVisible = new ObjectProperty<>(false);

    public ObjectProperty<Boolean> windowVisible() {
        return windowVisible;
    }

    public void show() {
        windowVisible.setValue(true);
    }

    public void hide() {
        windowVisible.setValue(false);
    }
}
