package ru.miron.nonstop.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.givenClasses.Coordinates;
import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;

import java.util.*;
import java.util.function.BiFunction;

public class DragonsViewController {
    @FXML
    HBox root;

    @FXML
    Canvas canvas;

    private final ListChangeListener<DragonWithKeyAndOwner> actualDragonsListChangeListener;
    private Coordinate windowShift;
    private Coordinate lastDragMouseCoordinate;
    private final Image mapImage;
    private final List<Image> dragonsImages;
    private final Map<DragonWithKeyAndOwner, Image> dragonsToImagesMapper;
    private List<DragonWithKeyAndOwner> mapDragons;
    private static final Coordinate MAP_START_OFFSET;
    private static final int DRAGONS_PICS_COUNT = 6;
    private static final int MIN_DRAGON_IMAGE_SIZE = 50;
    private static final int MAX_DRAGON_IMAGE_SIZE = 250;
    private static final int MIN_DRAGON_IMAGE_SIZE_WINGSPAN = 3;
    private static final int MAX_DRAGON_IMAGE_SIZE_WINGSPAN = 15;

    static {
        MAP_START_OFFSET = new Coordinate(-1500, -1500);
    }

    {
        windowShift = new Coordinate(0, 0);
        lastDragMouseCoordinate = null;
        mapImage = EmoCore.loadImage("dead/map.jpg");
        dragonsImages = loadSadDragons();
        actualDragonsListChangeListener = change -> {
            updateData();
            repaint();
        };
        dragonsToImagesMapper = new HashMap<>();
    }

    public void initialize() {
        System.out.println("inited dragons view controller");
        updateData();
        clearCanvas();
        EmoCore.addActualDragonsListener(actualDragonsListChangeListener);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::mouseMoveHandlerForDragSupport);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::processMouseClick);

        repaint();
    }

    public void close() {
        EmoCore.removeActualDragonsListener(actualDragonsListChangeListener);
    }

    private void clearCanvas() {
        var graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void processMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() < 2) {
            return;
        }
        var windowClickCoordinate = new Coordinate(mouseEvent.getX(), mouseEvent.getY());
        var mapClickCoordinate = Coordinate.sum(windowShift, windowClickCoordinate);
        var clickedDragon = getClickedDragon(mapClickCoordinate);
        if (clickedDragon != null) {
            EmoCore.createDragonMenuWindow(clickedDragon);
        }
    }

    private synchronized DragonWithKeyAndOwner getClickedDragon(Coordinate mapClickCoordinate) {
        for (var dragon : EmoCore.getActualDragonsWithMeta()) {
            var dragonCoordinate = Coordinate.convert(dragon.getDragon().getCoordinates());
            var dragonImage = dragonsToImagesMapper.get(dragon);
            var dragonImageSize = getDragonImageSize(dragon, dragonImage);
            var dragonImageCoordinate = getDragonImageCoordinate(dragonCoordinate, dragonImageSize);
            var rightDownCorner = Coordinate.sum(dragonImageCoordinate, dragonImageSize);
            var leftUpCorner = dragonImageCoordinate;
            if (TwoDimensioned.isIn(mapClickCoordinate, leftUpCorner, rightDownCorner)) {
                if (isPixelTransparent(dragonImage, Coordinate.sub(mapClickCoordinate, leftUpCorner))) {
                    return dragon;
                }
            }
        }
        return null;
    }


    private void mouseMoveHandlerForDragSupport(MouseEvent mouseEvent) {
        lastDragMouseCoordinate = new Coordinate(mouseEvent.getX(), mouseEvent.getY());
    }

    private void onMouseDragHandler(MouseEvent mouseEvent) {
        var mouseCoordinate = new Coordinate(mouseEvent.getX(), mouseEvent.getY());
        if (lastDragMouseCoordinate == null) {
            lastDragMouseCoordinate = mouseCoordinate;
            return;
        }
        var shift = Coordinate.sub(mouseCoordinate, lastDragMouseCoordinate);
        lastDragMouseCoordinate = mouseCoordinate;
        windowShift.sub(shift);
        repaint();
    }

    private synchronized void repaint() {
        var graphics = canvas.getGraphicsContext2D();
        clearCanvas();
        graphics.drawImage(mapImage, MAP_START_OFFSET.x - windowShift.x, MAP_START_OFFSET.y - windowShift.y);
        drawDragons();
    }

    private synchronized void updateData() {
        mapDragons = EmoCore.getActualDragonsWithMeta();
        for (var oldDragon : dragonsToImagesMapper.keySet()) {
            if (!mapDragons.contains(oldDragon)) {
                dragonsToImagesMapper.remove(oldDragon);
            }
        }
        for (var newDragon : mapDragons) {
            if (!dragonsToImagesMapper.containsKey(newDragon)) {
                dragonsToImagesMapper.put(newDragon, getRandomDragonImage());
            }
        }
    }

    private boolean isPixelTransparent(Image dragonImage, TwoDimensioned coordinate) {
        int pixel = dragonImage.getPixelReader().getArgb((int) coordinate.x, (int) coordinate.y);
        System.out.printf(Integer.toString(pixel));
        System.out.printf(Integer.toString(pixel >> 24));
        if ((pixel >> 24) == 0x00) {
            return true;
        }
        return false;
    }

    private void drawDragons() {
        try {
            for (var dragon : mapDragons) {
                var dragonCoordinate = Coordinate.convert(dragon.getDragon().getCoordinates());
                var dragonImage = dragonsToImagesMapper.get(dragon);
                var dragonImageSize = getDragonImageSize(dragon, dragonImage);
                var dragonImageMapCoordinate = getDragonImageCoordinate(dragonCoordinate, dragonImageSize);
                var dragonImageWindowCoordinate = Coordinate.sub(dragonImageMapCoordinate, windowShift);
                drawImage(dragonImage, dragonImageWindowCoordinate, dragonImageSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawImage(Image image, Coordinate imageCoordinate, TwoDimensioned size) {
        canvas.getGraphicsContext2D().drawImage(image, imageCoordinate.x, imageCoordinate.y, size.x, size.y);
    }

    private Coordinate getDragonImageCoordinate(Coordinate dragonCoordinate, TwoDimensioned dragonImageSize) {
        return new Coordinate(dragonCoordinate.x - dragonImageSize.x / 2, dragonCoordinate.y - dragonImageSize.y / 2);
    }

    private TwoDimensioned getDragonImageSize(DragonWithKeyAndOwner dragon, Image dragonImage) {
        var imageWidth = dragonImage.getWidth();
        var imageHeight = dragonImage.getHeight();
        var imageHeightToWidth = imageHeight / imageWidth;
        var wingspan = dragon.getDragon().getWingspan();

        if (wingspan <= MIN_DRAGON_IMAGE_SIZE_WINGSPAN) {
            return new TwoDimensioned(MIN_DRAGON_IMAGE_SIZE, MIN_DRAGON_IMAGE_SIZE * imageHeightToWidth);
        }
        if (wingspan >= MAX_DRAGON_IMAGE_SIZE_WINGSPAN) {
            return new TwoDimensioned(MAX_DRAGON_IMAGE_SIZE, MAX_DRAGON_IMAGE_SIZE * imageHeightToWidth);
        }
        var pxToOneWingspan = (MAX_DRAGON_IMAGE_SIZE - MIN_DRAGON_IMAGE_SIZE) / (MAX_DRAGON_IMAGE_SIZE_WINGSPAN - MIN_DRAGON_IMAGE_SIZE_WINGSPAN);
        var width = MIN_DRAGON_IMAGE_SIZE + (wingspan - MIN_DRAGON_IMAGE_SIZE_WINGSPAN) * pxToOneWingspan;
        var height = width * imageHeightToWidth;
        return new TwoDimensioned(width, height);
    }



    private Image getRandomDragonImage() {
        Random rand = new Random(System.currentTimeMillis());
        int dragonID = rand.nextInt(DRAGONS_PICS_COUNT);
        return dragonsImages.get(dragonID);
    }

    private List<Image> loadSadDragons() {
        var dragonsImages = new LinkedList<Image>();
        for (int i = 1; i <= DRAGONS_PICS_COUNT; i++) {
            dragonsImages.add(EmoCore.loadImage("dead/sad_dragons/%d.png".formatted(i)));
        }
        return dragonsImages;
    }

    public static class Coordinate extends TwoDimensioned {
        public Coordinate(double x, double y) {
            super(x, y);
        }

        public Coordinate(TwoDimensioned coordinate) {
            super(coordinate.x, coordinate.y);
        }

        public static Coordinate convert(Coordinates coordinates) {
            return new Coordinate(coordinates.getX(), coordinates.getY());
        }

        public static Coordinate sum(TwoDimensioned first, TwoDimensioned second) {
            return new Coordinate(TwoDimensioned.sum(first, second));
        }

        public static Coordinate sub(TwoDimensioned first, TwoDimensioned second) {
            return new Coordinate(TwoDimensioned.sub(first, second));
        }
    }

    public static class TwoDimensioned {
        public double x;
        public double y;

        public TwoDimensioned(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Checks is inside rectangle
         */
        public static boolean isIn(Coordinate point, Coordinate firstCorner, TwoDimensioned secondCorner) {
            return point.x >= Math.min(firstCorner.x, secondCorner.x) &&
                    point.x <= Math.max(firstCorner.x, secondCorner.x) &&
                    point.y >= Math.min(firstCorner.y, secondCorner.y) &&
                    point.y <= Math.max(firstCorner.y, secondCorner.y);
        }

        public void add(TwoDimensioned shift) {
            var thisDimension = TwoDimensioned.sum(this, shift);
            this.x = thisDimension.x;
            this.y = thisDimension.y;
        }

        public void sub(TwoDimensioned shift) {
            var thisDimension = TwoDimensioned.sub(this, shift);
            this.x = thisDimension.x;
            this.y = thisDimension.y;
        }

        public static TwoDimensioned sum(TwoDimensioned first, TwoDimensioned second) {
            return operation(first, second, (a, b) -> a + b);
        }

        public static TwoDimensioned sub(TwoDimensioned first, TwoDimensioned second) {
            return operation(first, second, (a, b) -> a - b);
        }

        private static TwoDimensioned operation(TwoDimensioned first, TwoDimensioned second, BiFunction<Double, Double, Double> operation) {
            return new TwoDimensioned(operation.apply(first.x, second.x), operation.apply(first.y, second.y));
        }

        private static double distance(TwoDimensioned first, TwoDimensioned second) {
            return Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2));
        }

        public static TwoDimensioned convert(Coordinates coordinates) {
            return new TwoDimensioned(coordinates.getX(), coordinates.getY());
        }

        @Override
        public String toString() {
            return "TwoDimensioned{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}
