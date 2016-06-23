package org.vaadin.am4v.framework.binding;

import java.io.Serializable;

import com.vaadin.ui.MenuBar;
import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeNotifier;

/**
 *
 */
public class MenuItemBinding<MODEL extends Serializable> extends Binding<MODEL, MenuBar.MenuItem> {

    /**
     * 
     * @param model
     * @param menuItem
     */
    public MenuItemBinding(MODEL model, MenuBar.MenuItem menuItem) {
        super(model, menuItem);
    }

    /**
     * 
     * @param menuItem
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
