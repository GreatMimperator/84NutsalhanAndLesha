package ru.miron.nonstop.logic.commands;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class EnterEntry implements JSONConvertable {
    private String login;
    private String password;

    /**
     * @param password not null
     * @param login not null
     * @throws IllegalArgumentException if login or password is null or empty
     */
    public EnterEntry(String login, String password) throws IllegalArgumentException {
        setLogin(login);
        setPassword(password);
    }

    public EnterEntry(EnterEntry enterEntry) {
        this(enterEntry.login, enterEntry.password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @param login not null
     * @throws IllegalArgumentException if login is null or empty
     */
    public void setLogin(String login) throws IllegalArgumentException {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.login = login;
    }

    /**
     * @param password not null
     * @throws IllegalArgumentException if passord is null or empty
     */
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.password = password;
    }

    @Override
    public JSONValue toJSON() {
        var root = new JSONMap();
        root.put("login", new JSONString(login));
        root.put("password", new JSONString(password));
        return root;
    }

    public void updateTo(EnterEntry enterEntry) {
        this.login = enterEntry.login;
        this.password = enterEntry.password; 
    }

    public void setEntry(EnterEntry setTo) {
        this.login = setTo.login;
        this.password = setTo.password;
    }


}
