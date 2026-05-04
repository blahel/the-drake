module thedrake {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens thedrake.ui to javafx.fxml;
    exports thedrake;
    exports thedrake.ui;
}
