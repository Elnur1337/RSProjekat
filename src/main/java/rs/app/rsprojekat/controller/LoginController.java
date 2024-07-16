package rs.app.rsprojekat.controller;

//Java Util Imports
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileWriter;

//BCrypt Imports
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

//JavaFX Imports
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

//JPA Imports
import javax.persistence.*;

public class LoginController {
    private String msg;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button homeBtn;
    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private CheckBox rememberMeInput;
    @FXML
    private Label msgLabel;

    public void switchToHomeScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegisterScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/register.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private boolean validate() {
        String email = emailInput.getText();
        final String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            msg = "Email nije u dobrom formatu!";
            return false;
        }
        String password = passwordInput.getText();
        if (password.length() < 8) {
            msg = "Šifra mora biti minimalno osam karaktera!";
            return false;
        }
        msg = "";
        return true;
    }

    public void login() throws IOException {
        if (validate()) {
            PauseTransition visibleMsg = new PauseTransition(Duration.millis(1500));

            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();

            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE email = :emailInput", User.class);
            query.setParameter("emailInput", emailInput.getText());

            User user;
            try {
                user = query.getSingleResult();
            } catch (NoResultException e) {
                entityManager.close();
                entityManagerFactory.close();
                msg = "Korisnik sa tim podacima ne postoji!";
                visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
                msgLabel.setText(msg);
                msgLabel.setStyle(msgLabel.getStyle() + " -fx-background-color: #8a1313;");
                msgLabel.setVisible(true);
                visibleMsg.play();
                return;
            } catch (Exception e) {
                entityManager.close();
                entityManagerFactory.close();
                msg = "Problem sa bazom, prijava nije moguca!";
                return;
            }
            if (!user.isApproved()) {
                msg = "Vaša registracija još nije prihvaćena, pokušajte kasnije!";
                visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
                msgLabel.setText(msg);
                msgLabel.setStyle(msgLabel.getStyle() + " -fx-background-color: #8a1313;");
                msgLabel.setVisible(true);
                visibleMsg.play();
                return;
            }

            Result result = BCrypt.verifyer().verify(passwordInput.getText().toCharArray(), user.getPass());
            if (result.verified) {
                user.setLoggedIn(true);
                entityManager.close();
                entityManagerFactory.close();
                final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                loader.load();

                IndexController indexController = loader.getController();
                indexController.setCurrentUser(user);
                msg = "Uspješna prijava!";
                msgLabel.setText(msg);
                msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
                msgLabel.setVisible(true);
                visibleMsg.setOnFinished(event -> {
                    msgLabel.setVisible(false);
                    homeBtn.fire();
                });
                visibleMsg.play();

                //Remember me logic
                if (!rememberMeInput.isSelected()) {
                    return;
                }
                File rememberMeFile = new File("rememberMe.txt");
                rememberMeFile.createNewFile();
                FileWriter rememberMeWriter = new FileWriter(rememberMeFile);
                rememberMeWriter.write("");
                rememberMeWriter.write(user.getEmail() + " | " + user.getPass());
                rememberMeWriter.close();
                return;
            }
            msg = "Pogrešna šifra!";
        }

        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
        msgLabel.setText(msg);
        msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
        msgLabel.setVisible(true);
        visibleMsg.play();
    }
}