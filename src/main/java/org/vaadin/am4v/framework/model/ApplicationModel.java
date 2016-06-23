package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.Optional;

/**
 *
 */
public abstract class ApplicationModel implements Serializable {

    private ApplicationModel parent;
    private NavigatorStrategy navigatorStrategy;
    private PushStrategy pushStrategy;
    private NotificationStrategy notificationStrategy;

    /**
     * 
     * @param navigatorStrategy
     * @param pushStrategy
     * @param notificationStrategy
     */
    public ApplicationModel(NavigatorStrategy navigatorStrategy, PushStrategy pushStrategy,
        NotificationStrategy notificationStrategy) {
        setNavigatorStrategy(navigatorStrategy);
        setPushStrategy(pushStrategy);
        setNotificationStrategy(notificationStrategy);
        parent = null;
    }

    /**
     *
     */
    public ApplicationModel() {
        this(NavigatorStrategy.getDefault(), PushStrategy.getDefault(), NotificationStrategy.getDefault());
    }

    /**
     *
     * @param parent
     */
    public ApplicationModel(ApplicationModel parent) {
        this.parent = parent;
        if (parent == null) {
            setNavigatorStrategy(NavigatorStrategy.getDefault());
            setPushStrategy(PushStrategy.getDefault());
            setNotificationStrategy(NotificationStrategy.getDefault());
        }
    }

    /**
     *
     * @return
     */
    public final NavigatorStrategy getNavigatorStrategy() {
        if (navigatorStrategy == null && parent != null) {
            return parent.getNavigatorStrategy();
        }
        return navigatorStrategy;
    }

    /**
     *
     * @param navigatorStrategy
     */
    public final void setNavigatorStrategy(NavigatorStrategy navigatorStrategy) {
        if (parent != null) {
            this.navigatorStrategy = navigatorStrategy;
        } else {
            this.navigatorStrategy = navigatorStrategy == null ? NavigatorStrategy.getDefault() : navigatorStrategy;
        }
    }

    /**
     *
     * @return
     */
    public final PushStrategy getPushStrategy() {
        if (pushStrategy == null && parent != null) {
            return parent.getPushStrategy();
        }
        return pushStrategy;
    }

    /**
     *
     * @param pushStrategy
     */
    public final void setPushStrategy(PushStrategy pushStrategy) {
        if (parent != null) {
            this.pushStrategy = pushStrategy;
        } else {
            this.pushStrategy = pushStrategy == null ? PushStrategy.getDefault() : pushStrategy;
        }
    }

    /**
     *
     * @return
     */
    public final NotificationStrategy getNotificationStrategy() {
        if (notificationStrategy == null && parent != null) {
            return parent.getNotificationStrategy();
        }
        return notificationStrategy;
    }

    /**
     *
     * @param notificationStrategy
     */
    public final void setNotificationStrategy(NotificationStrategy notificationStrategy) {
        if (parent != null) {
            this.notificationStrategy = notificationStrategy;
        } else {
            this.notificationStrategy = notificationStrategy == null ? NotificationStrategy.getDefault()
                : notificationStrategy;
        }
    }

    /**
     * 
     * @return
     */
    protected Optional<ApplicationModel> getParent() {
        return Optional.ofNullable(parent);
    }

    @FunctionalInterface
    public interface Observer<M extends ApplicationModel> extends Serializable {

        void setApplicationModel(M applicationModel);
    }
}
