package org.vaadin.am4v.demo.withframework;

import org.vaadin.am4v.demo.domain.Folder;
import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ApplicationModel;
import org.vaadin.am4v.framework.model.ApplicationProperty;

public class FolderTreeModel extends ApplicationModel {

    public final ApplicationAction refresh = new ApplicationAction(action -> {
    });

    public final ApplicationAction removeSelected = new ApplicationAction(action -> {

    });

    public final ApplicationAction addFolderToSelected = new ApplicationAction(action -> {

    });

    public final ApplicationProperty<Folder> selected = new ApplicationProperty<Folder>(null, Folder.class);

    public FolderTreeModel(MainModel parent) {
        super(parent);
    }
}
