package com.slowlang.views.about;

import com.slowlang.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends HorizontalLayout {

    public AboutView() {
    	
    	H1 workInProgress = new H1("WiP");
    	add(workInProgress);
    }

}
