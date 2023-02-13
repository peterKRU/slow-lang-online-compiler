package com.slowlang.views.about;

import com.slowlang.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends HorizontalLayout {

    public AboutView() {
    	
    	H1 workInProgress = new H1("WiP");
    	H1 workInProgress2 = new H1("WiP");
    	add(workInProgress);
    	add(workInProgress2);
    	
    	AceEditor codeEditor = new AceEditor();
    	codeEditor.setTheme(AceTheme.terminal);
    	codeEditor.setMode(AceMode.sql);
    	codeEditor.setValue("SELECT * FROM DB_01 WHERE ID = 123");
    	codeEditor.setSizeFull();  
    	
    	add(codeEditor);
    }

}
