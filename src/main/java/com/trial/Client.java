package com.trial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Client implements MessageCallback{
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    String username;
    private ChatGUI gui;
    
    Client(Socket socket, String username, ChatGUI gui){
        try {
            this.gui = gui;
            // Set itself as a callback in the GUI
            gui.setMessageCallback(this);
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            closeEverthing(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(String messageToSend){
        // i think this part would be changed to impliment GUI
        try {
            bufferedWriter.write(username + ": " +messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable()
        {
            @Override
            public void run(){
                String msgFromGroupChat;
                
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        gui.appendToPane(gui.discussionField,msgFromGroupChat);//every client waits for the msg sent form client handler and print it out on their consol
                    } catch (Exception e) {
                        closeEverthing(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (socket != null){
                socket.close();
            }
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageSent(String message) {
        // The callback from the GUI
        sendMessage(message);
    }
    @Override
    public void onDisconnect() {
        // The callback from the GUI for disconnection
        disconnect();
    }

    private void disconnect() {
        closeEverthing(socket, bufferedReader, bufferedWriter);
    }

    // public static void main(String[] args) throws UnknownHostException, IOException {

    //     Scanner scanner = new Scanner(System.in);
    //     System.out.print("Enter your username for the group chat: ");
    //     String username = scanner.nextLine();
    //     Socket socket = new Socket("localhost", 1234);
    //     Client client = new Client(socket, username,);
    // }
}