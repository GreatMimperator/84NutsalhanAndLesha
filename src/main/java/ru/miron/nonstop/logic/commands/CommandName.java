package ru.miron.nonstop.logic.commands;

public enum CommandName {
    REGISTER("register"),
    SIGN_IN("sign in"),
    INSERT_DRAGON("insert dragon"),
    GET_DRAGONS("get dragons"),
    DRAGONS_INFO("dragons info"),
    UPDATE_DRAGON("update dragon"),
    REMOVE_DRAGON_BY_KEY("remove dragon by key"),
    CLEAR_DRAGONS("clear dragons"),
    REMOVE_GREATER_DRAGONS("remove greater dragons"),
    REMOVE_LOWER_DRAGONS("remove lower dragons"),
    REPLACE_IF_DRAGON_IS_GREATER("replace if dragon is greater"),
    GET_WINGSPANS_SUM("get wingspans sum"),
    GET_DRAGONS_WITH_THIS_DESCRIPTION("get dragons with this description"),
    GET_UNIQUE_NUMBERS_OF_TREASURES("get unique numbers of treasures"),
    HELP("help"),
    EXIT("exit");

    public final String name;

    CommandName(String name) {
        this.name = name;
    }

    public boolean isArgumentless() {
        return switch(this) {
            case REGISTER, SIGN_IN, GET_DRAGONS, DRAGONS_INFO, CLEAR_DRAGONS, GET_WINGSPANS_SUM, GET_UNIQUE_NUMBERS_OF_TREASURES, HELP, EXIT ->
                true;
            default -> false;
        };
    }
}