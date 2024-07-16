package rs.app.rsprojekat.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import rs.app.rsprojekat.model.*;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

public class OrganizerController implements Initializable {
    //private static User user = new User();
    private String msg;
    private eventState state;
    private List<Dogadjaj> eventsList;
    Dogadjaj selectedEvent;

    private enum eventState {
        ACTIVE,
        INACTIVE
    }

    private final Map<String, Integer> monthNameNumberMap = new HashMap<>();
    private final Map<String, Integer> monthNameDaysMap = new HashMap<>();
    private List<Sector> sectorsList;
    private ArrayList<String> sectorPriceModifier = new ArrayList<>();

    //Image variables
    private File selectedImgFile;
    private String selectedImgExtension;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Long activeEventsLong;
    private Long inactiveEventsLong;

    @FXML
    private Button homeBtn;
    @FXML
    private Button cancelEventBtn;
    @FXML
    private Button editEventBtn;
    @FXML
    private Button addEventBtn;

    @FXML
    private Pagination eventsPagination;

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
    private ComboBox<String> sektorBox;

    @FXML
    private VBox sektorVBox;
    @FXML
    private VBox newEventPanel;

    @FXML
    private TextField nazivInput;
    @FXML
    private TextArea opisInput;
    @FXML
    private TextField imgPathShow;
    @FXML
    private TextField pocetakSatiInput;
    @FXML
    private TextField pocetakMinutiInput;
    @FXML
    private TextField baznaCijenaInput;
    @FXML
    private TextField krajSatiInput;
    @FXML
    private TextField krajMinutiInput;

    @FXML
    private Label msgLabel;
    @FXML
    private Label msgEventInfoLabel;
    @FXML
    private Label activeEventsNumber;
    @FXML
    private Label inactiveEventsNumber;
    @FXML
    private Label labelEventsMsg;

    @FXML
    private TextField nazivShow;
    @FXML
    private TextArea opisShow;
    @FXML
    private TextField startDateShow;
    @FXML
    private TextField endDateShow;
    @FXML
    private TextField cijenaShow;
    @FXML
    private TextField prodanoKarataShow;
    @FXML
    private TextField ukupnoKarataShow;
    @FXML
    private TextField rezervisanoKarataShow;
    @FXML
    private TextField mjestoShow;
    @FXML
    private TextField lokacijaShow;

    @FXML
    private ImageView eventImage;
    @FXML
    private ImageView sectorImage;

    @FXML
    private Pane eventPane, coverPane;
    @FXML
    private ScrollPane sectorScrollPane;

    public static String getKeyByValue(Map<String, Integer> map, Integer value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
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

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
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

        hideEventInfo();
        loadEvents();
        refreshEventsNumber();
    }

    private VBox showEventsPanel(int pageIndex) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<Dogadjaj> dogadjaji;
        if(state == eventState.ACTIVE)
            dogadjaji = eventsList.stream().filter(event -> event.isApproved() && event.isAvailable()).toList();
        else
            dogadjaji = eventsList.stream().filter(event -> event.isApproved() && !event.isAvailable()).toList();

        VBox page = new VBox();
        page.setPrefWidth(eventsPagination.getPrefWidth());
        page.setStyle("-fx-padding: 10;");

        int fromIndex = pageIndex * 5;
        int toIndex = Math.min(fromIndex + 5, dogadjaji.size());

        for (Dogadjaj d : dogadjaji.subList(fromIndex, toIndex)) {
            HBox dogadjajBox = new HBox();
            dogadjajBox.setAlignment(Pos.CENTER_LEFT);
            dogadjajBox.setPrefHeight(110.0);
            dogadjajBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #666; -fx-background-radius: 20; -fx-padding: 10;");
            VBox.setMargin(dogadjajBox, new Insets(0, 0, 10, 0));

            VBox titleAndDateVBox = new VBox();
            titleAndDateVBox.setPrefWidth(250.0);

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
            placeAndLocationVBox.setPrefWidth(250.0);

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
            imageBox.setPrefWidth(140);

            ImageView image = new ImageView();
            image.setFitHeight(140);
            image.setFitWidth(140);
            image.setPreserveRatio(true);
            // NEED TO CHECK WHETHER THIS WORKS WHEN ALL EVENTS HAVE IMAGES
//            image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(d.getImgPath()))));

            imageBox.getChildren().add(image);

            Region buttonBoxRegion = new Region();
            HBox.setHgrow(buttonBoxRegion, Priority.ALWAYS);


            VBox buttonsVBox = new VBox();
            buttonsVBox.setPrefWidth(100.0);
            buttonsVBox.setAlignment(Pos.CENTER);

            Button cancelBtn = new Button();
            cancelBtn.setMnemonicParsing(false);
            cancelBtn.setPrefWidth(100.0);
            cancelBtn.setStyle("-fx-background-color: #781510; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            cancelBtn.setTextFill(Color.WHITE);
            cancelBtn.setText("Otkaži događaj");
            cancelBtn.setFont(Font.font("SansSerif Bold", 18.0));
            cancelBtn.setCursor(Cursor.HAND);
            cancelBtn.setOnAction(event -> cancelEvent(d));

            Region buttonsRegion = new Region();
            VBox.setVgrow(buttonsRegion, Priority.ALWAYS);

            Button editBtn = new Button();
            editBtn.setMnemonicParsing(false);
            editBtn.setPrefWidth(100.0);
            editBtn.setStyle("-fx-background-color: #107811; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            editBtn.setFont(Font.font("SansSerif Bold", 18.0));
            editBtn.setCursor(Cursor.HAND);
            editBtn.setOnAction(event -> editEventInfo(d));

            if(state == eventState.ACTIVE) {
                cancelBtn.setVisible(true);
                editBtn.setVisible(true);
            } else {
                cancelBtn.setVisible(false);
                editBtn.setVisible(false);
            }

            buttonsVBox.getChildren().addAll(cancelBtn, buttonsRegion, editBtn);

            dogadjajBox.setMaxHeight(140);
            dogadjajBox.getChildren().addAll(titleAndDateVBox, placeAndLocationVBox, imageBox);
            dogadjajBox.setOnMouseClicked(event -> showEventInfo(d));
            page.getChildren().add(dogadjajBox);
        }
        return page;
    }

    public void showActiveEventsPanel(MouseEvent mouseEvent) {
        eventsPagination.setVisible(true);
        newEventPanel.setVisible(false);
        state = eventState.ACTIVE;
        refreshEventsPagination();
    }

    public void showInactiveEventsPanel(MouseEvent mouseEvent) {
        eventsPagination.setVisible(true);
        newEventPanel.setVisible(false);
        state = eventState.INACTIVE;
        refreshEventsPagination();
    }

    public void fillPodkategorijaBox() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<String> subcategoryNamesQuery = entityManager.createQuery("SELECT s.naziv FROM Subcategory s WHERE s.kategorija.naziv = :categoryNameInput", String.class);
        subcategoryNamesQuery.setParameter("categoryNameInput", kategorijaBox.getValue());
        final List<String> resultList = subcategoryNamesQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        podkategorijaBox.getItems().clear();
        podkategorijaBox.getItems().addAll(resultList);
    }

    public void fillLokacijaBox() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<String> locationNamesQuery = entityManager.createQuery("SELECT l.naziv FROM Location l WHERE l.mjesto.naziv = :placeNameInput", String.class);
        locationNamesQuery.setParameter("placeNameInput", mjestoBox.getValue());
        final List<String> resultList = locationNamesQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        lokacijaBox.getItems().clear();
        lokacijaBox.getItems().addAll(resultList);
    }

    public void fillSektorPane() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Sector> sectorQuery = entityManager.createQuery("SELECT s FROM Sector s WHERE s.lokacija.naziv = :locationNameInput AND s.lokacija.mjesto.naziv = :mjestoNameInput", Sector.class);
        sectorQuery.setParameter("locationNameInput", lokacijaBox.getValue());
        sectorQuery.setParameter("mjestoNameInput", mjestoBox.getValue());
        sectorsList = sectorQuery.getResultList();

        entityManager.close();
        entityManagerFactory.close();

        sektorVBox.getChildren().clear();
        int counter = 0;
        for (Sector sector : sectorsList) {
            HBox sectorHBox = new HBox();
            sectorHBox.setPrefHeight(50.0);
            sectorHBox.setAlignment(Pos.CENTER_LEFT);
            sectorScrollPane.setVvalue(sectorScrollPane.getVvalue() + 36);

            Label nazivLabel = new Label(sector.getNaziv() + "(Kap. " + sector.getKapacitet() + ")");
            nazivLabel.setPrefWidth(250.0);
            nazivLabel.setPrefHeight(36.0);
            nazivLabel.setFont(Font.font("SansSerif Regular", 18));
            nazivLabel.setStyle("-fx-padding: 5; -fx-border-color: lightgray; -fx-border-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            HBox.setMargin(nazivLabel, new Insets(0, 15, 0, 0));

            TextField priceModifierTextField = new TextField();
            priceModifierTextField.setPrefWidth(80.0);
            priceModifierTextField.setPrefHeight(36.0);
            priceModifierTextField.setStyle("-fx-background-radius: 50; -fx-effect:  dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            priceModifierTextField.setFont(Font.font("SansSerif Regular", 18.0));
            final int finalCounter = counter;
            priceModifierTextField.setOnKeyTyped(event -> {
                try {
                    sectorPriceModifier.set(finalCounter, priceModifierTextField.getText());
                } catch (IndexOutOfBoundsException e) {
                    sectorPriceModifier.add(finalCounter, priceModifierTextField.getText());
                }

            });

            sectorHBox.getChildren().addAll(nazivLabel, priceModifierTextField);
            sektorVBox.getChildren().add(sectorHBox);

            ++counter;
        }
    }

    private boolean validate() {
        if (nazivInput.getText().length() < 5 || nazivInput.getText().length() > 150) {
            msg = "Naziv mora biti između 5 i 150 karaktera!";
            return false;
        }
        if (opisInput.getText().length() < 5 || opisInput.getText().length() > 500) {
            msg = "Opis mora biti između 5 i 500 karaktera!";
            return false;
        }
        if (imgPathShow.getText().isEmpty()) {
            msg = "Slika događaja je obavezna!";
            return false;
        }
        try {
            pocetakGodinaBox.getValue().intValue();
        } catch (NullPointerException e) {
            msg = "Godina početka je obavezna!";
            return false;
        }
        try {
            pocetakMjesecBox.getValue().isEmpty();
        } catch (NullPointerException e) {
            msg = "Mjesec početka je obavezan!";
            return false;
        }
        try {
            pocetakDanBox.getValue().intValue();
        } catch (NullPointerException e) {
            msg = "Dan početka je obavezan!";
            return false;
        }
        int hoursInput;
        try {
            hoursInput = Integer.parseInt(pocetakSatiInput.getText());
        } catch (NumberFormatException e) {
            msg = "Sati početka moraju biti cijeli broj!";
            return false;
        }
        int minutesInput;
        try {
            minutesInput = Integer.parseInt(pocetakMinutiInput.getText());
        } catch (NumberFormatException e) {
            msg = "Minute početka moraju biti cijeli broj!";
            return false;
        }
        if (hoursInput < 0 || hoursInput > 23) {
            msg = "Vrijednost sati početka mora biti između -1 i 24!";
            return false;
        }
        if (minutesInput < 0 || minutesInput > 59) {
            msg = "Vrijednost minuta početka mora biti između -1 i 60!";
            return false;
        }
        try {
            Double.parseDouble(baznaCijenaInput.getText());
        } catch (NullPointerException | NumberFormatException e) {
            msg = "Bazna cijena mora biti realan broj!";
            return false;
        }
        try {
            krajGodinaBox.getValue().intValue();
        } catch (NullPointerException e) {
            msg = "Godina kraja je obavezna!";
            return false;
        }
        try {
            krajMjesecBox.getValue().isEmpty();
        } catch (NullPointerException e) {
            msg = "Mjesec kraja je obavezan!";
            return false;
        }
        try {
            krajDanBox.getValue().intValue();
        } catch (NullPointerException e) {
            msg = "Dan kraja je obavezan!";
            return false;
        }
        try {
            hoursInput = Integer.parseInt(krajSatiInput.getText());
        } catch (NumberFormatException e) {
            msg = "Sati kraja moraju biti cijeli broj!";
            return false;
        }
        try {
            minutesInput = Integer.parseInt(krajMinutiInput.getText());
        } catch (NumberFormatException e) {
            msg = "Minute kraja moraju biti cijeli broj!";
            return false;
        }
        if (hoursInput < 0 || hoursInput > 23) {
            msg = "Vrijednost sati kraja mora biti između -1 i 24!";
            return false;
        }
        if (minutesInput < 0 || minutesInput > 59) {
            msg = "Vrijednost minuta kraja mora biti između -1 i 60!";
            return false;
        }
        Timestamp startDate = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d", pocetakGodinaBox.getValue(), monthNameNumberMap.get(pocetakMjesecBox.getValue()), pocetakDanBox.getValue(), Integer.parseInt(pocetakSatiInput.getText()), Integer.parseInt(pocetakMinutiInput.getText()), 0));
        Timestamp endDate = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d", krajGodinaBox.getValue(), monthNameNumberMap.get(krajMjesecBox.getValue()), krajDanBox.getValue(), Integer.parseInt(krajSatiInput.getText()), Integer.parseInt(krajMinutiInput.getText()), 0));
        if(startDate.compareTo(endDate) >= 0) {
            msg = "Kraj događaja ne može biti prije početka!";
            return false;
        }
        try {
            podkategorijaBox.getValue().isEmpty();
        } catch (NullPointerException e) {
            msg = "Podkategorija je obavezna!";
            return false;
        }
        try {
            lokacijaBox.getValue().isEmpty();
        } catch (NullPointerException e) {
            msg = "Lokacija je obavezna!";
            return false;
        }
        for (int i = 0; i < sectorsList.size(); ++i) {
            try {
                Double.parseDouble(sectorPriceModifier.get(i));
            } catch (IndexOutOfBoundsException e) {
                msg = "Morate unijeti poskupljenje na sektore!";
                return false;
            } catch (NullPointerException | NumberFormatException e) {
                msg = "Poskupljenje za sektore mora biti realan broj!";
                return false;
            }
        }
        return true;
    }

    public void addEvent() {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
        visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
        if (!validate()) {
            msgLabel.setText(msg);
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabel.setVisible(true);
            visibleMsg.play();
            return;
        }
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Integer> maxEventIdQuery = entityManager.createQuery("SELECT MAX(id) FROM Dogadjaj", Integer.class);
        int id;
        if(maxEventIdQuery.getSingleResult() == null)
            id = 1;
        else
            id = maxEventIdQuery.getSingleResult() + 1;

        File currentDirFile = new File(".");
        String targetPath = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length() - 1);
        targetPath += "\\src\\main\\resources\\rs\\app\\rsprojekat\\controller";
        Path targetDirectory = Paths.get(targetPath).toAbsolutePath();
        Path destinationPath = targetDirectory.resolve(String.format("eventImageN%d.%s", id, selectedImgExtension));
        try {
            Files.copy(selectedImgFile.toPath(), destinationPath);
        } catch (IOException ignored) {}

        TypedQuery<Subcategory> subcategoryQuery = entityManager.createQuery("SELECT s FROM Subcategory s WHERE s.naziv = :subcategoryNameInput", Subcategory.class);
        subcategoryQuery.setParameter("subcategoryNameInput", podkategorijaBox.getValue());

        TypedQuery<Location> locationQuery = entityManager.createQuery("SELECT l FROM Location l WHERE l.naziv = :locationNameInput AND l.mjesto.naziv = :mjestoNameInput", Location.class);
        locationQuery.setParameter("locationNameInput", lokacijaBox.getValue());
        locationQuery.setParameter("mjestoNameInput", mjestoBox.getValue());

        Dogadjaj dogadjaj = new Dogadjaj();
        dogadjaj.setNaziv(nazivInput.getText());
        dogadjaj.setOpis(opisInput.getText());
        dogadjaj.setImgPath(String.format("eventImageN%d.%s", id, selectedImgExtension));
        dogadjaj.setStartDate(Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d", pocetakGodinaBox.getValue(), monthNameNumberMap.get(pocetakMjesecBox.getValue()), pocetakDanBox.getValue(), Integer.parseInt(pocetakSatiInput.getText()), Integer.parseInt(pocetakMinutiInput.getText()), 0)));
        dogadjaj.setEndDate(Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d", krajGodinaBox.getValue(), monthNameNumberMap.get(krajMjesecBox.getValue()), krajDanBox.getValue(), Integer.parseInt(krajSatiInput.getText()), Integer.parseInt(krajMinutiInput.getText()), 0)));
        dogadjaj.setBasePrice(Double.parseDouble(baznaCijenaInput.getText()));
        dogadjaj.setOrganizator(IndexController.getCurrentUser());
        dogadjaj.setPodkategorija(subcategoryQuery.getSingleResult());
        dogadjaj.setLokacija(locationQuery.getSingleResult());

        final EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(dogadjaj);

        for (int i = 0; i < sectorsList.size(); ++i) {
            TypedQuery<Seat> query = entityManager.createQuery("SELECT s FROM Seat s WHERE s.sektor = :sektor", Seat.class);
            query.setParameter("sektor", sectorsList.get(i));
            List<Seat> seats = query.getResultList();

            for(Seat seat : seats) {
                double cijena = Double.parseDouble(sectorPriceModifier.get(i));
                Ticket ticket = new Ticket();
                ticket.setDogadjaj(dogadjaj);
                ticket.setPrice(cijena + dogadjaj.getBasePrice());
                ticket.setSjedalo(seat);
                entityManager.persist(ticket);
            }
        }

        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();

        msg = "Događaj uspješno dodan!";
        msgLabel.setText(msg);
        msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        msgLabel.setVisible(true);
        visibleMsg.play();

        refreshEventsNumber();
        showNewEventPanel();
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
        activeEventsLong = eventsList.stream().filter(event -> event.isAvailable() && event.isApproved()).count();
        inactiveEventsLong = eventsList.stream().filter(event -> !event.isAvailable() && event.isApproved()).count();

        activeEventsNumber.setText(activeEventsLong.toString());
        inactiveEventsNumber.setText(inactiveEventsLong.toString());
    }

    private void loadEvents() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Dogadjaj> query = entityManager.createQuery("SELECT d FROM Dogadjaj d WHERE organizator = :korisnik", Dogadjaj.class);
        query.setParameter("korisnik", IndexController.getCurrentUser());
        eventsList = query.getResultList();
    }

    private void refreshEventsPagination() {
        refreshEventsNumber();
        if(state == eventState.ACTIVE)
            eventsPagination.setPageCount(activeEventsLong.intValue() / 6 + 1);
        else
            eventsPagination.setPageCount(inactiveEventsLong.intValue() / 6 + 1);
        eventsPagination.setPageFactory(this::showEventsPanel);
    }

    public void showNewEventPanel() {
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        newEventPanel.setVisible(true);
        newEventPanel.setManaged(true);
        addEventBtn.setOnAction(event -> addEvent());
    }

    public void addEventImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odabir slike za dogadjaj");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedImgFile = fileChooser.showOpenDialog(null);
        selectedImgExtension = selectedImgFile.toURI().toString().substring(selectedImgFile.toURI().toString().lastIndexOf(".") + 1);
        imgPathShow.setText(selectedImgFile.toPath().toString());
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = time.format(formatter);
        startDateShow.setText(formattedDateTime);
        time = d.getEndDate().toLocalDateTime();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        formattedDateTime = time.format(formatter);
        endDateShow.setText(formattedDateTime);
        mjestoShow.setText(d.getLokacija().getMjesto().getNaziv());
        lokacijaShow.setText(d.getLokacija().getNaziv());
        eventImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(d.getImgPath()))));
        cijenaShow.clear();
        prodanoKarataShow.clear();
        rezervisanoKarataShow.clear();
        ukupnoKarataShow.clear();

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
                    TypedQuery<Ticket> query = entityManager.createQuery("SELECT t FROM Ticket t JOIN Dogadjaj d ON t.dogadjaj = d JOIN Seat seat ON t.sjedalo = seat JOIN Sector sector ON seat.sektor = sector WHERE d.id = :idInput AND sector.naziv = :nazivInput", Ticket.class);
                    query.setParameter("idInput", d.getId());
                    query.setParameter("nazivInput", newValue);
                    List<Ticket> karte = query.getResultList();

                    prodanoKarataShow.setText(String.valueOf(karte.stream().filter(Ticket::getBought).count()));
                    prodanoKarataShow.setText(String.valueOf(karte.stream().filter(Ticket::getReserved).count()));
                    ukupnoKarataShow.setText(String.valueOf(karte.size()));
                    cijenaShow.setText(karte.get(0).getPrice() + "KM");

                    entityManager.close();
                    entityManagerFactory.close();
                }
            }
        });

        if(state == eventState.ACTIVE) {
            cancelEventBtn.setVisible(true);
            editEventBtn.setVisible(true);
        } else {
            cancelEventBtn.setVisible(false);
            editEventBtn.setVisible(false);
        }

        selectedEvent = d;
    }

    @FXML
    private void hideEventInfo() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), eventPane);
        slide.setToY(-1000);
        slide.play();
        slide = new TranslateTransition(Duration.millis(300), coverPane);
        slide.setToX(1000);
        slide.play();
    }

    private void editEventInfo(Dogadjaj d) {
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        hideEventInfo();

        newEventPanel.setVisible(true);

        nazivInput.setText(d.getNaziv());
        opisInput.setText(d.getOpis());
        LocalDateTime time = d.getStartDate().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        String formattedDateTime = time.format(formatter);
        pocetakGodinaBox.setValue(Integer.parseInt(formattedDateTime.substring(0, 4)));
        pocetakMjesecBox.setValue(getKeyByValue(monthNameNumberMap, Integer.parseInt(formattedDateTime.substring(5, 7))));
        pocetakDanBox.setValue(Integer.parseInt(formattedDateTime.substring(8, 10)));
        pocetakSatiInput.setText(formattedDateTime.substring(11, 13));
        pocetakMinutiInput.setText(formattedDateTime.substring(14));
        baznaCijenaInput.setText(String.valueOf(d.getBasePrice()));
        formattedDateTime = d.getEndDate().toLocalDateTime().format(formatter);
        krajGodinaBox.setValue(Integer.parseInt(formattedDateTime.substring(0, 4)));
        krajMjesecBox.setValue(getKeyByValue(monthNameNumberMap, Integer.parseInt(formattedDateTime.substring(5, 7))));
        krajDanBox.setValue(Integer.parseInt(formattedDateTime.substring(8, 10)));
        krajSatiInput.setText(formattedDateTime.substring(11, 13));
        krajMinutiInput.setText(formattedDateTime.substring(14));
        kategorijaBox.setValue(d.getPodkategorija().getKategorija().getNaziv());
        podkategorijaBox.setValue(d.getPodkategorija().getNaziv());
        mjestoBox.setValue(d.getLokacija().getMjesto().getNaziv());
        lokacijaBox.setValue(d.getLokacija().getNaziv());

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Sector> sectorQuery = entityManager.createQuery("SELECT s FROM Sector s WHERE s.lokacija.naziv = :locationNameInput AND s.lokacija.mjesto.naziv = :mjestoNameInput", Sector.class);
        sectorQuery.setParameter("locationNameInput", lokacijaBox.getValue());
        sectorQuery.setParameter("mjestoNameInput", mjestoBox.getValue());
        sectorsList = sectorQuery.getResultList();
        List<Ticket> karte = entityManager.createQuery("SELECT t FROM Ticket t WHERE t.dogadjaj = :dogadjaj", Ticket.class).setParameter("dogadjaj", d).getResultList();

        sektorVBox.getChildren().clear();
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

            TextField priceModifierTextField = new TextField();
            priceModifierTextField.setPrefWidth(80.0);
            priceModifierTextField.setPrefHeight(36.0);
            priceModifierTextField.setStyle("-fx-background-radius: 50; -fx-effect:  dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            priceModifierTextField.setFont(Font.font("SansSerif Regular", 18.0));
            priceModifierTextField.setText(String.valueOf(karte.stream().filter(karta -> karta.getSjedalo().getSector() == sector).toList().get(0).getPrice()));
            sectorPriceModifier.add(String.valueOf(karte.stream().filter(karta -> karta.getSjedalo().getSector() == sector).toList().get(0).getPrice()));

            sectorHBox.getChildren().addAll(nazivLabel, priceModifierTextField);
            sektorVBox.getChildren().add(sectorHBox);

            ++counter;
        }

        addEventBtn.setOnAction(event -> {
            PauseTransition visibleMsg = new PauseTransition(Duration.millis(2000));
            visibleMsg.setOnFinished(e -> msgLabel.setVisible(false));
            if (!validate()) {
                msgLabel.setText(msg);
                msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
                msgLabel.setVisible(true);
                visibleMsg.play();
                return;
            }

            EntityTransaction entityTransaction = entityManager.getTransaction();
            Dogadjaj stariDogadjaj = entityManager.find(Dogadjaj.class, d.getId());
            entityTransaction.begin();

            stariDogadjaj.setApproved(false);
            stariDogadjaj.setAvailable(false);

            Dogadjaj noviDogadjaj = new Dogadjaj(stariDogadjaj);
            stariDogadjaj.setOriginal(noviDogadjaj);
            noviDogadjaj.setApproved(false);
            noviDogadjaj.setAvailable(true);
            entityManager.persist(noviDogadjaj);

            if(!imgPathShow.getText().isEmpty()) {
                TypedQuery<Integer> maxEventIdQuery = entityManager.createQuery("SELECT MAX(id) FROM Dogadjaj", Integer.class);
                int id;
                if(maxEventIdQuery.getSingleResult() == null)
                    id = 1;
                else
                    id = maxEventIdQuery.getSingleResult() + 1;

                File currentDirFile = new File(".");
                String targetPath = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length() - 1);
                targetPath += "\\src\\main\\resources\\rs\\app\\rsprojekat\\controller";
                Path targetDirectory = Paths.get(targetPath).toAbsolutePath();
                Path destinationPath = targetDirectory.resolve(String.format("newEventImageN%d.%s", id, selectedImgExtension));
                try {
                    Files.copy(selectedImgFile.toPath(), destinationPath);
                } catch (IOException ignored) {}

                stariDogadjaj.setImgPath(String.format("newEventImageN%d.%s", id, selectedImgExtension));
            }

            TypedQuery<Subcategory> subcategoryQuery = entityManager.createQuery("SELECT s FROM Subcategory s WHERE s.naziv = :subcategoryNameInput", Subcategory.class);
            subcategoryQuery.setParameter("subcategoryNameInput", podkategorijaBox.getValue());

            TypedQuery<Location> locationQuery = entityManager.createQuery("SELECT l FROM Location l WHERE l.naziv = :locationNameInput AND l.mjesto.naziv = :mjestoNameInput", Location.class);
            locationQuery.setParameter("locationNameInput", lokacijaBox.getValue());
            locationQuery.setParameter("mjestoNameInput", mjestoBox.getValue());

            stariDogadjaj.setNaziv(nazivInput.getText());
            stariDogadjaj.setOpis(opisInput.getText());
            System.out.println(String.format("%04d-%02d-%02d %02d:%02d:%02d", pocetakGodinaBox.getValue(), monthNameNumberMap.get(pocetakMjesecBox.getValue()), pocetakDanBox.getValue(), Integer.parseInt(pocetakSatiInput.getText()), Integer.parseInt(pocetakMinutiInput.getText()), 0));
            stariDogadjaj.setStartDate(Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d", pocetakGodinaBox.getValue(), monthNameNumberMap.get(pocetakMjesecBox.getValue()), pocetakDanBox.getValue(), Integer.parseInt(pocetakSatiInput.getText()), Integer.parseInt(pocetakMinutiInput.getText()), 0)));
            stariDogadjaj.setEndDate(Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d", krajGodinaBox.getValue(), monthNameNumberMap.get(krajMjesecBox.getValue()), krajDanBox.getValue(), Integer.parseInt(krajSatiInput.getText()), Integer.parseInt(krajMinutiInput.getText()), 0)));
            stariDogadjaj.setBasePrice(Double.parseDouble(baznaCijenaInput.getText()));
            stariDogadjaj.setOrganizator(IndexController.getCurrentUser());
            stariDogadjaj.setPodkategorija(subcategoryQuery.getSingleResult());
            stariDogadjaj.setLokacija(locationQuery.getSingleResult());

            entityTransaction.commit();

            msg = "Događaj uspješno dodan!";
            msgLabel.setText(msg);
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
            msgLabel.setVisible(true);
            visibleMsg.play();

            refreshEventsNumber();
            showNewEventPanel();
        });
    }

    private void cancelEvent(Dogadjaj d) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Dogadjaj dogadjaj = entityManager.find(Dogadjaj.class, d.getId());
        List<Ticket> karte = entityManager.createQuery("SELECT t FROM Ticket t WHERE t.dogadjaj = :dogadjaj", Ticket.class).setParameter("dogadjaj", dogadjaj).getResultList();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        for(Ticket t : karte) {
            t.getKupac().setWallet(t.getKupac().getWallet() + t.getPrice());
            t.setKupac(null);
        }
        dogadjaj.setAvailable(false);
        entityTransaction.commit();
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(e -> labelEventsMsg.setVisible(false));
        labelEventsMsg.setText("Uspješno ste otkazali događaj.");
        labelEventsMsg.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
        labelEventsMsg.setVisible(true);
        visibleMsg.play();
        --activeEventsLong;
        refreshEventsPagination();

        entityManager.close();
        entityManagerFactory.close();
    }

    public void cancelEvent(ActionEvent actionEvent) {
        cancelEvent(selectedEvent);
    }

    public void editEvent(ActionEvent actionEvent) {
        editEventInfo(selectedEvent);
    }
}