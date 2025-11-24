module org.example.tiendav2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires org.slf4j;
    requires org.apache.commons.lang3;

    opens org.example.tiendav2 to javafx.fxml;
    opens org.example.tiendav2.controller to javafx.fxml;

    exports org.example.tiendav2;
    exports org.example.tiendav2.controller;
}
