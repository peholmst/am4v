package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import com.vaadin.data.Property;
import com.vaadin.data.Validatable;
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
        getModelAs(Validatable.class).ifPresent(m -> m.getValidators().forEach(getView()::addValidator));
    }

    @Override
    public void unbind() {
        getModelAs(Validatable.class).ifPresent(m -> m.getValidators().forEach(getView()::removeValidator));
        getView().setPropertyDataSource(null);
        super.unbind();
    }
}
