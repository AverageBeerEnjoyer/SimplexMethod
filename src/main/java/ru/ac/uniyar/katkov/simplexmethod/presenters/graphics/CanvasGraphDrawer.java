package ru.ac.uniyar.katkov.simplexmethod.presenters.graphics;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.ConvexHull;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.GraphicTask;

import java.util.ArrayList;
import java.util.List;


public class CanvasGraphDrawer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double x0, y0;
    private double centerX, centerY;
    private final double width, height;
    private Rectangle2D bounds;
    private Dot[] polygonToFill;
    private double initInterval;
    private int initIntervalToDraw;
    private final List<Straight> straights = new ArrayList<>();
    private Dot antiNormalVector;
    private Dot solution;
    private String ordinateName = "y";
    private String abscissaName = "x";

    public CanvasGraphDrawer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.x0 = width / 2;
        this.y0 = height / 2;
        this.centerX = 0;
        this.centerY = 0;
        initInterval = 20;
        setGCProperties();
        countBounds();
        countFillArea();
        draw();
    }

    private void setGCProperties() {
        gc.setFont(new Font("Verdana", 14));
    }

    public void move(double deltaX, double deltaY) {
        x0 += deltaX;
        y0 += deltaY;
        centerX -= deltaX / initInterval;
        centerY += deltaY / initInterval;
        countBounds();
        countFillArea();
        draw();
    }

    private void countBounds() {
        double widthUnits = width / initInterval;
        double heightUnits = height / initInterval;
        bounds = new Rectangle2D(centerX - widthUnits / 2, centerY - heightUnits / 2, widthUnits, heightUnits);
        initIntervalToDraw = (int) ((bounds.getMaxX() - bounds.getMinX()) / 10);
    }

    private void drawCoordinateSystem() {
        gc.setLineWidth(4);
        drawLine(new Straight(1, 0, 0));
        drawLine(new Straight(0, 1, 0));
        gc.setLineWidth(1);

        gc.strokeText(abscissaName, width - 15, y0 - 5);
        gc.strokeText(ordinateName, x0 + 5, 15);
        double l = 16 / initInterval;
        for (int i = (int) (bounds.getMinX() / initIntervalToDraw); i < (int) (bounds.getMaxX() / initIntervalToDraw) + 1; ++i) {
            int value = i * initIntervalToDraw;
            drawLine(new Dot(value, bounds.getMinY()), new Dot(value, bounds.getMaxY()));
            drawText("" + value, new Dot(value + l / 10, -l));
        }
        for (int i = (int) (bounds.getMinY() / initIntervalToDraw); i < (int) (bounds.getMaxY() / initIntervalToDraw) + 1; ++i) {
            int value = i * initIntervalToDraw;
            drawLine(new Dot(bounds.getMinX(), value), new Dot(bounds.getMaxX(), value));
            drawText("" + value, new Dot(-l, value + l / 10));
        }
    }

    private void drawCircle(Dot center, double radius) {
        Dot canvasCoords = convertCoordsGraphToCanvas(center);
        gc.fillOval(canvasCoords.x() - radius, canvasCoords.y() - radius, 2 * radius, 2 * radius);
    }

    private void drawLine(Dot d1, Dot d2) {
        Dot dot1 = convertCoordsGraphToCanvas(d1);
        Dot dot2 = convertCoordsGraphToCanvas(d2);
        gc.strokeLine(dot1.x(), dot1.y(), dot2.x(), dot2.y());
    }

    private void drawLine(Straight straight) {
        Pair<Dot, Dot> dots = straight.recount(bounds);
        if (dots == null) return;
        drawLine(dots.getKey(), dots.getValue());
    }

    private void drawText(String s, Dot d) {
        Dot canvasCoords = convertCoordsGraphToCanvas(d);
        gc.strokeText(s, canvasCoords.x(), canvasCoords.y());
    }

    private void clearGC() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void draw() {
        clearGC();
        fillArea();
        drawCoordinateSystem();
        drawTask();
    }

    public void removeTask() {
        straights.clear();
        antiNormalVector = null;
        solution = null;
        abscissaName = "x";
        ordinateName = "y";
    }

    private void drawSolution() {
        if (solution == null) return;
        gc.setFill(Color.BLUE);
        drawCircle(solution, 3);
    }

    private void fillArea() {

        double[] x = new double[polygonToFill.length];
        double[] y = new double[polygonToFill.length];
        for (int i = 0; i < polygonToFill.length; ++i) {
            Dot converted = convertCoordsGraphToCanvas(polygonToFill[i]);
            x[i] = converted.x();
            y[i] = converted.y();
        }
        gc.setFill(Color.rgb(117, 227, 104, 0.5));
        gc.fillPolygon(x, y, polygonToFill.length);
    }

    private void countFillArea() {
        Rectangle2D defaultArea = new Rectangle2D(Math.max(0, bounds.getMinX()), Math.max(0, bounds.getMinY()), Math.max(0, bounds.getMaxX()), Math.max(0, bounds.getMaxY()));
        polygonToFill = ConvexHull.countConvexHull(straights, defaultArea);
    }

    private void drawTask() {
        for (Straight straight : straights) {
            drawLine(straight);
        }
        drawAntiNormalVector();
        drawSolution();
    }

    private void drawAntiNormalVector() {
        if (antiNormalVector == null) return;
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        drawLine(new Dot(0,0), antiNormalVector);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        drawText("-n", antiNormalVector);
    }

    private void addStraightsFromMatrix(Matrix<?> matrix) {
        for (int i = 0; i < matrix.rows; ++i) {
            double a, b, c;
            a = -matrix.get(i, matrix.columns - 1).doubleValue();
            b = -matrix.get(i, matrix.columns - 2).doubleValue();
            c = matrix.getExt(i).doubleValue();
            Straight straight = new Straight(a, b, c);
            straights.add(straight);
        }
    }

    private void setOrdinateName(String s) {
        this.ordinateName = s;
    }

    private void setAbscissaName(String s) {
        this.abscissaName = s;
    }

    private void setTaskData(GraphicTask<?> task) {
        if (task == null) return;
        Matrix<?> matrix = task.getMatrix();
        int[] order = matrix.getOrder();

        setAbscissaName("x" + (order[matrix.columns - 1] + 1));
        setOrdinateName("x" + (order[matrix.columns - 2] + 1));
        addStraightsFromMatrix(matrix);

        countFillArea();

        antiNormalVector = task.getAntiNormalVector();
        this.solution = task.getSolutionDot();
    }

    public void setTask(GraphicTask<?> task) {
        removeTask();
        setTaskData(task);
        draw();
    }



    public void increaseInitInterval() {
        Dot center = convertCoordsGraphToCanvas(new Dot(centerX, centerY));
        initInterval *= 1.5;
        if (canvas.getWidth() / initInterval < 10) {
            initInterval /= 1.5;
            return;
        }
        x0 = center.x() + (x0 - center.x()) * 1.5;
        y0 = center.y() + (y0 - center.y()) * 1.5;
        countBounds();
        countFillArea();
        draw();
    }

    public void decreaseInitInterval() {
        Dot center = convertCoordsGraphToCanvas(new Dot(centerX, centerY));
        initInterval /= 1.5;
        if (canvas.getWidth() / initInterval > 1000000000) {
            initInterval *= 1.5;
            return;
        }
        x0 = center.x() + (x0 - center.x()) / 1.5;
        y0 = center.y() + (y0 - center.y()) / 1.5;
        countBounds();
        countFillArea();
        draw();
    }

    private Dot convertCoordsCanvasToGraph(Dot canvasCoords) {
        double x = (canvasCoords.x() - x0) / initInterval;
        double y = (y0 - canvasCoords.y()) / initInterval;
        return new Dot(x, y);
    }

    private Dot convertCoordsGraphToCanvas(Dot graphCoords) {
        double x = graphCoords.x() * initInterval + x0;
        double y = y0 - graphCoords.y() * initInterval;
        return new Dot(x, y);
    }
}
