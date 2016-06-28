package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
public abstract class ApplicationModel implements Serializable {

    private ApplicationModel parent;
    private NavigatorStrategy navigatorStrategy;
    private PushStrategy pushStrategy;
    private NotificationStrategy notificationStrategy;
    private final Set<MessageHandlerRegistration> messageHandlers = new HashSet<>();

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
        } else {
            parent.registerMessageHandler(Object.class, this::onParentMessage);
        }
    }

    /**
     *
     * @param messageClass
     * @param messageHandler
     * @param <M>
     */
    protected final <M> void registerMessageHandler(Class<? super M> messageClass, MessageHandler<M> messageHandler) {
        messageHandlers.add(new MessageHandlerRegistration(messageClass, messageHandler));
    }

    /**
     *
     * @param message
     */
    protected final void broadcastMessage(Object message) {
        forwardMessage(ApplicationModel.this, message);
    }

    private void forwardMessage(ApplicationModel source, Object message) {
        messageHandlers.stream().filter(h -> h.supports(message)).forEach(h -> h.handleMessage(source, message));
        if (parent != null) {
            parent.forwardMessage(source, message);
        }
    }

    private void onParentMessage(ApplicationModel source, Object message) {
        if (source != this) {
            messageHandlers.stream().filter(h -> h.supports(message)).forEach(h -> h.handleMessage(source, message));
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
    protected final Optional<ApplicationModel> getParent() {
        return Optional.ofNullable(parent);
    }

    @FunctionalInterface
    public interface Observer<M extends ApplicationModel> extends Serializable {

        void setApplicationModel(M applicationModel);
    }

    @FunctionalInterface
    public interface MessageHandler<M> extends Serializable {

        void onMessage(ApplicationModel source, M message);
    }

    @SuppressWarnings("unchecked")
    private static class MessageHandlerRegistration implements Serializable {
        private final Class messageClass;
        private final MessageHandler messageHandler;

        public MessageHandlerRegistration(Class<?> messageClass, MessageHandler<?> messageHandler) {
            this.messageClass = messageClass;
            this.messageHandler = messageHandler;
        }

        public boolean supports(Object message) {
            return this.messageClass.isInstance(message);
        }

        public void handleMessage(ApplicationModel source, Object message) {
            this.messageHandler.onMessage(source, message);
        }
    }
}
