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

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Long eventsNumberLong;
    private String message;
    private File selectedFile;

    @FXML
    private Button homeBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button imageBtn;
    @FXML
    private Button sendRequestButton;

    @FXML
    private ChoiceBox categoryBox;
    @FXML
    private ChoiceBox subCategoryBox;

    @FXML
    private TextField nazivInput;
    @FXML
    private TextField priceInput;
    @FXML
    private TextArea opisInput;

    @FXML
    private ImageView eventImage;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Spinner<Integer> startHourSpinner;
    @FXML
    private Spinner<Integer> startMinuteSpinner;
    @FXML
    private Spinner<Integer> endHourSpinner;
    @FXML
    private Spinner<Integer> endMinuteSpinner;

    // Spinner placeholders
    @FXML
    private Pane startHourSpinnerPlaceholder;
    @FXML
    private Pane startMinuteSpinnerPlaceholder;
    @FXML
    private Pane endHourSpinnerPlaceholder;
    @FXML
    private Pane endMinuteSpinnerPlaceholder;

    @FXML
    private Pagination eventsPagination;

    @FXML
    private Label eventsNumber;
    @FXML
    private Label msgLabel;

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

    public void logout(ActionEvent event) throws IOException {
        final IndexController indexController = new IndexController();
        indexController.setCurrentUser(new User());

        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();

        switchToHomeScene(new ActionEvent());
    }

    private void refreshEventsPagination() {
        eventsNumber.setText(eventsNumberLong.toString());
        eventsPagination.setPageCount(eventsNumberLong.intValue() / 6 + 1);
        eventsPagination.setPageFactory(this::getEventsPanel);
    }

    public void showMyEvents() {
        newEventPanel.setVisible(false);
        newEventPanel.setManaged(false);
        eventsPagination.setVisible(true);
        eventsPagination.setManaged(true);
        refreshEventsPagination();
    }

    private VBox getEventsPanel(int pageIndex) {
        System.out.println("Events panel!");
        return null;
    }

    public void makeNewEvent(MouseEvent mouseEvent) {
        eventsPagination.setVisible(false);
        eventsPagination.setManaged(false);
        newEventPanel.setVisible(true);
        newEventPanel.setManaged(true);
    }

    public void addEventImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odabir slike za dogadjaj");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            eventImage.setImage(image);
        }
    }

//    private void copyFile(File source, File destination) throws IOException {
//        try (FileInputStream fis = new FileInputStream(source);
//             FileOutputStream fos = new FileOutputStream(destination)) {
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = fis.read(buffer)) > 0) {
//                fos.write(buffer, 0, length);
//            }
//        }
//    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        user = IndexController.getCurrentUser();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE organizator = :korisnik", Long.class);
        query.setParameter("korisnik", IndexController.getCurrentUser());
        eventsNumberLong = query.getSingleResult();

        startHourSpinner = new Spinner<>();
        endHourSpinner = new Spinner<>();
        startMinuteSpinner = new Spinner<>();
        endMinuteSpinner = new Spinner<>();

        SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        startHourSpinner.setValueFactory(startHourFactory);
        SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        endHourSpinner.setValueFactory(endHourFactory);
        SpinnerValueFactory<Integer> startMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        startMinuteSpinner.setValueFactory(startMinuteFactory);
        SpinnerValueFactory<Integer> endMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        endMinuteSpinner.setValueFactory(endMinuteFactory);

        startHourSpinnerPlaceholder.getChildren().add(startHourSpinner);
        startMinuteSpinnerPlaceholder.getChildren().add(startMinuteSpinner);
        endHourSpinnerPlaceholder.getChildren().add(endHourSpinner);
        endMinuteSpinnerPlaceholder.getChildren().add(endMinuteSpinner);

        entityManager.close();
        entityManagerFactory.close();
        showMyEvents();

        // Fill choiceBoxes via queries

        categoryBox.setItems(FXCollections.observableArrayList(
                "Muzika", "Sport", "Kultura", "Kino", "Ostalo"
        ));

        categoryBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Muzika")) {
                subCategoryBox.setItems(FXCollections.observableArrayList(
                        "Koncert na otvorenom", "Koncert u zatvorenom"
                ));
            } else if (newValue.equals("Sport")) {
                subCategoryBox.setItems(FXCollections.observableArrayList(
                        "Fudbal", "Košarka", "Odbojka", "Rukomet"
                ));
            } else if (newValue.equals("Kultura")) {
                subCategoryBox.setItems(FXCollections.observableArrayList(
                        "Gala koncert", "Klasična muzika"
                ));
            } else if (newValue.equals("Kino")) {
                subCategoryBox.setItems(FXCollections.observableArrayList(
                        "Film", "Predstava"
                ));
            } else if (newValue.equals("Ostalo")) {
                subCategoryBox.setItems(FXCollections.observableArrayList("Ostalo"));
            }
        });

        categoryBox.getSelectionModel().selectFirst();
    }

    public boolean validateRequest() {
        if(categoryBox.getSelectionModel().getSelectedItem() == null) {
            message = "Morate odabrati kategoriju.";
            return false;
        }
        if(subCategoryBox.getSelectionModel().getSelectedItem() == null) {
            message = "Morate odabrati podkategoriju.";
            return false;
        }

        if(nazivInput.getText().length() < 3) {
            message = "Naziv događaja mora sadržavati minimalno tri karaktera.";
            return false;
        }
        if(!nazivInput.getText().matches("^[A-Za-z0-9-.,() čćžđš]*$")) {
            message = "Naziv događaja može sadržavati samo slova, brojeve i znakove '.', ',' '-', '(', ')'.";
            return false;
        }

        if(opisInput.getText().length() < 20) {
            message = "Opis događaja mora sadržavati minimalno dvadeset karaktera.";
            return false;
        }

        if(startDatePicker.getValue() == null) {
            message = "Morate odabrati datum početka događaja.";
            return false;
        }
        if(endDatePicker.getValue() == null) {
            message = "Morate odabrati datum završetka događaja.";
            return false;
        }
        if(Timestamp.valueOf(startDatePicker.getValue().atStartOfDay()).compareTo(Timestamp.valueOf(endDatePicker.getValue().atStartOfDay())) > 0) {
            message = "Datum početka ne može biti poslije datuma završetka.";
            return false;
        } else if((Timestamp.valueOf(startDatePicker.getValue().atStartOfDay()).compareTo(Timestamp.valueOf(endDatePicker.getValue().atStartOfDay())) == 0) && (startHourSpinner.getValue() > endHourSpinner.getValue())) {
            message = "Vrijeme početka ne može biti poslije vremena završetka.";
            return false;
        } else if((startHourSpinner.getValue() == endHourSpinner.getValue()) && (startMinuteSpinner.getValue() > endMinuteSpinner.getValue())) {
            message = "Vrijeme početka ne može biti poslije vremena završetka.";
            return false;
        }
        if((startHourSpinner.getValue() == null) || (startMinuteSpinner.getValue() == null)) {
            message = "Morate odabrati vrijeme pocetka dogadjaja.";
            return false;
        }
        if((endHourSpinner.getValue() == null) || (endMinuteSpinner.getValue() == null)) {
            message = "Morate odabrati vrijeme zavrsetka dogadjaja.";
            return false;
        }

        try{
            Double cijena = Double.parseDouble(priceInput.getText());

            if(cijena < 0.0) {
                message = "Cijena ne moze biti ispod 0KM.";
                return false;
            }
        } catch (NumberFormatException e) {
            message = "Nepravilan unos cijene karte.";
            return false;
        }

        return true;
    }

    public void sendRequest(ActionEvent actionEvent) {
        if(validateRequest()) {
            System.out.println("Validan unos.");

            // Get subcategory ID via query
            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();

            TypedQuery<Subcategory> queryPodkategorija = entityManager.createQuery("SELECT s FROM Subcategory s WHERE naziv = :nazivPodkategorije", Subcategory.class);
            queryPodkategorija.setParameter("nazivPodkategorije", subCategoryBox.getSelectionModel().getSelectedItem().toString());
            Subcategory sub = queryPodkategorija.getSingleResult();

            TypedQuery<Location> queryLokacija = entityManager.createQuery("SELECT l FROM Location l WHERE naziv = :nazivLokacija", Location.class);
//            // Dodati choiceBox sa mjestima i lokacijama
//            queryLokacija.setParameter("nazivLokacija", locationBox.getSelectionModel().getSelectedItem().toString());
            queryLokacija.setParameter("nazivLokacija", "Galaxis");
            Location loc = queryLokacija.getSingleResult();

            Dogadjaj dogadjaj = new Dogadjaj();
            dogadjaj.setPodkategorija(sub);
            dogadjaj.setLokacija(loc);
            dogadjaj.setOrganizator(user);
            dogadjaj.setNaziv(nazivInput.getText());
            dogadjaj.setOpis(opisInput.getText());
            Timestamp startDate = new Timestamp(Timestamp.valueOf(startDatePicker.getValue().atStartOfDay()).getTime() + (long)(startHourSpinner.getValue() * 60 * 60 * 1000) + (long)(startMinuteSpinner.getValue() * 60 * 1000));
            dogadjaj.setStartDate(startDate);
            Timestamp endDate = new Timestamp(Timestamp.valueOf(endDatePicker.getValue().atStartOfDay()).getTime() + (long)(endHourSpinner.getValue() * 60 * 60 * 1000) + (long)(endMinuteSpinner.getValue() * 60 * 1000));
            dogadjaj.setEndDate(endDate);
            dogadjaj.setBasePrice(Double.parseDouble(priceInput.getText()));

            TypedQuery<Integer> query2 = entityManager.createQuery("SELECT MAX(id) FROM Dogadjaj", Integer.class);
            Integer id;
            if(query2.getSingleResult() == null)
                id = 1;
            else
                id = query2.getSingleResult() + 1;
//            dogadjaj.setImgPath(String.format("src/main/resources/eventImages/%d.png", id));
            dogadjaj.setImgPath("");

            try {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                entityManager.persist(dogadjaj);

                // Copying images doesn't work
//                Path targetDirectory = Paths.get("src/main/resources/eventImages").toAbsolutePath();
//                Path destinationPath = targetDirectory.resolve(String.format("%d.jpg", id));
//
//                Files.copy(selectedFile.toPath(), destinationPath);
//                System.out.println("File copied to: " + destinationPath);

                entityTransaction.commit();
                entityManager.close();
                entityManagerFactory.close();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                message = "Problem pri organizaciji događaja. Pokušajte kasnije.";
                printMessage(false);
            }

            message = "Zahtjev za organizacijom događaja poslan.";
            printMessage(true);
        } else {
            System.out.println("Nevalidan unos podataka.");
            printMessage(false);
        }
    }

    public void printMessage(boolean successful) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
        visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
        msgLabel.setText(message);
        if (successful) {
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
        } else {
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
        }
        msgLabel.setVisible(true);
        visibleMsg.play();
    }
}