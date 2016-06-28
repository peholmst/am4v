package org.vaadin.am4v.framework.model;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Strategy for displaying notifications to the user from within application models. By abstracting this away, different
 * strategies can be plugged in for e.g. testing.
 */
public interface NotificationStrategy {

    /**
     * Shows a notification with the given caption, description and type.
     * 
     * @see Notification#show(String, String, Notification.Type)
     * @param caption the caption of the message.
     * @param description the description of the message.
     * @param type the type of the message.
     */
    void showNotification(String caption, String description, Notification.Type type);

    /**
     * Shows a notification with the given caption and type.
     * 
     * @param caption the caption of the message.
     * @param type the description of the message.
     */
    default void showNotification(String caption, Notification.Type type) {
        showNotification(caption, null, type);
    }

    /**
     * Shows a humanized message with the given caption.
     * 
     * @param caption the caption of the message.
     */
    default void showNotification(String caption) {
        showNotification(caption, Notification.Type.HUMANIZED_MESSAGE);
    }

    /**
     * Returns the default notification strategy, which will invoke
     * {@link Notification#show(String, String, Notification.Type)}.
     * 
     * @return the default notification strategy.
     */
    static NotificationStrategy getDefault() {
        return (caption, message, type) -> {
            if (Page.getCurrent() != null) {
                Notification.show(caption, message, type);
            } else {
                throw new IllegalStateException("No Page bound to current thread");
            }
        };
    }
}
