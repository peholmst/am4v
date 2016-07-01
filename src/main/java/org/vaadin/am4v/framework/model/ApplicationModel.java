package org.vaadin.am4v.framework.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Base class for application models. An application model is an abstraction of a part of the UI. The model exposes
 * {@link ApplicationAction actions} and {@link ApplicationProperty properties} that can be bound to UI elements. The
 * model can also expose other methods that the UI can invoke.
 * <p>
 * Application models can be grouped into hierarchies where the children inherit certain properties from their parent.
 * There is also a simple messaging mechanism where any model in the hierarchy can {@link #broadcastMessage(Object)}
 * broadcast messages} to all the other models. If a model is interested in a particular message, it can
 * {@link #registerMessageHandler(Class, MessageHandler)} register a message handler for it. This makes it possible for
 * models to communicate with each other in a decoupled way. It is also valid for application models to invoke each
 * other directly when/if that feels simpler than using message passing.
 * <p>
 * The model contains strategies for invoking different Vaadin services such as {@link PushStrategy server push}. By
 * using the strategy pattern, alternative implementations can be plugged in during testing. If a model hierarchy is
 * used, the strategies need only be added to the top-most model, from which the other models will inherit them.
 */
public abstract class ApplicationModel implements Serializable {

    private ApplicationModel parent;
    private NavigatorStrategy navigatorStrategy;
    private PushStrategy pushStrategy;
    private NotificationStrategy notificationStrategy;
    private WindowStrategy windowStrategy;
    private final Set<MessageHandlerRegistration> messageHandlers = new HashSet<>();

    /**
     * Creates a new root model (no parent) with the specified strategies.
     * 
     * @param navigatorStrategy the navigator strategy to use, or {@code null} to use the
     *        {@link NavigatorStrategy#getDefault() default}.
     * @param pushStrategy the push strategy to use, or {@code null} to use the {@link PushStrategy#getDefault()
     *        default}.
     * @param notificationStrategy the notification strategy to use, or {@code null} to use the
     *        {@link NotificationStrategy#getDefault() default}.
     * @param windowStrategy the window strategy to use, or {@code null} to use the {@link WindowStrategy#getDefault()
     *        default}.
     */
    public ApplicationModel(NavigatorStrategy navigatorStrategy, PushStrategy pushStrategy,
        NotificationStrategy notificationStrategy, WindowStrategy windowStrategy) {
        setNavigatorStrategy(navigatorStrategy);
        setPushStrategy(pushStrategy);
        setNotificationStrategy(notificationStrategy);
        setWindowStrategy(windowStrategy);
        parent = null;
    }

    /**
     * Creates a new root model (no parent) with the default strategies.
     *
     * @see NavigatorStrategy#getDefault()
     * @see PushStrategy#getDefault()
     * @see NotificationStrategy#getDefault()
     * @see WindowStrategy#getDefault()
     */
    public ApplicationModel() {
        this(NavigatorStrategy.getDefault(), PushStrategy.getDefault(), NotificationStrategy.getDefault(),
            WindowStrategy.getDefault());
    }

    /**
     * Creates a new child model with the specified parent. The strategies are inherited from the parent.
     *
     * @see #detachFromParent()
     * @param parent the parent model.
     */
    public ApplicationModel(ApplicationModel parent) {
        this.parent = Objects.requireNonNull(parent);
        parent.registerMessageHandler(Object.class, this::onParentMessage);
    }

    /**
     * Registers a new message handler. The handler will receive all messages of the specified message class, regardless
     * of where in the model hierarchy they have been broadcast.
     *
     * @see #broadcastMessage(Object)
     * @param messageClass the class of the messages to receive.
     * @param messageHandler the message handler.
     */
    protected final <M> void registerMessageHandler(Class<? super M> messageClass, MessageHandler<M> messageHandler) {
        Objects.requireNonNull(messageClass);
        Objects.requireNonNull(messageHandler);
        messageHandlers.add(new MessageHandlerRegistration(messageClass, messageHandler));
    }

    /**
     * Unregisters a message handler previously registered using {@link #registerMessageHandler(Class, MessageHandler)}.
     * 
     * @param messageClass the class of the messages to receive.
     * @param messageHandler the message handler.
     */
    protected final <M> void unregisterMessageHandler(Class<? super M> messageClass, MessageHandler<M> messageHandler) {
        Objects.requireNonNull(messageClass);
        Objects.requireNonNull(messageHandler);
        messageHandlers.remove(new MessageHandlerRegistration(messageClass, messageHandler));
    }

    /**
     * Broadcasts the given message to all models in the model hierarchy, including this model.
     *
     * @see #registerMessageHandler(Class, MessageHandler)
     * @param message the message to broadcast.
     */
    protected final void broadcastMessage(Object message) {
        Objects.requireNonNull(message);
        forwardMessage(ApplicationModel.this, message);
    }

    private void forwardMessage(ApplicationModel source, Object message) {
        // Store the parent in case any of the message handlers detaches the model from its parent.
        // The message should still reach all models in the hierarchy.
        ApplicationModel p = parent;
        notifyMessageHandlers(source, message);
        if (p != null) {
            p.forwardMessage(source, message);
        }
    }

    private void onParentMessage(ApplicationModel source, Object message) {
        if (source != this) {
            notifyMessageHandlers(source, message);
        }
    }

    private void notifyMessageHandlers(ApplicationModel source, Object message) {
        new HashSet<>(messageHandlers).stream().filter(h -> h.supports(message))
            .forEach(h -> h.handleMessage(source, message));
    }

    /**
     * Returns the {@link NavigatorStrategy} to use. If this model has a parent and no strategy has been explicitly
     * set, the strategy of the parent is returned.
     * 
     * @return the navigator strategy (never {@code null}).
     */
    public final NavigatorStrategy getNavigatorStrategy() {
        if (navigatorStrategy == null && parent != null) {
            return parent.getNavigatorStrategy();
        }
        return navigatorStrategy;
    }

    /**
     * Sets the navigator strategy to use.
     *
     * @see #getNavigatorStrategy()
     * @param navigatorStrategy the navigator strategy or {@code null} to use the {@link NavigatorStrategy#getDefault()
     *        default} or inherit from the parent model.
     */
    public final void setNavigatorStrategy(NavigatorStrategy navigatorStrategy) {
        if (parent != null) {
            this.navigatorStrategy = navigatorStrategy;
        } else {
            this.navigatorStrategy = navigatorStrategy == null ? NavigatorStrategy.getDefault() : navigatorStrategy;
        }
    }

    /**
     * Returns the {@link WindowStrategy} to use. If this model has a parent and no strategy has been explicitly set,
     * the strategy of the parent is returned.
     * 
     * @return the window strategy (never {@code null}).
     */
    public final WindowStrategy getWindowStrategy() {
        if (windowStrategy == null && parent != null) {
            return parent.getWindowStrategy();
        }
        return windowStrategy;
    }

    /**
     * Sets the window strategy to use.
     *
     * @see #getWindowStrategy()
     * @param windowStrategy the window strategy or {@code null} to use the {@link WindowStrategy#getDefault()
     *        default} or inherit from the parent model.
     */
    public final void setWindowStrategy(WindowStrategy windowStrategy) {
        if (parent != null) {
            this.windowStrategy = windowStrategy;
        } else {
            this.windowStrategy = windowStrategy == null ? WindowStrategy.getDefault() : windowStrategy;
        }
    }

    /**
     * Returns the {@link PushStrategy} to use. If this model has a parent and no strategy has been explicitly set,
     * the strategy of the parent is returned.
     * 
     * @return the push strategy (never {@code null}).
     */
    public final PushStrategy getPushStrategy() {
        if (pushStrategy == null && parent != null) {
            return parent.getPushStrategy();
        }
        return pushStrategy;
    }

    /**
     * Sets the push strategy to use.
     * 
     * @see #getPushStrategy()
     * @param pushStrategy the push strategy or {@code null} to use the {@link PushStrategy#getDefault() default} or
     *        inherit from the parent model.
     */
    public final void setPushStrategy(PushStrategy pushStrategy) {
        if (parent != null) {
            this.pushStrategy = pushStrategy;
        } else {
            this.pushStrategy = pushStrategy == null ? PushStrategy.getDefault() : pushStrategy;
        }
    }

    /**
     * Returns the {@link NotificationStrategy} to use. If this model has a parent and no strategy has been explicitly
     * set,
     * the strategy of the parent is returned.
     *
     * @return the notification strategy (never {@code null}).
     */
    public final NotificationStrategy getNotificationStrategy() {
        if (notificationStrategy == null && parent != null) {
            return parent.getNotificationStrategy();
        }
        return notificationStrategy;
    }

    /**
     * Sets the notification strategy to use.
     * 
     * @see #getNotificationStrategy()
     * @param notificationStrategy the notification strategy or {@code null} to use the
     *        {@link NotificationStrategy#getDefault() default} or inherit from the parent model.
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
     * Returns the parent of this model if it has one.
     * 
     * @see #detachFromParent()
     * @see #ApplicationModel(ApplicationModel)
     * @see #adapt(Class)
     * @return the parent model.
     */
    protected final Optional<ApplicationModel> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Attempts to adapt this model or any of its parent models to the specified class. This implementation will
     * check if this model is an instance of the specified class. If it is, this model is returned. Otherwise, it will
     * move up one step in the hierarchy and try again until a match is found or the root is reached. The purpose
     * of this method is to make it easy to access shared state from models higher up in the hierarchy.
     * <p>
     * Subclasses may override if they wish to support other means of adapting to a class than implementing an
     * interface or extending a base class.
     * 
     * @param clazz the class to adapt to.
     * @return the closest model in the hierarchy that supported the adapter.
     */
    protected <A> Optional<A> adapt(Class<A> clazz) {
        Objects.requireNonNull(clazz);
        if (clazz.isInstance(this)) {
            return Optional.of(clazz.cast(this));
        } else if (parent != null) {
            return parent.adapt(clazz);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Detaches this model from its parent, turning it into a root model. Any services inherited from the parent
     * will be replaced with their defaults. Once a model has been detached, it cannot be re-attached to a parent. This
     * is mainly useful for models that do not share the same scope as the parent model (i.e. they can be garbage
     * collected at different times).
     * <p>
     * Subclasses that wish to perform additional clean up when the model is detached should override {@link #detach()}.
     *
     * @see #ApplicationModel(ApplicationModel)
     * @see #getParent()
     */
    public final void detachFromParent() {
        if (parent != null) {
            parent.unregisterMessageHandler(Object.class, this::onParentMessage);
            if (notificationStrategy == null) {
                notificationStrategy = NotificationStrategy.getDefault();
            }
            if (pushStrategy == null) {
                pushStrategy = PushStrategy.getDefault();
            }
            if (navigatorStrategy == null) {
                navigatorStrategy = NavigatorStrategy.getDefault();
            }
            if (windowStrategy == null) {
                windowStrategy = WindowStrategy.getDefault();
            }
            detach();
            parent = null;
        }
    }

    /**
     * Called when the model is being detached from the parent model. At this point, the parent model is still
     * accessible by {@link #getParent()}. The default implementation does nothing, subclasses may override.
     */
    protected void detach() {
    }

    /**
     * Interface that can (optionally) be implemented by classes that observe a single application model.
     */
    @FunctionalInterface
    public interface Observer<M extends ApplicationModel> extends Serializable {

        /**
         * Sets the application model to observe.
         * 
         * @param applicationModel the application model, may be {@code null}.
         */
        void setApplicationModel(M applicationModel);
    }

    /**
     * Interface for message handlers.
     * 
     * @see #registerMessageHandler(Class, MessageHandler)
     * @see #broadcastMessage(Object)
     */
    @FunctionalInterface
    public interface MessageHandler<M> extends Serializable {

        /**
         * Called when a message has been received.
         * 
         * @param source the model that originally broadcast the message.
         * @param message the message.
         */
        void onMessage(ApplicationModel source, M message);
    }

    @SuppressWarnings("unchecked")
    private static class MessageHandlerRegistration implements Serializable {
        private final Class messageClass;
        private final MessageHandler messageHandler;

        MessageHandlerRegistration(Class<?> messageClass, MessageHandler<?> messageHandler) {
            this.messageClass = messageClass;
            this.messageHandler = messageHandler;
        }

        boolean supports(Object message) {
            return this.messageClass.isInstance(message);
        }

        void handleMessage(ApplicationModel source, Object message) {
            this.messageHandler.onMessage(source, message);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            MessageHandlerRegistration that = (MessageHandlerRegistration) o;

            if (!messageClass.equals(that.messageClass))
                return false;
            return messageHandler.equals(that.messageHandler);

        }

        @Override
        public int hashCode() {
            int result = messageClass.hashCode();
            result = 31 * result + messageHandler.hashCode();
            return result;
        }
    }
}
