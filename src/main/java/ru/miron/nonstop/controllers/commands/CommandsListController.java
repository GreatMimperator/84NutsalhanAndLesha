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
import ru.miron.nonstop.logic.commands.specificAnswers.WingspansSumGettingCommandSpecificAnswer;

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
    @FXML
    public Button clearOwnedDragonsButton;
    @FXML
    public Button removeOnCmpButton;
    @FXML
    public Button replaceIfGreaterButton;
    @FXML
    public Button wingspanSumGettingButton;
    @FXML
    public Button dragonsGettingButton;
    @FXML
    public Button backButton;

    public TableController tableController;

    @FXML
    public void initialize() {
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        updateLanguage();
    }

    @FXML
    public void setInsertScene(ActionEvent actionEvent) {
        EmoCore.createInsertDragonWindow();
    }

    @FXML
    public void setUpdateScene(ActionEvent actionEvent) {
        EmoCore.createUpdateDragonWindow();
    }

    @FXML
    public void setRemoveByKeyScene(ActionEvent actionEvent) {
        EmoCore.createRemoveByKeyWindow();
    }

    @FXML
    public void setReplaceIfGreaterScene(ActionEvent actionEvent) {
        EmoCore.createReplaceIfGreaterWindow();
    }

    @FXML
    public void showWingspanSum(ActionEvent actionEvent) {
        Command command = new Command(CommandName.GET_WINGSPANS_SUM, EmoCore.enterEntry, null);
        CommandAnswer wingspanGettingCommandAnswer;
        try {
            wingspanGettingCommandAnswer = EmoCore.tryToGetAnswer(command);
        } catch (IllegalStateException e) {
            return;
        }
        var wingspanGettingArgs = (WingspansSumGettingCommandSpecificAnswer) wingspanGettingCommandAnswer.getCommandSpecificAnswerObj();
        var wingspansSum = wingspanGettingArgs.getWingspansSum();
        var wingspanSumMsg = AppLocaleManager.getTextByLabel("wingspanSumMsg").formatted(wingspansSum);
        try {
            EmoCore.createInfoWindow(new LabelText(wingspanSumMsg, PLAIN_TEXT), "Wingspans sum");
        } catch (IOException e) {
            EmoCore.createErrorWithLoadingWindow();
        }
    }


    @FXML
    public void setDragonsInfoGettingScene(ActionEvent actionEvent) {
        var dragonsInfoGettingCommand = new Command(CommandName.DRAGONS_INFO, EmoCore.enterEntry, null);
        var commandAnswer = EmoCore.tryToGetAnswer(dragonsInfoGettingCommand);
        var specificAnswer = (DragonsInfoCommandSpecificAnswer) commandAnswer.getCommandSpecificAnswerObj();
        var messageFormat = "%s:\n\n";
        for (int i = 0; i < 3; i++) {
            messageFormat += "%s: %s\n";
        }
        var formattedDate = EmoCore.dateTimeFormatter.format(specificAnswer.getCreationDate());
        var message = messageFormat.formatted(
                AppLocaleManager.getTextByLabel("collectionInfoName"),
                AppLocaleManager.getTextByLabel("collectionTypeName"), specificAnswer.getCollectionType(),
                AppLocaleManager.getTextByLabel("creationDateName"), formattedDate,
                AppLocaleManager.getTextByLabel("dragonsCountName"), specificAnswer.getDragonsCount());
        try {
            EmoCore.createInfoWindow(new LabelText(message, PLAIN_TEXT), "Dragons collection info");
        } catch (IOException ioe) {}
    }

    @FXML
    public void setClearingConfirmation(ActionEvent actionEvent) {
        var login = EmoCore.enterEntry.getLogin();
        Predicate<DragonWithKeyAndOwner> notInOwn = dragonWithMeta -> !dragonWithMeta.getOwnerLogin().equals(login);
        if (EmoCore.getActualDragonsWithMeta().stream().allMatch(notInOwn)) {
            try {
                EmoCore.createInfoWindow("nothingToClearMsg", "Loading error");
            } catch (IOException ex) {}
            return;
        }
        Runnable clearAction = CommandsListController::clearOwnedDragons;
        EmoCore.createConfirmWindow(
                "clearConfirmMsg",
                "clearButtonName",
                clearAction,
                "Clearing confirmation");
    }

    public static void clearOwnedDragons() {
        var clearCommand = new Command(CommandName.CLEAR_DRAGONS, EmoCore.enterEntry, null);
        CommandAnswer clearCommandAnswer;
        try {
            clearCommandAnswer = EmoCore.tryToGetAnswer(clearCommand);
        } catch (IllegalStateException e) {
            return;
        }
        var clearArgs = (ClearDragonsCommandSpecificAnswer) clearCommandAnswer.getCommandSpecificAnswerObj();
        var clearedCount = clearArgs.getClearedCount();
        if (clearedCount == 0) {
            try {
                EmoCore.createInfoWindow("clearedNothingMsg", "Not cleared");
            } catch (IOException ioe) {}
        } else {
            try {
                var msg = AppLocaleManager.getTextByLabel("clearedCountMsg").formatted(clearedCount);
                var msgAsLabelText = new LabelText(msg, PLAIN_TEXT);
                EmoCore.createInfoWindow(msgAsLabelText, "Not cleared");
            } catch (IOException ioe) {}
        }
    }

    @FXML
    public void setRemoveOnCmpScene() {
        EmoCore.createRemoveOnCmpWindow();
    }


    @FXML
    public void updateDragons(ActionEvent actionEvent) {
        try {
            tableController.updateTableContentsTask().run();
        } catch (IllegalStateException e) {
            try {
                EmoCore.createInfoWindow("tableViewClosedMsg", "Table view closed");
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

    public void setParent(TableController tableController) {
        this.tableController = tableController;
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
        ElementsLocaleSetter.setLocalizedText(removeOnCmpButton, "removeOnCmpButtonName");
        ElementsLocaleSetter.setLocalizedText(replaceIfGreaterButton, "replaceIfGreaterButtonName");
        ElementsLocaleSetter.setLocalizedText(wingspanSumGettingButton, "wingspanSumButtonName");
    }

}
