package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.demo.domain.FolderService;
import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;
import org.vaadin.am4v.framework.model.WindowApplicationModel;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;

/**
 * This application model drives the window for creating new folders and adding them to the folder tree. Of all the
 * models in this example application, this is the one I have the most doubts about. It feels like there should be a
 * simpler way of popping up a window and committing/validating a form. Maybe
 * {@link com.vaadin.data.fieldgroup.FieldGroup}s should be included in the design in some way?
 */
public class AddFolderModel extends WindowApplicationModel {

    private final ApplicationProperty<Folder> parent = new ApplicationProperty<>(null, Folder.class);

    /**
     * Property containing the name of the new folder.
     */
    public final ApplicationProperty<String> name = new ApplicationProperty<>(null, String.class);

    /**
     * Action that will create the new folder and broadcast the {@link FolderAdded} message.
     */
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
        // I don't like the need to reset the model like this. Should come up with something better.
        this.parent.setValue(parent);
        this.name.setValue(null);
        show();
    }

    private void init() {
        name.addValidator(
            new StringLengthValidator("Please provide a name for the folder", 1, Integer.MAX_VALUE, false));
    }
}
