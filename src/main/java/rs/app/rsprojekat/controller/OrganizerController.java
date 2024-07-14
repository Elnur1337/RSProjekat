package rs.app.rsprojekat.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import rs.app.rsprojekat.model.*;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

public class OrganizerController implements Initializable {
    //private static User user = new User();
    private String msg;

    private final Map<String, Integer> monthNameNumberMap = new HashMap<>();
    private final Map<String, Integer> monthNameDaysMap = new HashMap<>();
    private List<Sector> sectorsList;
    private ArrayList<String> sectorCardsNumber = new ArrayList<>();

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

    //Create new event panel
    @FXML
    private ComboBox<Integer> pocetakGodinaBox;
    @FXML
    private ComboBox<String> pocetakMjesecBox;
    @FXML
    private ComboBox<Integer> pocetakDanBox;
    @FXML
    private ComboBox<Integer> krajGodinaBox;
    @FXML
    private  ComboBox<String> krajMjesecBox;
    @FXML
    private ComboBox<Integer> krajDanBox;
    @FXML
    private ComboBox<String> kategorijaBox;
    @FXML
    private ComboBox<String> mjestoBox;
    @FXML
    private ComboBox<String> podkategorijaBox;
    @FXML
    private ComboBox<String> lokacijaBox;
    @FXML
    private VBox sektorVBox;
    @FXML
    private TextField nazivInput;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE organizator = :korisnik", Long.class);
        query.setParameter("korisnik", IndexController.getCurrentUser());
        eventsNumberLong = query.getSingleResult();

        monthNameNumberMap.put("Januar", 1);
        monthNameNumberMap.put("Februar", 2);
        monthNameNumberMap.put("Mart", 3);
        monthNameNumberMap.put("April", 4);
        monthNameNumberMap.put("Maj", 5);
        monthNameNumberMap.put("Jun", 6);
        monthNameNumberMap.put("Jul", 7);
        monthNameNumberMap.put("August", 8);
        monthNameNumberMap.put("Septembar", 9);
        monthNameNumberMap.put("Oktobar", 10);
        monthNameNumberMap.put("Novembar", 11);
        monthNameNumberMap.put("Decembar", 12);

        monthNameDaysMap.put("Januar", 31);
        monthNameDaysMap.put("Februar", 29);
        monthNameDaysMap.put("Mart", 31);
        monthNameDaysMap.put("April", 30);
        monthNameDaysMap.put("Maj", 31);
        monthNameDaysMap.put("Jun", 30);
        monthNameDaysMap.put("Jul", 31);
        monthNameDaysMap.put("August", 31);
        monthNameDaysMap.put("Septembar", 30);
        monthNameDaysMap.put("Oktobar", 31);
        monthNameDaysMap.put("Novembar", 30);
        monthNameDaysMap.put("Decembar", 31);

        pocetakGodinaBox.getItems().addAll(Year.now().getValue(), Year.now().getValue() + 1);
        pocetakMjesecBox.getItems().addAll("Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "August", "Septembar", "Oktobar", "Novembar", "Decembar");
        pocetakMjesecBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    pocetakDanBox.getItems().clear();
                    for (int i = 1; i <= monthNameDaysMap.get(newValue); ++i) {
                        pocetakDanBox.getItems().add(i);
                    }
                }
        );

        krajGodinaBox.getItems().addAll(Year.now().getValue(), Year.now().getValue() + 1);
        krajMjesecBox.getItems().addAll("Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "August", "Septembar", "Oktobar", "Novembar", "Decembar");
        krajMjesecBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    krajDanBox.getItems().clear();
                    for (int i = 1; i <= monthNameDaysMap.get(newValue); ++i) {
                        krajDanBox.getItems().add(i);
                    }
                }
        );

        TypedQuery<String> categoryNamesQuery = entityManager.createQuery("SELECT c.naziv FROM Category c", String.class);
        List<String> resultList = categoryNamesQuery.getResultList();

        for (String categoryName : resultList) {
            kategorijaBox.getItems().add(categoryName);
        }

        TypedQuery<String> placeNamesQuery = entityManager.createQuery("SELECT p.naziv FROM Place p", String.class);
        resultList = placeNamesQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        for (String placeName : resultList) {
            mjestoBox.getItems().add(placeName);
        }
    }

    public void fillPodkategorijaBox() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<String> subcategoryNamesQuery = entityManager.createQuery("SELECT s.naziv FROM Subcategory s WHERE s.kategorija.naziv = :categoryNameInput", String.class);
        subcategoryNamesQuery.setParameter("categoryNameInput", kategorijaBox.getValue());
        final List<String> resultList = subcategoryNamesQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        for (String subcategoryName : resultList) {
            podkategorijaBox.getItems().add(subcategoryName);
        }
    }

    public void fillLokacijaBox() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<String> locationNamesQuery = entityManager.createQuery("SELECT l.naziv FROM Location l WHERE l.mjesto.naziv = :placeNameInput", String.class);
        locationNamesQuery.setParameter("placeNameInput", mjestoBox.getValue());
        final List<String> resultList = locationNamesQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        for (String locationName : resultList) {
            lokacijaBox.getItems().add(locationName);
        }
    }

    public void fillSektorPane() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Sector> sectorQuery = entityManager.createQuery("SELECT s FROM Sector s WHERE s.lokacija.naziv = :locationNameInput", Sector.class);
        sectorQuery.setParameter("locationNameInput", lokacijaBox.getValue());
        sectorsList = sectorQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        int counter = 0;
        for (Sector sector : sectorsList) {
            HBox sectorHBox = new HBox();
            sectorHBox.setPrefHeight(50.0);
            sectorHBox.setAlignment(Pos.CENTER_LEFT);

            Label nazivLabel = new Label(sector.getNaziv() + "(Kap. " + sector.getKapacitet() + ")");
            nazivLabel.setPrefWidth(250.0);
            nazivLabel.setPrefHeight(36.0);
            nazivLabel.setFont(Font.font("SansSerif Regular", 18));
            nazivLabel.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            HBox.setMargin(nazivLabel, new Insets(0, 15, 0, 0));

            TextField brojKartiTextField = new TextField();
            brojKartiTextField.setPrefWidth(80.0);
            brojKartiTextField.setPrefHeight(36.0);
            brojKartiTextField.setStyle("-fx-background-radius: 50; -fx-effect:  dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            brojKartiTextField.setFont(Font.font("SansSerif Regular", 18.0));
            final int finalCounter = counter;
            brojKartiTextField.setOnKeyTyped(event -> {
                try {
                    sectorCardsNumber.set(finalCounter, brojKartiTextField.getText());
                } catch (IndexOutOfBoundsException e) {
                    sectorCardsNumber.add(finalCounter, brojKartiTextField.getText());
                }

            });

            sectorHBox.getChildren().addAll(nazivLabel, brojKartiTextField);
            sektorVBox.getChildren().add(sectorHBox);

            ++counter;
        }
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

    public void addEventImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odabir slike za dogadjaj");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        String extension = selectedFile.toURI().toString().substring(selectedFile.toURI().toString().lastIndexOf(".") + 1);

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
        Path destinationPath = targetDirectory.resolve(String.format("%d.%s", id, extension));
        Files.copy(selectedFile.toPath(), destinationPath);
        dogadjaj.setImgPath(String.format("@eventImages/%d.%s", id, extension));
    }
}