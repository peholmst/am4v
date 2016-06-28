package org.vaadin.am4v.demo.ui;

import com.vaadin.ui.Table;

public class MessageListView extends Table implements MessageListModel.Observer<MessageListModel> {

    public MessageListView() {
        setSelectable(true);
        setMultiSelect(false);
        setImmediate(true);
    }

    @Override
    public void setApplicationModel(MessageListModel applicationModel) {
        if (applicationModel != null) {
            setPropertyDataSource(applicationModel.selected);
            setContainerDataSource(applicationModel.messages);
        } else {
            setPropertyDataSource(null);
            setContainerDataSource(null);
        }
    }
}
