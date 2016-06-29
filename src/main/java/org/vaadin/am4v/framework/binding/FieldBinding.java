package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;

/**
 * A binding that binds any model element to a {@link AbstractField}. If the model element implements {@link Property},
 * it will be bound to the field using {@link AbstractField#setPropertyDataSource(Property)}. Changes to the visibility
 * and enablement flags of the model element are reflected in the button but not the other way around.
 */
public class FieldBinding<MODEL extends Serializable> extends ComponentBinding<MODEL, AbstractField> {

    /**
     * Creates a new binding between the given model and view elements. Remember to also call {@link #bind()} to
     * perform the actual binding.
     *
     * @param model the model element.
     * @param field the view element (i.e. the field).
     */
    public FieldBinding(MODEL model, AbstractField field) {
        super(model, field);
    }

    @Override
    public void bind() {
        super.bind();
        getModelAs(Property.class).ifPresent(getView()::setPropertyDataSource);
        getView().setInvalidCommitted(true);
        getView().setBuffered(false);
        getView().setValidationVisible(true);
        getView().setImmediate(true);
    }

    @Override
    public void unbind() {
        getView().setPropertyDataSource(null);
        super.unbind();
    }
}
