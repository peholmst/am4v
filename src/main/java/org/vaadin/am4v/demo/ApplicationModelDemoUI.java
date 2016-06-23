package org.vaadin.am4v.demo;

import java.io.Serializable;

import org.vaadin.am4v.framework.model.ApplicationProperty;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ApplicationModelDemoUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);
        setContent(layout);

        TextField myFirstProperty = new TextField("My first property");
        layout.addComponent(myFirstProperty);
        TextField mySecondProperty = new TextField("My second property");
        layout.addComponent(mySecondProperty);
        Button swap = new Button("Swap enablement");
        layout.addComponent(swap);

        // Connect the model
        MyApplicationModel model = new MyApplicationModel();
        model.getMyFirstProperty().bind(myFirstProperty);
        model.getMySecondProperty().bind(mySecondProperty);
        swap.addClickListener(evt -> model.swapEnablementState());
    }

    public static class MyApplicationModel implements Serializable {
        private final ApplicationProperty<String> myFirstProperty = new ApplicationProperty<>("", String.class, false,
            true);
        private final ApplicationProperty<String> mySecondProperty = new ApplicationProperty<>("", String.class, false,
            false);

        public ApplicationProperty<String> getMyFirstProperty() {
            return myFirstProperty;
        }

        public ApplicationProperty<String> getMySecondProperty() {
            return mySecondProperty;
        }

        public void swapEnablementState() {
            myFirstProperty.setEnabled(mySecondProperty.isEnabled());
            mySecondProperty.setEnabled(!myFirstProperty.isEnabled());
        }
    }
    /*
     * @VaadinServletConfiguration(ui = ApplicationModelDemoUI.class, productionMode = false)
     * 
     * @WebServlet(urlPatterns = "/*")
     * public static class Servlet extends VaadinServlet {
     * 
     * }
     */
}
