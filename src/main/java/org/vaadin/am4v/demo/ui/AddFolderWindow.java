package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.framework.model.ApplicationModel;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

public class AddFolderWindow extends Window {

    private TextField name;
    private Button create;

    private AddFolderModel applicationModel;

    public AddFolderWindow(MainModel mainModel, Folder parentFolder) {
        super("Add Folder");
        // Not sure there is even a need for a separate model here...
        applicationModel = new AddFolderModel(mainModel, parentFolder);
        applicationModel.registerMessageHandler(FolderAdded.class, (source, folder) -> close());
        setModal(true);
        center();

        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.setSpacing(true);
        layout.setMargin(true);

        name = new TextField("Name");
        layout.addComponent(name);
        name.setImmediate(true);
        name.setNullRepresentation("");
        applicationModel.name.bind(name);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        layout.addComponent(buttons);

        create = new Button("Create");
        create.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttons.addComponent(create);
        applicationModel.create.bind(create);

        Button cancel = new Button("Cancel", evt -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        buttons.addComponent(cancel);
        name.focus();
    }

    @Override
    public void detach() {
        applicationModel.detachFromParent();
        super.detach();
    }

    private void onFolderAdded(ApplicationModel source, FolderAdded folderAdded) {

    }

}
