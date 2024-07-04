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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import rs.app.rsprojekat.model.User;
import rs.app.rsprojekat.model.Dogadjaj;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrganizerController implements Initializable {
    private static User user = new User();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button homeBtn;
    @FXML
    private Button logoutBtn;

    @FXML
    private Pagination eventsPagination;

    @FXML
    private Label eventsNumber;

    public void switchToHomeScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent event) throws IOException {
        final IndexController indexController = new IndexController();
        indexController.setCurrentUser(new User());

        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();
        // switchToHomeScene()?
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void showMyEvents() {
        eventsPagination.setPageFactory(this::getEventsPanel);
    }

    private VBox getEventsPanel(int pageIndex) {
        System.out.println("Events panel!");
        return null;
    }

    public void makeNewEvent(MouseEvent mouseEvent) {
        newEvent(0);
    }

    private VBox newEvent(int pageIndex) {
        System.out.println("Make new event!");
        return null;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE approved = 0", Long.class);
        eventsNumber.setText(query.getSingleResult().toString());
        eventsPagination.setPageCount(query.getSingleResult().intValue() / 5 + 1);
        eventsPagination.setPageFactory(this::getEventsPanel);
    }
}