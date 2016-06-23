package org.vaadin.am4v.framework.model;

import java.util.*;

import org.vaadin.am4v.framework.EnabledChangeListener;
import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeListener;
import org.vaadin.am4v.framework.VisibleChangeNotifier;
import org.vaadin.am4v.framework.binding.Binding;
import org.vaadin.am4v.framework.binding.BindingCollection;
import org.vaadin.am4v.framework.binding.ComponentBinding;
import org.vaadin.am4v.framework.binding.FieldBinding;

import com.vaadin.data.Validatable;
import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

/**
 * 
 * @param <T>
 */
public class ApplicationProperty<T> extends ObjectProperty<T>
    implements EnabledChangeNotifier, VisibleChangeNotifier, Validatable {

    private final BindingCollection bindings = new BindingCollection();
    private final List<EnabledChangeListener> enabledChangeListeners = new LinkedList<>();
    private final List<VisibleChangeListener> visibleChangeListeners = new LinkedList<>();
    private final List<Validator> validators = new LinkedList<>();
    private boolean enabled = true;
    private boolean visible = true;
    private boolean invalidAllowed = true;

    public ApplicationProperty(T value) {
        super(value);
    }

    public ApplicationProperty(T value, Class<T> type) {
        super(value, type);
    }

    public ApplicationProperty(T value, Class<T> type, boolean readOnly) {
        super(value, type, readOnly);
    }

    public ApplicationProperty(T value, Class<T> type, boolean readOnly, boolean enabled) {
        super(value, type, readOnly);
        this.enabled = enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            new LinkedList<>(enabledChangeListeners).forEach(l -> l.onEnabledChange(this));
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void addEnabledChangeListener(EnabledChangeListener listener) {
        if (listener != null) {
            enabledChangeListeners.add(listener);
        }
    }

    @Override
    public void removeEnabledChangeListener(EnabledChangeListener listener) {
        if (listener != null) {
            enabledChangeListeners.remove(listener);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;
            new LinkedList<>(visibleChangeListeners).forEach(l -> l.onVisibleChanged(this));
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void addVisibleChangeListener(VisibleChangeListener listener) {
        if (listener != null) {
            visibleChangeListeners.add(listener);
        }
    }

    @Override
    public void removeVisibleChangeListener(VisibleChangeListener listener) {
        if (listener != null) {
            visibleChangeListeners.remove(listener);
        }
    }

    /**
     *
     * @param component
     */
    public void bind(Component component) {
        bind(component, new ComponentBinding<>(this, component));
    }

    /**
     * 
     * @param field
     */
    public void bind(AbstractField<T> field) {
        bind(field, new FieldBinding<>(this, field));
    }

    /**
     *
     * @param view
     * @param binding
     * @param <VIEW>
     */
    protected final <VIEW> void bind(VIEW view, Binding<ApplicationProperty, VIEW> binding) {
        Objects.requireNonNull(view, "view must not be null");
        Objects.requireNonNull(binding, "binding must not be null");
        if (binding.getModel() != this) {
            throw new IllegalArgumentException("Binding must be bound to this ApplicationProperty");
        }
        bindings.bind(view, binding);
    }

    /**
     *
     * @param view
     */
    public final void unbind(Object view) {
        bindings.unbind(view);
    }

    @Override
    public void addValidator(Validator validator) {
        if (validator != null) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Validator validator) {
        if (validator != null) {
            validators.remove(validator);
        }
    }

    @Override
    public void removeAllValidators() {
        validators.clear();
    }

    @Override
    public Collection<Validator> getValidators() {
        return Collections.unmodifiableCollection(validators);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (Validator.InvalidValueException ex) {
            return false;
        }
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        Object value = getValue();
        validators.forEach(v -> v.validate(value));
    }

    @Override
    public boolean isInvalidAllowed() {
        return invalidAllowed;
    }

    @Override
    public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
        this.invalidAllowed = invalidValueAllowed;
    }
}
