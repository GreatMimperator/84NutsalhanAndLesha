package ru.miron.nonstop.givenClasses;

import javafx.scene.control.ChoiceBox;

public enum DragonType {
    WATER,
    UNDERGROUND,
    AIR,
    FIRE;

    /**
     * @throws IllegalArgumentException if couldn't define DragonType 
     */
    public static DragonType getByName(String name) {
        return switch (name.toLowerCase()) {
            case "water" -> WATER;
            case "underground" -> UNDERGROUND;
            case "air" -> AIR;
            case "fire" -> FIRE;
            default -> throw new IllegalArgumentException();
        };
    }

    public String getName() {
        return switch (this) {
            case WATER -> "water";
            case UNDERGROUND -> "underground";
            case AIR -> "air";
            case FIRE -> "fire"; 
        };
    }

    public static DragonType getSelected(ChoiceBox<DragonType> typeChoiceBox) {
        return typeChoiceBox.getSelectionModel().selectedItemProperty().get();
    }

}