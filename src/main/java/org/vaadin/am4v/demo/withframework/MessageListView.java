package org.vaadin.am4v.demo.withframework;

import com.vaadin.ui.Grid;

public class MessageListView extends Grid implements MessageListModel.Observer<MessageListModel> {

    @Override
    public void setApplicationModel(MessageListModel applicationModel) {

    }
}
