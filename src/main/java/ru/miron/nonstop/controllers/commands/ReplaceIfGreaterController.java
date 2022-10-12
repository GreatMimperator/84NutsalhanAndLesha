package ru.miron.nonstop.controllers.commands;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.controllers.Utils;
import ru.miron.nonstop.controllers.Validate;
import ru.miron.nonstop.givenClasses.*;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.args.ReplaceIfDragonIsGreaterCommandArgs;
import ru.miron.nonstop.logic.commands.args.UpdateCommandArgs;

import java.io.IOException;

import static ru.miron.nonstop.controllers.Validate.ErrorVariant;
import static ru.miron.nonstop.controllers.Validate.ErrorVariant.NONE;

public class ReplaceIfGreaterController implements LanguageUpdatable {
    public HBox replaceIfGreaterPane;
    public Label keyLabel;
    public TextField keyField;
    public Label keyErrorLabel;
    public Button fillWithPreviousDataButton;
    public Label nameLabel;
    public TextField nameField;
    public Label nameErrorLabel;
    public Label ageLabel;
    public TextField ageField;
    public Label ageErrorLabel;
    public Label descriptionLabel;
    public TextArea descriptionArea;
    public Label descriptionErrorLabel;
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
    public Button replaceIfGreaterButton;
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


    @FXML
    public void initialize() {
        System.out.println("Inited update dragon controller");
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

    @FXML
    public void fillWithPreviousData(ActionEvent actionEvent) {
        keyErrorLabelVariant = keyWithErrorLabelProcess.updateErrorLabel();
        if (keyErrorLabelVariant == NONE) {
            var key = keyField.getText().trim();
            DragonWithKeyAndOwner previousDragon = EmoCore.getActualDragonsWithMetaByKey(key);
            if (previousDragon == null) {
                try {
                    EmoCore.createInfoWindow("hasNotCashedDragonWithKeyMsg", "Hasn't cached dragon with this id");
                } catch (IOException e) {}
                return;
            }
            var nameOfPrevious = previousDragon.getDragon().getName();
            var xCoordinateOfPrevious = previousDragon.getDragon().getCoordinates().getX();
            var yCoordinateOfPrevious = previousDragon.getDragon().getCoordinates().getY();
            var ageOfPrevious = previousDragon.getDragon().getAge();
            var descriptionOfPrevious = previousDragon.getDragon().getDescription();
            var wingspanOfPrevious = previousDragon.getDragon().getWingspan();
            var typeOfPrevious = previousDragon.getDragon().getType();
            var treasuresOfPrevious = previousDragon.getDragon().getCave().getNumberOfTreasures();
            nameField.setText(nameOfPrevious);
            xField.setText(Float.toString(xCoordinateOfPrevious));
            yField.setText(Long.toString(yCoordinateOfPrevious));
            ageField.setText(Long.toString(ageOfPrevious));
            descriptionArea.setText(descriptionOfPrevious);
            wingspanField.setText(Integer.toString(wingspanOfPrevious));
            typeChoiceBox.setValue(typeOfPrevious);
            treasuresField.setText(Float.toString(treasuresOfPrevious));
            try {
                EmoCore.createInfoWindow("cashedDragonLoadedUsingKeyMsg", "Cached dragon loaded with this key");
            } catch (IOException e) {}
        } else {
            try {
                EmoCore.createInfoWindow("wrongKeyOfCashedDragonMsg", "Wrong cached dragon key");
            } catch (IOException e) {}
        }
    }

    public void clearInputs(ActionEvent actionEvent) {
        nameField.clear();
        ageField.clear();
        descriptionArea.clear();
        keyField.clear();
        xField.clear();
        yField.clear();
        wingspanField.clear();
        try {
            Utils.resetTypeChoiceBox(typeChoiceBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
        treasuresField.clear();
    }

    public void tryReplaceDragon(ActionEvent actionEvent) {
        if (checkInputsAndShowIfBad()) {
            System.out.println("Update dragons fields are bad. So, wont send any data");
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
            var replaceCommand = new Command(CommandName.UPDATE_DRAGON, EmoCore.enterEntry, new ReplaceIfDragonIsGreaterCommandArgs(key, dragonWithKeyAndOwner));
            CommandAnswer replaceCommandAnswer;
            try {
                replaceCommandAnswer = EmoCore.tryToGetAnswer(replaceCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var replacedState = replaceCommandAnswer.getCommandAnswerWithoutArgs().getState();
            switch (replacedState) {
                case "replaced" -> {
                    try {
                        EmoCore.createInfoWindow("dragonReplacedMsg", "Updated");
                    } catch (IOException ioe) {}
                }
                case "wrong key" -> {
                    try {
                        EmoCore.createInfoWindow("wrongKeyOnUpdateMsg", "Wrong id");
                    } catch (IOException ioe) {}
                }
                case "not yours" -> {
                    try {
                        EmoCore.createInfoWindow("notYoursDragonMsg", "Not yours");
                    } catch (IOException ioe) {}
                }
                default -> {
                    throw  new IllegalStateException("Wrong state got");
                }
            }
        }
    }

    public void setKeyTextFieldValue(String key) {
        keyField.setText(key);
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
        return (Stage) replaceIfGreaterPane.getScene().getWindow();
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setLocalizedText(fillWithPreviousDataButton, "filledWithPreviousDataButtonName");
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
        ElementsLocaleSetter.setLocalizedText(clearInputsButton, "clearInputsButtonName");
        ElementsLocaleSetter.setLocalizedText(replaceIfGreaterButton, "replaceIfGreaterButtonName");
        ElementsLocaleSetter.setLocalizedText(exitButton, "exitButtonName");
    }
}