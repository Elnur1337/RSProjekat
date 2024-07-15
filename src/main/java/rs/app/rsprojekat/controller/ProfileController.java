package rs.app.rsprojekat.controller;

// Imports for PDF
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.animation.TranslateTransition;

import javafx.util.Duration;
import rs.app.rsprojekat.model.*;

import javax.imageio.ImageIO;
import javax.persistence.*;

public class ProfileController implements Initializable {
    private static User user = new User();
    private String msg;
    private ticketState state;
    private Long kupljeneKarteLong;
    private Long rezervisaneKarteLong;
    private List<Ticket> tickets = new ArrayList<>();

    private enum ticketState {
        BOUGHT,
        RESERVED
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button homeBtn;
    @FXML
    private Button adminPanelBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button uplataBtn;

    @FXML
    private Label kupljeneKarteLabel;
    @FXML
    private Label rezervisaneKarteLabel;
    @FXML
    private Label stanjeLabel;
    @FXML
    private Label msgLabel;
    @FXML
    private Label notEnoughMoneyLabel;

    @FXML
    private TextField imeShow;
    @FXML
    private TextField prezimeShow;
    @FXML
    private TextField datumRodjenjaShow;
    @FXML
    private TextField emailShow;
    @FXML
    private TextField stanjeShow;
    @FXML
    private TextField imeInput;
    @FXML
    private TextField prezimeInput;
    @FXML
    private TextField brojKarticeInput;
    @FXML
    private TextField datumIstekaInput;
    @FXML
    private TextField cvvInput;
    @FXML
    private TextField iznosInput;

    @FXML
    private VBox infoPanel;
    @FXML
    private VBox walletPanel;

    @FXML
    private Pagination kartePagination;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = IndexController.getCurrentUser();

        if(user.isAdmin()) {
            adminPanelBtn.setVisible(true);
            adminPanelBtn.setManaged(true);
        } else {
            adminPanelBtn.setVisible(false);
            adminPanelBtn.setManaged(false);
        }

        if(user.isOrganizator()) {
            eventsBtn.setVisible(true);
            eventsBtn.setManaged(true);
        } else {
            eventsBtn.setVisible(false);
            eventsBtn.setManaged(false);
        }

        loadTickets();
        showInfoPanel();
    }

    private void switchScene(String sceneURL, ActionEvent event) throws IOException {
        final URL url = Paths.get(sceneURL).toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeScene(ActionEvent actionEvent) throws IOException {
        switchScene("src/main/resources/rs/app/rsprojekat/index.fxml", actionEvent);
    }

    public void switchToAdminPanelScene(ActionEvent actionEvent) throws IOException {
        switchScene("src/main/resources/rs/app/rsprojekat/adminPanel.fxml", actionEvent);
    }

    public void switchToOrganizerScene(ActionEvent actionEvent) throws IOException {
        switchScene("src/main/resources/rs/app/rsprojekat/organizer.fxml", actionEvent);
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        final IndexController indexController = new IndexController();
        indexController.setCurrentUser(new User());

        FileWriter rememberMeWriter = new FileWriter("rememberMe.txt");
        rememberMeWriter.write("");
        rememberMeWriter.close();
        homeBtn.fire();
    }

    private void showInfoPanel() {
        infoPanel.setVisible(true);
        kartePagination.setVisible(false);
        walletPanel.setVisible(false);

        imeShow.setText(user.getIme());
        prezimeShow.setText(user.getPrezime());
        datumRodjenjaShow.setText((new SimpleDateFormat("dd-MM-yyyy")).format(user.getDatumRod()));
        emailShow.setText(user.getEmail());
        stanjeShow.setText(user.getWallet() + "KM");
    }

    public void showInfoPanel(MouseEvent mouseEvent) {
        showInfoPanel();
    }

    private void loadTickets() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Ticket> query = entityManager.createQuery("SELECT t FROM Ticket t WHERE t.kupac = :user ORDER BY t.dogadjaj.startDate", Ticket.class);
        query.setParameter("user", user);
        tickets = query.getResultList();
        entityManager.close();
        entityManagerFactory.close();

        kupljeneKarteLong = tickets.stream().filter(Ticket::getBought).count();
        kupljeneKarteLabel.setText(kupljeneKarteLong.toString());
        rezervisaneKarteLong = tickets.stream().filter(Ticket::getReserved).count();
        rezervisaneKarteLabel.setText(rezervisaneKarteLong.toString());
    }

    private void refreshTicketsPagination() {
        kupljeneKarteLabel.setText(kupljeneKarteLong.toString());
        rezervisaneKarteLabel.setText(rezervisaneKarteLong.toString());

        if(state == ticketState.BOUGHT)
            kartePagination.setPageCount(kupljeneKarteLong.intValue() / 6 + 1);
        else
            kartePagination.setPageCount(rezervisaneKarteLong.intValue() / 6 + 1);

        loadTickets();
        kartePagination.setPageFactory(this::getTicketsPanel);
    }

    private VBox getTicketsPanel(int pageIndex) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<Ticket> karte;
        if(state == ticketState.BOUGHT)
            karte = tickets.stream().filter(Ticket::getBought).toList();
        else
            karte = tickets.stream().filter(Ticket::getReserved).toList();

        VBox page = new VBox();
        page.setPrefWidth(kartePagination.getPrefWidth());
        page.setStyle("-fx-padding: 10;");

        int fromIndex = pageIndex * 5;
        int toIndex = Math.min(fromIndex + 5, karte.size());

        for (Ticket t : karte.subList(fromIndex, toIndex)) {
            HBox ticketBox = new HBox();
            ticketBox.setAlignment(Pos.CENTER_LEFT);
            ticketBox.setPrefHeight(110.0);
            ticketBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #666; -fx-background-radius: 20; -fx-padding: 10;");
            VBox.setMargin(ticketBox, new Insets(0, 0, 10, 0));

            VBox titleAndDateVBox = new VBox();
            titleAndDateVBox.setPrefWidth(200.0);

            Label title = new Label();
            title.setText("Naziv: " + t.getDogadjaj().getNaziv());
            title.setTextFill(Color.WHITE);
            title.setFont(Font.font("SansSerif Regular", 18.0));

            Region titleAndDateRegion = new Region();
            VBox.setVgrow(titleAndDateRegion, Priority.ALWAYS);

            Label date = new Label();
            date.setText("Datum: " + (new SimpleDateFormat("dd-MM-yyyy")).format(t.getDogadjaj().getStartDate()));
            date.setTextFill(Color.WHITE);
            date.setFont(Font.font("SansSerif Regular", 18.0));

            titleAndDateVBox.getChildren().addAll(title, titleAndDateRegion, date);


            VBox placeAndLocationVBox = new VBox();
            placeAndLocationVBox.setPrefWidth(250.0);

            Label place = new Label();
            place.setText("Mjesto: " + t.getDogadjaj().getLokacija().getMjesto().getNaziv());
            place.setTextFill(Color.WHITE);
            place.setFont(Font.font("SansSerif Regular", 18.0));

            Region placeAndLocationRegion = new Region();
            VBox.setVgrow(placeAndLocationRegion, Priority.ALWAYS);

            Label location = new Label();
            location.setText("Lokacija: " + t.getDogadjaj().getLokacija().getNaziv());
            location.setTextFill(Color.WHITE);
            location.setFont(Font.font("SansSerif Regular", 18.0));

            placeAndLocationVBox.getChildren().addAll(place, placeAndLocationRegion, location);


            VBox seatAndDeadlineVBox = new VBox();
            seatAndDeadlineVBox.setPrefWidth(300.0);

            Label seat = new Label();
            seat.setText("Sjedalo: " + t.getSjedalo().getSector().getNaziv() + ", " + t.getSjedalo().getBrojSjedala());
            seat.setTextFill(Color.WHITE);
            seat.setFont(Font.font("SansSerif Regular", 18.0));

            Region seatAndDeadlineRegion = new Region();
            VBox.setVgrow(seatAndDeadlineRegion, Priority.ALWAYS);

            Label deadline = new Label();
            deadline.setTextFill(Color.WHITE);
            deadline.setFont(Font.font("SansSerif Regular", 18.0));
            if(state == ticketState.BOUGHT)
                deadline.setVisible(false);
            else {
                deadline.setVisible(true);
                deadline.setText("Istek rezervacije: " + (new SimpleDateFormat("dd-MM-yyyy HH:mm")).format(t.getReservedTo()));
            }

            seatAndDeadlineVBox.getChildren().addAll(seat, seatAndDeadlineRegion, deadline);


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
            cancelBtn.setText("Otkaži");
            cancelBtn.setFont(Font.font("SansSerif Bold", 18.0));
            cancelBtn.setCursor(Cursor.HAND);
            cancelBtn.setOnAction(event -> {
                Ticket karta = entityManager.find(Ticket.class, t.getId());
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                karta.setKupac(null);
                karta.setReserved(false);
                karta.setReservedTo(null);
                entityManager.persist(karta);
                entityTransaction.commit();
                PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
                visibleMsg.setOnFinished(e -> notEnoughMoneyLabel.setVisible(false));
                notEnoughMoneyLabel.setText("Uspješno ste otkazali rezervaciju.");
                notEnoughMoneyLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
                notEnoughMoneyLabel.setVisible(true);
                visibleMsg.play();
                --rezervisaneKarteLong;
                tickets.remove(karta);
                refreshTicketsPagination();
            });

            Region buttonsRegion = new Region();
            VBox.setVgrow(buttonsRegion, Priority.ALWAYS);

            Button buyOrPrintBtn = new Button();
            buyOrPrintBtn.setMnemonicParsing(false);
            buyOrPrintBtn.setPrefWidth(100.0);
            buyOrPrintBtn.setStyle("-fx-background-color: #107811; -fx-background-radius: 50; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2);");
            buyOrPrintBtn.setFont(Font.font("SansSerif Bold", 18.0));
            buyOrPrintBtn.setCursor(Cursor.HAND);

            if(state == ticketState.BOUGHT) {
                cancelBtn.setVisible(false);
                buyOrPrintBtn.setText("Printaj kartu");
                buyOrPrintBtn.setTextFill(Color.BLACK);
                buttonsVBox.setPrefWidth(150);
                buyOrPrintBtn.setPrefWidth(150);
                buyOrPrintBtn.setOnAction(event -> {
                    try {
                        printPDF(t);
                    } catch (Exception e) {
                        msg = "Neuspjelo printanje karte.";
                        printMessage(false);
                    }
                });
            } else {
                cancelBtn.setVisible(true);
                buyOrPrintBtn.setText("Kupi");
                buyOrPrintBtn.setTextFill(Color.WHITE);
                buyOrPrintBtn.setOnAction(event -> {
                    Ticket karta = entityManager.find(Ticket.class, t.getId());
                    EntityTransaction entityTransaction = entityManager.getTransaction();
                    entityTransaction.begin();
                    PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
                    Double wallet = karta.getKupac().getWallet();
                    if(karta.getPrice() > wallet) {
                        visibleMsg.setOnFinished(e -> notEnoughMoneyLabel.setVisible(false));
                        notEnoughMoneyLabel.setText("Nemate dovoljno sredstava na računu.");
                        notEnoughMoneyLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #8a1313; -fx-border-color: #ad4c4c;");
                        notEnoughMoneyLabel.setVisible(true);
                        visibleMsg.play();
                    } else {
                        karta.getKupac().setWallet(wallet - (karta.getPrice() + karta.getDogadjaj().getBasePrice()));
                        karta.setReserved(false);
                        karta.setReservedTo(null);
                        karta.setBought(true);
                        entityManager.persist(karta);
                        entityTransaction.commit();
                        --rezervisaneKarteLong;
                        ++kupljeneKarteLong;
                        refreshWallet();
                        refreshTicketsPagination();
                        notEnoughMoneyLabel.setText("Uspješno ste kupili kartu.");
                        notEnoughMoneyLabel.setStyle("-fx-background-radius: 50; -fx-border-width: 1; -fx-border-radius: 50; -fx-padding: 7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 6, 0.0, 0, 4), dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0.0, 0, 2); -fx-background-color: #468847; -fx-border-color: #69A56A;");
                        notEnoughMoneyLabel.setVisible(true);
                        visibleMsg.play();
                    }
                });
            }

            buttonsVBox.getChildren().addAll(cancelBtn, buttonsRegion, buyOrPrintBtn);

            ticketBox.getChildren().addAll(titleAndDateVBox, placeAndLocationVBox, seatAndDeadlineVBox, buttonsRegion, buttonsVBox);
            page.getChildren().add(ticketBox);
        }
        return page;
    }

    private void showBoughtTicketsPanel() {
        infoPanel.setVisible(false);
        kartePagination.setVisible(true);
        walletPanel.setVisible(false);

        state = ticketState.BOUGHT;
        refreshTicketsPagination();
    }

    public void showBoughtTicketsPanel(MouseEvent mouseEvent) {
        showBoughtTicketsPanel();
    }

    private void showReservedTicketsPanel() {
        infoPanel.setVisible(false);
        kartePagination.setVisible(true);
        walletPanel.setVisible(false);

        state = ticketState.RESERVED;
        refreshTicketsPagination();
    }

    public void showReservedTicketsPanel(MouseEvent mouseEvent) {
        showReservedTicketsPanel();
    }

    private void showWalletPanel() {
        infoPanel.setVisible(false);
        kartePagination.setVisible(false);
        walletPanel.setVisible(true);

        refreshWallet();

        imeInput.clear();
        imeInput.setPromptText("Ime");
        prezimeInput.clear();
        prezimeInput.setPromptText("Prezime");
        brojKarticeInput.clear();
        brojKarticeInput.setPromptText("Broj kartice");
        datumIstekaInput.clear();
        datumIstekaInput.setPromptText("mjesec/godina isteka");
        cvvInput.clear();
        cvvInput.setPromptText("CVV broj");
    }

    public void showWalletPanel(MouseEvent mouseEvent) {
        showWalletPanel();
    }

    private boolean validate() {
        String ime = imeInput.getText();
        String prezime = prezimeInput.getText();
        String brojKartice = brojKarticeInput.getText();
        String datumIsteka = datumIstekaInput.getText();
        String cvv = cvvInput.getText();
        Double iznos = 0.0;

        if(ime.length() < 2 || ime.length() > 20) {
            System.out.println(ime);
            msg = "Ime mora sadržavati 2-20 znakova.";
            return false;
        }
        if(!ime.matches("^[A-Z][a-zA-Zčćžđš]*$")) {
            System.out.println(ime);
            msg = "Ime mora početi velikim slovom i smije sadržavati samo slova.";
            return false;
        }

        if(prezime.length() < 2 || prezime.length() > 20) {
            System.out.println(prezime);
            msg = "Prezime mora sadržavati 2-20 znakova.";
            return false;
        }
        if(!prezime.matches("^[A-Z][a-zA-Zčćžđš]*$")) {
            System.out.println(prezime);
            msg = "Prezime mora početi velikim slovom i smije sadržavati samo slova.";
            return false;
        }

        if(!brojKartice.matches("\\d+") || brojKartice.length() != 16) {
            System.out.println(brojKartice);
            msg = "Pogrešan unos broja kartice.";
            return false;
        }

        if (datumIsteka.matches("^\\d{2}/\\d{2}$")) {
            String[] parts = datumIsteka.split("/");
            Long mjesec = Long.parseLong(parts[0]);
            Long godina = Long.parseLong(parts[1]);

            LocalDate currentDate = LocalDate.now();
            int trenutniMjesec = currentDate.getMonthValue();
            int trenutnaGodina = currentDate.getYear() - 2000;

            if(trenutnaGodina > godina || (trenutnaGodina == godina && trenutniMjesec > mjesec)) {
                System.out.println(datumIsteka);
                msg = "Kartica je istekla.";
                return false;
            }
        } else {
            System.out.println(datumIsteka);
            msg = "Pogrešan unos datuma isteka.";
            return false;
        }

        if(!cvv.matches("\\d+") || cvv.length() != 3) {
            System.out.println(cvv);
            msg = "Pogrešan CVV broj kartice.";
            return false;
        }

        try {
            iznos = Double.parseDouble(iznosInput.getText());
        } catch(NumberFormatException e) {
            System.out.println(iznos);
            msg = "Pogrešan unos iznosa.";
            return false;
        }

        if(iznos < 10.0) {
            System.out.println(iznos);
            msg = "Iznos ne moze biti manji od 10KM.";
            return false;
        }

        return true;
    }

    public void uplatiNovac(ActionEvent actionEvent) {
        if(validate()) {
            final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
            final EntityManager entityManager = entityManagerFactory.createEntityManager();

            User updatedUser = entityManager.find(User.class, user.getId());
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            updatedUser.setWallet(user.getWallet() + Double.parseDouble(iznosInput.getText()));
            entityTransaction.commit();
            user = updatedUser;
            refreshWallet();

            msg = "Uspješno ste uplatili novac na račun.";
            printMessage(true);
        } else {
            printMessage(false);
        }
    }

    private void refreshWallet() {
        stanjeLabel.setText("Stanje na računu: " + user.getWallet() + "KM");
    }

    private void printMessage(boolean successful) {
        PauseTransition visibleMsg = new PauseTransition(Duration.millis(3000));
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

    private void printPDF(Ticket ticket) throws IOException, WriterException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            LocalDateTime time = ticket.getDogadjaj().getStartDate().toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedDateTime = time.format(formatter);

            Paragraph title = new Paragraph(ticket.getDogadjaj().getNaziv())
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            Paragraph kupacHeader = new Paragraph("Informacije o kupcu")
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginBottom(10);
            document.add(kupacHeader);

            addTicketDetail(document, "Ime", ticket.getKupac().getIme());
            addTicketDetail(document, "Prezime", ticket.getKupac().getPrezime());

            Paragraph dogadjajHeader = new Paragraph("Informacije o dogadjaju")
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginTop(20)
                    .setMarginBottom(10);
            document.add(dogadjajHeader);

            addTicketDetail(document, "Mjesto", ticket.getDogadjaj().getLokacija().getMjesto().getNaziv());
            addTicketDetail(document, "Lokacija", ticket.getDogadjaj().getLokacija().getNaziv());
            addTicketDetail(document, "Vrijeme pocetka", formattedDateTime);

            Paragraph sjedaloHeader = new Paragraph("Informacije o sjedistu")
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                    .setMarginTop(20)
                    .setMarginBottom(10);
            document.add(sjedaloHeader);

            addTicketDetail(document, "Sektor", ticket.getSjedalo().getSector().getNaziv());
            addTicketDetail(document, "Sjedalo", String.valueOf(ticket.getSjedalo().getBrojSjedala()));

            String QRCodeData = ticket.getId() + " "
                    + ticket.getDogadjaj().getNaziv() + " "
                    + ticket.getSjedalo().getSector().getNaziv() + " "
                    + ticket.getSjedalo().getBrojSjedala() + " "
                    + ticket.getKupac().getEmail();

            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            String QRCodePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".png";
            generateQRcode(QRCodeData, QRCodePath, charset, hashMap, 200, 200);
            ImageData imageData = ImageDataFactory.create(QRCodePath);
            Image img = new Image(String.valueOf(imageData));

            // Creating Image object from the imagedata
//            doc.add(img);

            document.close();
        }
    }

    private void addTicketDetail(Document document, String label, String value) {
        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));

        Paragraph paragraphLabel = new Paragraph(label).setBold().setFontSize(12).setFontColor(ColorConstants.BLUE);
        Paragraph paragraphValue = new Paragraph(value).setFontSize(12);

        table.addCell(new Cell().add(paragraphLabel).setWidth(UnitValue.createPercentValue(50)));
        table.addCell(new Cell().add(paragraphValue).setWidth(UnitValue.createPercentValue(50)));

        table.setMarginBottom(10);
        document.add(table);
    }

    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException
    {
        String newPath = path.substring(0, path.lastIndexOf(".")) + ".png";
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, "png", new File(newPath));
    }


}