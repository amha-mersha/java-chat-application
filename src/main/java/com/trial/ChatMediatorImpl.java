package com.trial;

import java.util.ArrayList;
import java.util.List;

public class ChatMediatorImpl implements ChatMediator {
    private final List<String> userList = new ArrayList<>();
    private final List<ChatGUI> chatGUIs = new ArrayList<>();

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

    public void notifyUserListUpdate() {
        for (ChatGUI chatGUI : chatGUIs) {
            chatGUI.updateSharedUserList(this);
        }
    }
}
    