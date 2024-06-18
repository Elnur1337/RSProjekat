package main;
	
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.model.User;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Parent;

public class Main extends Application {
	private static final String PERSISTENCE_UNIT_NAME = "rstest";
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("assets/icon.png")));
			//Validating ticket icons created by surang - Flaticon - "https://www.flaticon.com/free-icons/validating-ticket"
			
			User user = new User();
			
			EntityManager em = emf.createEntityManager();
			
			//user.setIsLoggedIn(true);
			//Pokupiti remember me podatke ako postoje
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/index.fxml"));
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