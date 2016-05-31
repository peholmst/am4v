package org.vaadin.am4v.framework;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;

public class ApplicationModelProperty<T> extends ObjectProperty<T> {

    private List<EnabledChangeListener> enabledChangeListeners;
    private boolean enabled;

    public ApplicationModelProperty(T value) {
        super(value);
    }

    public ApplicationModelProperty(T value, Class<T> type) {
        super(value, type);
    }

    public ApplicationModelProperty(T value, Class<T> type, boolean readOnly) {
        super(value, type, readOnly);
    }

    public ApplicationModelProperty(T value, Class<T> type, boolean readOnly, boolean enabled) {
        super(value, type, readOnly);
        this.enabled = enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabledChangeListeners != null) {
            new LinkedList<>(enabledChangeListeners)
                .forEach(listener -> listener.enabledChange((EnabledChangeEvent) () -> ApplicationModelProperty.this));
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addEnabledChangeListener(EnabledChangeListener listener) {
        if (enabledChangeListeners == null) {
            enabledChangeListeners = new LinkedList<>();
        }
        enabledChangeListeners.add(listener);
    }

    public void removeEnabledChangeListener(EnabledChangeListener listener) {
        if (enabledChangeListeners != null) {
            enabledChangeListeners.remove(listener);
            if (enabledChangeListeners.isEmpty()) {
                enabledChangeListeners = null;
            }
        }
    }

    public interface EnabledChangeEvent extends Serializable {
        ApplicationModelProperty<?> getProperty();
    }

    public interface EnabledChangeListener extends Serializable {
        void enabledChange(EnabledChangeEvent event);
    }

    private Map<Component, EnabledChangeListener> componentEnabledChangeListenerMap;

    public void bind(Component component) {
        if (componentEnabledChangeListenerMap == null) {
            componentEnabledChangeListenerMap = new HashMap<>();
        }
        if (!componentEnabledChangeListenerMap.containsKey(component)) {
            if (component instanceof Property.Viewer) {
                ((Property.Viewer) component).setPropertyDataSource(this);
            }
            component.setEnabled(isEnabled());
            EnabledChangeListener listener = event -> component.setEnabled(event.getProperty().isEnabled());
            componentEnabledChangeListenerMap.put(component, listener);
            addEnabledChangeListener(listener);
        }
    }

    public void unbind(Component component) {
        if (component instanceof Property.Viewer) {
            ((Property.Viewer) component).setPropertyDataSource(null);
        }
        EnabledChangeListener listener = componentEnabledChangeListenerMap.remove(component);
        if (listener != null) {
            removeEnabledChangeListener(listener);
        }
        if (componentEnabledChangeListenerMap.isEmpty()) {
            componentEnabledChangeListenerMap = null;
        }
    }
}
