module rs.app.rsprojekat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;

    requires java.sql;
    requires java.persistence;
    requires bcrypt;

    opens rs.app.rsprojekat to javafx.fxml;
    opens rs.app.rsprojekat.controller to javafx.fxml;
    opens rs.app.rsprojekat.model to org.hibernate.orm.core;

    exports rs.app.rsprojekat;
    exports rs.app.rsprojekat.controller;
}