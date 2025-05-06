package org.example.clientsevermsgexample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerView {

    @FXML
    private TextField tf_message;
    @FXML
    private Button button_send;
    @FXML
    private VBox vbox_messages;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    @FXML
    public void initialize(){
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5555);
                clientSocket =serverSocket.accept();
                dis =new DataInputStream(clientSocket.getInputStream());
                dos =new DataOutputStream(clientSocket.getOutputStream());

                while(true){
                    String msg =dis.readUTF();
                    Platform.runLater(() -> addMessage("Client: " + msg));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        button_send.setOnAction(e -> sendMessage());
    }
    private void sendMessage(){
        String msg = tf_message.getText();
        if (!msg.isEmpty()) {
            try {
                dos.writeUTF(msg);
                addMessage("You: " + msg);
                tf_message.clear();
            } catch (Exception e) {
                e.printStackTrace();}
        }
    }
    private void addMessage(String msg){
        Text text =new Text(msg);
        vbox_messages.getChildren().add(text);}
}
