package org.vaadin.am4v.framework.binding;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeNotifier;

/**
 * Base class for bindings that binds a model element to a view element.
 */
public abstract class Binding<MODEL, VIEW> implements Serializable {

    private final MODEL model;
    private final VIEW view;

    /**
     * Creates a new binding between the given model and view elements. Remember to also call {@link #bind()} to
     * perform the actual binding.
     *
     * @param model the model element.
     * @param view the view element.
     */
    protected Binding(MODEL model, VIEW view) {
        this.model = Objects.requireNonNull(model, "model must not be null");
        this.view = Objects.requireNonNull(view, "view must not be null");
    }

    /**
     * Returns the model element of this binding.
     * 
     * @return the model element.
     */
    public final MODEL getModel() {
        return model;
    }

    /**
     * Returns the model element cast to another class if supported.
     * 
     * @see #getModel()
     * @param clazz the class to cast to.
     * @return the model element if the cast was successful.
     */
    protected final <T> Optional<T> getModelAs(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        if (clazz.isInstance(model)) {
            return Optional.of(clazz.cast(model));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the view element of this binding.
     * 
     * @return
     */
    public final VIEW getView() {
        return view;
    }

    /**
     * Called when the visibility state of the model element is changed, if the model element implements the
     * {@link EnabledChangeNotifier} interface. Otherwise, this method will never get called.
     * 
     * @param source the model element.
     */
    protected abstract void onVisibleChange(VisibleChangeNotifier source);

    /**
     * Called when the enablement state of the model element is changed, if the model element implements the
     * {@link VisibleChangeNotifier} interface. Otherwise, this method will never get called.
     * 
     * @param source the model element.
     */
    protected abstract void onEnabledChange(EnabledChangeNotifier source);

    /**
     * Performs the actual binding, registering listeners, etc. Subclasses may override but must remember to call
     * {@code super.bind()}.
     * 
     * @see #unbind()
     */
    public void bind() {
        Optional<EnabledChangeNotifier> enabledChangeNotifier = getModelAs(EnabledChangeNotifier.class);
        enabledChangeNotifier.ifPresent(m -> m.addEnabledChangeListener(this::onEnabledChange));
        enabledChangeNotifier.ifPresent(this::onEnabledChange);

        Optional<VisibleChangeNotifier> visibleChangeNotifier = getModelAs(VisibleChangeNotifier.class);
        visibleChangeNotifier.ifPresent(m -> m.addVisibleChangeListener(this::onVisibleChange));
        visibleChangeNotifier.ifPresent(this::onVisibleChange);
    }

    /**
     * Removes the binding previously created by {@link #bind()}, unregistering listeners and otherwise cleaning up
     * resources. Subclasses may override but must remember to call {@code super.bind()}.
     */
    public void unbind() {
        getModelAs(VisibleChangeNotifier.class).ifPresent(m -> m.removeVisibleChangeListener(this::onVisibleChange));
        getModelAs(EnabledChangeNotifier.class).ifPresent(m -> m.removeEnabledChangeListener(this::onEnabledChange));
    }
}
