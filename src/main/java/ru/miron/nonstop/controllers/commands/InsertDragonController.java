package ru.miron.nonstop.controllers.commands;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.controllers.TableController;
import ru.miron.nonstop.controllers.Utils;
import ru.miron.nonstop.controllers.Validate;
import ru.miron.nonstop.givenClasses.*;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.args.InsertCommandArgs;

import java.io.IOException;

import static ru.miron.nonstop.controllers.Validate.ErrorVariant;
import static ru.miron.nonstop.controllers.Validate.ErrorVariant.NONE;

public class InsertDragonController implements LanguageUpdatable {
    public HBox insertDragonPane;
    public Label nameLabel;
    public TextField nameField;
    public Label nameErrorLabel;
    public Label ageLabel;
    public TextField ageField;
    public Label ageErrorLabel;
    public Label descriptionLabel;
    public TextArea descriptionArea;
    public Label descriptionErrorLabel;
    public Label keyLabel;
    public TextField keyField;
    public Label keyErrorLabel;
    public Label xLabel;
    public TextField xField;
    public Label xErrorLabel;
    public Label yLabel;
    public TextField yField;
    public Label yErrorLabel;
    public Label wingspanLabel;
    public TextField wingspanField;
    public Label wingspanErrorLabel;
    public Label typeLabel;
    public ChoiceBox<DragonType> typeChoiceBox;
    public Label treasuresLabel;
    public TextField treasuresField;
    public Label treasuresErrorLabel;
    public Button insertDragonButton;
    public Button clearInputsButton;
    public ChoiceBox<String> languageSelector;
    public Button exitButton;

    public ErrorVariant keyErrorLabelVariant;
    public ErrorVariant nameErrorLabelVariant;
    public ErrorVariant ageErrorLabelVariant;
    public ErrorVariant descriptionErrorLabelVariant;
    public ErrorVariant xErrorLabelVariant;
    public ErrorVariant yErrorLabelVariant;
    public ErrorVariant wingspanErrorLabelVariant;
    public ErrorVariant treasuresErrorLabelVariant;

    private Validate.InputWithErrorLabelProcess keyWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess nameWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess ageWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess descriptionWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess xCoordinateWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess yCoordinateWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess wingspanWithErrorLabelProcess;
    private Validate.InputWithErrorLabelProcess treasuresWithErrorLabelProcess;

    private CommandsListController commandsListController;

    @FXML
    public void initialize() {
        System.out.println("Inited insert dragon controller");
        initErrorVariantsInfrastructure();
        Utils.resetTypeChoiceBox(typeChoiceBox);
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        updateLanguage();
    }

    private void initErrorVariantsInfrastructure() {
        keyErrorLabelVariant = NONE;
        nameErrorLabelVariant = NONE;
        ageErrorLabelVariant = NONE;
        descriptionErrorLabelVariant = NONE;
        xErrorLabelVariant = NONE;
        yErrorLabelVariant = NONE;
        wingspanErrorLabelVariant = NONE;
        treasuresErrorLabelVariant = NONE;

        keyWithErrorLabelProcess = Validate.SimpleString.initProcessor(keyField, keyErrorLabel, 1, 80);
        nameWithErrorLabelProcess = Validate.SimpleString.initProcessor(nameField, nameErrorLabel, 1, 80);
        ageWithErrorLabelProcess = Validate.Numbers.initPositiveLongProcessor(ageField, ageErrorLabel);
        descriptionWithErrorLabelProcess = Validate.SimpleString.initProcessor(descriptionArea, descriptionLabel, 240);
        xCoordinateWithErrorLabelProcess = Validate.Numbers.initFloatOrDoubleProcessor(xField, xErrorLabel);
        yCoordinateWithErrorLabelProcess = Validate.Numbers.initLongProcessor(yField, yErrorLabel);
        wingspanWithErrorLabelProcess = Validate.Numbers.initPositiveIntegerProcessor(wingspanField, wingspanErrorLabel);
        treasuresWithErrorLabelProcess = Validate.Numbers.initNotNegativeFloatProcessor(treasuresField, treasuresErrorLabel);
    }

    public void clearInputs(ActionEvent actionEvent) {
        nameField.clear();
        ageField.clear();
        descriptionArea.clear();
        keyField.clear();
        xField.clear();
        yField.clear();
        wingspanField.clear();
        Utils.resetTypeChoiceBox(typeChoiceBox);
        treasuresField.clear();
    }

    public void tryInsert(ActionEvent actionEvent) {
        if (checkInputsAndShowIfBad()) {
            System.out.println("Insert dragons fields are bad. So, wont send any data");
            try {
                EmoCore.createInfoWindow("badEnteredInfoMsg", "Bad entered info");
            } catch (IOException e) {}
        } else {
            System.out.println("Dragon fields are good. So, will send to server");
            var key = keyField.getText();
            var name = nameField.getText();
            var xCoordinate = Float.parseFloat(xField.getText());
            var yCoordinate = Long.parseLong(yField.getText());
            var coordinates = new Coordinates(xCoordinate, yCoordinate);
            var age = Long.parseLong(ageField.getText());
            var description = descriptionArea.getText();
            var wingspan = Integer.parseInt(wingspanField.getText());
            var type = DragonType.getSelected(typeChoiceBox);
            var cave = new DragonCave(Float.parseFloat(treasuresField.getText()));
            var dragon = new Dragon(null, name, coordinates, null, age, description, wingspan, type, cave);
            var dragonWithKeyAndOwner = new DragonWithKeyAndOwner(dragon, key, null);
            var insertCommand = new Command(CommandName.INSERT_DRAGON, EmoCore.enterEntry, new InsertCommandArgs(dragonWithKeyAndOwner));
            CommandAnswer insertCommandAnswer;
            try {
                insertCommandAnswer = EmoCore.tryToGetAnswer(insertCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var insertState = insertCommandAnswer.getCommandAnswerWithoutArgs().getState();
            switch (insertState) {
                case "inserted" -> {
                    try {
                        EmoCore.createInfoWindow("dragonInsertedMsg", "Inserted");
                    } catch (IOException ioe) {}
                }
                case "dublicate key" -> {
                    try {
                        EmoCore.createInfoWindow("duplicateKeyOnInsertMsg", "Dublicate key");
                    } catch (IOException ioe) {}
                }
                default -> {
                    throw  new IllegalStateException("Wrong state got");
                }
            }
        }
    }

    private boolean checkInputsAndShowIfBad() {
        nameErrorLabelVariant = nameWithErrorLabelProcess.updateErrorLabel();
        ageErrorLabelVariant = ageWithErrorLabelProcess.updateErrorLabel();
        descriptionErrorLabelVariant = descriptionWithErrorLabelProcess.updateErrorLabel();
        keyErrorLabelVariant = keyWithErrorLabelProcess.updateErrorLabel();
        xErrorLabelVariant = xCoordinateWithErrorLabelProcess.updateErrorLabel();
        yErrorLabelVariant = yCoordinateWithErrorLabelProcess.updateErrorLabel();
        wingspanErrorLabelVariant = wingspanWithErrorLabelProcess.updateErrorLabel();
        treasuresErrorLabelVariant = treasuresWithErrorLabelProcess.updateErrorLabel();
        return nameErrorLabelVariant != NONE ||
                ageErrorLabelVariant != NONE ||
                descriptionErrorLabelVariant != NONE ||
                keyErrorLabelVariant != NONE ||
                xErrorLabelVariant != NONE ||
                yErrorLabelVariant != NONE ||
                wingspanErrorLabelVariant != NONE ||
                treasuresErrorLabelVariant != NONE;
    }

    public void openCommandsListInstead(ActionEvent actionEvent) {
        getStage().close();
    }

    public Stage getStage() {
        return (Stage) insertDragonPane.getScene().getWindow();
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setLocalizedText(nameLabel, "nameLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(nameField, "nameFieldPromptText");
        nameWithErrorLabelProcess.setLocalizedErrorLabelIfHas(nameErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(ageLabel, "ageLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(ageField, "positiveWholeNumberFieldPromptText");
        ageWithErrorLabelProcess.setLocalizedErrorLabelIfHas(ageErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(descriptionLabel, "descriptionLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(descriptionArea, "descriptionAreaPromptText");
        descriptionWithErrorLabelProcess.setLocalizedErrorLabelIfHas(descriptionErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(keyLabel, "keyLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(keyField, "keyFieldPromptText");
        keyWithErrorLabelProcess.setLocalizedErrorLabelIfHas(keyErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(xLabel, "xLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(xField, "floatFieldPromptText");
        xCoordinateWithErrorLabelProcess.setLocalizedErrorLabelIfHas(xErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(yLabel, "yLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(yField, "wholeNumberFieldPromptText");
        yCoordinateWithErrorLabelProcess.setLocalizedErrorLabelIfHas(yErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(wingspanLabel, "wingspanLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(wingspanField, "positiveWholeNumberFieldPromptText");
        wingspanWithErrorLabelProcess.setLocalizedErrorLabelIfHas(wingspanErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(typeLabel, "typeLabelName");
        ElementsLocaleSetter.setLocalizedDragonTypeChoiceBox(typeChoiceBox);
        ElementsLocaleSetter.setLocalizedText(treasuresLabel, "treasuresLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(treasuresField, "notNegativeFloatNumberFieldPromptText");
        treasuresWithErrorLabelProcess.setLocalizedErrorLabelIfHas(ageErrorLabelVariant);
        ElementsLocaleSetter.setLocalizedText(insertDragonButton, "insertDragonsButtonName");
        ElementsLocaleSetter.setLocalizedText(clearInputsButton, "clearInputsButtonName");
        ElementsLocaleSetter.setLocalizedText(exitButton, "exitButtonName");
    }

}
