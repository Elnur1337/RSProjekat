package rs.app.rsprojekat.controller;

import javafx.animation.PauseTransition;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import rs.app.rsprojekat.model.Place;
import rs.app.rsprojekat.model.User;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    private String msg;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button homeBtn;

    @FXML
    private Pagination usersPagination;
    @FXML
    private Label usersReqNumber;
    private Long usersReqNumberLong;

    @FXML
    private VBox placePanel;
    @FXML
    private TextField nazivPlaceInput;
    @FXML
    private Label msgLabelPlace;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE approved = 0", Long.class);
        usersReqNumberLong = query.getSingleResult();

        entityManager.close();
        entityManagerFactory.close();
        showUserPanel();
    }

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

    private VBox getUserPanel(int pageIndex) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<User> userList = new ArrayList<>();
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE approved = 0", User.class).setFirstResult(pageIndex * 5).setMaxResults(5);
        try  {
            userList = query.getResultList();
        } catch (NoResultException ignored) {}

        VBox page = new VBox();
        page.setPrefWidth(usersPagination.getPrefWidth());
        page.setStyle("-fx-padding: 10;");

        for (User u : userList) {
            HBox userBox = new HBox();
            userBox.setAlignment(Pos.CENTER_LEFT);
            userBox.setPrefHeight(110.0);
            userBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #666; -fx-background-radius: 20; -fx-padding: 10;");
            VBox.setMargin(userBox, new Insets(0, 0, 10, 0));

            VBox firstAndLastNameVBox = new VBox();
            firstAndLastNameVBox.setPrefWidth(200.0);

            Label firstName = new Label();
            firstName.setText("Ime: " + u.getIme());
            firstName.setTextFill(Color.WHITE);
            firstName.setFont(Font.font("SansSerif Regular", 18.0));

            Region firstAndLastNameRegion = new Region();
            VBox.setVgrow(firstAndLastNameRegion, Priority.ALWAYS);

            Label lastName = new Label();
            lastName.setText("Prezime: " + u.getPrezime());
            lastName.setTextFill(Color.WHITE);
            lastName.setFont(Font.font("SansSerif Regular", 18.0));

            firstAndLastNameVBox.getChildren().addAll(firstName, firstAndLastNameRegion, lastName);


            VBox emailAndOrganizatorVBox = new VBox();
            emailAndOrganizatorVBox.setPrefWidth(300.0);

            Label email = new Label();
            email.setText("Email: " + u.getEmail());
            email.setTextFill(Color.WHITE);
            email.setFont(Font.font("SansSerif Regular", 18.0));

            Region emailAndOrganizatorRegion = new Region();
            VBox.setVgrow(emailAndOrganizatorRegion, Priority.ALWAYS);

            Label organizator = new Label();
            if (u.isOrganizator()) {
                organizator.setText("Organizator: Da");
            } else {
                organizator.setText("Organizator: Ne");
            }
            organizator.setTextFill(Color.WHITE);
            organizator.setFont(Font.font("SansSerif Regular", 18.0));

            emailAndOrganizatorVBox.getChildren().addAll(email, emailAndOrganizatorRegion, organizator);


            VBox birthDateVBox = new VBox();
            birthDateVBox.setPrefWidth(250.0);

            Label birthDate = new Label();
            birthDate.setText("Datum rođenja: " + u.getDatumRod());
            birthDate.setTextFill(Color.WHITE);
            birthDate.setFont(Font.font("SansSerif Regular", 18.0));

            birthDateVBox.getChildren().add(birthDate);


            Region userBoxRegion = new Region();
            HBox.setHgrow(userBoxRegion, Priority.ALWAYS);


            VBox buttonsVBox = new VBox();
            buttonsVBox.setPrefWidth(100.0);
            buttonsVBox.setAlignment(Pos.CENTER);

            Button approveBtn = new Button();
            approveBtn.setMnemonicParsing(false);
            approveBtn.setPrefWidth(100.0);
            approveBtn.setStyle("-fx-background-color: #107811; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            approveBtn.setTextFill(Color.WHITE);
            approveBtn.setText("Prihvati");
            approveBtn.setFont(Font.font("SansSerif Bold", 18.0));
            approveBtn.setCursor(Cursor.HAND);
            approveBtn.setOnAction(event -> {
                User user = entityManager.find(User.class, u.getId());
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                user.setApproved(true);
                entityTransaction.commit();
                --usersReqNumberLong;
                refreshUsersPagination();
            });

            Region buttonsRegion = new Region();
            VBox.setVgrow(buttonsRegion, Priority.ALWAYS);

            Button rejectBtn = new Button();
            rejectBtn.setMnemonicParsing(false);
            rejectBtn.setPrefWidth(100.0);
            rejectBtn.setStyle("-fx-background-color: #781510; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            rejectBtn.setTextFill(Color.WHITE);
            rejectBtn.setText("Odbij");
            rejectBtn.setFont(Font.font("SansSerif Bold", 18.0));
            rejectBtn.setCursor(Cursor.HAND);
            rejectBtn.setOnAction(event -> {
                User user = entityManager.find(User.class, u.getId());
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                entityManager.remove(user);
                entityTransaction.commit();
                refreshUsersPagination();
            });

            buttonsVBox.getChildren().addAll(approveBtn, buttonsRegion, rejectBtn);

            userBox.getChildren().addAll(firstAndLastNameVBox, emailAndOrganizatorVBox, birthDateVBox, userBoxRegion, buttonsVBox);
            page.getChildren().add(userBox);
        }
        return page;
    }

    private void refreshUsersPagination() {
        usersReqNumber.setText(usersReqNumberLong.toString());
        usersPagination.setPageCount(usersReqNumberLong.intValue() / 6 + 1);
        usersPagination.setPageFactory(this::getUserPanel);
    }

    public void showUserPanel() {
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        usersPagination.setVisible(true);
        usersPagination.setManaged(true);
        refreshUsersPagination();
    }

    public void showPlacePanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        placePanel.setVisible(true);
        placePanel.setManaged(true);
    }

    public void addPlace() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(event -> msgLabelPlace.setVisible(false));

        if (nazivPlaceInput.getText().length() < 2) {
            msg = "Naziv mora imate vise od 2 karaktera!";
            msgLabelPlace.setText(msg);
            msgLabelPlace.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelPlace.setVisible(true);
            visibleMsg.play();
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Place p WHERE naziv = :nazivInput", Long.class);
        query.setParameter("nazivInput", nazivPlaceInput.getText());
        if (query.getSingleResult().intValue() > 0) {
            msg = "Mjesto sa tim nazivom već postoji!";
            msgLabelPlace.setText(msg);
            msgLabelPlace.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelPlace.setVisible(true);
            visibleMsg.play();
            return;
        }
        Place place = new Place();
        place.setNaziv(nazivPlaceInput.getText());
        final EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(place);
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        msg = "Mjesto uspješno dodano!";
        msgLabelPlace.setText(msg);
        msgLabelPlace.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        msgLabelPlace.setVisible(true);
        visibleMsg.play();
    }
}
