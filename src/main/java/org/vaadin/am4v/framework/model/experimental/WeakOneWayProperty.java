package org.vaadin.am4v.framework.model.experimental;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.WeakHashMap;

import com.vaadin.data.Property;

/**
 * A property implementation that appears read-only to the outside world, but that can be changed from the backend (
 * i.e. owning model). Application models that use this property actually create instances of
 * {@link WeakOneWayProperty.Backend} but expose only the {@link Backend#getProperty() property} to the outside world.
 * Any {@link com.vaadin.data.Property.ValueChangeListener}s are registered using weak references so there is no
 * need to explicitly remove them afterwards.
 */
public class WeakOneWayProperty<T> implements Property<T>, Property.ValueChangeNotifier {

    private final WeakHashMap<ValueChangeListener, Object> listeners = new WeakHashMap<>();
    private final Class<T> type;
    private T value;

    /**
     * Creates a new {@link WeakOneWayProperty} with the given value and type.
     * 
     * @param value the value of the property.
     * @param type the type of the property.
     */
    protected WeakOneWayProperty(T value, Class<T> type) {
        this.type = type;
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T newValue) throws ReadOnlyException {
        throw new ReadOnlyException("This property is read only from the outside");
    }

    /**
     * Sets the value of the property, notifying all the listeners of the change. If the new value and the old value
     * are equal or {@code null}, nothing happens.
     * 
     * @param newValue the new value.
     */
    protected void doSetValue(T newValue) {
        if (!Objects.equals(this.value, newValue)) {
            this.value = newValue;
            ValueChangeEvent event = (ValueChangeEvent) () -> WeakOneWayProperty.this;
            new HashSet<>(listeners.keySet()).forEach(listener -> listener.valueChange(event));
        }
    }

    @Override
    public Class<? extends T> getType() {
        return type;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        throw new UnsupportedOperationException("The readOnly flag cannot be changed");
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (listener != null) {
            listeners.put(listener, null);
        }
    }

    @Override
    @Deprecated
    public void addListener(ValueChangeListener listener) {
        addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    @Deprecated
    public void removeListener(ValueChangeListener listener) {
        removeValueChangeListener(listener);
    }

    /**
     * Backend class for {@link WeakOneWayProperty}. Application models should refer to the property using this class
     * and expose {@link #getProperty()} to the outside world.
     */
    public static class Backend<T> implements Serializable {

        private final WeakOneWayProperty<T> property;

        /**
         * Creates a new {@code Backend}.
         * 
         * @param initialValue the initial value of the property.
         * @param type the type of the property.
         */
        public Backend(T initialValue, Class<T> type) {
            property = new WeakOneWayProperty<T>(initialValue, type);
        }

        /**
         * Sets the value of the property, notifying any value change listeners.
         *
         * @see WeakOneWayProperty#doSetValue(Object)
         * @param newValue the new value.
         */
        public void setValue(T newValue) {
            property.doSetValue(newValue);
        }

        /**
         * Returns the {@link WeakOneWayProperty} backed by this backend.
         * 
         * @return the property.
         */
        public WeakOneWayProperty<T> getProperty() {
            return property;
        }
    }
}
