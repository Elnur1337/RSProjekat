package rs.app.rsprojekat.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import rs.app.rsprojekat.model.Location;
import rs.app.rsprojekat.model.Subcategory;
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
import java.util.concurrent.Callable;

public class OrganizerController implements Initializable {
    private static User user = new User();
    private String msg;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Long eventsNumberLong;
    @FXML
    private Button homeBtn;
    @FXML
    private Label eventsNumber;

    @FXML
    private Pagination eventsPagination;
    @FXML
    private VBox newEventPanel;

    public void switchToHomeScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent ignoredEvent) throws IOException {
        final IndexController indexController = new IndexController();
        indexController.setCurrentUser(new User());

        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();
        homeBtn.fire();
    }

    private void refreshEventsNumber() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE organizator = :korisnik", Long.class);
        query.setParameter("korisnik", IndexController.getCurrentUser());
        eventsNumberLong = query.getSingleResult();

        eventsNumber.setText(eventsNumberLong.toString());

        entityManager.close();
        entityManagerFactory.close();
    }

    private VBox loadMyEvents(int pageIndex) {
        System.out.println("Events panel!");
        return null;
    }

    private void refreshEventsPagination() {
        refreshEventsNumber();
        eventsPagination.setPageCount(eventsNumberLong.intValue() / 6 + 1);
        eventsPagination.setPageFactory(this::loadMyEvents);
    }

    public void showMyEventsPanel() {
        newEventPanel.setVisible(false);
        newEventPanel.setManaged(false);
        eventsPagination.setVisible(true);
        eventsPagination.setManaged(true);
        refreshEventsPagination();
    }

    public void showNewEventPanel(MouseEvent mouseEvent) {
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        newEventPanel.setVisible(true);
        newEventPanel.setManaged(true);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        user = IndexController.getCurrentUser();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE organizator = :korisnik", Long.class);
        query.setParameter("korisnik", IndexController.getCurrentUser());
        eventsNumberLong = query.getSingleResult();
        entityManager.close();
        entityManagerFactory.close();
    }


    public void addEventImage(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odabir slike za dogadjaj");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        System.out.println(selectedFile.toURI().toString());

        Dogadjaj dogadjaj = new Dogadjaj();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Integer> query2 = entityManager.createQuery("SELECT MAX(id) FROM Dogadjaj", Integer.class);
        int id;
        if(query2.getSingleResult() == null)
            id = 1;
        else
            id = query2.getSingleResult() + 1;

        File currentDirFile = new File(".");
        String targetPath = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length() - 1);
        targetPath += "\\src\\main\\resources\\rs\\app\\rsprojekat\\eventImages";
        Path targetDirectory = Paths.get(targetPath).toAbsolutePath();
        Path destinationPath = targetDirectory.resolve(String.format("%d.jpg", id));

        Files.copy(selectedFile.toPath(), destinationPath);
        System.out.println("File copied to: " + destinationPath);

        String extension = destinationPath.toString().substring(destinationPath.toString().lastIndexOf(".") + 1);
        dogadjaj.setImgPath(String.format("@eventImages/%d.%s", id, extension));
    }
}