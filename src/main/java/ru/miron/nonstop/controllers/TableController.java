package ru.miron.nonstop.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.givenClasses.*;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.specificAnswers.DragonsGettingCommandSpecificAnswer;

import java.io.IOException;
import java.net.ConnectException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.PLAIN_TEXT;

public class TableController implements LanguageUpdatable {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd kk:mm:ss");

    @FXML
    TableView<DragonWithKeyAndOwner> dragonsTableView;
    @FXML
    Button commandsListButton;
    @FXML
    Button filterButton;
    @FXML
    ChoiceBox<String> languageSelector;
    @FXML
    Label authorizedAsLabel;
    @FXML
    Label authorizedLoginLabel;
    @FXML
    Button exitButton;

    public ObservableList<DragonWithKeyAndOwner> dragonsObservableList;

    TableColumn<DragonWithKeyAndOwner, String> ownerColumn;
    TableColumn<DragonWithKeyAndOwner, Long> idColumn;
    TableColumn<DragonWithKeyAndOwner, String> nameColumn;
    TableColumn<DragonWithKeyAndOwner, Float> xColumn;
    TableColumn<DragonWithKeyAndOwner, Long> yColumn;
    TableColumn<DragonWithKeyAndOwner, ZonedDateTime> dateColumn;
    TableColumn<DragonWithKeyAndOwner, Long> ageColumn;
    TableColumn<DragonWithKeyAndOwner, String> descriptionColumn;
    TableColumn<DragonWithKeyAndOwner, Integer> wingspanColumn;
    TableColumn<DragonWithKeyAndOwner, String> typeColumn;
    TableColumn<DragonWithKeyAndOwner, Float> treasuresColumn;

//    public ScheduledExecutorService dataUpdater;
//
//    {
//        dataUpdater = Executors.newScheduledThreadPool(1);
//    }

    @FXML
    public void initialize() {
        initializeTable();
        // setting current user login
        authorizedLoginLabel.setText(EmoCore.enterEntry.getLogin());
        // setting up language selector
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        updateLanguage();
    }

    public void initializeTable() {
        setColumnsWithoutLang();
        // opening info view on row click
        dragonsTableView.setOnMouseClicked(getOnTableMouseClickAction());
        // dragons list init
        dragonsObservableList = FXCollections.synchronizedObservableList(FXCollections.observableList(new LinkedList<>()));
        dragonsTableView.setItems(dragonsObservableList);
        dragonsObservableList.add(new DragonWithKeyAndOwner(new Dragon(222222222222222L, "longnameeeeeeeeeeeeeeeeeeeeee", new Coordinates(1.2222222f, 22222222222222222L), ZonedDateTime.now(), 11111111111111111L, "long description i want to tell", 2123, DragonType.AIR, new DragonCave(21.12121f)), "key", "loginnnnnnnnnnnnnnn"));
        dragonsObservableList.add(new DragonWithKeyAndOwner(new Dragon(2123L, "ado", new Coordinates(1.2222222f, 22222222222222222L), ZonedDateTime.now(), 11111111111111111L, "long description i want to tell", 2123, DragonType.AIR, new DragonCave(21.12121f)), "key", "loginnnnnnnnnnnnnnn"));
//        dataUpdater.scheduleAtFixedRate(updateTableContentsTask(),0,  3, TimeUnit.SECONDS);
        updateTableContentsTask().run();
    }

    public Runnable updateTableContentsTask() {
        return () -> {
            var dragonsGettingCommand = new Command(CommandName.GET_DRAGONS, EmoCore.enterEntry, null);
            try {
                var commandAnswer = EmoCore.udpClient.tryToGetCommandAnswer(dragonsGettingCommand);
                if (commandAnswer.getCommandAnswerWithoutArgs().getIsError()) {
                    return;
                }
                var gotDragonsWithMetaAnswer = (DragonsGettingCommandSpecificAnswer) commandAnswer.getCommandSpecificAnswerObj();
                var gotDragonsWithMeta = gotDragonsWithMetaAnswer.getDragons();
                dragonsObservableList.setAll(gotDragonsWithMeta);
            } catch (ConnectException e) {}
        };
    }


    public void setColumnsWithoutLang() {
        // init columns
        ownerColumn = new TableColumn<DragonWithKeyAndOwner, String>();
        idColumn = new TableColumn<DragonWithKeyAndOwner, Long>();
        nameColumn = new TableColumn<DragonWithKeyAndOwner, String>();
        xColumn = new TableColumn<DragonWithKeyAndOwner, Float>();
        yColumn = new TableColumn<DragonWithKeyAndOwner, Long>();
        dateColumn = new TableColumn<DragonWithKeyAndOwner, ZonedDateTime>();
        ageColumn = new TableColumn<DragonWithKeyAndOwner, Long>();
        descriptionColumn = new TableColumn<DragonWithKeyAndOwner, String>();
        wingspanColumn = new TableColumn<DragonWithKeyAndOwner, Integer>();
        typeColumn = new TableColumn<DragonWithKeyAndOwner, String>();
        treasuresColumn = new TableColumn<DragonWithKeyAndOwner, Float>();
        // setting cell value factories
        ownerColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().nameProperty());
        idColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().nameProperty());
        xColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().xProperty().asObject());
        yColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().yProperty().asObject());
        dateColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().dateProperty());
        ageColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().ageProperty().asObject());
        descriptionColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().descriptionProperty());
        wingspanColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().wingspanProperty().asObject());
        typeColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().typeProperty());
        treasuresColumn.setCellValueFactory(dragonWithKeyAndOwner -> dragonWithKeyAndOwner.getValue().treasuresProperty().asObject());
        // another way of showing
        dateColumn.setCellFactory(getDateCell(dateTimeFormatter)); // todo:
        // set comparators
        ownerColumn.setComparator(Comparator.comparingInt(String::length));
        nameColumn.setComparator(Comparator.comparingInt(String::length));
        descriptionColumn.setComparator(Comparator.comparingInt(String::length));
        // adding columns
        dragonsTableView.getColumns().setAll(ownerColumn, idColumn, nameColumn, xColumn, yColumn, dateColumn, ageColumn, descriptionColumn, wingspanColumn, typeColumn, treasuresColumn);
        // table is not resizable
        // so bounding columns
        for (var column : dragonsTableView.getColumns()) {
            column.setResizable(false);
            column.setMaxWidth(100);
            column.setMinWidth(100);
        }
    }

    @FXML
    public void exitBtnActionListener() {
//        dataUpdater.shutdownNow();
        EmoCore.setHelloScene();
    }

    @FXML
    public void commandsListBtnActionListener(ActionEvent actionEvent) {
        EmoCore.tryCreateCommandsListWindow();
    }

    public static <ROW,T extends Temporal> Callback<TableColumn<ROW, T>, TableCell<ROW, T>> getDateCell(DateTimeFormatter format) {
        return column -> new TableCell<ROW, T>() {
            @Override
            protected void updateItem (T item, boolean empty) {
                super.updateItem (item, empty);
                if (item == null || empty) {
                    setText (null);
                }
                else {
                    setText (format.format(item));
                }
            }
        };
    }

    public EventHandler<MouseEvent> getOnTableMouseClickAction() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    var selectedCells = dragonsTableView.getSelectionModel().getSelectedCells();
                    if (selectedCells.size() == 0) {
                        return;
                    }
                    TablePosition pos = selectedCells.get(0);
                    int row = pos.getRow();
                    var clickedDragonWithMeta = dragonsObservableList.get(row);
                    var dragonWithMetaInfoTemplate = AppLocaleManager.getTextByLabel("chooseDragonByClickMsg") + "\n\n";
                    for (int i = 0; i < 11; i++) {
                        dragonWithMetaInfoTemplate += "%s: %s\n";
                    }
                    var dragonWithMetaInfo = String.format(dragonWithMetaInfoTemplate,
                            AppLocaleManager.getTextByLabel("ownerLoginName"), clickedDragonWithMeta.getOwnerLogin(),
                            AppLocaleManager.getTextByLabel("idName"), clickedDragonWithMeta.getDragon().getId(),
                            AppLocaleManager.getTextByLabel("nameName"), clickedDragonWithMeta.getDragon().getName(),
                            AppLocaleManager.getTextByLabel("xName"), clickedDragonWithMeta.getDragon().getCoordinates().getX(),
                            AppLocaleManager.getTextByLabel("yName"), clickedDragonWithMeta.getDragon().getCoordinates().getY(),
                            AppLocaleManager.getTextByLabel("dateName"), clickedDragonWithMeta.getDragon().getCreationDate().format(dateTimeFormatter),
                            AppLocaleManager.getTextByLabel("ageName"), clickedDragonWithMeta.getDragon().getAge(),
                            AppLocaleManager.getTextByLabel("descriptionName"), clickedDragonWithMeta.getDragon().getDescription(),
                            AppLocaleManager.getTextByLabel("wingspanName"), clickedDragonWithMeta.getDragon().getWingspan(),
                            AppLocaleManager.getTextByLabel("typeName"), clickedDragonWithMeta.getCapitalizedType(),
                            AppLocaleManager.getTextByLabel("treasuresName"), clickedDragonWithMeta.getDragon().getCave().getNumberOfTreasures());
                    try {
                        EmoCore.createInfoAutoClosableWindow(new LabelText(dragonWithMetaInfo, PLAIN_TEXT), "Clicked dragon info");
                    } catch (IOException e) {}
                }
            }
        };
    }

    @Override
    public void updateLanguage() {
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(ownerColumn, "ownerLoginName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(idColumn, "idName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(nameColumn, "nameName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(xColumn, "xName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(yColumn, "yName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(dateColumn, "dateName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(ageColumn, "ageName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(descriptionColumn, "descriptionName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(wingspanColumn, "wingspanName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(typeColumn, "typeName");
        ElementsLocaleSetter.setHeaderTextOfColumnInConcreteLanguage(treasuresColumn, "treasuresName");
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(authorizedAsLabel, "authorizedAsName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(exitButton, "exitButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(commandsListButton, "commandsButtonName");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(filterButton, "filterButtonName");
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }

}
