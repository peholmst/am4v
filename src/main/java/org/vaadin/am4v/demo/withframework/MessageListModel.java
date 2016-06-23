package org.vaadin.am4v.demo.withframework;

import org.vaadin.am4v.demo.domain.Message;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;

public class MessageListModel extends ApplicationModel {

    public final ApplicationProperty<Message> selected = new ApplicationProperty<Message>(null, Message.class);

    public MessageListModel(FolderTreeModel parent) {
        super(parent);
    }
}
