package rs.app.rsprojekat.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
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

public class OrganizerController implements Initializable {
    private static User user = new User();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Long eventsNumberLong;
    private String message;

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

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            eventImage.setImage(image);

            File destinationFile = new File("src/main/resources/copiedImage.jpg");
            try {
                copyFile(selectedFile, destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyFile(File source, File destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(d) FROM Dogadjaj d WHERE approved = 0", Long.class);
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

        } else {
            System.out.println("Nevalidan unos podataka.");

            PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
            visibleMsg.setOnFinished(event -> msgLabel.setVisible(false));
            msgLabel.setText(message);
            msgLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
            msgLabel.setVisible(true);
            visibleMsg.play();
        }
    }
}