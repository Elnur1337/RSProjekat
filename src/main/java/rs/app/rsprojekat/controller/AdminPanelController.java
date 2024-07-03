package rs.app.rsprojekat.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rs.app.rsprojekat.model.User;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private List<User> userList;

    @FXML
    private Pagination usersPagination;

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
        final URL url = Paths.get("src/main/resources/rs/app/rsprojekat/index.fxml").toUri().toURL();
        root = FXMLLoader.load(url);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void showUserPanel() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE approved = 0", User.class);
        try  {
            userList = query.getResultList();
        } catch (NoResultException ignored) {}
        for (User u : userList) {
            System.out.println(u.getEmail());
        }
    }



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        usersPagination.setPageFactory(this::userTest);
    }

    VBox userTest(int pageIndex) {
        //ovo prebaciti u callback
        //kreirati lijep dizajn boxova sa svim informacijama
        //dodati mogucnost prihvatanja i odbijanja i refresh na svako odbijanje
        //istu funkcionalnost dodati i na klik na menu
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("rsprojekat");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE approved = 0", User.class).setFirstResult(pageIndex * 5).setMaxResults(5);
        try  {
            userList = query.getResultList();
        } catch (NoResultException ignored) {}

        VBox page = new VBox();
        for (User u : userList) {
            HBox userBox = new HBox();
            Label firstName = new Label(u.getIme());
            Label email = new Label(u.getEmail());
            userBox.getChildren().add(email);
            page.getChildren().add(userBox);
            System.out.println(email.getText());
        }
        return page;
    }
}
