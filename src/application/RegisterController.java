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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
	private String errorMsg;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button homeBtn;
	@FXML
	private TextField imeInput;
	@FXML
	private TextField prezimeInput;
	@FXML
	private TextField emailInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private PasswordField passwordRepeatInput;
	@FXML
	private ChoiceBox<Integer> yearInput;
	@FXML
	private ChoiceBox<Integer> monthInput;
	@FXML
	private ChoiceBox<Integer> dayInput;
	@FXML
	private CheckBox isOrganizatorInput;
	@FXML
	private Button registerBtn;
	@FXML
	private Label errorMsgLabel;
	
	public void switchToHomeScene(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("index.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		return;
	}
	
	private bool validate()
}
