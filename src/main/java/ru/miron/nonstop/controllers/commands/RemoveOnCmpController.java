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
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.args.RemoveLowerOrGreaterDragonsCommandArgs;
import ru.miron.nonstop.logic.commands.args.UpdateCommandArgs;
import ru.miron.nonstop.logic.commands.specificAnswers.RemoveGreaterOrLowerDragonsCommandSpecificAnswer;

import java.io.IOException;

import static ru.miron.nonstop.controllers.Validate.ErrorVariant;
import static ru.miron.nonstop.controllers.Validate.ErrorVariant.NONE;
import static ru.miron.nonstop.locales.entities.LabelText.TextType.PLAIN_TEXT;

public class RemoveOnCmpController implements LanguageUpdatable {
    public HBox removeOnCmpPane;
    public Label idLabel;
    public TextField idField;
    public Button fillWithPreviousDataButton;
    public Label idErrorLabel;
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
    public Button updateDragonButton;
    public Button clearInputsButton;
    public Button removeGreaterButton;
    public Button removeLowerButton;
    public ChoiceBox<String> languageSelector;
    public Button exitButton;

    public ErrorVariant idErrorLabelVariant;
    public ErrorVariant keyErrorLabelVariant;
    public ErrorVariant nameErrorLabelVariant;
    public ErrorVariant ageErrorLabelVariant;
    public ErrorVariant descriptionErrorLabelVariant;
    public ErrorVariant xErrorLabelVariant;
    public ErrorVariant yErrorLabelVariant;
    public ErrorVariant wingspanErrorLabelVariant;
    public ErrorVariant treasuresErrorLabelVariant;

    private Validate.InputWithErrorLabelProcess idWithErrorLabelProcess;
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
        idErrorLabelVariant = NONE;
        keyErrorLabelVariant = NONE;
        nameErrorLabelVariant = NONE;
        ageErrorLabelVariant = NONE;
        descriptionErrorLabelVariant = NONE;
        xErrorLabelVariant = NONE;
        yErrorLabelVariant = NONE;
        wingspanErrorLabelVariant = NONE;
        treasuresErrorLabelVariant = NONE;

        idWithErrorLabelProcess = Validate.Numbers.initPositiveLongProcessor(idField, idErrorLabel);
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
        idErrorLabelVariant = idWithErrorLabelProcess.updateErrorLabel();
        if (idErrorLabelVariant == NONE) {
            var id = Long.parseLong(idField.getText());
            DragonWithKeyAndOwner previousDragon = EmoCore.getActualDragonsWithMetaById(id);
            if (previousDragon == null) {
                try {
                    EmoCore.createInfoWindow("hasNotCashedDragonWithIdMsg", "Hasn't cached dragon with this id");
                } catch (IOException e) {}
                return;
            }
            var keyOfPrevious = previousDragon.getKey();
            var nameOfPrevious = previousDragon.getDragon().getName();
            var xCoordinateOfPrevious = previousDragon.getDragon().getCoordinates().getX();
            var yCoordinateOfPrevious = previousDragon.getDragon().getCoordinates().getY();
            var ageOfPrevious = previousDragon.getDragon().getAge();
            var descriptionOfPrevious = previousDragon.getDragon().getDescription();
            var wingspanOfPrevious = previousDragon.getDragon().getWingspan();
            var typeOfPrevious = previousDragon.getDragon().getType();
            var treasuresOfPrevious = previousDragon.getDragon().getCave().getNumberOfTreasures();
            keyField.setText(keyOfPrevious);
            nameField.setText(nameOfPrevious);
            xField.setText(Float.toString(xCoordinateOfPrevious));
            yField.setText(Long.toString(yCoordinateOfPrevious));
            ageField.setText(Long.toString(ageOfPrevious));
            descriptionArea.setText(descriptionOfPrevious);
            wingspanField.setText(Integer.toString(wingspanOfPrevious));
            typeChoiceBox.setValue(typeOfPrevious);
            treasuresField.setText(Float.toString(treasuresOfPrevious));
            try {
                EmoCore.createInfoWindow("cashedDragonLoadedUsingIdMsg", "Cached dragon loaded with this id");
            } catch (IOException e) {}
        } else {
            try {
                EmoCore.createInfoWindow("wrongIdOfCashedDragonMsg", "Wrong cached dragon id");
            } catch (IOException e) {}
        }
    }

    public void clearInputs(ActionEvent actionEvent) {
        idField.clear();
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

    @FXML
    public void tryRemoveGreater() {
        tryRemoveGreaterOrLower(CommandName.REMOVE_GREATER_DRAGONS);
    }

    @FXML
    public void tryRemoveLower() {
        tryRemoveGreaterOrLower(CommandName.REMOVE_GREATER_DRAGONS);
    }

    public void tryRemoveGreaterOrLower(CommandName commandName) {
        if (commandName != CommandName.REMOVE_GREATER_DRAGONS && commandName != CommandName.REMOVE_LOWER_DRAGONS) {
            throw new IllegalArgumentException();
        }
        if (checkInputsAndShowIfBad()) {
            System.out.println("Remove dragons fields are bad. So, wont send any data");
            try {
                EmoCore.createInfoWindow("badEnteredInfoMsg", "Bad entered info");
            } catch (IOException e) {}
        } else {
            System.out.println("Dragon fields are good. So, will send to server");
            var id = Long.parseLong(idField.getText());
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
            var removeOnCompareCommand = new Command(commandName, EmoCore.enterEntry, new UpdateCommandArgs(id, dragonWithKeyAndOwner));
            CommandAnswer removeOnCompareAnswer;
            try {
                removeOnCompareAnswer = EmoCore.tryToGetAnswer(removeOnCompareCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var removeOnCmpAnswerObj = (RemoveGreaterOrLowerDragonsCommandSpecificAnswer) removeOnCompareAnswer.getCommandSpecificAnswerObj();
            var removedCount = removeOnCmpAnswerObj.getRemovedCount();
            if (removedCount == 0) {
                try {
                    EmoCore.createInfoWindow("clearedNothingMsg", "Not cleared");
                } catch (IOException ioe) {}
            } else {
                try {
                    var msg = AppLocaleManager.getTextByLabel("clearedCountMsg").formatted(removedCount);
                    var msgAsLabelText = new LabelText(msg, PLAIN_TEXT);
                    EmoCore.createInfoWindow(msgAsLabelText, "Cleared");
                } catch (IOException ioe) {}
            }
        }
    }

    public void setIdTextFieldValue(long id) {
        idField.setText(Long.toString(id));
    }


    private boolean checkInputsAndShowIfBad() {
        idErrorLabelVariant = idWithErrorLabelProcess.updateErrorLabel();
        nameErrorLabelVariant = nameWithErrorLabelProcess.updateErrorLabel();
        ageErrorLabelVariant = ageWithErrorLabelProcess.updateErrorLabel();
        descriptionErrorLabelVariant = descriptionWithErrorLabelProcess.updateErrorLabel();
        keyErrorLabelVariant = keyWithErrorLabelProcess.updateErrorLabel();
        xErrorLabelVariant = xCoordinateWithErrorLabelProcess.updateErrorLabel();
        yErrorLabelVariant = yCoordinateWithErrorLabelProcess.updateErrorLabel();
        wingspanErrorLabelVariant = wingspanWithErrorLabelProcess.updateErrorLabel();
        treasuresErrorLabelVariant = treasuresWithErrorLabelProcess.updateErrorLabel();
        return idErrorLabelVariant != NONE ||
                nameErrorLabelVariant != NONE ||
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
        return (Stage) removeOnCmpPane.getScene().getWindow();
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setLocalizedText(idLabel, "idLabelName");
        ElementsLocaleSetter.setLocalizedPromptText(idField, "positiveWholeNumberFieldPromptText");
        idWithErrorLabelProcess.setLocalizedErrorLabelIfHas(idErrorLabelVariant);
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
        ElementsLocaleSetter.setLocalizedText(removeGreaterButton, "removeGreaterDragonsButtonName");
        ElementsLocaleSetter.setLocalizedText(removeLowerButton, "removeLowerDragonsButtonName");
        ElementsLocaleSetter.setLocalizedText(exitButton, "exitButtonName");
    }

}
