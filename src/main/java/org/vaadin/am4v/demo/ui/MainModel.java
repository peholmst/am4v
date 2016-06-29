package org.vaadin.am4v.demo.ui;

import org.vaadin.am4v.framework.model.ApplicationModel;

/**
 * This is the root model in the model hierarchy. It does not contain any state, but is used so that the other
 * models can send messages to each other.
 *
 * @see FolderTreeModel
 * @see AddFolderModel
 * @see MessageListModel
 * @see MessageModel
 */
public class MainModel extends ApplicationModel {
}
