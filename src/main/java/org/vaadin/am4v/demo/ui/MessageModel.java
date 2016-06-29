package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.demo.domain.Message;
import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;

/**
 * This application model contains the currently selected message. It is a direct child of {@link MessageListModel} and
 * reacts whenever the currently selected message is changed.
 */
public class MessageModel extends ApplicationModel {

    /**
     * Property containing the subject of the current message.
     */
    public final ApplicationProperty<String> subject = new ApplicationProperty<>(null, String.class, true);
    /**
     * Property containing the sender of the current message.
     */
    public final ApplicationProperty<String> sender = new ApplicationProperty<>(null, String.class, true);
    /**
     * Property containing the recipient(s) of the current message.
     */
    public final ApplicationProperty<String> recipient = new ApplicationProperty<>(null, String.class, true);
    /**
     * Property containing the CC of the current message.
     */
    public final ApplicationProperty<String> cc = new ApplicationProperty<>(null, String.class, true);
    /**
     * Property containing the body of the current message.
     */
    public final ApplicationProperty<String> body = new ApplicationProperty<>(null, String.class, true);

    /**
     * Action for replying to the message.
     */
    public final ApplicationAction reply = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Reply invoked");
    });

    /**
     * Action for forwarding the message.
     */
    public final ApplicationAction forward = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Forward invoked");
    });

    /**
     * Action for deleting the message.
     */
    public final ApplicationAction delete = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Delete invoked");
    });

    /**
     * Action for moving the message to another folder.
     */
    public final ApplicationAction move = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Move invoked");
    });

    public MessageModel(MessageListModel parent) {
        super(parent);
        // This model has the same scope as the parent model so no need to remove the listener afterwards
        parent.selected.addValueChangeListener(evt -> messageSelected((Message) evt.getProperty().getValue()));
    }

    private void messageSelected(Message message) {
        if (message != null) {
            setValue(subject, message.getSubject());
            setValue(sender, message.getFrom());
            setValue(recipient, String.join(", ", message.getTo()));
            setValue(cc, String.join(", ", message.getCc()));
            cc.setVisible(message.getCc().size() > 0);
            setValue(body, message.getBody());
        } else {
            setValue(subject, null);
            setValue(sender, null);
            setValue(recipient, null);
            setValue(cc, null);
            setValue(body, null);
        }
    }

    private static <T> void setValue(ApplicationProperty<T> property, T value) {
        boolean wasReadOnly = property.isReadOnly();
        property.setReadOnly(false);
        try {
            property.setValue(value);
        } finally {
            property.setReadOnly(wasReadOnly);
        }
    }
}
