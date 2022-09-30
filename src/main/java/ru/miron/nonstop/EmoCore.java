package ru.miron.nonstop;
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import ru.miron.nonstop.controllers.EnterController;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;

import java.io.IOException;

public class EmoCore extends Application {
    private static Stage mainStage;
    private static Object currentController;

    static {
        mainStage = null;
        currentController = null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        System.out.println("init of the application");
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("starting the application");
        this.mainStage = stage;
        setHelloScene();
        stage.setTitle("you won't see this");
        stage.setResizable(true);
        stage.show();
    }

    public static void setRegisterScene() {
        setContainerFromLoaderAsMainStageScene("views/register-view.fxml");
    }

    public static void setHelloScene() {
        setContainerFromLoaderAsMainStageScene("views/hello-view.fxml");
    }

    private static void setContainerFromLoaderAsMainStageScene(String resourceName) {
        var loader = new FXMLLoader(EmoCore.class.getResource(resourceName));
        try {
            var registerScene = new Scene(loader.load());
            manageOnControllerChangeLanguageListenersChange(loader);
            setScene(registerScene);
        } catch (IOException e) {
            System.out.println("Load error");
        }
    }

    private static void manageOnControllerChangeLanguageListenersChange(FXMLLoader loader) {
        if (currentController != null && currentController instanceof LanguageUpdatable) {
            AppLocaleManager.removeLanguageUpdatableListener((LanguageUpdatable) currentController);
        }
        currentController = loader.getController();
        if (currentController instanceof LanguageUpdatable) {
            AppLocaleManager.addLanguageUpdatableListener((LanguageUpdatable) currentController);
        }
    }

    private static void setScene(Scene sceneToSet) {
        mainStage.setScene(sceneToSet);
    }


    @Override
    public void stop() {
        System.out.println("stopping the application");
    }
}