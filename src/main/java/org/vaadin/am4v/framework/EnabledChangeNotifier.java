package org.vaadin.am4v.framework;

import java.io.Serializable;

/**
 * Interface to be implemented by elements that can be enabled or disabled and that can notify listeners when
 * this state is changed.
 */
public interface EnabledChangeNotifier extends Serializable {

    /**
     * Returns whether this element is enabled or disabled.
     * 
     * @return true if enabled, false if disabled.
     */
    boolean isEnabled();

    /**
     * Enables or disables this element, notifying listeners of the change.
     * 
     * @param enabled true to enable, false to disable.
     */
    void setEnabled(boolean enabled);

    /**
     * Registers the specified listener to be notified when the {@code enabled} state is changed. If the listener
     * is {@code null}, nothing happens.
     *
     * @param listener the listener to add.
     */
    void addEnabledChangeListener(EnabledChangeListener listener);

    /**
     * Removes the specified listener. After this, it will no longer be notified when the {@code enabled} state is
     * changed. If the listener is {@code null} or had not been registered in the first place, nothing happens.
     * 
     * @param listener the listener to remove.
     */
    void removeEnabledChangeListener(EnabledChangeListener listener);
}
