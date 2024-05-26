package application;
	
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
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			//Validating ticket icons created by surang - Flaticon - "https://www.flaticon.com/free-icons/validating-ticket"
			
			User user = new User();
			//user.setIsLoggedIn(true);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
			Parent root = loader.load();
			
			IndexController indexController = loader.getController();
			indexController.setCurrentUser(user);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		return;
	}
}