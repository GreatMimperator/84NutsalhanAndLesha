package ru.miron.nonstop.controllers.commands;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.controllers.Validate;
import ru.miron.nonstop.givenClasses.*;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.args.InsertCommandArgs;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static ru.miron.nonstop.controllers.Validate.*;

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

    public List<DragonType> choiceBoxDragonTypes;

    public SimpleStringErrorLabelVariant keyErrorLabelVariant;
    public SimpleStringErrorLabelVariant nameErrorLabelVariant;
    public PositiveWholeNumberErrorLabelVariant ageErrorLabelVariant;
    public SimpleStringErrorLabelVariant descriptionErrorLabelVariant;
    public FloatErrorLabelVariant xErrorLabelVariant;
    public WholeNumberErrorLabelVariant yErrorLabelVariant;
    public PositiveWholeNumberErrorLabelVariant wingspanErrorLabelVariant;
    public NotNegativeFloatErrorLabelVariant treasuresErrorLabelVariant;

    {
        keyErrorLabelVariant = null;
        nameErrorLabelVariant = null;
        ageErrorLabelVariant = null;
        descriptionErrorLabelVariant = null;
        xErrorLabelVariant = null;
        yErrorLabelVariant = null;
        wingspanErrorLabelVariant = null;
        treasuresErrorLabelVariant = null;
    }

    @FXML
    public void initialize() {
        System.out.println();
        fillTypeChoiceBox();
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        updateLanguage();
    }

    public void fillTypeChoiceBox() {
        choiceBoxDragonTypes = FXCollections.observableList(new LinkedList<>());
        for (var dragonType : DragonType.values()) {
            choiceBoxDragonTypes.add(dragonType);
        }
        typeChoiceBox.setItems((ObservableList<DragonType>) choiceBoxDragonTypes);
        typeChoiceBox.setValue(DragonType.values()[0]);
        typeChoiceBox.getItems().forEach(System.out::println);
    }


    public void onClearInputsBtnAction(ActionEvent actionEvent) {
        nameField.clear();
        ageField.clear();
        descriptionArea.clear();
        keyField.clear();
        xField.clear();
        yField.clear();
        wingspanField.clear();
        // somehow clear type? todo:
        treasuresField.clear();
    }

    public void onInsertDragonBtnAction(ActionEvent actionEvent) {
        if (checkInputsAndShowIfBad()) {
            System.out.println("Insert dragons fields are bad. So, wont send any data");
            try {
                EmoCore.createInfoAutoClosableWindow("badEnteredInfoMsg", "Bad entered info");
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
            var type = DragonType.AIR;
            var cave = new DragonCave(Float.parseFloat(treasuresField.getText()));
            var dragon = new Dragon(null, name, coordinates, null, age, description, wingspan, type, cave);
            var dragonWithKeyAndOwner = new DragonWithKeyAndOwner(dragon, key, null);
            var insertCommand = new Command(CommandName.INSERT_DRAGON, EmoCore.enterEntry, new InsertCommandArgs(dragonWithKeyAndOwner));
            CommandAnswer insertCommandAnswer;
            try {
                insertCommandAnswer = EmoCore.tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(insertCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var insertState = insertCommandAnswer.getCommandAnswerWithoutArgs().getState();
            switch (insertState) {
                case "inserted" -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("dragonInsertedMsg", "Inserted");
                    } catch (IOException ioe) {}
                }
                case "dublicate key" -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("duplicateKeyOnInsertMsg", "Dublicate key");
                    } catch (IOException ioe) {}
                }
                default -> {
                    throw  new IllegalStateException("Wrong state got");
                }
            }
        }
    }

    private boolean checkInputsAndShowIfBad() {
        nameErrorLabelVariant =
                Validate.validateSimpleStringAndShowTextLabelWithErrorIfBad(nameField, nameErrorLabel, 80);
        ageErrorLabelVariant =
                Validate.validatePositiveIntegerAndShowTextLabelWithErrorIfBad(ageField, ageErrorLabel);
        descriptionErrorLabelVariant =
                Validate.validateSimpleStringAndShowTextLabelWithErrorIfBad(descriptionArea.getText(), descriptionErrorLabel, 240);
        keyErrorLabelVariant =
                Validate.validateSimpleStringAndShowTextLabelWithErrorIfBad(keyField, keyErrorLabel, 80);
        xErrorLabelVariant =
                Validate.validateFloatFieldAndShowTextLabelWithErrorIfBad(xField, xErrorLabel);
        yErrorLabelVariant =
                Validate.validateLongAndShowTextLabelWithErrorIfBad(yField, yErrorLabel);
        wingspanErrorLabelVariant =
                Validate.validatePositiveLongAndShowTextLabelWithErrorIfBad(wingspanField, wingspanErrorLabel);
        treasuresErrorLabelVariant =
                Validate.validateNotNegativeFloatFieldAndShowTextLabelWithErrorIfBad(treasuresField, treasuresErrorLabel);
        return nameErrorLabelVariant != null ||
                ageErrorLabelVariant != null ||
                descriptionErrorLabelVariant != null ||
                keyErrorLabelVariant != null ||
                xErrorLabelVariant != null ||
                yErrorLabelVariant != null ||
                wingspanErrorLabelVariant != null ||
                treasuresErrorLabelVariant != null;
    }

    public void onExitBtnAction(ActionEvent actionEvent) {
        getStage().close();
    }

    public Stage getStage() {
        return (Stage) insertDragonPane.getScene().getWindow();
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(nameLabel, "nameLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(nameField, "nameFieldPromptText");
        Validate.setSimpleStringErrorLabelInCurrentLanguageIfHasVariant(nameErrorLabel, nameErrorLabelVariant, 80);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(ageLabel, "ageLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(ageField, "ageFieldPromptText");
        Validate.setPositiveWholeNumberErrorLabelInCurrentLanguageIfHasVariant(ageErrorLabel, ageErrorLabelVariant);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(descriptionLabel, "descriptionLabelName");
        ElementsLocaleSetter.setTextAreaPromptTextInCurrentLanguage(descriptionArea, "descriptionAreaPromptText");
        Validate.setSimpleStringErrorLabelInCurrentLanguageIfHasVariant(descriptionErrorLabel, descriptionErrorLabelVariant, 240);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(keyLabel, "keyLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(keyField, "keyFieldPromptText");
        Validate.setSimpleStringErrorLabelInCurrentLanguageIfHasVariant(keyErrorLabel, keyErrorLabelVariant, 80);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(xLabel, "xLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(xField, "xFieldPromptText");
        Validate.setFloatErrorLabelInCurrentLanguageIfHasVariant(xErrorLabel, xErrorLabelVariant);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(yLabel, "yLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(yField, "yFieldPromptText");
        Validate.setWholeNumberErrorLabelInCurrentLanguageIfHasVariant(yErrorLabel, yErrorLabelVariant);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(wingspanLabel, "wingspanLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(wingspanField, "wingspanFieldPromptText");
        Validate.setPositiveWholeNumberErrorLabelInCurrentLanguageIfHasVariant(wingspanErrorLabel, wingspanErrorLabelVariant);
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(typeLabel, "typeLabelName");
//        ElementsLocaleSetter.setChoiceBoxInCurrentLanguage(typeChoiceBox, )
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(treasuresLabel, "treasuresLabelName");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(treasuresField, "treasureFieldPromptText");
        Validate.setNotNegativeFloatErrorLabelInCurrentLanguageIfHasVariant(treasuresErrorLabel, treasuresErrorLabelVariant);
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(insertDragonButton, "insertDragonsButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(clearInputsButton, "clearInputsButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(exitButton, "exitButtonName");
    }

}
