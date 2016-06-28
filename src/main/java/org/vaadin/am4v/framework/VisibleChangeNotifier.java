package org.vaadin.am4v.framework;

import java.io.Serializable;

/**
 * Interface to be implemented by elements that can be shown or hidden and that can notify listeners when this state
 * is changed.
 */
public interface VisibleChangeNotifier extends Serializable {

    /**
     * Returns whether this element is visible or hidden.
     * 
     * @return true if visible, false if hidden.
     */
    boolean isVisible();

    /**
     * Shows or hides this element, notifying listeners of the change.
     * 
     * @param visible true to show, false to hide.
     */
    void setVisible(boolean visible);

    /**
     * Registers the specified listener to be notified when the {@code visible} state is changed. If the listener
     * is {@code null}, nothing happens.
     *
     * @param listener the listener to add.
     */
    void addVisibleChangeListener(VisibleChangeListener listener);

    /**
     * Removes the specified listener. After this, it will no longer be notified when the {@code visible} state is
     * changed. If the listener is {@code null} or had not been registered in the first place, nothing happens.
     * 
     * @param listener the listener to remove.
     */
    void removeVisibleChangeListener(VisibleChangeListener listener);
}
