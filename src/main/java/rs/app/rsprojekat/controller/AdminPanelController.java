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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import rs.app.rsprojekat.model.*;

import javax.persistence.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    private String msg;

    //Image veriables
    private File selectedImgFile;
    private String selectedImgExtension;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Location currSectorLocation;

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
    private VBox subcategoryPanel;
    @FXML
    private VBox categoryPanel;
    @FXML
    private TextField nazivPlaceInput;
    @FXML
    private Label msgLabelPlace;
    @FXML
    private Label msgLabelSubcategory;
    @FXML
    private Label msgLabelCategory;
    @FXML
    private VBox locationPanel;
    @FXML
    private ComboBox<String> placeInput;
    @FXML
    private TextField nazivLocationInput;
    @FXML
    private TextField nazivSubcategoryInput;
    @FXML
    private TextField nazivCategoryInput;

    @FXML
    private TextField adresaLocationInput;
    @FXML
    private TextField imgPathLocationInput;
    @FXML
    private Label msgLabelLocation;

    @FXML
    private VBox sectorPanel;
    @FXML
    private ComboBox<String> sectorPlaceInput;
    @FXML
    private ComboBox<String> categoryInput;
    @FXML
    private ComboBox<String> sectorLocationInput;
    @FXML
    private VBox sectorContainer;
    @FXML
    private TextField sectorNameInput;
    @FXML
    private TextField sectorCapacityInput;
    @FXML
    private Label msgLabelSector;
    @FXML
    private Pagination eventsPagination;
    @FXML
    private Label eventsReqNumber;
    private Long eventsReqNumberLong;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE approved = 0", Long.class);
        usersReqNumberLong = query.getSingleResult();

        TypedQuery<Long> eventsReqNumberQuery = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE approved = 0", Long.class);
        eventsReqNumberLong = eventsReqNumberQuery.getSingleResult();
        eventsReqNumber.setText(eventsReqNumberLong.toString());

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
                --usersReqNumberLong;
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

    private void getPlacesNames (ComboBox<String> placeInput) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabelLocation.setVisible(false));

        placeInput.getItems().clear();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<String> query = entityManager.createQuery("SELECT p.naziv FROM Place p", String.class);
        List<String> placesList= query.getResultList();

        placeInput.getItems().addAll(placesList);
        entityManager.close();
        entityManagerFactory.close();
    }
    private void getCategoryNames (ComboBox<String> categoryInput) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabelCategory.setVisible(false));

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<String> categoriesList = new ArrayList<>();

        TypedQuery<String> query = entityManager.createQuery("SELECT c.naziv FROM Category c", String.class);
        try {
            categoriesList = query.getResultList();
        } catch (NoResultException e) {
            msg = "Prvo morate dodati kategoriju!";
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
        }
        categoryInput.getItems().addAll(categoriesList);
        entityManager.close();
        entityManagerFactory.close();
    }

    public void showUserPanel() {
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        usersPagination.setVisible(true);
        usersPagination.setManaged(true);
        refreshUsersPagination();
    }

    public void showPlacePanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        placePanel.setVisible(true);
        placePanel.setManaged(true);
    }
    public void showCategoryPanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        categoryPanel.setVisible(true);
        categoryPanel.setManaged(true);
    }

    public void showSubcategoryPanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        subcategoryPanel.setVisible(true);
        subcategoryPanel.setManaged(true);
        getCategoryNames(categoryInput);
    }

    public void showLocationPanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        locationPanel.setVisible(true);
        locationPanel.setManaged(true);
        getPlacesNames(placeInput);
    }

    public void showSectorPanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        sectorPanel.setVisible(true);
        sectorPanel.setManaged(true);
        getPlacesNames(sectorPlaceInput);
    }

    public void showEventPanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        eventsPagination.setVisible(true);
        eventsPagination.setManaged(true);
        refreshEventsPagination();
    }

    private VBox getEventPanel(int pageIndex) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Dogadjaj> eventsQeury = entityManager.createQuery("SELECT d FROM Dogadjaj d WHERE approved = 0", Dogadjaj.class).setFirstResult(pageIndex).setMaxResults(1);
        List<Dogadjaj> eventsList = eventsQeury.getResultList();

        VBox page = new VBox();
        page.setPrefWidth(eventsPagination.getPrefWidth());
        page.setStyle("-fx-padding: 10;");

        for (Dogadjaj event : eventsList) {
            HBox nazivHBox = new HBox();
            nazivHBox.setPrefWidth(page.getPrefWidth());
            nazivHBox.setPrefHeight(45.0);
            nazivHBox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(nazivHBox, new Insets(0, 0, 10, 0));

            Label nazivLabel = new Label("Naziv:");
            nazivLabel.setPrefWidth(62.0);
            nazivLabel.setPrefHeight(26.0);
            nazivLabel.setFont(Font.font("SansSerif Regular", 22.0));
            nazivLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(nazivLabel, new Insets(0, 15, 0, 0));

            Label nazivContainerLabel = new Label(event.getNaziv());
            nazivContainerLabel.setPrefWidth(834.0);
            nazivContainerLabel.setPrefHeight(42.0);
            nazivContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            nazivContainerLabel.setAlignment(Pos.CENTER_LEFT);
            nazivContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            nazivHBox.getChildren().addAll(nazivLabel, nazivContainerLabel);

            HBox pocetakKrajHBox = new HBox();
            pocetakKrajHBox.setPrefWidth(page.getPrefWidth());
            pocetakKrajHBox.setPrefHeight(45.0);
            pocetakKrajHBox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(pocetakKrajHBox, new Insets(0, 0, 10, 0));

            HBox pocetakHBox = new HBox();
            pocetakHBox.setPrefWidth(pocetakKrajHBox.getPrefWidth() / 2);
            pocetakHBox.setPrefHeight(45.0);
            pocetakHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(pocetakHBox, new Insets(0, 10, 0, 0));

            HBox krajHBox = new HBox();
            krajHBox.setPrefWidth(pocetakKrajHBox.getPrefWidth() / 2);
            krajHBox.setPrefHeight(45.0);
            krajHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(krajHBox, new Insets(0, 0, 0, 10));

            Label pocetakLabel = new Label("Početak:");
            pocetakLabel.setPrefWidth(87.0);
            pocetakLabel.setPrefHeight(26.0);
            pocetakLabel.setFont(Font.font("SansSerif Regular", 22.0));
            pocetakLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(pocetakLabel, new Insets(0, 15, 0, 0));

            Label pocetakContainerLabel = new Label(event.getStartDate().toString());
            pocetakContainerLabel.setPrefWidth(344.0);
            pocetakContainerLabel.setPrefHeight(42.0);
            pocetakContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            pocetakContainerLabel.setAlignment(Pos.CENTER_LEFT);
            pocetakContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            pocetakHBox.getChildren().addAll(pocetakLabel, pocetakContainerLabel);

            Label krajLabel = new Label("Kraj:");
            krajLabel.setPrefWidth(47.0);
            krajLabel.setPrefHeight(26.0);
            krajLabel.setFont(Font.font("SansSerif Regular", 22.0));
            krajLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(krajLabel, new Insets(0, 15, 0, 0));

            Label krajContainerLabel = new Label(event.getEndDate().toString());
            krajContainerLabel.setPrefWidth(386.0);
            krajContainerLabel.setPrefHeight(42.0);
            krajContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            krajContainerLabel.setAlignment(Pos.CENTER_LEFT);
            krajContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            krajHBox.getChildren().addAll(krajLabel, krajContainerLabel);

            pocetakKrajHBox.getChildren().addAll(pocetakHBox, krajHBox);

            HBox kategorijaPodkategorijaHBox = new HBox();
            kategorijaPodkategorijaHBox.setPrefWidth(page.getPrefWidth());
            kategorijaPodkategorijaHBox.setPrefHeight(45.0);
            kategorijaPodkategorijaHBox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(kategorijaPodkategorijaHBox, new Insets(0, 0, 10, 0));

            HBox kategorijaHBox = new HBox();
            kategorijaHBox.setPrefWidth(kategorijaPodkategorijaHBox.getPrefWidth() / 2);
            kategorijaHBox.setPrefHeight(45.0);
            kategorijaHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(kategorijaHBox, new Insets(0, 10, 0, 0));

            HBox podkategorijaHBox = new HBox();
            podkategorijaHBox.setPrefWidth(kategorijaPodkategorijaHBox.getPrefWidth() / 2);
            podkategorijaHBox.setPrefHeight(45.0);
            podkategorijaHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(podkategorijaHBox, new Insets(0, 0, 0, 10));

            Label kategorijaLabel = new Label("Kategorija:");
            kategorijaLabel.setPrefWidth(106.0);
            kategorijaLabel.setPrefHeight(26.0);
            kategorijaLabel.setFont(Font.font("SansSerif Regular", 22.0));
            kategorijaLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(kategorijaLabel, new Insets(0, 15, 0, 0));

            Label kategorijaContainerLabel = new Label(event.getPodkategorija().getKategorija().getNaziv());
            kategorijaContainerLabel.setPrefWidth(328.0);
            kategorijaContainerLabel.setPrefHeight(42.0);
            kategorijaContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            kategorijaContainerLabel.setAlignment(Pos.CENTER_LEFT);
            kategorijaContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            kategorijaHBox.getChildren().addAll(kategorijaLabel, kategorijaContainerLabel);

            Label podkategorijaLabel = new Label("Podkategorija:");
            podkategorijaLabel.setPrefWidth(256.0);
            podkategorijaLabel.setPrefHeight(26.0);
            podkategorijaLabel.setFont(Font.font("SansSerif Regular", 22.0));
            podkategorijaLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(podkategorijaLabel, new Insets(0, 15, 0, 0));

            Label podkategorijaContainerLabel = new Label(event.getPodkategorija().getNaziv());
            podkategorijaContainerLabel.setPrefWidth(386.0);
            podkategorijaContainerLabel.setPrefHeight(42.0);
            podkategorijaContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            podkategorijaContainerLabel.setAlignment(Pos.CENTER_LEFT);
            podkategorijaContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            podkategorijaHBox.getChildren().addAll(podkategorijaLabel, podkategorijaContainerLabel);

            kategorijaPodkategorijaHBox.getChildren().addAll(kategorijaHBox, podkategorijaHBox);

            HBox mjestoLokacijaHBox = new HBox();
            mjestoLokacijaHBox.setPrefWidth(page.getPrefWidth());
            mjestoLokacijaHBox.setPrefHeight(45.0);
            mjestoLokacijaHBox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(mjestoLokacijaHBox, new Insets(0, 0, 10, 0));

            HBox mjestoHBox = new HBox();
            mjestoHBox.setPrefWidth(pocetakKrajHBox.getPrefWidth() / 2);
            mjestoHBox.setPrefHeight(45.0);
            mjestoHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(mjestoHBox, new Insets(0, 10, 0, 0));

            HBox lokacijaHBox = new HBox();
            lokacijaHBox.setPrefWidth(pocetakKrajHBox.getPrefWidth() / 2);
            lokacijaHBox.setPrefHeight(45.0);
            lokacijaHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(lokacijaHBox, new Insets(0, 0, 0, 10));

            Label mjestoLabel = new Label("Mjesto:");
            mjestoLabel.setPrefWidth(73.0);
            mjestoLabel.setPrefHeight(26.0);
            mjestoLabel.setFont(Font.font("SansSerif Regular", 22.0));
            mjestoLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(mjestoLabel, new Insets(0, 15, 0, 0));

            Label mjestoContainerLabel = new Label(event.getLokacija().getMjesto().getNaziv());
            mjestoContainerLabel.setPrefWidth(361.0);
            mjestoContainerLabel.setPrefHeight(42.0);
            mjestoContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            mjestoContainerLabel.setAlignment(Pos.CENTER_LEFT);
            mjestoContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            mjestoHBox.getChildren().addAll(mjestoLabel, mjestoContainerLabel);

            Label lokacijaLabel = new Label("Lokacija:");
            lokacijaLabel.setPrefWidth(139.0);
            lokacijaLabel.setPrefHeight(26.0);
            lokacijaLabel.setFont(Font.font("SansSerif Regular", 22.0));
            lokacijaLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(lokacijaLabel, new Insets(0, 15, 0, 0));

            Label lokacijaContainerLabel = new Label(event.getLokacija().getNaziv());
            lokacijaContainerLabel.setPrefWidth(386.0);
            lokacijaContainerLabel.setPrefHeight(42.0);
            lokacijaContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            lokacijaContainerLabel.setAlignment(Pos.CENTER_LEFT);
            lokacijaContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            lokacijaHBox.getChildren().addAll(lokacijaLabel, lokacijaContainerLabel);

            mjestoLokacijaHBox.getChildren().addAll(mjestoHBox, lokacijaHBox);

            HBox organizatorCijenaHBox = new HBox();
            organizatorCijenaHBox.setPrefWidth(page.getPrefWidth());
            organizatorCijenaHBox.setPrefHeight(45.0);
            organizatorCijenaHBox.setAlignment(Pos.CENTER_LEFT);
            VBox.setMargin(organizatorCijenaHBox, new Insets(0, 0, 10, 0));

            HBox organizatorHBox = new HBox();
            organizatorHBox.setPrefWidth(organizatorCijenaHBox.getPrefWidth() / 2);
            organizatorHBox.setPrefHeight(45.0);
            organizatorHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(organizatorHBox, new Insets(0, 10, 0, 0));

            HBox cijenaHBox = new HBox();
            cijenaHBox.setPrefWidth(organizatorCijenaHBox.getPrefWidth() / 2);
            cijenaHBox.setPrefHeight(45.0);
            cijenaHBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(cijenaHBox, new Insets(0, 0, 0, 10));

            Label organizatorLabel = new Label("Organizator:");
            organizatorLabel.setPrefWidth(127.0);
            organizatorLabel.setPrefHeight(26.0);
            organizatorLabel.setFont(Font.font("SansSerif Regular", 22.0));
            organizatorLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(organizatorLabel, new Insets(0, 15, 0, 0));

            Label organizatorContainerLabel = new Label(event.getOrganizator().getIme() + " " + event.getOrganizator().getPrezime());
            organizatorContainerLabel.setPrefWidth(304.0);
            organizatorContainerLabel.setPrefHeight(42.0);
            organizatorContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            organizatorContainerLabel.setAlignment(Pos.CENTER_LEFT);
            organizatorContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            organizatorHBox.getChildren().addAll(organizatorLabel, organizatorContainerLabel);

            Label cijenaLabel = new Label("Cijena:");
            cijenaLabel.setPrefWidth(100.0);
            cijenaLabel.setPrefHeight(26.0);
            cijenaLabel.setFont(Font.font("SansSerif Regular", 22.0));
            cijenaLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(cijenaLabel, new Insets(0, 15, 0, 0));

            Label cijenaContainerLabel = new Label(event.getBasePrice() + "KM");
            cijenaContainerLabel.setPrefWidth(386.0);
            cijenaContainerLabel.setPrefHeight(42.0);
            cijenaContainerLabel.setFont(Font.font("SansSerif Regular", 22.0));
            cijenaContainerLabel.setAlignment(Pos.CENTER_LEFT);
            cijenaContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 50; -fx-border-radius: 50;");

            cijenaHBox.getChildren().addAll(cijenaLabel, cijenaContainerLabel);

            organizatorCijenaHBox.getChildren().addAll(organizatorHBox, cijenaHBox);

            VBox opisVBox = new VBox();
            opisVBox.setPrefWidth(page.getPrefWidth());
            opisVBox.setPrefHeight(182.0);
            opisVBox.setAlignment(Pos.TOP_LEFT);
            VBox.setMargin(opisVBox, new Insets(0, 0, 10, 0));

            Label opisLabel = new Label("Opis:");
            opisLabel.setPrefWidth(55.0);
            opisLabel.setPrefHeight(26.0);
            opisLabel.setFont(Font.font("SansSerif Regular", 22.0));
            opisLabel.setAlignment(Pos.CENTER_LEFT);
            HBox.setMargin(opisLabel, new Insets(0, 0, 10, 0));

            Label opisContainerLabel = new Label(event.getOpis());
            opisContainerLabel.setPrefWidth(908.0);
            opisContainerLabel.setPrefHeight(157.0);
            opisContainerLabel.setFont(Font.font("SansSerif Regular", 20.0));
            opisContainerLabel.setAlignment(Pos.TOP_LEFT);
            opisContainerLabel.setStyle("-fx-border-color: lightgray; -fx-padding: 7; -fx-background-radius: 20; -fx-border-radius: 20;");

            opisVBox.getChildren().addAll(opisLabel, opisContainerLabel);

            ImageView eventImageView = new ImageView();
            eventImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(event.getImgPath()))));
            eventImageView.setPickOnBounds(true);
            eventImageView.setPreserveRatio(true);
            eventImageView.setFitWidth(page.getPrefWidth());
            eventImageView.setFitHeight(150.0);
            VBox.setMargin(eventImageView, new Insets(0, 0, 10, 0));

            HBox buttonsHBox = new HBox();
            buttonsHBox.setPrefWidth(page.getPrefWidth());
            buttonsHBox.setPrefHeight(60.0);
            buttonsHBox.setAlignment(Pos.CENTER_RIGHT);

            Button approveBtn = new Button();
            approveBtn.setMnemonicParsing(false);
            approveBtn.setPrefWidth(100.0);
            approveBtn.setStyle("-fx-background-color: #107811; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            approveBtn.setTextFill(Color.WHITE);
            approveBtn.setText("Prihvati");
            approveBtn.setFont(Font.font("SansSerif Bold", 18.0));
            approveBtn.setCursor(Cursor.HAND);
            approveBtn.setOnAction(eventClick -> {
                Dogadjaj dogadjaj = entityManager.find(Dogadjaj.class, event.getId());
                final EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                dogadjaj.setApproved(true);
                dogadjaj.setAvailable(true);
                entityTransaction.commit();
                --eventsReqNumberLong;
                refreshEventsPagination();
            });
            HBox.setMargin(approveBtn, new Insets(0, 15, 0, 0));

            Button rejectBtn = new Button();
            rejectBtn.setMnemonicParsing(false);
            rejectBtn.setPrefWidth(100.0);
            rejectBtn.setStyle("-fx-background-color: #781510; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            rejectBtn.setTextFill(Color.WHITE);
            rejectBtn.setText("Odbij");
            rejectBtn.setFont(Font.font("SansSerif Bold", 18.0));
            rejectBtn.setCursor(Cursor.HAND);
            rejectBtn.setOnAction(eventClick -> {
                Path currDir = Paths.get("").toAbsolutePath();
                Path imgToDelete = currDir.resolve("src/main/resources/rs/app/rsprojekat/controller/" + event.getImgPath());
                try {
                    Files.delete(imgToDelete);
                } catch (IOException ignored) {}
                Dogadjaj dogadjaj = entityManager.find(Dogadjaj.class, event.getId());
                final EntityTransaction entityTransaction = entityManager.getTransaction();
                TypedQuery<Ticket> ticketsQuery = entityManager.createQuery("SELECT t FROM Ticket t WHERE t.dogadjaj = :dogadjajInput", Ticket.class);
                ticketsQuery.setParameter("dogadjajInput", event);
                List<Ticket> ticketsList = ticketsQuery.getResultList();
                entityTransaction.begin();
                for (Ticket ticket : ticketsList) {
                    entityManager.remove(ticket);
                }
                entityManager.remove(dogadjaj);
                entityTransaction.commit();
                --eventsReqNumberLong;
                refreshEventsPagination();
            });

            buttonsHBox.getChildren().addAll(approveBtn, rejectBtn);

            page.getChildren().addAll(nazivHBox, pocetakKrajHBox, kategorijaPodkategorijaHBox, mjestoLokacijaHBox, organizatorCijenaHBox, opisVBox, eventImageView, buttonsHBox);
        }
        return page;
    }

    private void refreshEventsPagination() {
        eventsReqNumber.setText(eventsReqNumberLong.toString());
        eventsPagination.setPageCount(eventsReqNumberLong.intValue() == 0 ? 1 : eventsReqNumberLong.intValue());
        eventsPagination.setPageFactory(this::getEventPanel);
    }

    public void addPlace() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
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
        nazivPlaceInput.setText("");
    }

    public void addImgPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odaberite sliku lokacije");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));

        selectedImgFile = fileChooser.showOpenDialog(null);
        selectedImgExtension = selectedImgFile.toURI().toString().substring(selectedImgFile.toURI().toString().lastIndexOf(".") + 1);
        imgPathLocationInput.setText(selectedImgFile.toPath().toString());
    }

    private boolean validateLocations() {
        if (nazivLocationInput.getText().length() < 2 || nazivLocationInput.getText().length() > 100) {
            msg = "Naziv mora biti između 2 i 100 karaktera!";
            return false;
        }
        if (adresaLocationInput.getText().length() < 2 || adresaLocationInput.getText().length() > 100) {
            msg = "Adresa mora biti između 2 i 100 karaktera!";
            return false;
        }
        if (imgPathLocationInput.getText().equals("Slika lokacije")) {
            msg = "Slika lokacije je obavezna!";
            return false;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Place> queryPlace = entityManager.createQuery("SELECT p FROM Place p WHERE naziv = :nazivInput", Place.class);
        queryPlace.setParameter("nazivInput", placeInput.getValue());
        final Place place = queryPlace.getSingleResult();

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(l) FROM Location l WHERE naziv = :nazivInput AND mjesto = :mjestoInput", Long.class);
        query.setParameter("nazivInput", nazivLocationInput.getText());
        query.setParameter("mjestoInput", place);
        final int numberOfLocationsWithInputName = query.getSingleResult().intValue();
        entityManager.close();
        entityManagerFactory.close();
        if (numberOfLocationsWithInputName > 0) {
            msg = "Lokacija sa tim nazivom već postoji!";
            return false;
        }
        return true;
    }

    public void addLocation() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabelLocation.setVisible(false));

        if (!validateLocations()) {
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Integer> maxLocationIdQuery = entityManager.createQuery("SELECT MAX(id) FROM Location", Integer.class);
        int id;
        if(maxLocationIdQuery.getSingleResult() == null)
            id = 1;
        else
            id = maxLocationIdQuery.getSingleResult() + 1;

        File currentDirFile = new File(".");
        String targetPath = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length() - 1);
        targetPath += "\\src\\main\\resources\\rs\\app\\rsprojekat\\controller";
        Path targetDirectory = Paths.get(targetPath).toAbsolutePath();
        Path destinationPath = targetDirectory.resolve(String.format("locationImageN%d.%s", id, selectedImgExtension));
        try {
            Files.copy(selectedImgFile.toPath(), destinationPath);
        } catch (IOException ignored) {}

        TypedQuery<Place> queryPlace = entityManager.createQuery("SELECT p FROM Place p WHERE naziv = :nazivInput", Place.class);
        queryPlace.setParameter("nazivInput", placeInput.getValue());

        Location location = new Location();
        location.setNaziv(nazivLocationInput.getText());
        location.setImgPath(String.format("locationImageN%d.%s", id, selectedImgExtension));
        location.setAdresa(adresaLocationInput.getText());
        location.setMjesto(queryPlace.getSingleResult());

        final EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(location);
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        msg = "Lokacija uspješno dodana!";
        msgLabelLocation.setText(msg);
        msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        msgLabelLocation.setVisible(true);
        visibleMsg.play();
        placeInput.setValue("");
        nazivLocationInput.setText("");
        adresaLocationInput.setText("");
        imgPathLocationInput.setText("Slika lokacije");
    }

    public void getSectorLocations() {
        sectorLocationInput.getItems().clear();
        sectorContainer.getChildren().clear();
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<String> query = entityManager.createQuery("SELECT l.naziv FROM Location l WHERE l.mjesto.naziv = :nazivInput", String.class);
        query.setParameter("nazivInput", sectorPlaceInput.getValue());

        List<String> locationsList = new ArrayList<>();
        try {
            locationsList = query.getResultList();
        } catch (NoResultException ignored) {
            System.out.println("Nema lokacija u ovom mjestu");
        }
        entityManager.close();
        entityManagerFactory.close();
        sectorLocationInput.getItems().addAll(locationsList);
    }

    public void getSectors() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Location> locationQuery = entityManager.createQuery("SELECT l FROM Location l WHERE l.naziv = :lokacijaNazivInput AND l.mjesto.naziv = :mjestoNazivInput", Location.class);
        locationQuery.setParameter("lokacijaNazivInput", sectorLocationInput.getValue());
        locationQuery.setParameter("mjestoNazivInput", sectorPlaceInput.getValue());

        currSectorLocation = locationQuery.getSingleResult();

        TypedQuery<Sector> sectorQuery = entityManager.createQuery("SELECT s FROM Sector s WHERE s.lokacija = :lokacijaInput", Sector.class);
        sectorQuery.setParameter("lokacijaInput", currSectorLocation);

        List<Sector> sectorsList = sectorQuery.getResultList();

        for (Sector sector : sectorsList) {
            HBox sectorHBox = new HBox();
            sectorHBox.setAlignment(Pos.CENTER_LEFT);
            sectorHBox.setPrefHeight(50.0);

            Label nazivLabel = new Label();
            nazivLabel.setPrefWidth(200.0);
            nazivLabel.setPrefHeight(29.0);
            nazivLabel.setFont(Font.font("SansSerif Regular", 18));
            nazivLabel.setStyle("-fx-padding: 5; -fx-border-color: #666; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            HBox.setMargin(nazivLabel, new Insets(0, 15, 0, 0));
            nazivLabel.setText(sector.getNaziv());

            Label kapacitetLabel = new Label();
            kapacitetLabel.setPrefWidth(80.0);
            kapacitetLabel.setPrefHeight(29.0);
            kapacitetLabel.setFont(Font.font("SansSerif Regular", 18));
            kapacitetLabel.setStyle("-fx-padding: 5; -fx-border-color: #666; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            HBox.setMargin(kapacitetLabel, new Insets(0, 15, 0, 0));
            kapacitetLabel.setText(Integer.toString(sector.getKapacitet()));

            HBox iconHBox = new HBox();
            iconHBox.setPrefWidth(60.0);
            iconHBox.setPrefHeight(39.0);
            iconHBox.setMaxHeight(39.0);
            iconHBox.setAlignment(Pos.CENTER);
            iconHBox.setStyle("-fx-padding: 5; -fx-border-color: #666; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            iconHBox.setCursor(Cursor.HAND);
            iconHBox.setOnMouseClicked(event -> {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                entityManager.remove(sector);
                entityTransaction.commit();
                sectorContainer.getChildren().remove(sectorHBox);
            });

            ImageView trashCanIcon = new ImageView();
            trashCanIcon.setPickOnBounds(true);
            trashCanIcon.setPreserveRatio(true);
            trashCanIcon.setFitWidth(18.0);
            trashCanIcon.setFitHeight(21.0);
            trashCanIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("trash-can-solid.png"))));

            iconHBox.getChildren().add(trashCanIcon);
            sectorHBox.getChildren().addAll(nazivLabel, kapacitetLabel, iconHBox);
            sectorContainer.getChildren().add(sectorHBox);
        }
    }


    public void addSubcategory() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabelSubcategory.setVisible(false));

        if (nazivSubcategoryInput.getText().length() < 2) {
            msg = "Naziv mora imate vise od 2 karaktera!";
            msgLabelSubcategory.setText(msg);
            msgLabelSubcategory.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelSubcategory.setVisible(true);
            visibleMsg.play();
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Category> queryCategory = entityManager.createQuery("SELECT c FROM Category c WHERE naziv = :nazivInput", Category.class);
        queryCategory.setParameter("nazivInput", categoryInput.getValue());
        final Category category = queryCategory.getSingleResult();

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(s) FROM Subcategory s WHERE naziv = :nazivInput AND kategorija = :categoryInput", Long.class);
        query.setParameter("nazivInput", nazivSubcategoryInput.getText());
        query.setParameter("categoryInput", category);

        TypedQuery<Long> queryQ = entityManager.createQuery("SELECT COUNT(s) FROM Subcategory s WHERE naziv = :nazivInput ", Long.class);
        queryQ.setParameter("nazivInput", nazivSubcategoryInput.getText());

        if (queryQ.getSingleResult().intValue() > 0) {
            msg = "Podkategorija sa tim nazivom već postoji!";
            msgLabelSubcategory.setText(msg);
            msgLabelSubcategory.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelSubcategory.setVisible(true);
            visibleMsg.play();
            return;
        }

        Subcategory c = new Subcategory();
        c.setNaziv(nazivSubcategoryInput.getText());
        c.setCategory(category);
        final EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(c);
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        msg = "Podkategorija uspješno dodana!";
        msgLabelSubcategory.setText(msg);
        msgLabelSubcategory.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        msgLabelSubcategory.setVisible(true);
        visibleMsg.play();
        nazivSubcategoryInput.setText("");
        categoryInput.setValue("");
    }

    public void addCategory() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabelCategory.setVisible(false));

        if (nazivCategoryInput.getText().length() < 2) {
            msg = "Naziv mora imate vise od 2 karaktera!";
            msgLabelCategory.setText(msg);
            msgLabelCategory.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelCategory.setVisible(true);
            visibleMsg.play();
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(c) FROM Category c WHERE naziv = :nazivInput", Long.class);
        query.setParameter("nazivInput", nazivCategoryInput.getText());
        if (query.getSingleResult().intValue() > 0) {
            msg = "Kategorija sa tim nazivom već postoji!";
            msgLabelCategory.setText(msg);
            msgLabelCategory.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelCategory.setVisible(true);
            visibleMsg.play();
            return;
        }
        Category c = new Category();
        c.setNaziv(nazivCategoryInput.getText());
        final EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(c);
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        msg = "Kategorija uspješno dodana!";
        msgLabelCategory.setText(msg);
        msgLabelCategory.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        msgLabelCategory.setVisible(true);
        visibleMsg.play();
        nazivCategoryInput.setText("");
    }

    public void addSector() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabelSector.setVisible(false));

        if (sectorNameInput.getText().length() < 2) {
            msg = "Naziv mora imate vise od 2 karaktera!";
            msgLabelSector.setText(msg);
            msgLabelSector.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelSector.setVisible(true);
            visibleMsg.play();
            return;
        }

        int sectorCapacity;
        try {
            sectorCapacity = Integer.parseInt(sectorCapacityInput.getText());
        } catch (NumberFormatException e) {
            msg = "Kapacitet mora biti cijeli broj!";
            msgLabelSector.setText(msg);
            msgLabelSector.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelSector.setVisible(true);
            visibleMsg.play();
            return;
        }
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        Sector sector = new Sector();
        sector.setNaziv(sectorNameInput.getText());
        sector.setKapacitet(sectorCapacity);
        sector.setLokacija(currSectorLocation);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(sector);

        for(int i = 1; i <= sectorCapacity; ++i) {
            Seat seat = new Seat();
            seat.setSector(sector);
            seat.setBrojSjedala(i);
            entityManager.persist(seat);
        }

        entityTransaction.commit();

        HBox sectorHBox = new HBox();
        sectorHBox.setAlignment(Pos.CENTER_LEFT);
        sectorHBox.setPrefHeight(50.0);

        Label nazivLabel = new Label();
        nazivLabel.setPrefWidth(200.0);
        nazivLabel.setPrefHeight(29.0);
        nazivLabel.setFont(Font.font("SansSerif Regular", 18));
        nazivLabel.setStyle("-fx-padding: 5; -fx-border-color: #666; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
        HBox.setMargin(nazivLabel, new Insets(0, 15, 0, 0));
        nazivLabel.setText(sector.getNaziv());

        Label kapacitetLabel = new Label();
        kapacitetLabel.setPrefWidth(80.0);
        kapacitetLabel.setPrefHeight(29.0);
        kapacitetLabel.setFont(Font.font("SansSerif Regular", 18));
        kapacitetLabel.setStyle("-fx-padding: 5; -fx-border-color: #666; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
        HBox.setMargin(kapacitetLabel, new Insets(0, 15, 0, 0));
        kapacitetLabel.setText(Integer.toString(sector.getKapacitet()));

        HBox iconHBox = new HBox();
        iconHBox.setPrefWidth(60.0);
        iconHBox.setPrefHeight(39.0);
        iconHBox.setMaxHeight(39.0);
        iconHBox.setAlignment(Pos.CENTER);
        iconHBox.setStyle("-fx-padding: 5; -fx-border-color: #666; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
        iconHBox.setCursor(Cursor.HAND);
        iconHBox.setOnMouseClicked(event -> {
            entityTransaction.begin();
            TypedQuery<Seat> query = entityManager.createQuery("SELECT s FROM Seat s WHERE s.sektor = :sektor", Seat.class);
            query.setParameter("sektor", sector);
            List<Seat> seats = query.getResultList();
            for(Seat seat : seats) {
                entityManager.remove(seat);
            }
            entityManager.remove(sector);
            entityTransaction.commit();
            sectorContainer.getChildren().remove(sectorHBox);
        });

        ImageView trashCanIcon = new ImageView();
        trashCanIcon.setPickOnBounds(true);
        trashCanIcon.setPreserveRatio(true);
        trashCanIcon.setFitWidth(18.0);
        trashCanIcon.setFitHeight(21.0);
        trashCanIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("trash-can-solid.png"))));

        iconHBox.getChildren().add(trashCanIcon);
        sectorHBox.getChildren().addAll(nazivLabel, kapacitetLabel, iconHBox);
        sectorContainer.getChildren().add(sectorHBox);

        msg = "Sektor uspješno dodan!";
        msgLabelSector.setText(msg);
        msgLabelSector.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        msgLabelSector.setVisible(true);
        visibleMsg.play();
        sectorNameInput.setText("");
        sectorCapacityInput.setText("");
    }

        //NAPRAVITI PROMJENE ZA SEKTOR NAPRAVITI DVA INPUTA NA DNU I DA SE TAKO DODAJE NOVI A GORE PROMIJENITI U LABELE I NA KLIK KANTE DA SE BRISE
        //Takodjer nakon promjene mjesta i lokacije ocistiti polje
        //Odraditi refactor koda (kreirati funkciju za message display)
        //Odraditi refactor koda (jedan objekat za insets a ne vise njih za svaki)
        //Prebaciti style u application.css? Ako bude radilo

}