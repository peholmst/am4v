package org.vaadin.am4v.framework.binding;

import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeNotifier;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
public abstract class Binding<MODEL, VIEW> implements Serializable {

    private final MODEL model;
    private final VIEW view;

    /**
     * 
     * @param model
     * @param view
     */
    public Binding(MODEL model, VIEW view) {
        this.model = Objects.requireNonNull(model, "model must not be null");
        this.view = Objects.requireNonNull(view, "view must not be null");
    }

    /**
     * 
     * @return
     */
    public final MODEL getModel() {
        return model;
    }

    /**
     * 
     * @param clazz
     * @param <T>
     * @return
     */
    protected final <T> Optional<T> getModelAs(Class<T> clazz) {
        if (clazz.isInstance(model)) {
            return Optional.of(clazz.cast(model));
        } else {
            return Optional.empty();
        }
    }

    /**
     *
     * @return
     */
    public final VIEW getView() {
        return view;
    }

    /**
     *
     * @param source
     */
    protected abstract void onVisibleChange(VisibleChangeNotifier source);

    /**
     *
     * @param source
     */
    protected abstract void onEnabledChange(EnabledChangeNotifier source);

    /**
     *
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
     * 
     */
    public void unbind() {
        getModelAs(VisibleChangeNotifier.class).ifPresent(m -> m.removeVisibleChangeListener(this::onVisibleChange));
        getModelAs(EnabledChangeNotifier.class).ifPresent(m -> m.removeEnabledChangeListener(this::onEnabledChange));
    }
}
