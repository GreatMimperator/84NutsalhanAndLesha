package ru.miron.nonstop;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import ru.miron.nonstop.controllers.*;
import ru.miron.nonstop.controllers.commands.*;
import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
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
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.*;

public class EmoCore extends Application {
    private static Stage mainStage;
    public static UDPClient udpClient;
    public static EnterEntry enterEntry;

    private static ObservableList<DragonWithKeyAndOwner> actualDragonsWithMeta;
    public static final DateTimeFormatter dateTimeFormatter;

    static {
        mainStage = null;
        enterEntry = null;
        actualDragonsWithMeta = FXCollections.observableList(new LinkedList<>());
        dateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd kk:mm:ss");
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
//            createDragonsCaveWindow();
//            enterEntry = new EnterEntry("imperator", "imperator");
//            setInsertDragonAsStageScene(this.mainStage);
            stage.setTitle("You won't see this");
        } catch (IOException e) {
            e.printStackTrace();
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
     * @return Controller
     * @throws IOException if has load problems
     */
    public static InfoTextBoxController createErrorInfoWindow(ErrorDescription errorDescription) throws IOException {
        var errorTypeLabelName = AppLocaleManager.getQueryErrorTypeLabelName(errorDescription.getErrorType());
        var errorMessage = errorDescription.getMessage();
        var errorText = errorTypeLabelName + ": " + errorMessage;
        var errorLabelText = new LabelText(errorText, PLAIN_TEXT);
        // set up error label text
        var stage = new Stage();
        var controller = (InfoTextBoxController) setStageScene("views/info-view.fxml", stage);
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
    public static Object createInfoWindow(String infoLabelName, String windowTitle) throws IOException {
        var infoLabelText = new LabelText(infoLabelName, LABEL_NAME);
        return createInfoWindow(infoLabelText, windowTitle);
    }

    /**
     * @return Controller
     * @throws IOException if has load problems
     */
    public static InfoTextBoxController createInfoWindow(LabelText infoLabelText, String windowTitle) throws IOException {
        var stage = new Stage();
        var controller = (InfoTextBoxController) setStageScene("views/info-view.fxml", stage);
        controller.setInfoLabelText(infoLabelText);
        controller.setOnOkClickAction(stage::close);
        stage.setTitle(windowTitle);
        stage.show();
        return controller;
    }

    public static void setInfoSceneAsMain(String infoLabelName) {
        setInfoSceneAsMain(infoLabelName, null);
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
        var infoTextBoxController = (InfoTextBoxController) setStageScene("views/info-view.fxml", mainStage);
        infoTextBoxController.setInfoLabelText(infoLabelText);
        if (onButtonLabelText != null) {
            infoTextBoxController.setOnButtonLabelName(onButtonLabelText);
        }
    }

    public static void createDragonInfoWindow(DragonWithKeyAndOwner dragonWithMeta) {
        var dragonWithMetaInfoTemplate = AppLocaleManager.getTextByLabel("chooseDragonByClickMsg") + "\n\n";
        for (int i = 0; i < 11; i++) {
            dragonWithMetaInfoTemplate += "%s: %s\n";
        }
        var dragonWithMetaInfo = String.format(dragonWithMetaInfoTemplate,
                AppLocaleManager.getTextByLabel("ownerLoginName"), dragonWithMeta.getOwnerLogin(),
                AppLocaleManager.getTextByLabel("idName"), dragonWithMeta.getDragon().getId(),
                AppLocaleManager.getTextByLabel("keyName"), dragonWithMeta.getKey(),
                AppLocaleManager.getTextByLabel("nameName"), dragonWithMeta.getDragon().getName(),
                AppLocaleManager.getTextByLabel("xName"), dragonWithMeta.getDragon().getCoordinates().getX(),
                AppLocaleManager.getTextByLabel("yName"), dragonWithMeta.getDragon().getCoordinates().getY(),
                AppLocaleManager.getTextByLabel("dateName"), dragonWithMeta.getDragon().getCreationDate().format(dateTimeFormatter),
                AppLocaleManager.getTextByLabel("ageName"), dragonWithMeta.getDragon().getAge(),
                AppLocaleManager.getTextByLabel("descriptionName"), dragonWithMeta.getDragon().getDescription(),
                AppLocaleManager.getTextByLabel("wingspanName"), dragonWithMeta.getDragon().getWingspan(),
                AppLocaleManager.getTextByLabel("typeName"), ElementsLocaleSetter.getLocalizedDragonType(dragonWithMeta.getDragon().getType()),
                AppLocaleManager.getTextByLabel("treasuresName"), dragonWithMeta.getDragon().getCave().getNumberOfTreasures());
        try {
            EmoCore.createInfoWindow(new LabelText(dragonWithMetaInfo, PLAIN_TEXT), "Clicked dragon info");
        } catch (IOException e) {}
    }

    public static RegisterController setRegisterScene() {
        return (RegisterController) setStageScene("views/register-view.fxml", mainStage);
    }

    public static EnterController setHelloScene() {
        return (EnterController) setStageScene("views/hello-view.fxml", mainStage);
    }

    public static TableController setTableScene() {
        return (TableController) setStageScene("views/table-view.fxml", mainStage);
    }

    public static void createErrorWithLoadingWindow() {
        try {
            createInfoWindow("errorWithSceneLoadingMsg", "Error with scene loading");
        } catch (IOException ioe) {}
    }

    public static CommandsListController createCommandsListWindow() {
        var stage = new Stage();
        var controller = (CommandsListController) setStageScene("views/commands/commands_list-view.fxml", stage);
        if (controller == null) {
            return null;
        }
        stage.show();
        return controller;
    }

    public static InsertDragonController createInsertDragonWindow() {
        return (InsertDragonController) createSceneWindow("views/commands/insert_dragon-view.fxml");
    }

    public static UpdateDragonController createUpdateDragonWindow() {
        return (UpdateDragonController) createSceneWindow("views/commands/update_dragon-view.fxml");
    }

    public static UpdateDragonController createUpdateDragonWindow(long presetId) {
        var controller = createUpdateDragonWindow();
        controller.setIdTextFieldValue(presetId);
        return controller;
    }

    public static ConfirmationController createDragonMenuWindow(DragonWithKeyAndOwner dragonWithMeta) {
        return EmoCore.createConfirmWindow("chooseDragonMenuMsg",
                "showInfoButtonName",
                () -> EmoCore.createDragonInfoWindow(dragonWithMeta),
                "changeDragonButtonName",
                () -> EmoCore.createUpdateDragonWindow(dragonWithMeta.getDragon().getId()),
                "Choose dragon menu");
    }

    public static RemoveDragonByKeyController createRemoveByKeyWindow() {
        return (RemoveDragonByKeyController) createSceneWindow("views/commands/remove_by_key-view.fxml");
    }

    public static RemoveOnCmpController createRemoveOnCmpWindow() {
        return (RemoveOnCmpController) createSceneWindow("views/commands/remove_on_compare-view.fxml");
    }

    public static ReplaceIfGreaterController createReplaceIfGreaterWindow() {
        return (ReplaceIfGreaterController) createSceneWindow("views/commands/replace_if_greater-view.fxml");
    }

    public static ConfirmationController createConfirmWindow(
            String msgLabelName,
            String confirmButtonName,
            Runnable onConfirmAction,
            String backButtonName,
            Runnable onBackAction,
            String windowTitle) {
        var controller = createConfirmWindow(msgLabelName, confirmButtonName, onConfirmAction, windowTitle);
        controller.setBackButtonName(backButtonName);
        controller.setBackAction(onBackAction);
        controller.updateLanguage();
        return controller;
    }

    public static ConfirmationController createConfirmWindow(
            String msgLabelName,
            String confirmButtonName,
            Runnable onConfirmAction,
            String windowTitle) {
        var controller = (ConfirmationController) createSceneWindow("views/confirmation-view.fxml");
        controller.setMsgLabelName(msgLabelName);
        controller.setConfirmButtonName(confirmButtonName);
        controller.setConfirmAction(onConfirmAction);
        controller.updateLanguage();
        controller.getStage().setTitle(windowTitle);
        return controller;
    }

    /**
     * @return Controller
     * @throws IOException if has load problems
     */
    public static DragonsViewController createDragonsViewWindow() {
        var stage = new Stage();
        var controller = (DragonsViewController) setStageScene("views/dragons-view.fxml", stage);
        stage.show();
        return controller;
    }

    private static Object createSceneWindow(String resourceName) {
        var stage = new Stage();
        var controller = setStageScene(resourceName, stage);
        stage.show();
        return controller;
    }

    /**
     *
     * @return Controller object if set scene and subscribed it, null else (and show load error)
     */
    private static Object setStageScene(String resourceName, Stage stage) {
        try {
            var loader = new FXMLLoader(EmoCore.class.getResource(resourceName));
            var scene = new Scene(loader.load());
            var controller = loader.getController();
            trySubscribeControllerAsLanguageUpdatable(controller);
            stage.setOnCloseRequest(e -> tryUnsubscribeControllerAsLanguageUpdatable(controller));
            stage.setScene(scene);
            return controller;
        } catch (IOException ioe) {
            createErrorWithLoadingWindow();
            return null;
        }
    }

    private static boolean tryUnsubscribeControllerAsLanguageUpdatable(Object controller) {
        if (controller != null && controller instanceof LanguageUpdatable) {
            AppLocaleManager.removeLanguageUpdatableListener((LanguageUpdatable) controller);
            return true;
        }
        return false;
    }

    private static boolean trySubscribeControllerAsLanguageUpdatable(Object controller) {
        if (controller != null && controller instanceof LanguageUpdatable) {
            AppLocaleManager.addLanguageUpdatableListener((LanguageUpdatable) controller);
            return true;
        }
        return false;
    }

    /**
     * tries getting answer, shows infoWindow with error on error (tries showing only one time)
     * @throws IllegalStateException if has any errors - should be processed
     */
    public static CommandAnswer tryToGetAnswer(Command commandToSend) throws IllegalStateException {
        try {
            var commandAnswer = EmoCore.udpClient.tryToGetCommandAnswer(commandToSend);
            var registerCommandAnswerWithoutArgs = commandAnswer.getCommandAnswerWithoutArgs();
            if (registerCommandAnswerWithoutArgs.getIsError()) {
                var queryErrorDescription = registerCommandAnswerWithoutArgs.getErrorDescription();
                try {
                    createErrorInfoWindow(queryErrorDescription);
                } catch (IOException ioe) {}
                throw new IllegalStateException();
            }
            return commandAnswer;
        } catch (ConnectException e) {
            try {
                createInfoWindow("connectionErrorMsg", "Connection error");
            } catch (IOException ioe) {}
        } catch (IllegalArgumentException e) {
            try {
                createInfoWindow("badQueryAnswerMsg", "Bad answer error");
            } catch (IOException ioe) {}
        }
        throw new IllegalStateException();
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

    public static void addActualDragonsListener(ListChangeListener<DragonWithKeyAndOwner> changeListener) {
        synchronized (actualDragonsWithMeta) {
            actualDragonsWithMeta.addListener(changeListener);
        }
    }

    public static void removeActualDragonsListener(ListChangeListener<DragonWithKeyAndOwner> changeListener) {
        synchronized (actualDragonsWithMeta) {
            actualDragonsWithMeta.removeListener(changeListener);
        }
    }

    public static Image loadImage(String insideResourcePath) {
        return new Image(EmoCore.class.getResource(insideResourcePath).toString());
    }

}