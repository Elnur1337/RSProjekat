package rs.app.rsprojekat.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//JavaFX Imports
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

//Model Imports
import rs.app.rsprojekat.model.User;

import rs.app.rsprojekat.DatabaseConnection;

public class LoginController {
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

    public void switchToHomeScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return;
    }

    public void switchToRegisterScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/register.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private boolean validate() {
        String email = emailInput.getText();
        final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            msg = "Email nije u dobrom formatu!";
            return false;
        }
        String password = passwordInput.getText();
        if (password.length() < 8) {
            msg = "Sifra mora biti minimalno osam karaktera!";
            return false;
        }
        msg = "";
        return true;
    }

    public void login() throws IOException {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));

        if (validate()) {
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement prepStatment = connection.prepareStatement("SELECT id, ime, prezime, email, pass, wallet, admin, organizator, datum_rod, created_at FROM korisnik WHERE email = ? AND approved = 1");
                prepStatment.setString(1, emailInput.getText());
                ResultSet res = prepStatment.executeQuery();
                if (!res.next()) {
                    connection.close();
                    msg = "Korisnik sa tim podacima ne postoji!";
                    visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
                    msgLabel.setText(msg);
                    msgLabel.setStyle(msgLabel.getStyle() + " -fx-background-color: #8a1313;");
                    msgLabel.setVisible(true);
                    visibleMsg.play();
                    return;
                }
                String hashedPassword = res.getString("pass");
                Result result = BCrypt.verifyer().verify(passwordInput.getText().toCharArray(), hashedPassword);
                if (result.verified) {
                    User user = new User();
                    user.setId(res.getInt("id"));
                    user.setIme(res.getString("ime"));
                    user.setPrezime(res.getString("prezime"));
                    user.setEmail(res.getString("email"));
                    user.setWallet(res.getDouble("wallet"));
                    user.setDatumRod(res.getDate("datum_rod"));
                    user.setCreatedAt(res.getTimestamp("created_at"));
                    user.setLoggedIn(true);
                    connection.close();
                    final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
                    FXMLLoader loader = new FXMLLoader(url);
                    loader.load();

                    IndexController indexController = loader.getController();
                    indexController.setCurrentUser(user);
                    msg = "Uspjesna prijava!";
                    msgLabel.setText(msg);
                    msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
                    msgLabel.setVisible(true);
                    visibleMsg.setOnFinished(event -> {
                        msgLabel.setVisible(false);
                        homeBtn.fire();
                    });
                    visibleMsg.play();
                    return;
                }
                msg = "Pogrešna šifra!";
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                msg = "Problem sa bazom, registracija nije moguca!";
            }
        }
        visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
        msgLabel.setText(msg);
        msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
        msgLabel.setVisible(true);
        visibleMsg.play();
    }
}
