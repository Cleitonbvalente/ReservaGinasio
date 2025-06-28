module com.reservaginasiooficial.reservaginasiooficial {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.reservaginasiooficial.reservaginasiooficial to javafx.fxml;
    exports com.reservaginasiooficial.reservaginasiooficial;
}