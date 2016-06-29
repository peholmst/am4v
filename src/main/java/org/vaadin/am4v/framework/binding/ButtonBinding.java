package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import org.vaadin.am4v.framework.EnabledChangeNotifier;

import com.vaadin.ui.Button;

/**
 * A binding that binds any model element to a {@link Button}. If the model element implements {@link Runnable}, it
 * will be invoked whenever the button is clicked. The binding also supports restoring the enablement state of the
 * button if {@link Button#isDisableOnClick() disable on click} is used. Changes to the visibility and enablement flags
 * of the model element are reflected in the button but not the other way around.
 */
public class ButtonBinding<MODEL extends Serializable> extends ComponentBinding<MODEL, Button> {

    /**
     * Creates a new binding between the given model and view elements. Remember to also call {@link #bind()} to
     * perform the actual binding.
     *
     * @param model the model element.
     * @param button the view element (i.e. the button).
     */
    public ButtonBinding(MODEL model, Button button) {
        super(model, button);
    }

    /**
     * Invokes the model element if it implements the {@link Runnable} interface and restores the enablement state
     * of the button if disable on click is used.
     * 
     * @param event the click event from the button.
     */
    protected void onButtonClick(Button.ClickEvent event) {
        try {
            getModelAs(Runnable.class).ifPresent(Runnable::run);
        } finally {
            if (getView().isDisableOnClick()) {
                getModelAs(EnabledChangeNotifier.class).ifPresent(this::onEnabledChange);
            }
        }
    }

    @Override
    public void bind() {
        super.bind();
        getView().addClickListener(this::onButtonClick);
    }

    @Override
    public void unbind() {
        getView().removeClickListener(this::onButtonClick);
        super.unbind();
    }
}
