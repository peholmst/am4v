package org.vaadin.am4v.demo.ui;

import java.io.Serializable;

import org.vaadin.am4v.demo.domain.Folder;

public class FolderAdded implements Serializable {
    private final Folder folder;

    public FolderAdded(Folder folder) {
        this.folder = folder;
    }

    public Folder getFolder() {
        return folder;
    }
}
