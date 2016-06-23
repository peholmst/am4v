package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import com.vaadin.ui.Component;
import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeNotifier;

/**
 * 
 * @param <MODEL>
 * @param <VIEW>
 */
public class ComponentBinding<MODEL extends Serializable, VIEW extends Component> extends Binding<MODEL, VIEW> {

    /**
     *
     * @param model
     * @param view
     */
    public ComponentBinding(MODEL model, VIEW view) {
        super(model, view);
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
