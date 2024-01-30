package com.trial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
// import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    // amha: database
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection messageCollection;

    ClientHandler(Socket socket){
        try{
            this.mongoClient = MongoClients.create("mongodb://localhost:27017");
            this.database = mongoClient.getDatabase("chatdb");
            this.messageCollection = database.getCollection("messages");

            this.socket = socket;
            //socket.getInputStream gives us a byte stream so we wrapp it with InputStreamReader to character stream, the same with the output stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();//this would be accepted from the user as they sign in  
            clientHandlers.add(this);
            broadcastMessage("SERVER: " +this.clientUsername+ " has entered the chat!");
        }catch (IOException e){
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }
    
    @Override
    public void run(){
        //Everything in here is going to be run on a separate thread
        String messageFromClient;

        while (socket.isConnected()) {
            try{
                messageFromClient = bufferedReader.readLine();
                //amha
                // Store the message in MongoDB
                Document document = new Document("sender",this.clientUsername)
                                        .append("message", messageFromClient)
                                        .append("timestamp", System.currentTimeMillis());
                messageCollection.insertOne(document);
                broadcastMessage(messageFromClient);
            } catch(IOException e){
                closeEverthing(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(this.clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();// newline character: this is to finish the line because the clients would be expecting a newline()
                    clientHandler.bufferedWriter.flush(); // to small to fill the buffer so we flush it
                }
            }catch(IOException e){
                closeEverthing(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + this.clientUsername + " has left the chat!");
    }

    public void closeEverthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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
}