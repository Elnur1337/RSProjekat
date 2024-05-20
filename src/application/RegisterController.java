package application;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
	
	private boolean validate() {
		String ime = imeInput.getText();
		if (ime.length() < 2 ||  ime.length() > 20) {
			errorMsg = "Ime mora biti izmedju 2 i 20 karaktera!";
			return false;
		}
		if (ime.charAt(0) == ' ') {
			errorMsg = "Ime ne moze pocinjati sa praznim mjestom!";
			return false;
		}
		String prezime = prezimeInput.getText();
		if (prezime.length() < 2 || prezime.length() > 20) {
			errorMsg = "Prezime mora biti izmedju 2 i 20 karaktera!";
			return false;
		}
		if (prezime.charAt(0) == ' ') {
			errorMsg = "Prezime ne moze pocinjati sa praznim mjestom!";
			return false;
		}
		String email = emailInput.getText();
		final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			errorMsg = "Email nije u dobrom formatu!";
			return false;
		}
		String password = passwordInput.getText();
		if (password.length() < 8) {
			errorMsg = "Sifra mora biti minimalno osam karaktera!";
			return false;
		}
		String passwordRepeat = passwordRepeatInput.getText();
		if (password != passwordRepeat) {
			errorMsg = "Ponovljena sifra se ne poklapa!";
			return false;
		}
		return true;
	}
	
	public void register() {
		if (validate()) {
			//Registruj korisnika u bazu
		}
		return;
	}
}
