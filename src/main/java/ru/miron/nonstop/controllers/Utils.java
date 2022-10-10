package ru.miron.nonstop.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import ru.miron.nonstop.givenClasses.DragonType;

import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static void resetTypeChoiceBox(ChoiceBox<DragonType> typeChoiceBox) {
        List<DragonType> choiceBoxDragonTypes = FXCollections.observableList(new LinkedList<>());
        for (var dragonType : DragonType.values()) {
            choiceBoxDragonTypes.add(dragonType);
        }
        typeChoiceBox.setItems((ObservableList<DragonType>) choiceBoxDragonTypes);
        typeChoiceBox.setValue(DragonType.FIRE);
    }

}
