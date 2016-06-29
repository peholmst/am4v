package org.vaadin.am4v.demo.ui;

import java.io.Serializable;

import org.vaadin.am4v.demo.domain.Folder;

/**
 * Message broadcast to models when a new folder is added to the folder tree.
 * 
 * @see org.vaadin.am4v.framework.model.ApplicationModel#broadcastMessage(Object)
 * @see AddFolderModel
 * @see FolderTreeModel
 */
public class FolderAdded implements Serializable {
    private final Folder folder;

    public FolderAdded(Folder folder) {
        this.folder = folder;
    }

    public Folder getFolder() {
        return folder;
    }
}
