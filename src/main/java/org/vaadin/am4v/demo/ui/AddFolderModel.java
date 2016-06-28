package org.vaadin.am4v.demo.ui;

import com.vaadin.data.Validator;
import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.demo.domain.FolderService;
import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;
import org.vaadin.am4v.framework.model.WindowApplicationModel;

import com.vaadin.data.validator.StringLengthValidator;

public class AddFolderModel extends WindowApplicationModel {

    private final ApplicationProperty<Folder> parent = new ApplicationProperty<>(null, Folder.class);

    public final ApplicationProperty<String> name = new ApplicationProperty<>(null, String.class);

    public final ApplicationAction create = new ApplicationAction(action -> {
        try {
            name.validate();
            Folder folder = FolderService.getInstance().addFolder(parent.getValue(), name.getValue(), true);
            broadcastMessage(new FolderAdded(folder));
            hide();
        } catch (Validator.InvalidValueException ex) {
            // NOP, the UI is already showing the error
        }
    });

    public AddFolderModel(ApplicationModel parent) {
        super(parent);
        init();
    }

    public void show(Folder parent) {
        this.parent.setValue(parent);
        this.name.setValue(null);
        show();
    }

    private void init() {
        name.addValidator(
            new StringLengthValidator("Please provide a name for the folder", 1, Integer.MAX_VALUE, false));
    }
}
