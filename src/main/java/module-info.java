module ru.miron.nonstop {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.miron.nonstop to javafx.fxml;
    opens ru.miron.nonstop.controllers to javafx.fxml;
    exports ru.miron.nonstop;
    opens ru.miron.nonstop.controllers.commands to javafx.fxml;
}