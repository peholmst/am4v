package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.framework.ui.WindowFactory;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

public class AddFolderView extends WindowFactory<AddFolderModel> {

    public AddFolderView(AddFolderModel windowApplicationModel) {
        super(windowApplicationModel);
    }

    @Override
    protected Window createWindow() {
        Window win = new Window("Add Folder");
        win.setModal(true);
        win.center();

        VerticalLayout layout = new VerticalLayout();
        win.setContent(layout);
        layout.setSpacing(true);
        layout.setMargin(true);

        TextField name = new TextField("Name");
        layout.addComponent(name);
        name.setImmediate(true);
        name.setNullRepresentation("");
        getApplicationModel().name.bind(name);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        layout.addComponent(buttons);

        Button create = new Button("Create");
        create.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttons.addComponent(create);
        getApplicationModel().create.bind(create);

        Button cancel = new Button("Cancel", evt -> win.close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        buttons.addComponent(cancel);

        win.addCloseListener(evt -> {
            getApplicationModel().name.unbind(name);
            getApplicationModel().create.unbind(create);
        });

        name.focus();

        return win;
    }
}
