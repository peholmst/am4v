package org.vaadin.am4v.demo.withframework;

import com.vaadin.ui.Tree;

public class FolderTreeView extends Tree implements FolderTreeModel.Observer<FolderTreeModel> {

    @Override
    public void setApplicationModel(FolderTreeModel applicationModel) {

    }
}
