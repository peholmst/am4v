package org.vaadin.am4v.framework.model;

import java.io.Serializable;
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
 * An application action is an operation that a user of the application can perform. An action can be enabled or
 * disabled, shown or hidden and bound to a UI component. For example, the same action can be bound to both a menu item
 * and a button. Both will execute the action and both will follow the state of the action (enabled, hidden, etc).
 * <p>
 * Typically the actions will be defined in an {@link ApplicationModel}.
 * 
 * @see ContextualApplicationAction
 * @see ApplicationProperty
 */
public class ApplicationAction implements EnabledChangeNotifier, VisibleChangeNotifier, Runnable {

    private final BindingCollection bindings = new BindingCollection();
    private final List<EnabledChangeListener> enabledChangeListeners = new LinkedList<>();
    private final List<VisibleChangeListener> visibleChangeListeners = new LinkedList<>();
    private boolean enabled = true;
    private boolean visible = true;
    private final ActionWorker worker;

    /**
     * Default constructor for the action. You will have to override {@link #run()} if you use this constructor.
     */
    protected ApplicationAction() {
        worker = (action) -> {
            throw new IllegalStateException("Please override the run() method");
        };
    }

    /**
     * Constructor that takes a worker that actually implements the action. This is to make it possible to use
     * lambdas without having to override anything.
     * 
     * @param worker the worker that implements the action.
     */
    public ApplicationAction(ActionWorker worker) {
        this.worker = Objects.requireNonNull(worker, "worker must not be null");
    }

    /**
     * Performs the action. If an {@link ActionWorker} has been set, it will be executed. Otherwise, this method
     * will throw an exception and will need to be overridden.
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
     * Binds this action to the specified menu item.
     * 
     * @see MenuItemBinding
     * @param menuItem the menu item.
     */
    public void bind(MenuBar.MenuItem menuItem) {
        bind(new MenuItemBinding<>(this, menuItem));
    }

    /**
     * Bins this action to the specified button.
     * 
     * @see ButtonBinding
     * @param button the button.
     */
    public void bind(Button button) {
        bind(new ButtonBinding<>(this, button));
    }

    /**
     * Binds this action to the specified component.
     *
     * @see ComponentBinding
     * @param component the component.
     */
    public void bind(Component component) {
        bind(new ComponentBinding<>(this, component));
    }

    /**
     * Registers the specified {@code binding} with this action.
     * 
     * @param binding the binding to use.
     */
    protected final <VIEW> void bind(Binding<ApplicationAction, VIEW> binding) {
        Objects.requireNonNull(binding, "binding must not be null");
        if (binding.getModel() != this) {
            throw new IllegalArgumentException("Binding must be bound to this ApplicationAction");
        }
        bindings.bind(binding);
    }

    /**
     * Unbinds this action from the specified view (UI component).
     * 
     * @param view the view.
     */
    public final void unbind(Object view) {
        bindings.unbind(view);
    }

    /**
     * Functional interface for an action worker that makes it possible to implement {@link ApplicationAction}s using
     * lambdas.
     *
     * @see ApplicationAction#ApplicationAction(ActionWorker)
     */
    @FunctionalInterface
    public interface ActionWorker extends Serializable {
        /**
         * Executes the action.
         * 
         * @param action the action that is being executed (i.e. the owner of this worker).
         */
        void execute(ApplicationAction action);
    }
}
