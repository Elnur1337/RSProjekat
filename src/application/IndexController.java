package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IndexController implements Initializable {
	private static User user = new User();
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button homeBtn;
	@FXML
	private Button loginBtn;
	@FXML
	private Button registerBtn;
	@FXML
	private Button logoutBtn;
	
	public IndexController setCurrentUser(User second) {
		user = second;
		return this;
	}
	
	public void switchToRegisterScene(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("view/register.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		return;
	}
	
	public void switchToLoginScene(ActionEvent event) throws IOException {
		System.out.println(getClass().getResource("view/login.fxml"));
		root = FXMLLoader.load(getClass().getResource("/application/view/login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
		return;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (user.isLoggedIn()) {
			loginBtn.setVisible(false);
			loginBtn.setManaged(false);
			registerBtn.setVisible(false);
			registerBtn.setManaged(false);
			logoutBtn.setVisible(true);
			logoutBtn.setManaged(true);
		} else {
			loginBtn.setVisible(true);
			loginBtn.setManaged(true);
			registerBtn.setVisible(true);
			registerBtn.setManaged(true);
			logoutBtn.setVisible(false);
			logoutBtn.setManaged(false);
		}
		return;
	}
	
	public void logout() {
		user = new User();
		//Setovati nogo usera i refreshovati scenu?
		loginBtn.setVisible(true);
		loginBtn.setManaged(true);
		registerBtn.setVisible(true);
		registerBtn.setManaged(true);
		logoutBtn.setVisible(false);
		logoutBtn.setManaged(false);
		return;
	}
}
