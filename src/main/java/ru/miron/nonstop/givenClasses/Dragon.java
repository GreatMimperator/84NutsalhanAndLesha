package ru.miron.nonstop.givenClasses;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes.Name;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.JSONIntegerNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class Dragon implements Comparable<Dragon>, JSONConvertable {
    /**
     * {@code ZonedDateTime creationDate} will be sent using this pattern.
     * @see {@link #creationDate}
     */
    public static final DateTimeFormatter SEND_FORMATER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    
    /**
     * Identifies the {@link #Dragon}
     * It is unique positive value
     * Can be {@code null} if process doesn't need it 
     */
    private Long id;

    /**
     * {@link #Dragon}'s name. Isn't empty
     */
    private String name;
    
    /**
     * {@link #Dragon}'s coordinates
     */
    private Coordinates coordinates;

    /**
     * {@link #Dragon}'s object creation date
     */
    private ZonedDateTime creationDate;

    /**
     * {@link #Dragon}'s age
     * Positive value
     */
    private long age;

    /**
     * {@link #Dragon}'s description
     * Can be empty, but not {@code null}
     */
    private String description;

    /**
     * {@link #Dragon}'s wingspan ("Размах крыла" in Russian)
     * Positive value
     */
    private int wingspan;

    /**
     * {@link #Dragon}'s type
     * Not {@code null}
     */
    private DragonType type;

    /**
     * {@link #Dragon}'s cave he owns
     * Not {@code null}
     */
    private DragonCave cave;

    /**
     * for creating own f. methods 
     */
    private Dragon() {}

    /**
     * {@link #Dragon}'s id and creation date are creating by default
     * 
     * @param id to set (format: {@link #id})
     * @param name name to set (format: {@link #name})
     * @param coordinates coordinates to set (format: {@link #coordinates})
     * @param age age to set (format: {@link #age})
     * @param description description to set (format: {@link #description})
     * @param wingspan wingspan to set (format: {@link #wingspan})
     * @param type type to set (format: {@link #type})
     * @param cave cave to set (format: {@link #cave})
     * @throws IllegalArgumentException if format is wrong
     */
    public Dragon(Long id, String name, Coordinates coordinates,
            ZonedDateTime creationDate, long age, String description,
            int wingspan, DragonType type, DragonCave cave) 
                throws IllegalArgumentException {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setAge(age);
        setDescription(description);
        setWingspan(wingspan);
        setType(type);
        setCave(cave);
    }

    public static class DragonBuilder {
        private Long id;
        private String name;
        private Coordinates coordinates;
        private ZonedDateTime creationDate;
        private long age;
        private String description;
        private int wingspan;
        private DragonType dragonType;
        private DragonCave dragonCave;

        private boolean nameSetted = false,
            coordinatesSetted = false,
            ageSetted = false,
            descriptionSetted = false,
            wingspanSetted = false,
            dragonTypeSetted = false,
            dragonCaveSetted = false;

        public DragonBuilder() {
            this.id = null;
            this.name = null;
            this.coordinates = null;
            this.creationDate = null;
            this.age = 0;
            this.description = null;
            this.wingspan = 0;
            this.dragonType = null;
            this.dragonCave = null;
        }

        /**
         * @throws IllegalStateException if hasn't setted required keys
         * @throws IllegalArgumentException if some of args has illegal format
         */
        public Dragon build() throws IllegalStateException, IllegalArgumentException {
            if (nameSetted && ageSetted && coordinatesSetted && descriptionSetted && 
                    wingspanSetted  && dragonTypeSetted && dragonCaveSetted) {
                return new Dragon(id, name, coordinates, creationDate, age, description, wingspan, dragonType, dragonCave);
            } else {
                throw new IllegalStateException("Cannot build because smth was not setted");
            }
        }

        public void setId(Long id) throws IllegalArgumentException {
            this.id = id;
        }

        public void setName(String name) {
            nameSetted = true;
            this.name = name;
        }

        public void setAge(long age) {
            ageSetted = true;
            this.age = age;
        }

        public void setCoordinates(Coordinates coordinates) {
            coordinatesSetted = true;
            this.coordinates = coordinates;
        }

        public void setDescription(String description) {
            descriptionSetted = true;
            this.description = description;
        }

        public void setWingspan(int wingspan) {
            wingspanSetted = true;
            this.wingspan = wingspan;
        }


        public void setDragonType(DragonType dragonType) {
            dragonTypeSetted = true;
            this.dragonType = dragonType;
        }

        public void setDragonCave(DragonCave dragonCave) {
            dragonCaveSetted = true;
            this.dragonCave = dragonCave;
        }
    }

    public boolean isFull() {
        return id != null;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @throws IllegalArgumentException if id isn't null and isn't positive 
     */
    public void setId(Long id) throws IllegalArgumentException {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    /**
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @throws IllegalArgumentException if coordinates is null
     */
    public void setCoordinates (Coordinates coordinates) throws IllegalArgumentException {
        if (coordinates == null) {
            throw new IllegalArgumentException();
        }
        this.coordinates = coordinates;
    }

    /**
     * @return coordinates object
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCreationDate(ZonedDateTime creationDate) throws IllegalArgumentException {
        this.creationDate = creationDate;
    }

    /**
     * @return creationDate
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @throws IllegalArgumentException if age is not positive 
     */
    public void setAge(long age) throws IllegalArgumentException {
        if (age <= 0) {
            throw new IllegalArgumentException();
        }
        this.age = age;
    }

    /**
     * @return age
     */
    public long getAge() {
        return age;
    }

    /**
     * @throws IllegalArgumentException if description is null or empty 
     */
    public void setDescription(String description) throws IllegalArgumentException {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.description  = description;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @throws IllegalArgumentException if wingspan is not positive 
     */
    public void setWingspan(int wingspan) throws IllegalArgumentException {
        if (wingspan <= 0) {
            throw new IllegalArgumentException();
        }
        this.wingspan = wingspan;
    }

    /**
     * @return wingspan
     */
    public int getWingspan() {
        return wingspan;
    }

    /**
     * @throws IllegalArgumentException if type is null
     */
    public void setType(DragonType type) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        this.type  = type;
    }

    /**
     * @return type
     */
    public DragonType getType() {
        return type;
    }

    /**
     * @throws IllegalArgumentException if cave is null
     */
    public void setCave(DragonCave cave) throws IllegalArgumentException {
        if (cave == null) {
            throw new IllegalArgumentException();
        }
        this.cave = cave;
    }

    /**
     * @return cave
     */
    public DragonCave getCave() {
        return cave;
    }

    /**
     * @return {@link #Dragon}'s compare result: age (oldest), wingspan (biggest), number of treasures (biggest) 
     * Note: this class has a natural ordering that is inconsistent with equals
     */
    @Override
    public int compareTo(Dragon dragon) {
        return Comparator.<Dragon, Long>comparing(d -> d.age).reversed()
            .thenComparing(d -> d.wingspan)
            .thenComparing(d -> d.cave.getNumberOfTreasures())
            .compare(this, dragon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, age, description, wingspan, type, cave);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Dragon dragon = (Dragon) obj;
        return Objects.equals(dragon.name, this.name) &&
            Objects.equals(dragon.coordinates, this.coordinates) &&
            Objects.equals(dragon.creationDate, this.creationDate) &&
            Objects.equals(dragon.age, this.age) &&
            Objects.equals(dragon.description, this.description) &&
            Objects.equals(dragon.wingspan, this.wingspan) &&
            dragon.type == this.type &&
            Objects.equals(dragon.cave, this.cave);
    }


    @Override
    public String toString() {
        return "Dragon [age=" + age + ", cave=" + cave + ", coordinates=" + coordinates + ", creationDate="
                + creationDate + ", description=" + description + ", id=" + id + ", name=" + name + ", type=" + type.getName()
                + ", wingspan=" + wingspan + "]";
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        if (id != null) {
            root.put("id", new JSONIntegerNumber(id));
        }
        root.put("name", new JSONString(name));
        root.put("coordinates", coordinates.toJSON());
        if (creationDate != null) {
            root.put("creation date", new JSONString(SEND_FORMATER.format(creationDate)));
        }
        root.put("age", new JSONIntegerNumber(age));
        root.put("description", new JSONString(description));
        root.put("wingspan", new JSONIntegerNumber(wingspan));
        root.put("type name", new JSONString(type.getName()));
        root.putValues(cave.toJSON().asMap());
        return root;
    }

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public static Dragon initFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        var nameJSONValue = root.get("name");
        var coordinatesJSONValue = root.get("coordinates");
        var ageJSONValue = root.get("age");
        var descriptionJSONValue = root.get("description");
        var wingspanJSONValue = root.get("wingspan");
        var typeNameJSONValue = root.get("type name");
        var numberOfTreasuresJSONValue = root.get("number of treasures");
        var idJSONValue = root.get("id");
        var creationDateJSONValue = root.get("creation date");
        var dragon = new Dragon();
        try {
            dragon.setName(nameJSONValue.asString().getValue());
            dragon.setCoordinates(Coordinates.initFromTheRoot(coordinatesJSONValue.asMap().getValues()));
            dragon.setAge(ageJSONValue.asInteger().getValue());
            dragon.setDescription(descriptionJSONValue.asString().getValue());
            dragon.setWingspan(Math.toIntExact(wingspanJSONValue.asInteger().getValue()));
            dragon.setType(DragonType.getByName(typeNameJSONValue.asString().getValue()));
            dragon.setCave(new DragonCave((float) numberOfTreasuresJSONValue.asDouble().getValue()));
            if (idJSONValue != null) {
                dragon.setId(idJSONValue.asInteger().getValue());
            }
            if (creationDateJSONValue != null) {
                dragon.setCreationDate(ZonedDateTime.parse(creationDateJSONValue.asString().getValue())); 
            }
            return dragon;
        } catch (ClassCastException | ArithmeticException | DateTimeParseException e) {
            throw new IllegalArgumentException();
        }
    }
}
