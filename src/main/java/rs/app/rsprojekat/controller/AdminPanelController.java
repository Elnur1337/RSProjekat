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
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    private String msg;

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
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(event -> msgLabelLocation.setVisible(false));

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<String> placesList = new ArrayList<>();

        TypedQuery<String> query = entityManager.createQuery("SELECT p.naziv FROM Place p", String.class);
        try {
            placesList = query.getResultList();
        } catch (NoResultException e) {
            msg = "Prvo morate dodati mjesto!";
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
        }
        placeInput.getItems().addAll(placesList);
        entityManager.close();
        entityManagerFactory.close();
    }
    private void getCategoryNames (ComboBox<String> categoryInput) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
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
        usersPagination.setVisible(true);
        usersPagination.setManaged(true);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        refreshUsersPagination();
    }

    public void showPlacePanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(false);
        sectorPanel.setManaged(false);
        placePanel.setVisible(true);
        placePanel.setManaged(true);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
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
        categoryPanel.setVisible(true);
        categoryPanel.setManaged(true);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
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
        locationPanel.setVisible(true);
        locationPanel.setManaged(true);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        getPlacesNames(placeInput);
    }

    public void showSectorPanel() {
        usersPagination.setVisible(false);
        usersPagination.setManaged(false);
        placePanel.setVisible(false);
        placePanel.setManaged(false);
        locationPanel.setVisible(false);
        locationPanel.setManaged(false);
        sectorPanel.setVisible(true);
        sectorPanel.setManaged(true);
        categoryPanel.setVisible(false);
        categoryPanel.setManaged(false);
        subcategoryPanel.setVisible(false);
        subcategoryPanel.setManaged(false);
        getPlacesNames(sectorPlaceInput);
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
        nazivPlaceInput.setText("");
    }

    public void addImgPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odaberite sliku lokacije");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));

        final File selectedLocationImg = fileChooser.showOpenDialog(null);

        if (selectedLocationImg != null) {
            imgPathLocationInput.setText(selectedLocationImg.toURI().toString());
        }
    }

    public void addLocation() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(event -> msgLabelLocation.setVisible(false));

        if (nazivLocationInput.getText().length() < 2 || nazivLocationInput.getText().length() > 100) {
            msg = "Naziv mora biti između 2 i 100 karaktera!";
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
            return;
        }
        if (adresaLocationInput.getText().length() < 2 || adresaLocationInput.getText().length() > 100) {
            msg = "Adresa mora biti između 2 i 100 karaktera!";
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
            return;
        }
        if (imgPathLocationInput.getText().equals("Slika lokacije")) {
            msg = "Slika lokacije je obavezna!";
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Place> queryPlace = entityManager.createQuery("SELECT p FROM Place p WHERE naziv = :nazivInput", Place.class);
        queryPlace.setParameter("nazivInput", placeInput.getValue());
        final Place place = queryPlace.getSingleResult();

        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(l) FROM Location l WHERE naziv = :nazivInput AND mjesto = :mjestoInput", Long.class);
        query.setParameter("nazivInput", nazivLocationInput.getText());
        query.setParameter("mjestoInput", place);

        if (query.getSingleResult().intValue() > 0) {
            msg = "Lokacija sa tim nazivom već postoji!";
            msgLabelLocation.setText(msg);
            msgLabelLocation.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabelLocation.setVisible(true);
            visibleMsg.play();
            return;
        }

        Location location = new Location();
        location.setNaziv(nazivLocationInput.getText());
        location.setImgPath(imgPathLocationInput.getText());
        location.setAdresa(adresaLocationInput.getText());
        location.setMjesto(place);

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
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
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

        //  TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(s) FROM Subcategory s WHERE naziv = :nazivInput", Long.class);
        //   query.setParameter("nazivInput", nazivSubcategoryInput.getText());

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
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
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
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
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