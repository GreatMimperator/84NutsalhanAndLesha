package ru.miron.nonstop.givenClasses;

import java.time.ZonedDateTime;
import java.util.Map;

import javafx.beans.property.*;
import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

/**
 * Contains required {@code Dragon} with optional {@code key} and {@code ownerLogin}
 */
public class DragonWithKeyAndOwner implements JSONConvertable {
    /**
     * Cannot be {@code null}
     */
    private Dragon dragon;
    /**
     * Can be {@code null} if process doesn't need it
     * Cannot be blank
     */
    private String key;
    /**
     * Can be {@code null} if process doesn't need it 
     */
    private String ownerLogin;

    /**
     * for creating own f. methods 
     */
    private DragonWithKeyAndOwner() {}

    /**
     * @param dragon not {@code null} value
     * @param key can be {@code null}
     * @param ownerLogin can be {@code null}
     * 
     * @throws IllegalArgumentException if dragon is {@code null}
     */
    public DragonWithKeyAndOwner(Dragon dragon, String key, String ownerLogin) throws IllegalArgumentException {
        setDragon(dragon);
        setKey(key);
        setOwnerLogin(ownerLogin);
    }

    /**
     * @param dragon not {@code null} value
     * 
     * @throws IllegalArgumentException if dragon is {@code null}
     */
    public DragonWithKeyAndOwner(Dragon dragon) throws IllegalArgumentException {
        this(dragon, null, null);
    }

    public Dragon getDragon() {
        return dragon;
    }

    /**
     * @param dragon not {@code null} value
     * 
     * @throws IllegalArgumentException if dragon is {@code null}
     */
    public void setDragon(Dragon dragon) throws IllegalArgumentException {
        if (dragon == null) {
            throw new IllegalArgumentException();
        }
        this.dragon = dragon;
    }

    public String getKey() {
        return key;
    }

    /**
     * @param key can be {@code null} if process doesn't need it
     */
    public void setKey(String key) {
        if (key.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.key = key;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    /**
     * @param ownerLogin can be {@code null} if process doesn't need it
     */
    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        if (ownerLogin != null) {
            root.put("owner login", new JSONString(ownerLogin));
        }
        if (key != null) {
            root.put("key", new JSONString(key));
        }
        root.putValues(dragon.toJSON().asMap());
        return root;
    }

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public static DragonWithKeyAndOwner initFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        var dragonWithKeyAndOwner = new DragonWithKeyAndOwner();
        var keyJSONValue = root.get("key");
        var ownerLoginJSONValue = root.get("owner login");
        try {
            if (keyJSONValue != null) {
                dragonWithKeyAndOwner.setKey(keyJSONValue.asString().getValue());
            }
            if (ownerLoginJSONValue != null) {
                dragonWithKeyAndOwner.setOwnerLogin(ownerLoginJSONValue.asString().getValue());
            }
            dragonWithKeyAndOwner.setDragon(Dragon.initFromTheRoot(root));
            return dragonWithKeyAndOwner;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isFull() {
        return ownerLogin != null && key != null && dragon.isFull();
    }

    public boolean hasKey() {
        return key != null; 
    }

    public StringProperty ownerProperty() {
        return new SimpleStringProperty(ownerLogin);
    }

    public LongProperty idProperty() {
        return new SimpleLongProperty(dragon.getId());
    }

    public StringProperty keyProperty() {
        return new SimpleStringProperty(getKey());
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(dragon.getName());
    }

    public FloatProperty xProperty() {
        return new SimpleFloatProperty(dragon.getCoordinates().getX());
    }

    public LongProperty yProperty() {
        return new SimpleLongProperty(dragon.getCoordinates().getY());
    }

    public ObjectProperty<ZonedDateTime> dateProperty() {
        return new SimpleObjectProperty<>(dragon.getCreationDate());
    }

    public LongProperty ageProperty() {
        return new SimpleLongProperty(dragon.getAge());
    }

    public StringProperty descriptionProperty() {
        return new SimpleStringProperty(dragon.getDescription());
    }

    public IntegerProperty wingspanProperty() {
        return new SimpleIntegerProperty(dragon.getWingspan());
    }

    public StringProperty typeProperty() {
        return new SimpleStringProperty(getCapitalizedType());
    }

    public FloatProperty treasuresProperty() {
        return new SimpleFloatProperty(dragon.getCave().getNumberOfTreasures());
    }

    public String getCapitalizedType() {
        return capitalize(dragon.getType().getName());
    }

    private static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
