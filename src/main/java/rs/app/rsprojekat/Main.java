package rs.app.rsprojekat;

//Java Util Imports
import java.util.Objects;

//JavaFX Imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Parent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            if (getClass().getResourceAsStream("icon.png") != null) {
                primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
                //Validating ticket icons created by surang - Flaticon - "https://www.flaticon.com/free-icons/validating-ticket"
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            System.out.println("Aplikacija se ne moze pokrenuti!");
        }
    }

    public static void main(String[] args) { launch(args); }
}