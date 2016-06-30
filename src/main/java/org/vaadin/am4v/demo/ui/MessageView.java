package org.vaadin.am4v.demo.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

public class MessageView extends VerticalLayout implements MessageModel.Observer<MessageModel> {

    private TextField from;
    private TextField to;
    private TextField cc;
    private TextField subject;
    private TextArea body;
    private FormLayout headers;
    private HorizontalLayout buttons;
    private Button reply;
    private Button forward;
    private Button delete;
    private Button move;

    private MessageModel applicationModel;

    public MessageView() {
        setMargin(true);
        setSpacing(true);

        headers = new FormLayout();
        headers.setWidth("100%");
        headers.setMargin(false);
        addComponent(headers);

        from = new TextField("From");
        from.setWidth("100%");
        headers.addComponent(from);

        to = new TextField("To");
        to.setWidth("100%");
        headers.addComponent(to);

        cc = new TextField("CC");
        cc.setWidth("100%");
        headers.addComponent(cc);

        subject = new TextField("Subject");
        subject.setWidth("100%");
        headers.addComponent(subject);

        buttons = new HorizontalLayout();
        reply = new Button(FontAwesome.MAIL_REPLY);
        buttons.addComponent(reply);
        forward = new Button(FontAwesome.MAIL_FORWARD);
        buttons.addComponent(forward);
        delete = new Button(FontAwesome.REMOVE);
        buttons.addComponent(delete);
        move = new Button(FontAwesome.FOLDER);
        buttons.addComponent(move);
        addComponent(buttons);
        setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        body = new TextArea();
        body.setSizeFull();
        addComponent(body);
        setExpandRatio(body, 1.0f);
    }

    @Override
    public void setApplicationModel(MessageModel applicationModel) {
        if (applicationModel != null) {
            applicationModel.sender.bind(from);
            applicationModel.recipient.bind(to);
            applicationModel.cc.bind(cc);
            applicationModel.subject.bind(subject);
            applicationModel.body.bind(body);
            applicationModel.reply.bind(reply);
            applicationModel.forward.bind(forward);
            applicationModel.delete.bind(delete);
            applicationModel.move.bind(move);
        } else if (this.applicationModel != null) {
            this.applicationModel.sender.unbind(from);
            this.applicationModel.recipient.unbind(to);
            this.applicationModel.cc.unbind(cc);
            this.applicationModel.subject.unbind(subject);
            this.applicationModel.body.unbind(body);
            this.applicationModel.reply.unbind(reply);
            this.applicationModel.forward.unbind(forward);
            this.applicationModel.delete.unbind(delete);
            this.applicationModel.move.unbind(move);
        }
        this.applicationModel = applicationModel;
    }
}
