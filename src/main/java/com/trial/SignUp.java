package com.trial;

import javax.swing.*;

import org.bson.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SignUp extends JFrame {
    private JLabel greating;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton back;
    private JButton signUpButton;
    private JTextField userName;
    private JPasswordField passwordField;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection users;
    
    
    SignUp(SignIn signIn){
        this.setTitle("Sign Up");
        this.setSize(400, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("chatdb");
        users = database.getCollection("users");

         // Welcome label panel
         JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         titlePanel.setPreferredSize(new Dimension(400, 120)); // Set preferred size
         greating = new JLabel("Hello There!");
         greating.setFont(new Font("Arial", Font.BOLD, 30));
         titlePanel.add(greating);
         this.add(titlePanel);
 
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
         signUpButton = new CustomButton("Sign Up",new Color(78, 153, 245),Color.WHITE,20);
         signUpButton.setFocusPainted(false);
 
         back = new CustomButton("Back to Sign In",new Color(78, 153, 245),Color.WHITE,20);
         back.setFocusPainted(false);

         // Add ActionListener to Sign Up button
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String enteredUsername = userName.getText();
            String enteredPassword = new String(passwordField.getPassword());
                if (users.find(new Document("username", enteredUsername.trim())).first() == null) {
                    boolean condition = true;
                    if (enteredPassword.trim().length() < 5 ){
                        JOptionPane.showMessageDialog(SignUp.this, "Enter a valid password.");
                        condition = false;
                    }
                    // The username is not taken, proceed with sign-up
                    if (enteredUsername.trim().length() < 0){
                        JOptionPane.showMessageDialog(SignUp.this, "Enter a valid username.");
                        condition = false;
                    }
                    if (condition){
                    users.insertOne(new Document("username", enteredUsername.trim()).append("password", enteredPassword));
                    JOptionPane.showMessageDialog(SignUp.this, "You have signed up successfully, proceed to sign in.");
                    signIn.setVisible(true);
                    dispose();  // Close the current SignUp frame
                    }
                } else {
                    // The username is taken
                    JOptionPane.showMessageDialog(SignUp.this, "That username is taken, please choose another one.");
                }
            
            }
        });
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                signIn.setVisible(true);
                dispose();
            }
        });

         buttonsPanel.add(signUpButton);
         buttonsPanel.add(back);
         this.add(buttonsPanel);
    }

    
}
