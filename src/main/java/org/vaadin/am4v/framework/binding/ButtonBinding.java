package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import com.vaadin.ui.Button;
import org.vaadin.am4v.framework.EnabledChangeNotifier;

/**
 *
 */
public class ButtonBinding<MODEL extends Serializable> extends ComponentBinding<MODEL, Button> {

    /**
     * 
     * @param model
     * @param view
     */
    public ButtonBinding(MODEL model, Button view) {
        super(model, view);
    }

    /**
     *
     * @param event
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
