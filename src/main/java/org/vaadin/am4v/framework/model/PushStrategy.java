package org.vaadin.am4v.framework.model;

import java.io.Serializable;

import com.vaadin.ui.UI;

/**
 * Strategy for performing server pushes. By abstracting this away, different strategies can be plugged in for e.g.
 * testing.
 */
public interface PushStrategy extends Serializable {

    /**
     * Executes the given job safely and ends with a server push.
     * 
     * @see UI#access(Runnable)
     * @param job the job to execute.
     */
    void execute(Runnable job);

    /**
     * Returns the default push strategy, which is to pass the job straight to the {@link UI#access(Runnable)} method
     * of the current {@link UI}.
     * 
     * @see UI#getCurrent()
     * @return the default push strategy.
     */
    static PushStrategy getDefault() {
        return (PushStrategy) job -> UI.getCurrent().access(job);
    }
}
