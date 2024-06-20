package rs.app.rsprojekat;

//JavaFX Imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Parent;

//Model Imports
import rs.app.rsprojekat.model.User;

//Controller Imports
import rs.app.rsprojekat.controller.IndexController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            //Validating ticket icons created by surang - Flaticon - "https://www.flaticon.com/free-icons/validating-ticket"

            User user = new User();

            //user.setIsLoggedIn(true);
            //Pokupiti remember me podatke ako postoje

            FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
            Parent root = loader.load();

            IndexController indexController = loader.getController();
            indexController.setCurrentUser(user);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            System.out.println("Aplikacija se ne moze pokrenuti!");
        }
    }

    public static void main(String[] args) { launch(args); }
}