package rs.app.rsprojekat.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import rs.app.rsprojekat.model.User;

import javax.persistence.*;

public class IndexController implements Initializable {
    private static User user = new User();

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
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button musicBtn;
    @FXML
    private Button sportBtn;
    @FXML
    private Button cultureBtn;
    @FXML
    private Button cinemaBtn;
    @FXML
    private Button miscBtn;

    private void setButtonVisibility(boolean visibility) {
        musicBtn.setVisible(visibility);
        musicBtn.setManaged(visibility);
        sportBtn.setVisible(visibility);
        sportBtn.setManaged(visibility);
        cultureBtn.setVisible(visibility);
        cultureBtn.setManaged(visibility);
        cinemaBtn.setVisible(visibility);
        cinemaBtn.setManaged(visibility);
        miscBtn.setVisible(visibility);
        miscBtn.setManaged(visibility);
    }

    public void setCurrentUser(User second) {
        user = second;
    }

    public static User getCurrentUser() {
        return user;
    }

    public void switchToRegisterScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/register.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/login.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAdminPanelScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/adminPanel.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMusicScene(ActionEvent event) throws IOException {
//        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/music.fxml").toUri().toURL();
//        root = FXMLLoader.load(url);
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
//        stage.setScene(scene);
//        stage.show();
        System.out.println("Music scene!");
    }

    public void switchToSportScene(ActionEvent event) throws IOException {
//        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/sport.fxml").toUri().toURL();
//        root = FXMLLoader.load(url);
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
//        stage.setScene(scene);
//        stage.show();
        System.out.println("Sport scene!");
    }

    public void switchToCultureScene(ActionEvent event) throws IOException {
//        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/culture.fxml").toUri().toURL();
//        root = FXMLLoader.load(url);
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
//        stage.setScene(scene);
//        stage.show();
        System.out.println("Culture scene!");
    }

    public void switchToCinemaScene(ActionEvent event) throws IOException {
//        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/cinema.fxml").toUri().toURL();
//        root = FXMLLoader.load(url);
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
//        stage.setScene(scene);
//        stage.show();
        System.out.println("Cinema scene!");
    }

    public void switchToMiscScene(ActionEvent event) throws IOException {
//        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/misc.fxml").toUri().toURL();
//        root = FXMLLoader.load(url);
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
//        stage.setScene(scene);
//        stage.show();
        System.out.println("Misc scene!");
    }

    public void switchToOrganizerScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/organizer.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            FileReader rememberMeReader = new FileReader("rememberMe.txt");
            int i;
            StringBuilder data = new StringBuilder();
            while ((i = rememberMeReader.read()) != -1) {
                data.append((char)i);
            }
            if (!data.isEmpty()) {
                final int separatorIndex = data.indexOf(" | ");

                final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
                final EntityManager entityManager = entityManagerFactory.createEntityManager();
                TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE email = :emailInput AND pass = :passInput", User.class);
                query.setParameter("emailInput", data.subSequence(0, separatorIndex));
                query.setParameter("passInput", data.subSequence(separatorIndex + 3, data.length()));

                user = query.getSingleResult();
                entityManager.close();
                entityManagerFactory.close();
                user.setLoggedIn(true);
            }
        } catch (NoResultException e) {
            try {
                FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
                rememberMeWriter.write("");
                rememberMeWriter.close();
            } catch (IOException ignored) {}
        } catch (IOException ignored) {}

        if (user.isLoggedIn()) {
            HBox.setMargin(loginBtn, Insets.EMPTY);
            loginBtn.setVisible(false);
            loginBtn.setManaged(false);
            HBox.setMargin(registerBtn, Insets.EMPTY);
            registerBtn.setVisible(false);
            registerBtn.setManaged(false);
            HBox.setMargin(logoutBtn, Insets.EMPTY);
            logoutBtn.setVisible(true);
            logoutBtn.setManaged(true);
            if (user.isAdmin()) {
                HBox.setMargin(homeBtn, new Insets(0, 10, 0, 0));
                adminPanelBtn.setVisible(true);
                adminPanelBtn.setManaged(true);
                eventsBtn.setVisible(true);
                eventsBtn.setManaged(true);
            } else if (user.isOrganizator()) {
                HBox.setMargin(homeBtn, new Insets(0, 10, 0, 0));
                eventsBtn.setVisible(true);
                eventsBtn.setManaged(true);
                adminPanelBtn.setVisible(false);
                adminPanelBtn.setManaged(false);
            } else {
                HBox.setMargin(homeBtn, Insets.EMPTY);
                adminPanelBtn.setVisible(false);
                adminPanelBtn.setManaged(false);
            }
            setButtonVisibility(true);
        } else {
            HBox.setMargin(loginBtn, new Insets(0, 10, 0, 0));
            loginBtn.setVisible(true);
            loginBtn.setManaged(true);
            HBox.setMargin(registerBtn, Insets.EMPTY);
            registerBtn.setVisible(true);
            registerBtn.setManaged(true);
            HBox.setMargin(logoutBtn, Insets.EMPTY);
            logoutBtn.setVisible(false);
            logoutBtn.setManaged(false);

            HBox.setMargin(homeBtn, Insets.EMPTY);
            adminPanelBtn.setVisible(false);
            adminPanelBtn.setManaged(false);
            eventsBtn.setVisible(false);
            eventsBtn.setManaged(false);

            setButtonVisibility(false);
        }
    }

    public void logout(ActionEvent event) throws IOException {
        user = new User();
        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
