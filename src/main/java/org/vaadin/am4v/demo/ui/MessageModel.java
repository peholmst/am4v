package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.demo.domain.Message;
import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;

public class MessageModel extends ApplicationModel {

    public final ApplicationProperty<String> subject = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> sender = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> recipient = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> cc = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> body = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<Message> message = new ApplicationProperty<>(null, Message.class, true);

    public final ApplicationAction reply = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Reply invoked");
    });

    public final ApplicationAction forward = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Forward invoked");
    });

    public final ApplicationAction delete = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Delete invoked");
    });

    public final ApplicationAction move = new ApplicationAction(action -> {
        getNotificationStrategy().showNotification("Move invoked");
    });

    public MessageModel(MessageListModel parent) {
        super(parent);
        // This model has the same scope as the parent model so no need to remove the listener afterwards
        parent.selected.addValueChangeListener(evt -> messageSelected((Message) evt.getProperty().getValue()));
    }

    private void messageSelected(Message message) {
        setValue(this.message, message);
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
