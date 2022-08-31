module com.example.banketprogram {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires poi.ooxml;
    requires poi;
    requires java.desktop;

    opens com.example.banketprogram to javafx.fxml;
    exports com.example.banketprogram;
}