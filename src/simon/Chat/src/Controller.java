package simon.Chat.src;

import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Controller implements Initializable , Runnable{

	@FXML
	private TextArea txtArea;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtNachricht;
	
	@FXML
	private	Button btnSend;
	
	private Socket client;
	BufferedReader reader;
	PrintWriter writer;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		this.txtArea.setEditable(false);
		
		btnSend.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				 sendMessageToServer();	 
			}
		});
		
		
	     try {
            client = new Socket("127.0.0.1", 5555);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream());
            System.out.println("Netzwerkverbindung hergestellt");
    } catch(Exception e) {
            System.out.println("Netzwerkverbindung konnte nicht hergestellt werden");
            e.printStackTrace();
    }
	     Thread t = new Thread(this); //new Controller durch this ersetzen dadurch würde die Exception am Anfang wegfallen
        t.start();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 String message;
		 
		try {
                 while((message = reader.readLine()) != null) {
                     System.out.println(message);   
                	 
                	 String k = message;
                	 Platform.runLater(() -> {
                		 appendTextMessages(k);});
                	 
                	 Platform.runLater(() -> {
                		 this.txtArea.positionCaret(txtArea.getText().length()); });
                        
                	 
                 }
         } catch (IOException e) {
                 appendTextMessages("Nachricht konnte nicht empfangen werden!");
                 e.printStackTrace();
         }
         
		 
		
	}
	public void sendMessageToServer() {
        writer.println(txtName.getText() + ": " + txtNachricht.getText());
        writer.flush();
       
        txtNachricht.setText("");
        txtNachricht.requestFocus();
}

public void appendTextMessages(String message) {
        this.txtArea.appendText(message + "\n");
}

}
