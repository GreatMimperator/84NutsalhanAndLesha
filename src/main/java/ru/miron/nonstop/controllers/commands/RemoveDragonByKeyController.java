package ru.miron.nonstop.controllers.commands;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.controllers.Validate;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.args.RemoveDragonByKeyCommandArgs;

import java.io.IOException;

import static ru.miron.nonstop.controllers.Validate.ErrorVariant.NONE;

public class RemoveDragonByKeyController implements LanguageUpdatable {
    public VBox removeByKeyPane;
    public ChoiceBox<String> languageSelector;
    public Label keyLabel;
    public TextField keyField;
    public Label keyErrorLabel;
    public Button removeByKeyButton;
    public Button exitButton;

    public Validate.ErrorVariant keyErrorLabelVariant;

    private Validate.InputWithErrorLabelProcess keyWithErrorLabelProcess;

    @FXML
    public void initialize() {
        System.out.println("Inited remove dragon by key controller");
        initErrorVariantInfrastructure();
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        updateLanguage();
    }


    public void initErrorVariantInfrastructure() {
        keyErrorLabelVariant = NONE;

        keyWithErrorLabelProcess = Validate.SimpleString.initProcessor(keyField, keyErrorLabel, 1, 80);
    }

    private boolean checkInputAndShowIfBad() {
        keyErrorLabelVariant = keyWithErrorLabelProcess.updateErrorLabel();
        return keyErrorLabelVariant != NONE;
    }

    public void openCommandsListInstead(ActionEvent actionEvent) {
        getStage().close();
        EmoCore.tryCreateCommandsListWindow();
    }

    public Stage getStage() {
        return (Stage) removeByKeyPane.getScene().getWindow();
    }

    public void tryRemoveByKey(ActionEvent actionEvent) {
        if (checkInputAndShowIfBad()) {
            System.out.println("Remove by key field is bad. So, wont send any data");
            try {
                EmoCore.createInfoAutoClosableWindow("badEnteredInfoMsg", "Bad entered info");
            } catch (IOException e) {}
        } else {
            System.out.println("Remove by key field is good");
            var key = keyField.getText().trim();
            var dragonWithMetaByKey = EmoCore.getActualDragonsWithMetaByKey(key);
            if (dragonWithMetaByKey == null) {
                try {
                    EmoCore.createInfoAutoClosableWindow("hasNotCashedDragonWithKeyMsg", "Bad entered info");
                } catch (IOException e) {}
                return;
            }
            var removeCommand = new Command(CommandName.REMOVE_DRAGON_BY_KEY, EmoCore.enterEntry, new RemoveDragonByKeyCommandArgs(key));
            CommandAnswer updateCommandAnswer;
            try {
                updateCommandAnswer = EmoCore.tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(removeCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var updatedState = updateCommandAnswer.getCommandAnswerWithoutArgs().getState();
            switch (updatedState) {
                case "removed" -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("dragonRemovedMsg", "Updated");
                    } catch (IOException ioe) {}
                }
                case "wrong key" -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("wrongKeyOnRemoveMsg", "Wrong id");
                    } catch (IOException ioe) {}
                }
                case "not yours" -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("notYoursDragonMsg", "Not yours");
                    } catch (IOException ioe) {}
                }
                default -> {
                    throw  new IllegalStateException("Wrong state got");
                }
            }
        }
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setLocalizedText(keyLabel, "keyLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(keyField, "keyFieldPromptText");
        keyWithErrorLabelProcess.setLocalizedErrorLabelIfHas(keyErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(removeByKeyButton, "removeByKeyButtonName");
        ElementsLocaleSetter.setLocalizedText(exitButton, "exitButtonName");
    }
}
