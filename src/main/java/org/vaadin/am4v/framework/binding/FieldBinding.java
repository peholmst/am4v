package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.AbstractField;

/**
 *
 */
public class FieldBinding<MODEL extends Serializable> extends ComponentBinding<MODEL, AbstractField> {

    /**
     * @param model
     * @param view
     */
    public FieldBinding(MODEL model, AbstractField view) {
        super(model, view);
    }

    @Override
    public void bind() {
        super.bind();
        getModelAs(Property.class).ifPresent(getView()::setPropertyDataSource);
        getView().setInvalidCommitted(true);
        getView().setBuffered(false);
        getView().setValidationVisible(true);
    }

    @Override
    public void unbind() {
        getView().setPropertyDataSource(null);
        super.unbind();
    }
}
