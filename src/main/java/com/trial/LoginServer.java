package com.trial;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class LoginServer{
    private ServerSocket serverSocket;
    
    LoginServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New Sign In!");
                ClientHandler clienthandler = new ClientHandler(socket);

                Thread thread = new Thread(clienthandler);
                thread.start();
            }
        }catch (IOException e){
            this.closeServerSocket();
        }
    }
    public void closeServerSocket(){
        try{
            if (serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4321);
        Server server = new Server(serverSocket);
        System.out.println("Port 1234 is now open.");

        server.startServer();
    }
}
