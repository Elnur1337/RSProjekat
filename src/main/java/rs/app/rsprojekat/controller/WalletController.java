package rs.app.rsprojekat.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.util.Duration;
import rs.app.rsprojekat.model.Dogadjaj;
import rs.app.rsprojekat.model.User;

import javax.persistence.*;

public class WalletController implements Initializable {
    private static User user = IndexController.getCurrentUser();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String msg;

    @FXML
    private Button homeBtn;
    @FXML
    private Button adminPanelBtn;
    @FXML
    private Button eventsBtn;

    @FXML
    private TextField imeInput;
    @FXML
    private TextField prezimeInput;
    @FXML
    private TextField brojKarticeInput;
    @FXML
    private TextField datumIstekaInput;
    @FXML
    private TextField cvvInput;
    @FXML
    private TextField iznosInput;

    @FXML
    private Label stanjeLabel;
    @FXML
    private Label msgLabel;

    private void refreshWallet() {
        stanjeLabel.setText("Stanje na računu: " + user.getWallet() + "KM");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshWallet();

        if(user.isAdmin()) {
            adminPanelBtn.setVisible(true);
            adminPanelBtn.setManaged(true);
        } else {
            adminPanelBtn.setVisible(false);
            adminPanelBtn.setManaged(false);
        }

        if(user.isOrganizator()) {
            eventsBtn.setVisible(true);
            eventsBtn.setManaged(true);
        } else {
            eventsBtn.setVisible(false);
            eventsBtn.setManaged(false);
        }
        
        imeInput.clear();
        imeInput.setPromptText("Ime");
        prezimeInput.clear();
        prezimeInput.setPromptText("Prezime");
        brojKarticeInput.clear();
        brojKarticeInput.setPromptText("Broj kartice");
        datumIstekaInput.clear();
        datumIstekaInput.setPromptText("mjesec/godina isteka");
        cvvInput.clear();
        cvvInput.setPromptText("CVV broj");
    }

    private void switchScene(String sceneURL, ActionEvent event) throws IOException {
        final URL url = Paths.get(sceneURL).toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeScene(ActionEvent actionEvent) throws IOException {
        switchScene("src/main/resources/rs/app/rsprojekat/index.fxml", actionEvent);
    }

    public void switchToAdminPanelScene(ActionEvent actionEvent) throws IOException {
        switchScene("src/main/resources/rs/app/rsprojekat/adminPanel.fxml", actionEvent);
    }

    public void switchToOrganizerScene(ActionEvent actionEvent) throws IOException {
        switchScene("src/main/resources/rs/app/rsprojekat/organizer.fxml", actionEvent);
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        final IndexController indexController = new IndexController();
        indexController.setCurrentUser(new User());

        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();
        homeBtn.fire();
    }

    private boolean validate() {
        String ime = imeInput.getText();
        String prezime = prezimeInput.getText();
        String brojKartice = brojKarticeInput.getText();
        String datumIsteka = datumIstekaInput.getText();
        String cvv = cvvInput.getText();
        Double iznos = 0.0;

        if(ime.length() < 2 || ime.length() > 20) {
            System.out.println(ime);
            msg = "Ime mora sadržavati 2-20 znakova.";
            return false;
        }
        if(!ime.matches("^[A-Z][a-zA-Zčćžđš]*$")) {
            System.out.println(ime);
            msg = "Ime mora početi velikim slovom i smije sadržavati samo slova.";
            return false;
        }

        if(prezime.length() < 2 || prezime.length() > 20) {
            System.out.println(prezime);
            msg = "Prezime mora sadržavati 2-20 znakova.";
            return false;
        }
        if(!prezime.matches("^[A-Z][a-zA-Zčćžđš]*$")) {
            System.out.println(prezime);
            msg = "Prezime mora početi velikim slovom i smije sadržavati samo slova.";
            return false;
        }

        if(!brojKartice.matches("\\d+") || brojKartice.length() != 16) {
            System.out.println(brojKartice);
            msg = "Pogrešan unos broja kartice.";
            return false;
        }

        if (datumIsteka.matches("^\\d{2}/\\d{2}$")) {
            String[] parts = datumIsteka.split("/");
            Long mjesec = Long.parseLong(parts[0]);
            Long godina = Long.parseLong(parts[1]);

            LocalDate currentDate = LocalDate.now();
            int trenutniMjesec = currentDate.getMonthValue();
            int trenutnaGodina = currentDate.getYear() - 2000;

            if(trenutnaGodina > godina || (trenutnaGodina == godina && trenutniMjesec > mjesec)) {
                System.out.println(datumIsteka);
                msg = "Kartica je istekla.";
                return false;
            }
        } else {
            System.out.println(datumIsteka);
            msg = "Pogrešan unos datuma isteka.";
            return false;
        }

        if(!cvv.matches("\\d+") || cvv.length() != 3) {
            System.out.println(cvv);
            msg = "Pogrešan CVV broj kartice.";
            return false;
        }

        try {
            iznos = Double.parseDouble(iznosInput.getText());
        } catch(NumberFormatException e) {
            System.out.println(iznos);
            msg = "Pogrešan unos iznosa.";
            return false;
        }

        if(iznos < 10.0) {
            System.out.println(iznos);
            msg = "Iznos ne moze biti manji od 10KM.";
            return false;
        }

        return true;
    }

    public void uplatiNovac(ActionEvent actionEvent) {
        if(validate()) {
            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();

            User updatedUser = entityManager.find(User.class, user.getId());
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            updatedUser.setWallet(user.getWallet() + Double.parseDouble(iznosInput.getText()));
            entityTransaction.commit();
            user = updatedUser;
            refreshWallet();

            msg = "Uspješno ste uplatili novac na račun.";
            printMessage(true);
        } else {
            printMessage(false);
        }
    }

    private void printMessage(boolean successful) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
        msgLabel.setText(msg);
        if (successful) {
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        } else {
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
        }
        msgLabel.setVisible(true);
        visibleMsg.play();
    }
}