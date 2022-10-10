package ru.miron.nonstop;
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import ru.miron.nonstop.controllers.ConfirmationController;
import ru.miron.nonstop.controllers.InfoTextBoxController;
import ru.miron.nonstop.controllers.TableController;
import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;
import ru.miron.nonstop.logic.UDPClient;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.EnterEntry;
import ru.miron.nonstop.logic.commands.ErrorDescription;
import ru.miron.nonstop.logic.dao.Config;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.*;

public class EmoCore extends Application {
    private static Stage mainStage;
    private static Object currentController;
    public static UDPClient udpClient;
    public static EnterEntry enterEntry;

    private static List<DragonWithKeyAndOwner> actualDragonsWithMeta;

    static {
        mainStage = null;
        currentController = null;
        enterEntry = null;
        actualDragonsWithMeta = new LinkedList<>();
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
        File udpConfig = new File("config.json");
        try {
            udpClient = new UDPClient(Config.init(udpConfig.toPath()));
            setHelloScene();
//            enterEntry = new EnterEntry("imperator", "imperator");
//            setInsertDragonAsStageScene(this.mainStage);
            stage.setTitle("You won't see this");
        } catch (IOException e) {
            setInfoSceneAsMain("errorWithConfigLoadingMsg");
            stage.setTitle("Error with config loading");
        } catch (IllegalStateException e) {
            setInfoSceneAsMain("errorWithConfigFileStructureMsg");
            stage.setTitle("Error with config file structure");
        }
        stage.setResizable(true);
        stage.show();
    }

    /**
     * @throws IllegalStateException if main stage is not table controller or even null
     */
    public static TableController tryGettingMainStageTableController() {
        try {
            return (TableController) currentController;
        } catch (ClassCastException | NullPointerException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * @return Controller
     * @throws IOException if has load problems
     */
    public static Object createInfoAutoClosableWindowWithQueryErrorDescription(ErrorDescription errorDescription) throws IOException {
        var errorTypeLabelName = AppLocaleManager.getQueryErrorTypeLabelName(errorDescription.getErrorType());
        var errorMessage = errorDescription.getMessage();
        var errorText = errorTypeLabelName + ": " + errorMessage;
        var errorLabelText = new LabelText(errorText, PLAIN_TEXT);
        // set up error label text
        var stage = new Stage();
        var controller = (InfoTextBoxController) setContainerFromLoaderAsStageScene("views/info-view.fxml", stage);
        controller.setInfoLabelText(errorLabelText);
        controller.setOnOkClickAction(stage::close);
        stage.setTitle("Query error description");
        stage.show();
        return controller;
    }

    /**
     * @return Controller
     * @throws IOException if has load problems
     */
    public static Object createInfoAutoClosableWindow(String infoLabelName, String windowTitle) throws IOException {
        var infoLabelText = new LabelText(infoLabelName, LABEL_NAME);
        return createInfoAutoClosableWindow(infoLabelText, windowTitle);
    }

    /**
     * @return Controller
     * @throws IOException if has load problems
     */
    public static Object createInfoAutoClosableWindow(LabelText infoLabelText, String windowTitle) throws IOException {
        var stage = new Stage();
        var controller = (InfoTextBoxController) setContainerFromLoaderAsStageScene("views/info-view.fxml", stage);
        controller.setInfoLabelText(infoLabelText);
        controller.setOnOkClickAction(stage::close);
        stage.setTitle(windowTitle);
        stage.show();
        return controller;
    }

    /**
     * @return Controller
     * @throws IOException if has load problems
     */
    public static Object createConfirmAutoClosableWindow(String msgLabelName, String confirmButtonName, Runnable onConfirmAction, String windowTitle) throws IOException {
        var stage = new Stage();
        var controller = (ConfirmationController) setContainerFromLoaderAsStageScene("views/confirmation-view.fxml", stage);
        controller.setMsgLabelName(msgLabelName);
        controller.setConfirmButtonName(confirmButtonName);
        controller.setConfirmAction(onConfirmAction);
        controller.updateLanguage();
        stage.setTitle(windowTitle);
        stage.show();
        return controller;
    }

    public static void setInfoSceneAsMain(String infoLabelName) {
        setInfoSceneAsMain(infoLabelName, null);
    }

    public static void setInfoSceneAsMain(LabelText infoLabelText) {
        setInfoSceneAsMain(infoLabelText, null);
    }

    /**
     * @param onButtonLabelName dont set if null
     */
    public static void setInfoSceneAsMain(String infoLabelName, String onButtonLabelName) {
        var infoLabelText = new LabelText(infoLabelName, LABEL_NAME);
        LabelText onButtonLabelText = null;
        if (onButtonLabelName != null) {
            onButtonLabelText = new LabelText(onButtonLabelName, LABEL_NAME);
        }
        setInfoSceneAsMain(infoLabelText, onButtonLabelText);
    }

    /**
     * @param onButtonLabelText dont set if null
     */
    public static void setInfoSceneAsMain(LabelText infoLabelText, LabelText onButtonLabelText) {
        tryUnsubscribeControllerAsLanguageUpdatable(currentController);
        setContainerFromLoaderAsMainStageScene("views/info-view.fxml");
        var infoTextBoxController = ((InfoTextBoxController) currentController);
        infoTextBoxController.setInfoLabelText(infoLabelText);
        if (onButtonLabelText != null) {
            infoTextBoxController.setOnButtonLabelName(onButtonLabelText);
        }
    }

    public static void setRegisterScene() {
        setContainerFromLoaderAsMainStageScene("views/register-view.fxml");
    }

    public static void setHelloScene() {
        setContainerFromLoaderAsMainStageScene("views/hello-view.fxml");
    }

    public static void setTableScene() {
        setContainerFromLoaderAsMainStageScene("views/table-view.fxml");
    }

    /**
     * shows (tries) info about bad open if had scene gen problems.
     * @return true if opened, false if had problems
     */
    public static boolean tryCreateCommandsListWindow() {
        var stage = new Stage();
        try {
            setContainerFromLoaderAsStageScene("views/commands/commands_list-view.fxml", stage);
        } catch (IOException ioe) {
            try {
                createInfoAutoClosableWindow("errorWithSceneLoadingMsg", "Error with scene loading");
            } catch (IOException ioe2) {}
            return false;
        }
        stage.setTitle("Commands list");
        stage.show();
        return true;
    }

    /**
     * shows (tries) info about bad open if had scene gen problems.
     * @return true if opened, false if had problems
     */
    public static boolean setInsertDragonAsStageScene(Stage commandsListStage) {
        try {
            setContainerFromLoaderAsStageScene("views/commands/insert_dragon-view.fxml", commandsListStage);
        } catch (IOException ioe) {
            try {
                createInfoAutoClosableWindow("errorWithSceneLoadingMsg", "Error with scene loading");
            } catch (IOException ioe2) {}
            return false;
        }
        commandsListStage.setTitle("Insert dragon");
        commandsListStage.show();
        return true;
    }

    /**
     * shows (tries) info about bad open if had scene gen problems.
     * @return true if opened, false if had problems
     */
    public static boolean setUpdateDragonAsStageScene(Stage commandsListStage) {
        try {
            setContainerFromLoaderAsStageScene("views/commands/update_dragon-view.fxml", commandsListStage);
        } catch (IOException ioe) {
            try {
                createInfoAutoClosableWindow("errorWithSceneLoadingMsg", "Error with scene loading");
            } catch (IOException ioe2) {}
            return false;
        }
        commandsListStage.setTitle("Update dragon");
        commandsListStage.show();
        return true;
    }

    /**
     * shows (tries) info about bad open if had scene gen problems.
     * @return true if opened, false if had problems
     */
    public static boolean setRemoveByKeyAsStageScene(Stage commandsListStage) {
        try {
            setContainerFromLoaderAsStageScene("views/commands/remove_by_key-view.fxml", commandsListStage);
        } catch (IOException ioe) {
            try {
                createInfoAutoClosableWindow("errorWithSceneLoadingMsg", "Error with scene loading");
            } catch (IOException ioe2) {}
            return false;
        }
        commandsListStage.setTitle("Remove by key");
        commandsListStage.show();
        return true;
    }

    /**
     * loads info scene if has problems with loading
     */
    private static void setContainerFromLoaderAsMainStageScene(String resourceName) {
        try {
            tryUnsubscribeControllerAsLanguageUpdatable(currentController);
            currentController = setContainerFromLoaderAsStageScene(resourceName, mainStage);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            setInfoSceneAsMain("errorWithSceneLoadingMsg");
        }
    }

    /**
     * User should unsubscribe stage scene, if it has
     * @return Controller object if set scene and subscribed it,
     * @throws IOException if FXMLLoader has problems with loading scene
     */
    private static Object setContainerFromLoaderAsStageScene(String resourceName, Stage stage) throws IOException {
        var loader = new FXMLLoader(EmoCore.class.getResource(resourceName));
        var scene = new Scene(loader.load());
        trySubscribeLoaderControllerAsLanguageUpdatable(loader);
        stage.setScene(scene);
        return loader.getController();
    }

    private static boolean tryUnsubscribeControllerAsLanguageUpdatable(Object controller) {
        if (controller != null && controller instanceof LanguageUpdatable) {
            AppLocaleManager.removeLanguageUpdatableListener((LanguageUpdatable) currentController);
            return true;
        }
        return false;
    }

    private static boolean trySubscribeLoaderControllerAsLanguageUpdatable(FXMLLoader loader) {
        var controller = loader.getController();
        if (controller instanceof LanguageUpdatable) {
            AppLocaleManager.addLanguageUpdatableListener((LanguageUpdatable) controller);
            return true;
        }
        return false;
    }

    /**
     * tries getting answer, shows infoWindow with error on error (tries showing only one time)
     * @throws IllegalStateException if has any errors - should be processed
     */
    public static CommandAnswer tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(Command commandToSend) throws IllegalStateException {
        try {
            var commandAnswer = EmoCore.udpClient.tryToGetCommandAnswer(commandToSend);
            var registerCommandAnswerWithoutArgs = commandAnswer.getCommandAnswerWithoutArgs();
            if (registerCommandAnswerWithoutArgs.getIsError()) {
                var queryErrorDescription = registerCommandAnswerWithoutArgs.getErrorDescription();
                try {
                    createInfoAutoClosableWindowWithQueryErrorDescription(queryErrorDescription);
                } catch (IOException ioe) {}
                throw new IllegalStateException();
            }
            return commandAnswer;
        } catch (ConnectException e) {
            try {
                createInfoAutoClosableWindow("connectionErrorMsg", "Connection error");
            } catch (IOException ioe) {}
        } catch (IllegalArgumentException e) {
            try {
                createInfoAutoClosableWindow("badQueryAnswerMsg", "Bad answer error");
            } catch (IOException ioe) {}
        }
        throw new IllegalStateException();
    }

    private static void setScene(Scene sceneToSet) {
        mainStage.setScene(sceneToSet);
    }

    @Override
    public void stop() {
        System.out.println("stopping the application");
    }

    public static List<DragonWithKeyAndOwner> getActualDragonsWithMeta() {
        synchronized (actualDragonsWithMeta) {
            return new LinkedList<>(actualDragonsWithMeta);
        }
    }

    public static DragonWithKeyAndOwner getActualDragonsWithMetaById(long id) {
        return getActualDragonsWithMetaByPredicate(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getDragon().getId() == id);
    }

    public static DragonWithKeyAndOwner getActualDragonsWithMetaByKey(String key) {
        return getActualDragonsWithMetaByPredicate(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getKey().equals(key));
    }

    public static DragonWithKeyAndOwner getActualDragonsWithMetaByPredicate(Predicate<DragonWithKeyAndOwner> isOk) {
        synchronized (actualDragonsWithMeta) {
            for (var dragonWithMeta : actualDragonsWithMeta) {
                if (isOk.test(dragonWithMeta)) {
                    return dragonWithMeta;
                }
            }
            return null;
        }
    }

    public static void setActualDragonsWithMeta(Collection<DragonWithKeyAndOwner> dragonsWithMeta) {
        synchronized (actualDragonsWithMeta) {
            actualDragonsWithMeta.clear();
            actualDragonsWithMeta.addAll(dragonsWithMeta);
        }
    }

}