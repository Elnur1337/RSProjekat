package rs.app.rsprojekat.controller;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

public class IndexController implements Initializable {
    private static User user = new User();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Long numOfEvents;
    private String selektovanoMjesto;
    private String selektovanaLokacija;
    private String msg;
    private boolean searched = false;
    private Dogadjaj selectedEvent;

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

    @FXML
    private Label msgLabel;


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
                if(newValue.length() > 4)
                    bottomPriceInput.setText(oldValue);
                if (!newValue.matches("\\d*")) {
                    bottomPriceInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        upperPriceInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 4)
                    upperPriceInput.setText(oldValue);
                if (!newValue.matches("\\d*")) {
                    upperPriceInput.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        brojKarataInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 4)
                    brojKarataInput.setText(oldValue);
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
            eventsList = eventsList.stream().filter(event -> event.getStartDate().toLocalDateTime().isAfter(LocalDateTime.now())).toList();
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

        selectedEvent = d;

        nazivShow.setText(d.getNaziv());
        opisShow.setText(d.getOpis());
        LocalDateTime time = d.getStartDate().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = time.format(formatter);
        startDateShow.setText(formattedDateTime);
        time = d.getEndDate().toLocalDateTime();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
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
                    TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(*) FROM Ticket t JOIN Dogadjaj d ON t.dogadjaj = d JOIN Seat seat ON t.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE d.id = :idInput AND sector.naziv = :nazivInput AND t.bought = false AND t.reserved = false", Long.class);
                    query.setParameter("idInput", d.getId());
                    query.setParameter("nazivInput", newValue);

                    Long brojKarata = query.getSingleResult();
                    slobodnoMjestaShow.setText(String.valueOf(brojKarata));

                    TypedQuery<Ticket> query3 = entityManager.createQuery("SELECT t FROM Ticket t JOIN Seat seat ON t.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE t.dogadjaj = :dogadjaj AND sector.naziv = :nazivInput", Ticket.class);
                    query3.setParameter("dogadjaj", d);
                    query3.setParameter("nazivInput", newValue);

                    List<Ticket> karte = query3.getResultList();
                    cijenaShow.setText((karte.get(0).getPrice() + d.getBasePrice()) + "KM");

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
        refreshNumOfEvents();
        eventsPagination.setPageCount(numOfEvents.intValue() / 6 + 1);
        eventsPagination.setPageFactory(this::getEvents);
    }

    private void refreshNumOfEvents() {
        numOfEvents = (long) showList.size();
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

        searchInput.clear();
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
        if(item != null) {
            if(!searchInput.getText().isEmpty()) showList = showList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getKategorija().getNaziv().equals(item)).toList();
            else showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getKategorija().getNaziv().equals(item)).toList();
        }

        placeBox.setPromptText("Mjesto");
        locationBox.setPromptText("Lokacija");
        bottomPriceInput.setPromptText("od");
        upperPriceInput.setPromptText("do");

        refreshEventsPagination();
    }

    public void changedSubCategory(ActionEvent actionEvent) {
        String item = subCategoryBox.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(!searchInput.getText().isEmpty()) showList = showList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getNaziv().equals(item)).toList();
            else showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getPodkategorija().getNaziv().equals(item)).toList();
        }

        placeBox.setPromptText("Mjesto");
        locationBox.setPromptText("Lokacija");
        bottomPriceInput.setPromptText("od");
        upperPriceInput.setPromptText("do");

        refreshEventsPagination();
    }

    public void searchEvents(ActionEvent actionEvent) {
        if(!showList.isEmpty())
            showList = showList.stream().filter(dogadjaj -> dogadjaj.getNaziv().toLowerCase().contains(searchInput.getText().toLowerCase())).toList();
        else
            showList = eventsList.stream().filter(dogadjaj -> dogadjaj.getNaziv().toLowerCase().contains(searchInput.getText().toLowerCase())).toList();

        refreshEventsPagination();
    }

    public void rezervacija(ActionEvent actionEvent) {
        Long slobodnoMjesta = Long.parseLong(slobodnoMjestaShow.getText());
        LocalDateTime maxRezervacijaDatum = selectedEvent.getStartDate().toLocalDateTime().minusDays(3);
        LocalDateTime rezervacijaDo = LocalDateTime.now().plusDays(1);
        rezervacijaDo = rezervacijaDo.isBefore(maxRezervacijaDatum) ? rezervacijaDo : maxRezervacijaDatum;

        if(LocalDateTime.now().plusDays(1).isAfter(maxRezervacijaDatum)) {
            msg = "Ne možete rezervisati kartu za ovaj događaj.";
            printMessage(false);
            return;
        }

        Long brojKarata = Long.parseLong(brojKarataInput.getText());

        if(brojKarata > slobodnoMjesta) {
            msg = "Nedovoljno slobodnih mjesta za uneseni broj karata.";
            printMessage(false);
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Ticket> query = entityManager.createQuery("SELECT ticket FROM Ticket ticket JOIN Seat seat ON ticket.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE ticket.dogadjaj = :dogadjaj AND sector.naziv = :sector AND ticket.bought = false AND ticket.reserved = false", Ticket.class);
        query.setParameter("dogadjaj", selectedEvent);
        query.setParameter("sector", sektorBox.getValue());
        List<Ticket> tickets = query.getResultList();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for(int i = 0; i < brojKarata; ++i) {
            tickets.get(i).setBought(false);
            tickets.get(i).setReserved(true);
            tickets.get(i).setReservedTo(Timestamp.valueOf(rezervacijaDo));
            tickets.get(i).setKupac(user);
            entityManager.persist(tickets.get(i));
        }

        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        slobodnoMjesta -= brojKarata;
        slobodnoMjestaShow.setText(slobodnoMjesta.toString());
        msg = brojKarata > 1 ? "Uspješno ste rezervisali karte za ovaj događaj." : "Uspješno ste rezervisali kartu za ovaj događaj.";
        printMessage(true);
    }

    public void kupovina(ActionEvent actionEvent) {
        Long slobodnoMjesta = Long.parseLong(slobodnoMjestaShow.getText());
        Long brojKarata;

        try {
            brojKarata = Long.parseLong(brojKarataInput.getText());
        } catch(NumberFormatException e) {
            msg = "Morate unijeti broj karata.";
            printMessage(false);
            return;
        }

        if(brojKarata > slobodnoMjesta) {
            msg = "Nedovoljno slobodnih mjesta za uneseni broj karata.";
            printMessage(false);
            return;
        }

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Ticket> query = entityManager.createQuery("SELECT ticket FROM Ticket ticket JOIN Seat seat ON ticket.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE ticket.dogadjaj = :dogadjaj AND sector.naziv = :sector AND ticket.bought = false AND ticket.reserved = false", Ticket.class);
        query.setParameter("dogadjaj", selectedEvent);
        query.setParameter("sector", sektorBox.getValue());
        List<Ticket> tickets = query.getResultList();

        double cijenaKarata = (tickets.get(0).getPrice() + tickets.get(0).getDogadjaj().getBasePrice()) * brojKarata;
        if(user.getWallet() < cijenaKarata) {
            msg = "Nedovoljno sredstava na računu za kupovinu karata.";
            printMessage(false);
            return;
        }

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for(int i = 0; i < brojKarata; ++i) {
            tickets.get(i).setBought(true);
            tickets.get(i).setReserved(false);
            tickets.get(i).setReservedTo(null);
            tickets.get(i).setKupac(user);
            entityManager.persist(tickets.get(i));
        }

        User u = entityManager.find(User.class, user.getId());
        u.setWallet(u.getWallet() - cijenaKarata);
        entityManager.persist(u);
        user = u;

        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        refreshWallet();
        slobodnoMjesta -= brojKarata;
        slobodnoMjestaShow.setText(slobodnoMjesta.toString());
        msg = brojKarata > 1 ? "Uspješno ste kupili karte za ovaj događaj." : "Uspješno ste kupili kartu za ovaj događaj.";
        printMessage(true);
    }

    private void printMessage(boolean successful) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(1500));
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
