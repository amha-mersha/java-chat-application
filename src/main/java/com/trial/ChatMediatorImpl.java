package com.trial;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class ChatMediatorImpl {
    private final List<String> userList = new ArrayList<>();
    private final List<ChatGUI> chatGUIs = new ArrayList<>();

    public void addUser(String username) {
        userList.add(username);
        notifyUserListUpdate();
    }

    public void removeUser(String username) {
        userList.remove(username);
        notifyUserListUpdate();
    }

    public ArrayList<String> getUserList() {
        return new ArrayList<>(userList);
    }

    public ChatGUI getGUI(String usernameString){
        for (ChatGUI gui : chatGUIs){
            if (gui.getName().equals(usernameString)){
                return gui;
            }
        }
        return null;
    }
    public void addChatGUI(ChatGUI chatGUI) {
        chatGUIs.add(chatGUI);
    }

    public void removeChatGUI(ChatGUI chatGUI) {
        chatGUIs.remove(chatGUI);
    }

    public void notifyUserListUpdate() {
    SwingUtilities.invokeLater(() -> {
        for (ChatGUI chatGUI : chatGUIs) {
            chatGUI.updateSharedUserList(this);
        }
    });
}

}
    