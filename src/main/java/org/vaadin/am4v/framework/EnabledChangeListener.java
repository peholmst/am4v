package org.vaadin.am4v.framework;

import java.io.Serializable;

/**
 * Interface for listeners that wish to be notified when the state of a {@link EnabledChangeNotifier} is changed.
 */
@FunctionalInterface
public interface EnabledChangeListener extends Serializable {

    /**
     * Called whenever the {@code enabled} state of {@code source} is changed.
     * 
     * @param source the source of the event (never {@code null}).
     */
    void onEnabledChange(EnabledChangeNotifier source);
}
