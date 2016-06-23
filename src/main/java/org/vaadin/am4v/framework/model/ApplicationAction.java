package org.vaadin.am4v.framework.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.vaadin.am4v.framework.EnabledChangeListener;
import org.vaadin.am4v.framework.EnabledChangeNotifier;
import org.vaadin.am4v.framework.VisibleChangeListener;
import org.vaadin.am4v.framework.VisibleChangeNotifier;
import org.vaadin.am4v.framework.binding.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;

/**
 * 
 */
public class ApplicationAction implements EnabledChangeNotifier, VisibleChangeNotifier, Runnable {

    private final BindingCollection bindings = new BindingCollection();
    private final List<EnabledChangeListener> enabledChangeListeners = new LinkedList<>();
    private final List<VisibleChangeListener> visibleChangeListeners = new LinkedList<>();
    private boolean enabled = true;
    private boolean visible = true;
    private final ActionWorker worker;

    /**
     * 
     */
    protected ApplicationAction() {
        worker = (action) -> {
            throw new IllegalStateException("Please override the run() method");
        };
    }

    /**
     * 
     * @param worker
     */
    public ApplicationAction(ActionWorker worker) {
        this.worker = Objects.requireNonNull(worker, "worker must not be null");
    }

    /**
     *
     */
    @Override
    public void run() {
        worker.execute(this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            new LinkedList<>(enabledChangeListeners).forEach(l -> l.onEnabledChange(this));
        }
    }

    @Override
    public void addEnabledChangeListener(EnabledChangeListener listener) {
        if (listener != null) {
            enabledChangeListeners.add(listener);
        }
    }

    @Override
    public void removeEnabledChangeListener(EnabledChangeListener listener) {
        if (listener != null) {
            enabledChangeListeners.remove(listener);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;
            new LinkedList<>(visibleChangeListeners).forEach(l -> l.onVisibleChanged(this));
        }
    }

    @Override
    public void addVisibleChangeListener(VisibleChangeListener listener) {
        if (listener != null) {
            visibleChangeListeners.add(listener);
        }
    }

    @Override
    public void removeVisibleChangeListener(VisibleChangeListener listener) {
        if (listener != null) {
            visibleChangeListeners.remove(listener);
        }
    }

    /**
     *
     * @param menuItem
     */
    public void bind(MenuBar.MenuItem menuItem) {
        bind(menuItem, new MenuItemBinding<>(this, menuItem));
    }

    /**
     *
     * @param button
     */
    public void bind(Button button) {
        bind(button, new ButtonBinding<>(this, button));
    }

    /**
     * 
     * @param component
     */
    public void bind(Component component) {
        bind(component, new ComponentBinding<>(this, component));
    }

    /**
     *
     * @param view
     * @param binding
     * @param <VIEW>
     */
    protected final <VIEW> void bind(VIEW view, Binding<ApplicationAction, VIEW> binding) {
        Objects.requireNonNull(view, "view must not be null");
        Objects.requireNonNull(binding, "binding must not be null");
        if (binding.getModel() != this) {
            throw new IllegalArgumentException("Binding must be bound to this ApplicationAction");
        }
        bindings.bind(view, binding);
    }

    /**
     * 
     * @param view
     */
    public final void unbind(Object view) {
        bindings.unbind(view);
    }

    public interface ActionWorker {
        void execute(ApplicationAction action);
    }
}
