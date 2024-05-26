package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;



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
			errorMsgLabel.setText(errorMsg);
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
		try {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			PreparedStatement prepStatment = connection.prepareStatement("SELECT email FROM korisnik WHERE email = ?");
			prepStatment.setString(1, email);
			ResultSet res = prepStatment.executeQuery();
			if (res.next()) {
				connection.close();
				errorMsg = "Korisnik sa unesenim emailom vec postoji!";
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			errorMsg = "Problem sa bazom, registracija nije moguca!";
			return false;
		}
		String password = passwordInput.getText();
		if (password.length() < 8) {
			errorMsg = "Sifra mora biti minimalno osam karaktera!";
			return false;
		}
		String passwordRepeat = passwordRepeatInput.getText();
		if (!password.equals(passwordRepeat)) {
			errorMsg = "Ponovljena sifra se ne poklapa!";
			return false;
		}
		try {
			yearInput.getValue();
			
		} catch (NullPointerException e) {
			errorMsg = "Niste unijeli godinu rodjenja!";
			return false;
		}
		try {
			monthInput.getValue();
			
		} catch (NullPointerException e) {
			errorMsg = "Niste unijeli mjesec rodjenja!";
			return false;
		}
		try {
			dayInput.getValue();
			
		} catch (NullPointerException e) {
			errorMsg = "Niste unijeli dan rodjenja!";
			return false;
		}
		errorMsg = "";
		return true;
	}
	
	public void register() {
		if (validate()) {
			errorMsgLabel.setVisible(false);
			errorMsgLabel.setText(errorMsg);			
			try {
				Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement prepStatment = connection.prepareStatement("INSERT INTO korisnik (ime, prezime, email, pass, organizator, datum_rod) VALUES (?, ?, ?, ?, ?, ?)");
				prepStatment.setString(1, imeInput.getText());
				prepStatment.setString(2, prezimeInput.getText());
				prepStatment.setString(3, emailInput.getText());
				String hashedPassword = BCrypt.withDefaults().hashToString(10, passwordInput.getText().toCharArray());
				prepStatment.setString(4, hashedPassword);
				if (isOrganizatorInput.isSelected()) {
					prepStatment.setInt(5, 1);
				} else {
					prepStatment.setInt(5, 0);
				}
				prepStatment.setDate(6, Date.valueOf(yearInput.getValue() + "-" + monthNameNumberMap.get(monthInput.getValue()) + "-" + dayInput.getValue()));
				prepStatment.execute();
				connection.close();
				System.out.println("Sve uredu!");
				
			} catch (ClassNotFoundException | SQLException e) {
				errorMsg = "Problem sa bazom, registracija nije moguca!";
				
				//Ispis poruke greske za bazu
				errorMsgLabel.setText(errorMsg);
				errorMsgLabel.setVisible(true);
				
				PauseTransition visibleErrorMsg = new PauseTransition();
				visibleErrorMsg.setDuration(Duration.seconds(5));
				visibleErrorMsg.setOnFinished(event -> errorMsgLabel.setVisible(false));
				visibleErrorMsg.play();
			}
			
			homeBtn.fire();
			return;
		}
		errorMsgLabel.setText(errorMsg);
		errorMsgLabel.setVisible(true);
		
		PauseTransition visibleErrorMsg = new PauseTransition();
		visibleErrorMsg.setDuration(Duration.seconds(5));
		visibleErrorMsg.setOnFinished(event -> errorMsgLabel.setVisible(false));
		visibleErrorMsg.play();
		return;
	}
}