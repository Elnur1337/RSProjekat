package rs.app.rsprojekat.controller;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.animation.TranslateTransition;

import javafx.util.Duration;
import rs.app.rsprojekat.model.*;

import javax.persistence.*;

public class ProfileController implements Initializable {
    private static User user = new User();
    private String msg;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button homeBtn;
    @FXML
    private Button adminPanelBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button uplataBtn;

    @FXML
    private Label stanjeLabel;
    @FXML
    private Label msgLabel;

    @FXML
    private TextField imeShow;
    @FXML
    private TextField prezimeShow;
    @FXML
    private TextField datumRodjenjaShow;
    @FXML
    private TextField emailShow;
    @FXML
    private TextField stanjeShow;
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
    private VBox infoPanel;
    @FXML
    private VBox walletPanel;

    @FXML
    private Pagination kartePagination;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = IndexController.getCurrentUser();

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

        showInfoPanel();
    }

    public void switchToHomeScene(ActionEvent actionEvent) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        final IndexController indexController = new IndexController();
        indexController.setCurrentUser(new User());

        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();
        homeBtn.fire();
    }

    private void showInfoPanel() {
        infoPanel.setVisible(true);
        kartePagination.setVisible(false);
        walletPanel.setVisible(false);

        imeShow.setText(user.getIme());
        prezimeShow.setText(user.getPrezime());
        datumRodjenjaShow.setText((new SimpleDateFormat("dd-MM-yyyy")).format(user.getDatumRod()));
        emailShow.setText(user.getEmail());
        stanjeShow.setText(user.getWallet() + "KM");
    }

    public void showInfoPanel(MouseEvent mouseEvent) {
        showInfoPanel();
    }

    private void showBoughtTicketsPanel() {

    }

    public void showBoughtTicketsPanel(MouseEvent mouseEvent) {
    }

    private void showReservedTicketsPanel() {

    }

    public void showReservedTicketsPanel(MouseEvent mouseEvent) {
    }

    private void showWalletPanel() {
        infoPanel.setVisible(false);
        kartePagination.setVisible(false);
        walletPanel.setVisible(true);

        refreshWallet();

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

    public void showWalletPanel(MouseEvent mouseEvent) {
        showWalletPanel();
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

    private void refreshWallet() {
        stanjeLabel.setText("Stanje na računu: " + user.getWallet() + "KM");
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