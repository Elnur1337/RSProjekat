module RSProjekat {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires java.persistence;
	requires org.hibernate.orm.core;
	requires mysql.connector.j;
	requires bcrypt;
	
	opens main to javafx.graphics, javafx.fxml;
}
