package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.Parameters;
import org.vaadin.am4v.framework.ui.ParameterizedWindow;
import org.vaadin.am4v.framework.ui.WindowName;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

@WindowName(AddFolderWindow.WINDOW_NAME)
public class AddFolderWindow extends Window implements ParameterizedWindow {

    public static final String WINDOW_NAME = "addFolderWindow";

    private TextField name;
    private Button create;

    private AddFolderModel applicationModel;

    public AddFolderWindow() {
        super("Add Folder");
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

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        layout.addComponent(buttons);

        create = new Button("Create");
        create.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttons.addComponent(create);

        Button cancel = new Button("Cancel", evt -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        buttons.addComponent(cancel);
        name.focus();
    }

    @Override
    public void detach() {
        if (applicationModel != null) {
            applicationModel.detachFromParent();
        }
        super.detach();
    }

    @Override
    public void setParameters(Parameters parameters) {
        ApplicationModel parentModel = parameters.getParameter(ApplicationModel.class);
        Folder parentFolder = parameters.getParameter(Folder.class);
        // Not sure there is even a need for a separate model here...
        applicationModel = new AddFolderModel(parentModel, parentFolder);
        applicationModel.getName().bind(name);
        applicationModel.getCreate().bind(create);
        applicationModel.getWindowClosed().addValueChangeListener(evt -> close());
    }
}
