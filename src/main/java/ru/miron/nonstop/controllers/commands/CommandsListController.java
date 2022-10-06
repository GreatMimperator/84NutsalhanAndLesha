package ru.miron.nonstop.controllers.commands;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.controllers.TableController;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.specificAnswers.DragonsInfoCommandSpecificAnswer;

import java.io.IOException;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.PLAIN_TEXT;

public class CommandsListController implements LanguageUpdatable {
    @FXML
    public VBox commandsListPane;
    @FXML
    public Button insertDragonButton;
    @FXML
    public Button dragonsInfoGettingButton;
    @FXML
    public Button dragonsGettingButton;
    @FXML
    public Button backButton;

    @FXML
    public void initialize() {
        updateLanguage();
    }

    @FXML
    public void insertDragonActionHandler(ActionEvent actionEvent) {
        EmoCore.setInsertDragonAsStageScene(getStage());
    }

    @FXML
    public void dragonsInfoGettingBtnActionHandler(ActionEvent actionEvent) {
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
    public void dragonsGettingBtnActionHandler(ActionEvent actionEvent) {
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
    public void backBtnActionHandler(ActionEvent actionEvent) {
        getStage().close();
    }

    public Stage getStage() {
        return (Stage) commandsListPane.getScene().getWindow();
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(dragonsInfoGettingButton, "dragonsInfoGettingButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(dragonsGettingButton, "dragonsGettingButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(backButton, "backButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(insertDragonButton, "insertDragonButtonName");
    }

}
