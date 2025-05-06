package org.example.clientsevermsgexample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientView {

    @FXML
    private TextField tf_message;
    @FXML
    private Button button_send;
    @FXML
    private VBox vbox_messages;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    @FXML
    public void initialize(){
        try {
            socket = new Socket("localhost", 5555);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true){
                        String msg = dis.readUTF();
                        Platform.runLater(() -> addMessage("Server: " +msg));}
                } catch (Exception e) {
                    e.printStackTrace();}
            }).start();

            button_send.setOnAction(e -> sendMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendMessage() {
        String msg = tf_message.getText();
        if (!msg.isEmpty()) {
            try{
                dos.writeUTF(msg);
                addMessage("You: " + msg);
                tf_message.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void addMessage(String msg){
        Text text = new Text(msg);
        vbox_messages.getChildren().add(text);
    }
}
