package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IndexController {
	private User user;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button homeBtn;
	@FXML
	private Button loginBtn;
	@FXML
	private Button registerBtn;
	
	
	public IndexController setCurrentUser(User second) {
		user = second;
		if (user.isLoggedIn()) {
			loginBtn.setVisible(false);
			registerBtn.setVisible(false);
		}
		return this;
	}
	
	public void switchToRegisterScene(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("register.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;
	}
}
