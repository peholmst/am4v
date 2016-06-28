package org.vaadin.am4v.demo.withframework;

import java.time.ZonedDateTime;

import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.demo.domain.Message;
import org.vaadin.am4v.demo.domain.MessageService;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class MessageListModel extends ApplicationModel {

    public final ApplicationProperty<Message> selected = new ApplicationProperty<Message>(null, Message.class);

    public final Container messages = new IndexedContainer() {
        {
            addContainerProperty("Subject", String.class, "");
            addContainerProperty("From", String.class, "");
            addContainerProperty("Date", ZonedDateTime.class, null);
        }
    };

    public MessageListModel(FolderTreeModel parent) {
        super(parent);
        // This model has the same scope as the parent model so no need to remove the listener afterwards
        parent.selected.addValueChangeListener(evt -> folderSelected((Folder) evt.getProperty().getValue()));
    }

    private void folderSelected(Folder folder) {
        messages.removeAllItems();
        if (folder != null) {
            MessageService.getInstance().getMessagesInFolder(folder).forEach(this::addMessageToContainer);
        }
    }

    private void addMessageToContainer(Message message) {
        Item item = messages.addItem(message);
        item.getItemProperty("Subject").setValue(message.getSubject());
        item.getItemProperty("From").setValue(message.getFrom());
        item.getItemProperty("Date").setValue(message.getDate());
    }
}
