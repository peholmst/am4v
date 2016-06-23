package org.vaadin.am4v.demo.withframework;

import com.vaadin.ui.VerticalLayout;

public class MessageView extends VerticalLayout implements MessageModel.Observer<MessageModel> {

    @Override
    public void setApplicationModel(MessageModel applicationModel) {

    }
}
