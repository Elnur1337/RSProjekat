package application;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController implements Initializable {
	private String errorMsg;
	private Map<String, Integer> monthNameNumberMap = new HashMap<>();
	private Map<String, Integer> monthNameDaysMap = new HashMap<>();
	
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
	private ComboBox<Integer> yearInput;
	@FXML
	private ComboBox<String> monthInput;
	@FXML
	private ComboBox<Integer> dayInput;
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		monthNameNumberMap.put("Januar", 1);
		monthNameNumberMap.put("Februar", 2);
		monthNameNumberMap.put("Mart", 3);
		monthNameNumberMap.put("April", 4);
		monthNameNumberMap.put("Maj", 5);
		monthNameNumberMap.put("Jun", 6);
		monthNameNumberMap.put("Jul", 7);
		monthNameNumberMap.put("August", 8);
		monthNameNumberMap.put("Septembar", 9);
		monthNameNumberMap.put("Oktobar", 10);
		monthNameNumberMap.put("Novembar", 11);
		monthNameNumberMap.put("Decembar", 12);
		
		monthNameDaysMap.put("Januar", 31);
		monthNameDaysMap.put("Februar", 29);
		monthNameDaysMap.put("Mart", 31);
		monthNameDaysMap.put("April", 30);
		monthNameDaysMap.put("Maj", 31);
		monthNameDaysMap.put("Jun", 30);
		monthNameDaysMap.put("Jul", 31);
		monthNameDaysMap.put("August", 31);
		monthNameDaysMap.put("Septembar", 30);
		monthNameDaysMap.put("Oktobar", 31);
		monthNameDaysMap.put("Novembar", 30);
		monthNameDaysMap.put("Decembar", 31);
		
		for (int i = 1970; i < 2025; ++i) {
			yearInput.getItems().add(i);
		}
		
		monthInput.getItems().addAll("Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "August", "Septembar", "Oktobar", "Novembar", "Decembar");
		
		monthInput.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						dayInput.getItems().clear();
						for (int i = 1; i <= monthNameDaysMap.get(newValue); ++i) {
							dayInput.getItems().add(i);
						}
					}
				}
		);
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
