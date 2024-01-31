package com.trial;

import java.util.ArrayList;

public class ChatMediatorImpl implements ChatMediator {
    private final ArrayList<String> userList = new ArrayList<>();
    private final ArrayList<ChatGUI> chatGUIs = new ArrayList<>();

    ChatMediatorImpl(){
    }

    @Override
    public void addUser(String username) {
        userList.add(username);
        notifyUserListUpdate();
    }

    @Override
    public void removeUser(String username) {
        userList.remove(username);
        notifyUserListUpdate();
    }

    @Override
    public ArrayList<String> getUserList() {
        return new ArrayList<>(userList);
    }

    public void addChatGUI(ChatGUI chatGUI) {
        chatGUIs.add(chatGUI);
    }

    public void removeChatGUI(ChatGUI chatGUI) {
        chatGUIs.remove(chatGUI);
    }

    private void notifyUserListUpdate() {
        for (ChatGUI chatGUI : chatGUIs) {
            chatGUI.updateSharedUserList();
        }
    }

}