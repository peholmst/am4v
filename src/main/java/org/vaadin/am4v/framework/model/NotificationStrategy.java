package org.vaadin.am4v.framework.model;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public interface NotificationStrategy {

    void showNotification(String caption, String message, Notification.Type type);

    default void showNotification(String caption, Notification.Type type) {
        showNotification(caption, null, type);
    }

    default void showNotification(String caption) {
        showNotification(caption, Notification.Type.HUMANIZED_MESSAGE);
    }

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
