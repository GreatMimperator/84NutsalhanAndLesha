package ru.miron.nonstop.controllers.commands;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.controllers.TableController;
import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.specificAnswers.ClearDragonsCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.DragonsInfoCommandSpecificAnswer;

import java.io.IOException;
import java.util.function.Predicate;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.PLAIN_TEXT;

public class CommandsListController implements LanguageUpdatable {
    @FXML
    public VBox commandsListPane;
    public ChoiceBox<String> languageSelector;
    @FXML
    public Button insertDragonButton;
    @FXML
    public Button updateDragonButton;
    @FXML
    public Button removeByKeyButton;
    @FXML
    public Button dragonsInfoGettingButton;
    public Button clearOwnedDragonsButton;
    @FXML
    public Button dragonsGettingButton;
    @FXML
    public Button backButton;

    @FXML
    public void initialize() {
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        updateLanguage();
    }

    @FXML
    public void setInsertScene(ActionEvent actionEvent) {
        EmoCore.setInsertDragonAsStageScene(getStage());
    }

    @FXML
    public void setUpdateScene(ActionEvent actionEvent) {
        EmoCore.setUpdateDragonAsStageScene(getStage());
    }

    @FXML
    public void setRemoveByKeyScene(ActionEvent actionEvent) {
        EmoCore.setRemoveByKeyAsStageScene(getStage());
    }

    @FXML
    public void setDragonsInfoGettingScene(ActionEvent actionEvent) {
        var dragonsInfoGettingCommand = new Command(CommandName.DRAGONS_INFO, EmoCore.enterEntry, null);
        var commandAnswer = EmoCore.tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(dragonsInfoGettingCommand);
        var specificAnswer = (DragonsInfoCommandSpecificAnswer) commandAnswer.getCommandSpecificAnswerObj();
        var messageFormat = "%s:\n\n";
        for (int i = 0; i < 3; i++) {
            messageFormat += "%s: %s\n";
        }
        var formattedDate = TableController.dateTimeFormatter.format(specificAnswer.getCreationDate());
        var message = messageFormat.formatted(
                AppLocaleManager.getTextByLabel("collectionInfoName"),
                AppLocaleManager.getTextByLabel("collectionTypeName"), specificAnswer.getCollectionType(),
                AppLocaleManager.getTextByLabel("creationDateName"), formattedDate,
                AppLocaleManager.getTextByLabel("dragonsCountName"), specificAnswer.getDragonsCount());
        try {
            EmoCore.createInfoAutoClosableWindow(new LabelText(message, PLAIN_TEXT), "Dragons collection info");
        } catch (IOException ioe) {}
    }

    @FXML
    public void setClearingConfirmation(ActionEvent actionEvent) {
        var login = EmoCore.enterEntry.getLogin();
        Predicate<DragonWithKeyAndOwner> notInOwn = dragonWithMeta -> !dragonWithMeta.getOwnerLogin().equals(login);
        if (EmoCore.getActualDragonsWithMeta().stream().allMatch(notInOwn)) {
            try {
                EmoCore.createInfoAutoClosableWindow("nothingToClearMsg", "Loading error");
            } catch (IOException ex) {}
            return;
        }
        try {
            Runnable clearAction = CommandsListController::clearOwnedDragons;
            EmoCore.createConfirmAutoClosableWindow(
                    "clearConfirmMsg",
                    "clearButtonName",
                    clearAction,
                    "Clearing confirmation");
        } catch (IOException e) {
            try {
                EmoCore.createInfoAutoClosableWindow("errorWithSceneLoadingMsg", "Loading error");
            } catch (IOException ex) {}
        }
    }

    public static void clearOwnedDragons() {
        var clearCommand = new Command(CommandName.CLEAR_DRAGONS, EmoCore.enterEntry, null);
        CommandAnswer clearCommandAnswer;
        try {
            clearCommandAnswer = EmoCore.tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(clearCommand);
        } catch (IllegalStateException e) {
            return;
        }
        var clearArgs = (ClearDragonsCommandSpecificAnswer) clearCommandAnswer.getCommandSpecificAnswerObj();
        var clearedCount = clearArgs.getClearedCount();
        if (clearedCount == 0) {
            try {
                EmoCore.createInfoAutoClosableWindow("clearedNothingMsg", "Not cleared");
            } catch (IOException ioe) {}
        } else {
            try {
                var msg = AppLocaleManager.getTextByLabel("clearedCountMsg").formatted(clearedCount);
                var msgAsLabelText = new LabelText(msg, PLAIN_TEXT);
                EmoCore.createInfoAutoClosableWindow(msgAsLabelText, "Not cleared");
            } catch (IOException ioe) {}
        }
    }


    @FXML
    public void updateDragons(ActionEvent actionEvent) {
        try {
            var tableController = EmoCore.tryGettingMainStageTableController();
            tableController.updateTableContentsTask().run();
        } catch (IllegalStateException e) {
            try {
                EmoCore.createInfoAutoClosableWindow("tableViewClosedMsg", "Table view closed");
            } catch (IOException ioe) {}
        }
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        getStage().close();
    }

    public Stage getStage() {
        return (Stage) commandsListPane.getScene().getWindow();
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setLocalizedText(dragonsInfoGettingButton, "dragonsInfoGettingButtonName");
        ElementsLocaleSetter.setLocalizedText(dragonsGettingButton, "dragonsGettingButtonName");
        ElementsLocaleSetter.setLocalizedText(backButton, "backButtonLabel");
        ElementsLocaleSetter.setLocalizedText(insertDragonButton, "insertDragonButtonName");
        ElementsLocaleSetter.setLocalizedText(updateDragonButton, "updateDragonButtonName");
        ElementsLocaleSetter.setLocalizedText(removeByKeyButton, "removeByKeyButtonName");
        ElementsLocaleSetter.setLocalizedText(clearOwnedDragonsButton, "clearOwnedDragonsButton");
    }

}
