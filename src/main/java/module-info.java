module com.example.parkinglotmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.parkinglotmanager to javafx.fxml;
    exports com.example.parkinglotmanager;
}