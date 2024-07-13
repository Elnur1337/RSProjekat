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
            if (getClass().getResourceAsStream("icons/icon.png") != null) {
                primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/icon.png"))));
                //Validating ticket icons created by surang - Flaticon - "https://www.flaticon.com/free-icons/validating-ticket"
            }

            final FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            System.out.println("Aplikacija se ne može pokrenuti!");
            System.out.println("Razlog: " + e.getMessage());
        }
    }

    public static void main(String[] args) { launch(args); }
}