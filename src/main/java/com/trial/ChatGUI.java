package com.trial;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatGUI {
    final JTextPane discussionField = new JTextPane();
    final JTextPane listUsers = new JTextPane();
    final JTextField textArea = new JTextField();
    private String oldMsg = "";
    private MessageCallback messageCallback;    
    private String usernameString;
    
    public ChatGUI(String usernameString){
        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);
        this.usernameString = usernameString;

        final JFrame mainFrame = new JFrame("Chat");  
        mainFrame.getContentPane().setLayout(null);
        mainFrame.setSize(700, 500);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Discussion thread module
        discussionField.setBounds(25, 25, 490, 320);
        discussionField.setFont(font);
        discussionField.setMargin(new Insets(6, 6, 6, 6));
        discussionField.setEditable(false);
        JScrollPane discussionScroll = new JScrollPane(discussionField);
        discussionScroll.setBounds(25, 25, 490, 320);

        discussionField.setContentType("text/html");
        discussionField.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        // Module of the user list
        listUsers.setBounds(520, 25, 156, 320);
        listUsers.setEditable(true);
        listUsers.setFont(font);
        listUsers.setMargin(new Insets(6, 6, 6, 6));
        listUsers.setEditable(false);
        JScrollPane listScroll = new JScrollPane(listUsers);
        listScroll.setBounds(520, 25, 156, 320);

        listUsers.setContentType("text/html");
        listUsers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        // Field message user input
        textArea.setBounds(25, 350, 510, 50);
        textArea.setFont(font);
        textArea.setMargin(new Insets(6, 6, 6, 6));
        final JScrollPane textScroll = new JScrollPane(textArea);
        textScroll.setBounds(25, 350, 510, 50);

        // button send
        final JButton sendButton = new CustomButton("Send", new Color(78, 153, 245),Color.WHITE,20);
        sendButton.setFont(font);
        sendButton.setBounds(575, 410, 100, 35);

        // button Disconnect
        final JButton disconnectButton = new CustomButton("Disconnect", new Color(78, 153, 245),Color.WHITE,20);
        disconnectButton.setFont(font);
        disconnectButton.setBounds(25, 410, 130, 35);

        JPanel buttons = new JPanel();
        buttons.setBounds(550,350,120,100);
        buttons.setLayout(new GridLayout(2,1,3,4));
        buttons.add(sendButton);
        buttons.add(disconnectButton);

        textArea.addKeyListener(new KeyAdapter() {
            // send message on Enter
            public void keyPressed(KeyEvent e) {
              if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                sendMessage();
              }
      
              // Get last message typed
              if (e.getKeyCode() == KeyEvent.VK_UP) {
                String currentMessage = textArea.getText().trim();
                textArea.setText(oldMsg);
                oldMsg = currentMessage;
              }
      
              if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                String currentMessage = textArea.getText().trim();
                textArea.setText(oldMsg);
                oldMsg = currentMessage;
              }
            }
          });

          sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
              sendMessage();
            }
          });

          disconnectButton.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent ae) {
              messageCallback.onDisconnect();
            }
          });
          Container board = mainFrame.getContentPane();
          board.add(discussionScroll, board);
          board.add(listScroll,board );
          board.add(textScroll, board);
          board.add(buttons, board);
          mainFrame.setVisible(true);
          setMessageCallback(messageCallback);
    }
    public void sendMessage() {
      String message = textArea.getText().trim();
      if (message.equals("")) {
        return;
      }
      if (message != null){
        messageCallback.onMessageSent(message);
      }
      this.oldMsg = message;
      textArea.requestFocus();
      textArea.setText(null);
      appendToPane(discussionField, "<b> YOU </b>" + ": " +message);
  }

  void appendToPane(JTextPane tp, String msg){
    HTMLDocument doc = (HTMLDocument)tp.getDocument();
    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
    try {
      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
      tp.setCaretPosition(doc.getLength());
    } catch(Exception e){
      e.printStackTrace();
    }
    
  }
  
  public void setMessageCallback(MessageCallback callback){
    messageCallback = callback;
  }
  public static void main(String[] args) {
    ChatGUI chatGUI = new ChatGUI("testcase");
  }
}
