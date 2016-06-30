package org.vaadin.am4v.demo.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.am4v.framework.binding.ActionHandlerBinding;

import com.vaadin.event.Action;
import com.vaadin.ui.Tree;

public class FolderTreeView extends VerticalLayout implements FolderTreeModel.Observer<FolderTreeModel> {

    private Tree tree;
    private ActionHandlerBinding contextMenu;
    private Action refresh;
    private Action remove;
    private Action add;
    private Button refreshBtn;
    private Button removeBtn;
    private Button addBtn;

    private FolderTreeModel applicationModel;

    public FolderTreeView() {
        addBtn = new Button(FontAwesome.PLUS);
        removeBtn = new Button(FontAwesome.MINUS);
        refreshBtn = new Button(FontAwesome.REFRESH);
        HorizontalLayout buttons = new HorizontalLayout(addBtn, removeBtn, refreshBtn);
        addComponent(buttons);

        tree = new Tree();
        tree.setSizeFull();
        tree.setImmediate(true);
        addComponent(tree);
        setExpandRatio(tree, 1.0f);

        contextMenu = new ActionHandlerBinding();
        add = contextMenu.add("Add Folder...");
        remove = contextMenu.add("Remove");
        refresh = contextMenu.add("Refresh");
        tree.addActionHandler(contextMenu);
        tree.setItemCaptionPropertyId("name");
    }

    @Override
    public void setApplicationModel(FolderTreeModel applicationModel) {
        if (applicationModel != null) {
            tree.setPropertyDataSource(applicationModel.selected);
            tree.setContainerDataSource(applicationModel.tree);

            contextMenu.bind(refresh, applicationModel.refresh);
            contextMenu.bind(add, applicationModel.addFolder);
            contextMenu.bind(remove, applicationModel.removeFolder);

            applicationModel.refresh.bind(refreshBtn);
            applicationModel.addFolder.bind(addBtn);
            applicationModel.removeFolder.bind(removeBtn);
        } else if (this.applicationModel != null) {
            tree.setPropertyDataSource(null);
            tree.setContainerDataSource(null);

            contextMenu.unbind(refresh);
            contextMenu.unbind(add);
            contextMenu.unbind(remove);

            this.applicationModel.refresh.unbind(refreshBtn);
            this.applicationModel.addFolder.unbind(addBtn);
            this.applicationModel.removeFolder.unbind(removeBtn);
        }
        this.applicationModel = applicationModel;
    }
}
