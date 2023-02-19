package com.slowlang.views.compiler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.slowlang.service.BotService;
import com.slowlang.service.CompilerService;
import com.slowlang.service.ExecutionEngineService;
import com.slowlang.service.Request;
import com.slowlang.service.Requests;
import com.slowlang.views.MainLayout;
import com.slowlang.views.UIConstants;
import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.MessageManager;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;


@SuppressWarnings("serial")
@PageTitle("Compiler")
@Route(value = "compiler", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class CompilerView extends HorizontalLayout {
	
	@SuppressWarnings("unused")
	private BotService botService;
	
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

    private ChatInfo[] chats = new ChatInfo[]{new ChatInfo("slow-lang-bot", 0)};
    private ChatInfo currentChat = chats[0];
    private Tabs tabs;

    public CompilerView(BotService botService) {
        
    	this.botService = botService;
    	
    	addClassNames("compiler-view", Width.FULL, Display.FLEX, Flex.AUTO);
        setSpacing(false);
        
        H3 codeEditorHeader = new H3("Code Editor");
        
        HorizontalLayout editorHeaderLayout = new HorizontalLayout(codeEditorHeader);
        editorHeaderLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        
        HorizontalLayout codeEditorButtonsLayout = new HorizontalLayout();
        
        Button compileAndRunButton = new Button("Compile&Run", new Icon(VaadinIcon.COMPILE));
        compileAndRunButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        
        Button generateParseTreeButton = new Button("Generate Parse Tree", new Icon(VaadinIcon.TREE_TABLE));
        generateParseTreeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        
        Button debugButton = new Button("Debug", new Icon(VaadinIcon.BUG));
        debugButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        
        Button testButton = new Button("Test", new Icon(VaadinIcon.COGS));
        testButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        
        Button explainSyntaxButton = new Button("Explain Syntax", new Icon(VaadinIcon.CLUSTER));
        explainSyntaxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        
        codeEditorButtonsLayout.add(compileAndRunButton);
        codeEditorButtonsLayout.add(generateParseTreeButton);
        codeEditorButtonsLayout.add(debugButton);
        codeEditorButtonsLayout.add(testButton);
        codeEditorButtonsLayout.add(explainSyntaxButton);
        
        AceEditor codeEditor = new AceEditor();
    	codeEditor.setTheme(AceTheme.terminal);
    	codeEditor.setMode(AceMode.java);
    	codeEditor.setValue(UIConstants.DEFAULT_CODE_INPUT);
    	codeEditor.setSizeFull(); 
        
    	VerticalLayout codeEditorLayout = new VerticalLayout(codeEditorHeader, codeEditorButtonsLayout, codeEditor);
    	add(codeEditorLayout);
    	
    	UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "User");
    	UserInfo botInfo = new UserInfo(UUID.randomUUID().toString(), "SlowLang Bot");
    	
    	tabs = new Tabs();
        for (ChatInfo chat : chats) {
        	
        	MessageManager userMessageManager = new MessageManager(this, userInfo, chat.getCollaborationTopic());
        	MessageManager botMessageManager = new MessageManager(this, botInfo, chat.getCollaborationTopic());
        	
        	botMessageManager.submit(UIConstants.DEFAULT_HELP_MESSAGE);
        	
        	userMessageManager.setMessageHandler(context -> {
                
            	CollaborationMessage message = context.getMessage();
            	UserInfo user = message.getUser();
            	String text = message.getText();
            	
            	if(user.getName().equals("User")) {
            		
            		String sourceCode = codeEditor.getValue();
            		Request userRequest = new Request(sourceCode, text, Requests.types);
            		
            		if(userRequest.equals(Requests.COMPILE_AND_RUN)) {
            			
            		}
            		
            		CompletableFuture<String> futureResponse = CompletableFuture.supplyAsync(() -> botService.handleRequest(userRequest));
            		futureResponse.thenAccept(response -> {
            			
            			botMessageManager.submit(response);
            		});

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
        
        H3 chatAreaHeader = new H3("SlowLang Chat Bot");
        
        HorizontalLayout chatAreaButtonsLayout = new HorizontalLayout();
        
        Button displayCommandsButton = new Button("Display Available Commands", new Icon(VaadinIcon.COMPILE));
        displayCommandsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        
        Button generateVerboseLog = new Button("Generate Verbose Log", new Icon(VaadinIcon.TREE_TABLE));
        generateVerboseLog.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        
        Button configureButton = new Button("Configure", new Icon(VaadinIcon.COGS));
        configureButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        
        Button clearButton = new Button("Clear", new Icon(VaadinIcon.COGS));
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        
        chatAreaButtonsLayout.add(displayCommandsButton);
        chatAreaButtonsLayout.add(generateVerboseLog);
        chatAreaButtonsLayout.add(configureButton);
        chatAreaButtonsLayout.add(clearButton);
        
        VerticalLayout chatContainer = new VerticalLayout(chatAreaHeader, chatAreaButtonsLayout);
        chatContainer.addClassNames(Flex.AUTO, Overflow.HIDDEN);
        chatContainer.add(list, input);
        chatContainer.addClassName("bg-contrast-5");
        add(chatContainer);
        setSizeFull();
        expand(list);
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
