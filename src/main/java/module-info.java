module org.example.monikas_frisoersalon_the_semicolons_projekt1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;


    opens org.example.monikas_frisoersalon_the_semicolons_projekt1 to javafx.fxml;
    exports org.example.monikas_frisoersalon_the_semicolons_projekt1;
    exports org.example.monikas_frisoersalon_the_semicolons_projekt1.ui;
    opens org.example.monikas_frisoersalon_the_semicolons_projekt1.ui to javafx.fxml;
}