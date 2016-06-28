package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by petterwork on 27/06/16.
 */
public class ContextualApplicationAction<C> extends ApplicationAction {

    private final ContextualActionWorker<C> worker;

    /**
     *
     */
    public ContextualApplicationAction() {
        worker = null;
    }

    /**
     *
     * @param worker
     */
    public ContextualApplicationAction(ContextualActionWorker worker) {
        this.worker = Objects.requireNonNull(worker, "worker must not be null");
    }

    @Override
    public void run() {
        worker.execute(this, null);
    }

    /**
     *
     * @param context
     */
    public void run(C context) {
        worker.execute(this, context);
    }

    /**
     *
     * @param context
     * @return
     */
    public boolean isVisible(C context) {
        return isVisible();
    }

    /**
     * 
     * @param context
     * @return
     */
    public boolean isEnabled(C context) {
        return isEnabled();
    }

    /**
     *
     * @param <C>
     */
    @FunctionalInterface
    public interface ContextualActionWorker<C> extends Serializable {
        void execute(ContextualApplicationAction<C> action, C context);
    }
}
