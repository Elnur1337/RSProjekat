module RSProjekat {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires bcrypt;
	
	opens application to javafx.graphics, javafx.fxml;
}
