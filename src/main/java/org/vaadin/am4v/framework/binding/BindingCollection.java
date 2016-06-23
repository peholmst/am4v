package org.vaadin.am4v.framework.binding;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 
 */
public class BindingCollection implements Serializable {

    private final Map<Object, Binding<?, ?>> bindingMap = new HashMap<>();

    /**
     * 
     * @param view
     * @param binding
     * @param <VIEW>
     */
    public <VIEW> void bind(VIEW view, Binding<?, VIEW> binding) {
        Objects.requireNonNull(view, "view must not be null");
        Objects.requireNonNull(binding, "binding must not be null");
        if (bindingMap.containsKey(view)) {
            throw new IllegalStateException("The view has already been bound to the model");
        }
        bindingMap.put(view, binding);
        binding.bind();
    }

    /**
     * 
     * @param view
     */
    public void unbind(Object view) {
        Objects.requireNonNull(view, "view must not be null");
        Binding<?, ?> binding = bindingMap.remove(view);
        if (binding != null) {
            binding.unbind();
        }
    }
}
