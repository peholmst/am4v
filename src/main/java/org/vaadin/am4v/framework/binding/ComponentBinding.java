package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeNotifier;

import com.vaadin.ui.Component;

/**
 * A binding that binds any model element to a {@link Component}. Changes to the visibility and enablement flags of the
 * model element are reflected in the component but not the other way around.
 */
public class ComponentBinding<MODEL extends Serializable, VIEW extends Component> extends Binding<MODEL, VIEW> {

    /**
     * Creates a new binding between the given model and view elements. Remember to also call {@link #bind()} to
     * perform the actual binding.
     *
     * @param model the model element.
     * @param component the view element (i.e. the component).
     */
    public ComponentBinding(MODEL model, VIEW component) {
        super(model, component);
    }

    @Override
    protected void onVisibleChange(VisibleChangeNotifier source) {
        getView().setVisible(source.isVisible());
    }

    @Override
    protected void onEnabledChange(EnabledChangeNotifier source) {
        getView().setEnabled(source.isEnabled());
    }
}
