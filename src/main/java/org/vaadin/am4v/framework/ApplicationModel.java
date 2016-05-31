package org.vaadin.am4v.framework;

import java.io.Serializable;

public abstract class ApplicationModel implements Serializable {

    private NavigatorStrategy navigatorStrategy;
    private PushStrategy pushStrategy;
    private NotificationStrategy notificationStrategy;

    public ApplicationModel(NavigatorStrategy navigatorStrategy, PushStrategy pushStrategy,
        NotificationStrategy notificationStrategy) {
        setNavigatorStrategy(navigatorStrategy);
        setPushStrategy(pushStrategy);
        setNotificationStrategy(notificationStrategy);
    }

    public ApplicationModel() {
        this(NavigatorStrategy.getDefault(), PushStrategy.getDefault(), NotificationStrategy.getDefault());
    }

    public final NavigatorStrategy getNavigatorStrategy() {
        return navigatorStrategy;
    }

    public final void setNavigatorStrategy(NavigatorStrategy navigatorStrategy) {
        this.navigatorStrategy = navigatorStrategy == null ? NavigatorStrategy.getDefault() : navigatorStrategy;
    }

    public final PushStrategy getPushStrategy() {
        return pushStrategy;
    }

    public final void setPushStrategy(PushStrategy pushStrategy) {
        this.pushStrategy = pushStrategy == null ? PushStrategy.getDefault() : pushStrategy;
    }

    public final NotificationStrategy getNotificationStrategy() {
        return notificationStrategy;
    }

    public final void setNotificationStrategy(NotificationStrategy notificationStrategy) {
        this.notificationStrategy = notificationStrategy == null ? NotificationStrategy.getDefault()
            : notificationStrategy;
    }
}
