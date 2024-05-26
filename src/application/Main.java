package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.Parent;

import java.sql.*;
import javax.sql.*;

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
		/*
		String url="jdbc:mysql://localhost:3306/rsprojekat";
		String username="root";
		//////////////////////////////////////////	//////////////////////////////////////////
		String password=" "; // UNIJETI password	
		//////////////////////////////////////////	//////////////////////////////////////////
		
		User user = new User(1002, "Damir", "Muminovic", "damirmuminovic@fet.ba", "123456", 500, 1, 1, Date.valueOf("2023-05-19"), 1, "2023-05-19");
     
		
		user.createUser(user); 
		
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection(url,username,password);
		
		Statement statement=con.createStatement();
		
		ResultSet resultSet=statement.executeQuery("SELECT *FROM korisnik");
		
		
		//ispis podataka o korisniku
		while (resultSet.next()) 
		{
			System.out.print("ID:"+resultSet.getString(1)+", Ime: "+resultSet.getString(2)+", Prezime: "+resultSet.getString(3));
			
		}
				}
		catch(Exception e)
		{
			System.out.println(e);
			
		}
		*/
	}
}