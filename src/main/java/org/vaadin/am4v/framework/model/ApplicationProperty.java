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
 * An extended version of the Vaadin {@link ObjectProperty} that adds support for enabling/disabling, showing/hiding
 * and validating the property. Properties can be bound to UI components such as different input fields.
 * <p>
 * Typically the properties will be defined in an {@link ApplicationModel}.
 * 
 * @see ApplicationAction
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

    /**
     * Creates a new application property.
     * 
     * @see ObjectProperty#ObjectProperty(Object)
     * @param value the value of the property (must not be {@code null}).
     */
    public ApplicationProperty(T value) {
        super(value);
    }

    /**
     * Creates a new application property.
     * 
     * @see ObjectProperty#ObjectProperty(Object, Class)
     * @param value the value of the property.
     * @param type the type of the property value.
     */
    public ApplicationProperty(T value, Class<T> type) {
        super(value, type);
    }

    /**
     * Creates a new application property.
     *
     * @see ObjectProperty#ObjectProperty(Object, Class, boolean)
     * @param value the value of the property.
     * @param type the type of the property value.
     * @param readOnly whether the property is read-only or writable.
     */
    public ApplicationProperty(T value, Class<T> type, boolean readOnly) {
        super(value, type, readOnly);
    }

    /**
     * Creates a new application property.
     * 
     * @param value the value of the property.
     * @param type the type of the property value.
     * @param readOnly whether the property is read-only or writable.
     * @param enabled whether the property is enabled or disabled.
     */
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
     * Binds this property to the specified component.
     *
     * @see ComponentBinding
     * @param component the component.
     */
    public void bind(Component component) {
        bind(component, new ComponentBinding<>(this, component));
    }

    /**
     * Binds this property to the specified field.
     * 
     * @see FieldBinding
     * @param field the field.
     */
    public void bind(AbstractField<T> field) {
        bind(field, new FieldBinding<>(this, field));
    }

    /**
     * Binds this property to the specified {@code view} using the specified {@code binding}.
     *
     * @param view the view (UI component).
     * @param binding the binding to use.
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
     * Unbinds this property from the specified view (UI component).
     *
     * @param view the view.
     */
    public final void unbind(Object view) {
        bindings.unbind(view);
    }

    @Override
    public final void addValidator(Validator validator) {
        if (validator != null) {
            validators.add(validator);
        }
    }

    @Override
    public final void removeValidator(Validator validator) {
        if (validator != null) {
            validators.remove(validator);
        }
    }

    @Override
    public final void removeAllValidators() {
        validators.clear();
    }

    @Override
    public final Collection<Validator> getValidators() {
        return Collections.unmodifiableCollection(validators);
    }

    @Override
    public final boolean isValid() {
        try {
            validate();
            return true;
        } catch (Validator.InvalidValueException ex) {
            return false;
        }
    }

    @Override
    public final void validate() throws Validator.InvalidValueException {
        Object value = getValue();
        validators.forEach(v -> v.validate(value));
    }

    @Override
    public final boolean isInvalidAllowed() {
        return invalidAllowed;
    }

    @Override
    public final void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
        this.invalidAllowed = invalidValueAllowed;
    }
}
