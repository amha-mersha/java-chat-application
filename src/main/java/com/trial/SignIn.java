package com.trial;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SignIn extends JFrame {
    JLabel welcomeLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;
    CustomButton signInButton;
    CustomButton signUpButton;
    JTextField userName;
    JPasswordField passwordField;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection users;
    

    public SignIn() {
        this.setTitle("Sign In");
        this.setSize(400, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Welcome label panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        welcomePanel.setPreferredSize(new Dimension(400, 120)); // Set preferred size
        welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomePanel.add(welcomeLabel);
        this.add(welcomePanel);

         // Username panel
         JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         usernamePanel.setPreferredSize(new Dimension(400, 20)); // Set preferred size
         userName = new JTextField(15);
         usernamePanel.add(new JLabel("Username"));
         usernamePanel.add(userName);
         this.add(usernamePanel);

        // Password panel
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setPreferredSize(new Dimension(400, 50)); // Set preferred size
        passwordField = new JPasswordField(15);
        passwordPanel.add(new JLabel("Password"));
        passwordPanel.add(passwordField);
        this.add(passwordPanel);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,5,0));
        buttonsPanel.setPreferredSize(new Dimension(250, 30)); // Set preferred size
        signInButton = new CustomButton("Sign In",new Color(78, 153, 245),Color.WHITE,20);
        signInButton.setFocusPainted(false);

        
        signUpButton = new CustomButton("Sign Up", new Color(78, 153, 245),Color.WHITE,20);
        signUpButton.setFocusPainted(false);
        
         // Add ActionListener to Sign In button
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = userName.getText();
                String enteredPassword = new String(passwordField.getPassword());
                String storedPassword = getPassword(enteredUsername);

                if (storedPassword == null) {
                    JOptionPane.showMessageDialog(SignIn.this, "The username doesn't exist");
                } else if (storedPassword.equals(enteredPassword)) {
                    // Successful login, perform actions here
                    try {
                        ChatGUI gui = new ChatGUI(enteredUsername);
                        Client client = new Client(new Socket("0.0.0.0", 1234),enteredUsername,gui);
                        client.listenForMessage();
                        JOptionPane.showMessageDialog(SignIn.this, "Login Successful");
                        dispose();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        JOptionPane.showMessageDialog(SignIn.this, "Couldn't connect to connect to server");
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(SignIn.this, "Incorrect password");
                }
            }
        });

        // Add ActionListener to Sign Up button
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to switch to the Sign Up page
                setVisible(false);
                SignUp signUp = new SignUp(SignIn.this);
                signUp.setVisible(true);
            }
        });

        
        buttonsPanel.add(signInButton);
        buttonsPanel.add(signUpButton);
        this.add(buttonsPanel);
        
        setVisible(true);
    }


    public String getPassword(String name) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("chatdb");
        MongoCollection collection = database.getCollection("users");

        Document result = (Document) collection.find(new Document("username", name)).first();
        if (result != null) {
            return result.getString("password");
        } else {
            return null;
        }
    }
    public static void main(String[] args) {
        SignIn signIn = new SignIn();
    }

}
