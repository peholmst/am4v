package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is an extension of the {@link ApplicationAction} that supports passing a per-execution context when running the
 * action. The need for this came up when adding support for context menus where you can perform an action on an item
 * without selecting it.
 */
public class ContextualApplicationAction<C> extends ApplicationAction {

    private final ContextualActionWorker<C> worker;

    /**
     * Default constructor for the action. You will have to override {@link #run(Object)} if you use this constructor.
     */
    protected ContextualApplicationAction() {
        worker = (action, context) -> {
            throw new IllegalStateException("Please override the run(C) method");
        };
    }

    /**
     * Constructor that takes a worker that actually implements the action. This is to make it possible to use
     * lambdas without having to override anything.
     *
     * @param worker the worker that implements the action.
     */
    public ContextualApplicationAction(ContextualActionWorker<C> worker) {
        this.worker = Objects.requireNonNull(worker, "worker must not be null");
    }

    @Override
    public final void run() {
        run(null);
    }

    /**
     * Performs the action. If a {@link ContextualActionWorker} has been set, it will be executed. Otherwise, this
     * method will throw an exception and will need to be overridden.
     *
     * @param context the context or {@code null} if no context is available.
     */
    public void run(C context) {
        worker.execute(this, context);
    }

    /**
     * Checks if this action is visible for the given {@code context}. By default, this method will delegate to
     * {@link #isVisible()}.
     * 
     * @param context the context or {@code null} if no context is available.
     * @return true if the action is visible, false if it is hidden.
     */
    public boolean isVisible(C context) {
        return isVisible();
    }

    /**
     * Checks if this action is enabled for the given {@code context}. By default, this method will delegate to
     * {@link #isEnabled()}.
     *
     * @param context the context or {@code null} if no context is available.
     * @return true if the action is enabled, false if it is disabled.
     */
    public boolean isEnabled(C context) {
        return isEnabled();
    }

    /**
     * Functional interface for an action worker that makes it possible to implement {@link ContextualApplicationAction}
     * s using lambdas.
     * 
     * @param <C> the type of the context.
     */
    @FunctionalInterface
    public interface ContextualActionWorker<C> extends Serializable {
        /**
         * Executes the action.
         * 
         * @param action the action that is being executed (i.e. the owner of this worker).
         * @param context the context or {@code null} if no context is available.
         */
        void execute(ContextualApplicationAction<C> action, C context);
    }
}
