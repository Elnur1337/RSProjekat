package rs.app.rsprojekat.controller;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
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
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Pagination usersPagination;

    @FXML
    private Label usersReqNumber;

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

    public void showUserPanel() {
        usersPagination.setPageFactory(this::getUserPanel);
        //Posle dodati da se ovaj prikaze a svi ostali sakriju
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE approved = 0", Long.class);
        usersReqNumber.setText(query.getSingleResult().toString());
        usersPagination.setPageCount(query.getSingleResult().intValue() / 5 + 1);

        //Posle zatvoriti entityManager i factory

        usersPagination.setPageFactory(this::getUserPanel);
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
                //I ova opcija radi ali ne dobijamo uvijek 5 novih na prvoj stranici ako ima vise od 5 nego se samo brisu
                //Ovo je vise optimizovano nego rjesenje ispod ali ne daje lijep efekat
//                userBox.setVisible(false);
//                userBox.setManaged(false);

                try {
                    final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/adminPanel.fxml").toUri().toURL();
                    root = FXMLLoader.load(url);
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ignored) {}
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
                try {
                    final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/adminPanel.fxml").toUri().toURL();
                    root = FXMLLoader.load(url);
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ignored) {}
            });

            buttonsVBox.getChildren().addAll(approveBtn, buttonsRegion, rejectBtn);

            userBox.getChildren().addAll(firstAndLastNameVBox, emailAndOrganizatorVBox, birthDateVBox, userBoxRegion, buttonsVBox);
            page.getChildren().add(userBox);
        }
        return page;
    }
}
