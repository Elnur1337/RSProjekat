package rs.app.rsprojekat.controller;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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

public class IndexController implements Initializable {
    private static User user = new User();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Long numOfEvents;
    private String selektovanoMjesto;
    private String selektovanaLokacija;

    private List<Dogadjaj> eventsList = new ArrayList<>();
    private List<Dogadjaj> showList = new ArrayList<>();

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
    private Button walletBtn;
    @FXML
    private Button filtersBtn;
    @FXML
    private Button clearFiltersBtn;
    @FXML
    private Button rezervacijaBtn;
    @FXML
    private Button kupovinaBtn;

    @FXML
    private Pagination eventsPagination;

    @FXML
    private VBox centralBox;
    @FXML
    private VBox resultsBox;
    @FXML
    private VBox filtersBox;

    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private ComboBox<String> subCategoryBox;
    @FXML
    private ComboBox<String> placeBox;
    @FXML
    private ComboBox<String> locationBox;
    @FXML
    private ComboBox<String> sektorBox;

    @FXML
    private Pane coverPane;
    @FXML
    private Pane eventPane;

    @FXML
    private TextField pretragaTextField;
    @FXML
    private TextField bottomPriceInput;
    @FXML
    private TextField upperPriceInput;
    @FXML
    private TextField searchInput;
    @FXML
    private TextField nazivShow;
    @FXML
    private TextArea opisShow;
    @FXML
    private TextField startDateShow;
    @FXML
    private TextField endDateShow;
    @FXML
    private TextField mjestoShow;
    @FXML
    private TextField lokacijaShow;
    @FXML
    private TextField cijenaShow;
    @FXML
    private TextField slobodnoMjestaShow;
    @FXML
    private TextField organizatorShow;
    @FXML
    private TextField brojKarataInput;


    private void setButtonVisibility(boolean visibility) {
        walletBtn.setVisible(visibility);
        walletBtn.setManaged(visibility);
        rezervacijaBtn.setVisible(visibility);
        rezervacijaBtn.setManaged(visibility);
        kupovinaBtn.setVisible(visibility);
        kupovinaBtn.setManaged(visibility);
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

    public void switchToOrganizerScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/organizer.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToWalletScene(ActionEvent event) throws IOException {
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/profile.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void refreshWallet() {
        walletBtn.setText("Profil: " + user.getWallet() + "KM");
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
            HBox.setMargin(walletBtn, new Insets(0, 10, 0, 0));
            if (user.isAdmin()) {
                HBox.setMargin(homeBtn, new Insets(0, 10, 0, 0));
                HBox.setMargin(adminPanelBtn, new Insets(0, 10, 0, 0));
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
            refreshWallet();
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

        bottomPriceInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    bottomPriceInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        upperPriceInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    upperPriceInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        brojKarataInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    brojKarataInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        eventsList = new ArrayList<>();
        TypedQuery<Dogadjaj> query = entityManager.createQuery("SELECT d FROM Dogadjaj d WHERE available = true AND approved = true", Dogadjaj.class);
        try  {
            eventsList = query.getResultList();
        } catch (NoResultException ignored) {}

        filtersBox.setTranslateX(320);
        coverPane.setTranslateX(-1200);
        filtersBtn.setOnAction(event -> {
            if (filtersBox.getTranslateX() != 0) {
                showMenu(filtersBox);
                placeBox.setPromptText("Mjesto");
                locationBox.setPromptText("Lokacija");
                loadPlaces();
            } else {
                hideFilters();
            }
        });

        hideEventInfo();

        loadCategoryBox();
        loadSubCategoryBox();
        refreshEventsPagination();
    }

    private void loadCategoryBox() {
        categoryBox.getItems().clear();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c", Category.class);
        List<Category> categories = query.getResultList();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getNaziv());
        }

        categoryBox.getItems().setAll(categoryNames);
    }

    private void loadSubCategoryBox() {
        subCategoryBox.getItems().clear();
        if(categoryBox.getSelectionModel().getSelectedItem() != null) {

            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            TypedQuery<Subcategory> query = entityManager.createQuery("SELECT s FROM Subcategory s WHERE kategorija.naziv = :naziv", Subcategory.class);
            query.setParameter("naziv", categoryBox.getValue());
            List<Subcategory> subcategories = query.getResultList();
            List<String> subCategoryNames = new ArrayList<>();
            for (Subcategory subcategory : subcategories) {
                subCategoryNames.add(subcategory.getNaziv());
            }

            subCategoryBox.getItems().setAll(subCategoryNames);
        }
    }

    private void loadPlaces() {
        placeBox.getItems().clear();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Place> query = entityManager.createQuery("SELECT p FROM Place p", Place.class);
        List<Place> places = query.getResultList();
        List<String> placeNames = new ArrayList<>();
        for (Place place : places) {
            placeNames.add(place.getNaziv());
        }

        placeBox.getItems().setAll(placeNames);
    }

    private void loadLocations() {
        locationBox.getItems().clear();
        if(placeBox.getSelectionModel().getSelectedItem() != null) {

            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            TypedQuery<Location> query = entityManager.createQuery("SELECT l FROM Location l WHERE mjesto.naziv = :naziv", Location.class);
            query.setParameter("naziv", placeBox.getValue());
            List<Location> locations = query.getResultList();
            List<String> locationNames = new ArrayList<>();
            for (Location location : locations) {
                locationNames.add(location.getNaziv());
            }

            locationBox.getItems().setAll(locationNames);
        }
    }

    public void loadLocations(ActionEvent event) { loadLocations(); }

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

//    @FXML
//    private void pretraga() {
//        String uneseniTekst = pretragaTextField.getText();
//        String criteria = criteriaComboBox.getValue();
//
//        if (uneseniTekst == null || criteria == null) {
//            System.err.println("Neispravan unos");
//            return;
//        }
//
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//        List<Dogadjaj> result = new ArrayList<>();
//
//        try {
//            if ("Naziv".equals(criteria)) {
//                TypedQuery<Dogadjaj> query = entityManager.createQuery("SELECT d FROM Dogadjaj d WHERE d.naziv LIKE :tekst", Dogadjaj.class);
//                query.setParameter("tekst", "%" + uneseniTekst + "%");
//                result = query.getResultList();
//            } else if ("Cijena".equals(criteria)) {
//              /*  TypedQuery<Dogadjaj> query = entityManager.createQuery("SELECT d FROM Dogadjaj d WHERE d.cijena LIKE :tekst", Dogadjaj.class);
//                query.setParameter("tekst", "%" + uneseniTekst + "%");
//                result = query.getResultList();
//                */
//
//            } else if ("all".equals(criteria)) {
//                TypedQuery<Dogadjaj> query = entityManager.createQuery("SELECT d FROM Dogadjaj d", Dogadjaj.class);
//                result = query.getResultList();
//            }
//
//            centralBox.getChildren().clear();
//
//            // VBox
//            VBox resultsBox = new VBox();
//            resultsBox.setSpacing(20);
//            resultsBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px; -fx-border-color: #ddd; -fx-border-width: 1px; -fx-border-radius: 5px;");
//
//            for (Dogadjaj event : result) {
//                VBox box = new VBox();
//                box.setSpacing(10);
//                box.setMinWidth(600);
//                box.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 3px;");
//
//                Label nazivLabel = new Label(event.getNaziv());
//                nazivLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
//
//                Label opisLabel = new Label(event.getOpis());
//                opisLabel.setStyle("-fx-font-size: 14px;");
//
//                box.getChildren().addAll(nazivLabel, opisLabel);
//
//                resultsBox.getChildren().add(box);
//            }
//
//            // Create ScrollPane
//            ScrollPane scrollPane = new ScrollPane();
//            scrollPane.setContent(resultsBox);
//            scrollPane.setFitToWidth(true);
//            scrollPane.setFitToHeight(true);
//            scrollPane.setStyle("-fx-background-color: transparent; -fx-min-width: 700px; -fx-min-height: 600px;"); // Adjust minimum width and height as needed
//
//            // Add ScrollPane
//            centralBox.getChildren().add(scrollPane);
//            VBox.setVgrow(scrollPane, Priority.ALWAYS);
//
//        } catch (Exception e) {
//            System.err.println("Greska: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            entityManager.close();
//            entityManagerFactory.close();
//        }
//    }

    private VBox getEvents(int pageIndex) {
        VBox page = new VBox();
        page.setPrefWidth(eventsPagination.getPrefWidth());
        page.setStyle("-fx-padding: 10;");

        int fromIndex = pageIndex * 5;
        int toIndex = Math.min(fromIndex + 5, showList.size());

        for (Dogadjaj d : showList.subList(fromIndex, toIndex)) {
            System.out.println(d);
            HBox dogadjajBox = new HBox();
            dogadjajBox.setAlignment(Pos.CENTER_LEFT);
            dogadjajBox.setPrefHeight(110.0);
            dogadjajBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #666; -fx-background-radius: 20; -fx-padding: 10;");
            VBox.setMargin(dogadjajBox, new Insets(0, 0, 10, 0));

            VBox titleAndDateVBox = new VBox();
            titleAndDateVBox.setPrefWidth(200.0);

            Label title = new Label();
            title.setText("Naziv: " + d.getNaziv());
            title.setTextFill(Color.WHITE);
            title.setFont(Font.font("SansSerif Regular", 18.0));

            Region titleAndDateRegion = new Region();
            VBox.setVgrow(titleAndDateRegion, Priority.ALWAYS);

            Label date = new Label();
            LocalDateTime time = d.getStartDate().toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDateTime = time.format(formatter);
            date.setText("Vrijeme: " + formattedDateTime);
            date.setTextFill(Color.WHITE);
            date.setFont(Font.font("SansSerif Regular", 18.0));

            titleAndDateVBox.getChildren().addAll(title, titleAndDateRegion, date);

            VBox placeAndLocationVBox = new VBox();
            placeAndLocationVBox.setPrefWidth(300.0);

            Label place = new Label();
            place.setText("Mjesto: " + d.getLokacija().getMjesto().getNaziv());
            place.setTextFill(Color.WHITE);
            place.setFont(Font.font("SansSerif Regular", 18.0));

            Region placeAndLocationRegion = new Region();
            VBox.setVgrow(placeAndLocationRegion, Priority.ALWAYS);

            Label location = new Label();
            location.setText("Lokacija: " + d.getLokacija().getNaziv());
            location.setTextFill(Color.WHITE);
            location.setFont(Font.font("SansSerif Regular", 18.0));

            placeAndLocationVBox.getChildren().addAll(place, placeAndLocationRegion, location);

            VBox imageBox = new VBox();
            imageBox.setPrefWidth(250.0);

            ImageView image = new ImageView();
            image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("events.png"))));

            imageBox.getChildren().add(image);

            dogadjajBox.getChildren().addAll(titleAndDateVBox, placeAndLocationVBox, imageBox);
            dogadjajBox.setOnMouseClicked(event -> showEventInfo(d));
            page.getChildren().add(dogadjajBox);
        }
        return page;
    }

    private void showEventInfo(Dogadjaj d) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), eventPane);
        slide.setToY(0);
        slide.play();
        slide = new TranslateTransition(Duration.millis(300), coverPane);
        slide.setToX(0);
        slide.play();

        nazivShow.setText(d.getNaziv());
        opisShow.setText(d.getOpis());
        LocalDateTime time = d.getStartDate().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");
        String formattedDateTime = time.format(formatter);
        startDateShow.setText(formattedDateTime);
        time = d.getEndDate().toLocalDateTime();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");
        formattedDateTime = time.format(formatter);
        endDateShow.setText(formattedDateTime);
        mjestoShow.setText(d.getLokacija().getMjesto().getNaziv());
        lokacijaShow.setText(d.getLokacija().getNaziv());
        organizatorShow.setText(d.getOrganizator().getIme() + " " + d.getOrganizator().getPrezime());

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Sector> query = entityManager.createQuery("SELECT DISTINCT(s) FROM Dogadjaj d JOIN Sector s ON s.lokacija = d.lokacija WHERE d.id = :idInput", Sector.class);
        query.setParameter("idInput", d.getId());
        List<Sector> sectors = query.getResultList();
        entityManager.close();
        entityManagerFactory.close();

        sektorBox.getItems().setAll(sectors.stream().map(Sector::getNaziv).toList());
        sektorBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
                    final EntityManager entityManager = entityManagerFactory.createEntityManager();
                    TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(*) FROM Ticket t JOIN Dogadjaj d ON t.dogadjaj = d JOIN Seat seat ON t.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE d.id = :idInput AND sector.naziv = :nazivInput AND (t.bought = false OR t.reserved = false)", Long.class);
                    query.setParameter("idInput", d.getId());
                    query.setParameter("nazivInput", newValue);

                    Long brojKarata = query.getSingleResult();
                    slobodnoMjestaShow.setText(String.valueOf(brojKarata));

                    TypedQuery<Double> query3 = entityManager.createQuery("SELECT DISTINCT(price) FROM Ticket t JOIN Dogadjaj d ON t.dogadjaj = d JOIN Seat seat ON t.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE d.id = :idInput AND sector.naziv = :nazivInput AND (t.bought = false OR t.reserved = false)", Double.class);
                    query3.setParameter("idInput", d.getId());
                    query3.setParameter("nazivInput", newValue);

                    Double cijena = query3.getSingleResult();
                    cijenaShow.setText(String.valueOf(cijena));

                    entityManager.close();
                    entityManagerFactory.close();
                }
            }
        });
    }

    @FXML
    private void hideEventInfo() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), eventPane);
        slide.setToY(-900);
        slide.play();
        slide = new TranslateTransition(Duration.millis(300), coverPane);
        slide.setToX(-1200);
        slide.play();
    }

    private void refreshEventsPagination() {
        refreshEventsPagination("");
    }

    private void refreshEventsPagination(String filters) {
        refreshNumOfEvents(filters);
        eventsPagination.setPageCount(numOfEvents.intValue() / 6 + 1);
        eventsPagination.setPageFactory(this::getEvents);
    }

    private void refreshNumOfEvents() {
        refreshNumOfEvents("");
    }

    private void refreshNumOfEvents(String filters) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d " + filters, Long.class);
        numOfEvents = query.getSingleResult();
    }

    private void showMenu(VBox menu) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), menu);
        slide.setToX(0);
        slide.play();
        slide = new TranslateTransition(Duration.millis(300), coverPane);
        slide.setToX(0);
        slide.play();

        System.out.println("Selektovano mjesto: " + selektovanoMjesto);
        System.out.println("Selektovana lokacija: " + selektovanaLokacija);

        if(selektovanoMjesto == null)
            placeBox.setPromptText("Mjesto");
        else
            placeBox.setValue(selektovanoMjesto);

        if(selektovanaLokacija == null)
            locationBox.setPromptText("Lokacija");
        else
            locationBox.setValue(selektovanaLokacija);

        if(bottomPriceInput.getText().isEmpty())
            bottomPriceInput.setPromptText("od");

        if(upperPriceInput.getText().isEmpty())
            upperPriceInput.setPromptText("do");
    }

    private void hideFilters() {
        if(filtersBox.getTranslateX() != 320) {
            TranslateTransition slide = new TranslateTransition(Duration.millis(300), filtersBox);
            slide.setToX(320);
            slide.play();
        }
        if(coverPane.getTranslateX() != -1200) {
            TranslateTransition slide = new TranslateTransition(Duration.millis(300), coverPane);
            slide.setToX(-1200);
            slide.play();
        }
    }

    public void hideMenu(MouseEvent mouseEvent) {
        hideFilters();
        hideEventInfo();
    }

    public void applyFilters(ActionEvent actionEvent) {
        if(categoryBox.getSelectionModel().getSelectedItem() == null)
            showList = eventsList.stream().toList();
        else if(subCategoryBox.getSelectionModel().getSelectedItem() != null)
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getNaziv().equals(subCategoryBox.getSelectionModel().getSelectedItem())).toList();
        else
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getKategorija().getNaziv().equals(categoryBox.getSelectionModel().getSelectedItem())).toList();

        String selectedPlace = placeBox.getSelectionModel().getSelectedItem();
        if(selectedPlace != null) {
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getLokacija().getMjesto().getNaziv().equals(selectedPlace)).toList();
            selektovanoMjesto = selectedPlace;
        }

        String selectedLocation = locationBox.getSelectionModel().getSelectedItem();
        if(selectedLocation != null) {
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getLokacija().getNaziv().equals(selectedLocation)).toList();
            selektovanaLokacija = selectedLocation;
        }

        if(!bottomPriceInput.getText().isEmpty()) {
            Double bottomPrice = Double.parseDouble(bottomPriceInput.getText());
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getBasePrice() >= bottomPrice).toList();
        }

        if(!upperPriceInput.getText().isEmpty()) {
            Double upperPrice = Double.parseDouble(upperPriceInput.getText());
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getBasePrice() <= upperPrice).toList();
        }

        hideFilters();
        clearFiltersBtn.setVisible(true);
        refreshEventsPagination();
    }

    public void clearFilters(ActionEvent actionEvent) {
        selektovanoMjesto = selektovanaLokacija = null;

        placeBox.getSelectionModel().clearSelection();
        placeBox.setPromptText("Mjesto");
        locationBox.getSelectionModel().clearSelection();
        locationBox.setPromptText("Lokacija");
        bottomPriceInput.clear();
        bottomPriceInput.setPromptText("od");
        upperPriceInput.clear();
        upperPriceInput.setPromptText("do");

        if(categoryBox.getSelectionModel().getSelectedItem() == null)
            showList = new ArrayList<>();
        else if(subCategoryBox.getSelectionModel().getSelectedItem() != null)
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getNaziv().equals(subCategoryBox.getSelectionModel().getSelectedItem())).toList();
        else
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getKategorija().getNaziv().equals(categoryBox.getSelectionModel().getSelectedItem())).toList();

        hideFilters();
        clearFiltersBtn.setVisible(false);
        refreshEventsPagination();
    }

    public void changedCategory(ActionEvent actionEvent) {
        loadSubCategoryBox();

        String item = categoryBox.getSelectionModel().getSelectedItem();
        if(item != null)
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getKategorija().getNaziv().equals(item)).toList();

        placeBox.setPromptText("Mjesto");
        locationBox.setPromptText("Lokacija");
        bottomPriceInput.setPromptText("od");
        upperPriceInput.setPromptText("do");

        refreshEventsPagination();
    }

    public void changedSubCategory(ActionEvent actionEvent) {
        String item = subCategoryBox.getSelectionModel().getSelectedItem();
        if(item != null)
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getNaziv().equals(item)).toList();

        placeBox.setPromptText("Mjesto");
        locationBox.setPromptText("Lokacija");
        bottomPriceInput.setPromptText("od");
        upperPriceInput.setPromptText("do");

        refreshEventsPagination();
    }

    public void searchEvents(ActionEvent actionEvent) {
        if(!showList.isEmpty())
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getNaziv().contains(searchInput.getText())).toList();
        else
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getNaziv().contains(searchInput.getText())).toList();

        refreshEventsPagination();
    }

    public void rezervacija(ActionEvent actionEvent) {
        System.out.println("Rezervacija");
    }

    public void kupovina(ActionEvent actionEvent) {
        System.out.println("Kupovina");
    }
}
