package com.trial;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Server{
    private ServerSocket serverSocket;
    private ChatMediatorImpl mediator;
    
    Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
        this.mediator = new ChatMediatorImpl() ;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected!");
                ClientHandler clienthandler = new ClientHandler(socket,mediator);

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
        ServerSocket serverSocket = new ServerSocket(8080);
        Server server = new Server(serverSocket);
        System.out.println("Port 8080 is now open." + serverSocket.getInetAddress());

        server.startServer();
    }
}
