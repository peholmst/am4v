package org.vaadin.am4v.framework.binding;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A collection of bindings, mainly used by models to keep track of bindings and their views so that they can be
 * properly unbound when no longer needed.
 */
public class BindingCollection implements Serializable {

    private final Map<Object, Binding<?, ?>> bindingMap = new HashMap<>();

    /**
     * Adds the specified binding to the collection of bindings and invokes its {@link Binding#bind()} method.
     * 
     * @param binding the binding to register.
     * @throws IllegalStateException if a binding has already been registered for the same view.
     */
    public <VIEW> void bind(Binding<?, VIEW> binding) {
        Objects.requireNonNull(binding, "binding must not be null");
        VIEW view = binding.getView();
        if (bindingMap.containsKey(view)) {
            throw new IllegalStateException("The binding has already been registered");
        }
        bindingMap.put(view, binding);
        binding.bind();
    }

    /**
     * Removes the binding for the specified view and invokes its {@link Binding#unbind()} method. If no binding is
     * found, nothing happens.
     * 
     * @param view the view to unbind.
     */
    public void unbind(Object view) {
        Objects.requireNonNull(view, "view must not be null");
        Binding<?, ?> binding = bindingMap.remove(view);
        if (binding != null) {
            binding.unbind();
        }
    }
}
