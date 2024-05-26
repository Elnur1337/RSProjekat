package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	private User user;
	private String msg;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button homeBtn;
	@FXML
	private Button registerBtn;
	@FXML
	private TextField emailInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private CheckBox rememberMeInput;
	@FXML
	private Button loginBtn;
	@FXML
	private Label msgLabel;
	
	public LoginController setCurrentUser(User second) {
		user = second;
		return this;
	}
	
	public void switchToHomeScene(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("index.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;
	}
	
	public void switchToRegisterScene(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("register.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;
	}
	
	public void login() {
		
		return;
	}
}
