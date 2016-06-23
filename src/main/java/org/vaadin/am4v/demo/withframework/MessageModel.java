package org.vaadin.am4v.demo.withframework;

import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;

public class MessageModel extends ApplicationModel {

    public final ApplicationProperty<String> subject = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> sender = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> recipient = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> cc = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> bcc = new ApplicationProperty<>(null, String.class, true);
    public final ApplicationProperty<String> body = new ApplicationProperty<>(null, String.class, true);

    public final ApplicationAction reply = new ApplicationAction(action -> {

    });

    public final ApplicationAction forward = new ApplicationAction(action -> {

    });

    public final ApplicationAction delete = new ApplicationAction(action -> {

    });

    public final ApplicationAction move = new ApplicationAction(action -> {

    });

    public MessageModel(MessageListModel parent) {
        super(parent);
    }
}
