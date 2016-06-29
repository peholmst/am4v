package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeNotifier;

import com.vaadin.ui.MenuBar;

/**
 * A binding that binds any model element to a {@link com.vaadin.ui.MenuBar.MenuItem}. If the model element implements
 * {@link Runnable}, it will be invoked whenever the menu item is clicked. Changes to the visibility and enablement
 * flags of the model element are reflected in the menu item but not the other way around.
 */
public class MenuItemBinding<MODEL extends Serializable> extends Binding<MODEL, MenuBar.MenuItem> {

    /**
     * Creates a new binding between the given model and view elements. Remember to also call {@link #bind()} to
     * perform the actual binding.
     *
     * @param model the model element.
     * @param menuItem the view element (i.e. the menu item).
     */
    public MenuItemBinding(MODEL model, MenuBar.MenuItem menuItem) {
        super(model, menuItem);
    }

    /**
     * Invokes the model element if it implements the {@link Runnable} interface.
     *
     * @param menuItem the menu item.
     */
    protected void onMenuItemSelected(MenuBar.MenuItem menuItem) {
        getModelAs(Runnable.class).ifPresent(Runnable::run);
    }

    @Override
    protected void onVisibleChange(VisibleChangeNotifier source) {
        getView().setVisible(source.isVisible());
    }

    @Override
    protected void onEnabledChange(EnabledChangeNotifier source) {
        getView().setEnabled(source.isEnabled());
    }

    @Override
    public void unbind() {
        getView().setCommand(null);
    }

    @Override
    public void bind() {
        if (getView().getCommand() != null) {
            throw new IllegalStateException("The menu item already has a Command assigned");
        }
        getView().setCommand(this::onMenuItemSelected);
    }
}
