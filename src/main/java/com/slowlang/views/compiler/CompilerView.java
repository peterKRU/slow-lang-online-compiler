package com.slowlang.views.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;

import com.slowlang.service.CompilerService;
import com.slowlang.service.ExecutionEngineService;
import com.slowlang.views.MainLayout;
import com.vaadin.collaborationengine.AbstractCollaborationManager.ActivationHandler;
import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.MessageHandler;
import com.vaadin.collaborationengine.MessageManager;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasText.WhiteSpace;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;


@PageTitle("Compiler")
@Route(value = "compiler", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class CompilerView extends HorizontalLayout {
	
	private CompilerService compilerService;
	private ExecutionEngineService executionEngineService;
	private ProgressBar editorProgressBar = new ProgressBar();
	private ProgressBar chatProgressBar = new ProgressBar();
	
    public static class ChatTab extends Tab {
        
    	private final ChatInfo chatInfo;

        public ChatTab(ChatInfo chatInfo) {
            this.chatInfo = chatInfo;
        }

        public ChatInfo getChatInfo() {
            return chatInfo;
        }
    }

    public static class ChatInfo {
        private String name;
        private int unread;
        private Span unreadBadge;

        private ChatInfo(String name, int unread) {
            this.name = name;
            this.unread = unread;
        }

        public void resetUnread() {
            unread = 0;
            updateBadge();
        }

        public void incrementUnread() {
            unread++;
            updateBadge();
        }

        private void updateBadge() {
            unreadBadge.setText(unread + "");
            unreadBadge.setVisible(unread != 0);
        }

        public void setUnreadBadge(Span unreadBadge) {
            this.unreadBadge = unreadBadge;
            updateBadge();
        }

        public String getCollaborationTopic() {
            return "chat/" + name;
        }
    }

    private ChatInfo[] chats = new ChatInfo[]{new ChatInfo("general", 0), new ChatInfo("support", 0),
            new ChatInfo("casual", 0)};
    private ChatInfo currentChat = chats[0];
    private Tabs tabs;

    public CompilerView(CompilerService compilerService, ExecutionEngineService executionEngineService) {
        
    	this.compilerService = compilerService;
    	this.executionEngineService = executionEngineService;
    	
    	addClassNames("compiler-view", Width.FULL, Display.FLEX, Flex.AUTO);
        setSpacing(false);
        
        H3 codeEditorHeader = new H3("Code Editor");
        
    	editorProgressBar.setIndeterminate(true);
    	editorProgressBar.setVisible(false);
    	editorProgressBar.setWidth("650px");
    	
        HorizontalLayout editorHeaderLayout = new HorizontalLayout(codeEditorHeader, editorProgressBar);
        editorHeaderLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
    	
        MenuBar compilerCommandBar = new MenuBar();
        compilerCommandBar.addItem("Compile");
        compilerCommandBar.addItem("Test");
        compilerCommandBar.addItem("Mockup Button");
        compilerCommandBar.addItem("Mockup Button");
        compilerCommandBar.addItem("Mockup Button");
        compilerCommandBar.addItem("Mockup Button");
        
        AceEditor codeEditor = new AceEditor();
    	codeEditor.setTheme(AceTheme.terminal);
    	codeEditor.setMode(AceMode.sql);
    	codeEditor.setValue("Mockup code");
    	codeEditor.setSizeFull(); 
        
    	VerticalLayout codeEditorLayout = new VerticalLayout(editorHeaderLayout, compilerCommandBar, codeEditor);
    	add(codeEditorLayout);
    	
    	UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "User");
    	UserInfo botInfo = new UserInfo(UUID.randomUUID().toString(), "SlowLang Bot");
    	
    	tabs = new Tabs();
        for (ChatInfo chat : chats) {
        	
        	MessageManager userMessageManager = new MessageManager(this, userInfo, chat.getCollaborationTopic());
        	MessageManager botMessageManager = new MessageManager(this, botInfo, chat.getCollaborationTopic());
        	
        	userMessageManager.setMessageHandler(context -> {
                
            	CollaborationMessage message = context.getMessage();
            	UserInfo user = message.getUser();
            	String text = message.getText();
            	
            	if(user.getName().equals("User")) {
            		
            		botMessageManager.submit("Mockup server response");
            	}
            	
            	Notification.show(user.getName() + ": " + text);
            	
            	if (currentChat != chat) {
                    chat.incrementUnread();
                }
            });
        	
            tabs.add(createTab(chat));
        }
        
        tabs.setOrientation(Orientation.VERTICAL);
        tabs.addClassNames(Flex.GROW, Flex.SHRINK, Overflow.HIDDEN);
        
        CollaborationMessageList list = new CollaborationMessageList(userInfo, currentChat.getCollaborationTopic());
        list.setSizeFull();
        
        CollaborationMessageInput input = new CollaborationMessageInput(list);
        input.setWidthFull();
        
        H3 chatAreaHeader = new H3("Output Area");
        
    	editorProgressBar.setIndeterminate(true);
    	editorProgressBar.setVisible(false);
    	editorProgressBar.setWidth("650px");
        
    	HorizontalLayout chatHeaderLayout = new HorizontalLayout(chatAreaHeader, editorProgressBar); 
    	chatHeaderLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
    	
        MenuBar chatAreaCommandBar = new MenuBar();
        chatAreaCommandBar.addItem("Run");
        chatAreaCommandBar.addItem("Debug");
        chatAreaCommandBar.addItem("Mockup Button");
        chatAreaCommandBar.addItem("Mockup Button");
        chatAreaCommandBar.addItem("Mockup Button");
        chatAreaCommandBar.addItem("Mockup Button");
        
        chatAreaCommandBar.addThemeVariants(MenuBarVariant.MATERIAL_OUTLINED);
        
        VerticalLayout chatContainer = new VerticalLayout(chatHeaderLayout, chatAreaCommandBar);
        chatContainer.addClassNames(Flex.AUTO, Overflow.HIDDEN);
        
        Aside side = new Aside();
        side.addClassNames(Display.FLEX, FlexDirection.COLUMN, Flex.GROW_NONE, Flex.SHRINK_NONE, Background.CONTRAST_5);
        side.setWidth("18rem");
        Header header = new Header();
        header.addClassNames(Display.FLEX, FlexDirection.ROW, Width.FULL, AlignItems.CENTER, Padding.MEDIUM,
                BoxSizing.BORDER);
        H3 channels = new H3("Channels");
        channels.addClassNames(Flex.GROW, Margin.NONE);
        CollaborationAvatarGroup avatarGroup = new CollaborationAvatarGroup(userInfo, "chat");
        avatarGroup.setMaxItemsVisible(4);
        avatarGroup.addClassNames(Width.AUTO);

        header.add(channels, avatarGroup);

        side.add(header, tabs);
        
        chatContainer.add(list, input);

        
        chatContainer.addClassName("bg-contrast-5");
        add(chatContainer);
        setSizeFull();
        expand(list);
        
        tabs.addSelectedChangeListener(event -> {
            currentChat = ((ChatTab) event.getSelectedTab()).getChatInfo();
            currentChat.resetUnread();
            list.setTopic(currentChat.getCollaborationTopic());
        });
    }
    
    private void compile(String sourceCode) {
    	
    	compilerService.compile(sourceCode);
    	Notification.show("Compiling...");
    }
    
    private ChatTab createTab(ChatInfo chat) {
        ChatTab tab = new ChatTab(chat);
        tab.addClassNames(JustifyContent.BETWEEN);

        Span badge = new Span();
        chat.setUnreadBadge(badge);
        badge.getElement().getThemeList().add("badge small contrast");
        tab.add(new Span("#" + chat.name), badge);

        return tab;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        Page page = attachEvent.getUI().getPage();
        page.retrieveExtendedClientDetails(details -> {
            setMobile(details.getWindowInnerWidth() < 740);
        });
        page.addBrowserWindowResizeListener(e -> {
            setMobile(e.getWidth() < 740);
        });
    }

    private void setMobile(boolean mobile) {
        tabs.setOrientation(mobile ? Orientation.HORIZONTAL : Orientation.VERTICAL);
    }

}
