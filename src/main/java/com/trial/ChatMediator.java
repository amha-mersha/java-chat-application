package com.trial;

import java.util.ArrayList;

public interface ChatMediator {
    void addUser(String username);
    void removeUser(String username);
    ArrayList<String> getUserList();
}
