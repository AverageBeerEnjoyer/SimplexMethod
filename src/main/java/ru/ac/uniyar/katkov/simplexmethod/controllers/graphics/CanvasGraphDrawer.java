package ru.ac.uniyar.katkov.simplexmethod.controllers.graphics;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.ConvexHull;
import ru.ac.uniyar.katkov.simplexmethod.math.Matrix;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.table.SimplexTable;
import ru.ac.uniyar.katkov.simplexmethod.math.simplex.task.Task;

import java.util.ArrayList;
import java.util.List;

import static ru.ac.uniyar.katkov.simplexmethod.Utils.getlast;
import static ru.ac.uniyar.katkov.simplexmethod.Utils.pair;

public class CanvasGraphDrawer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final double x0, y0;
    private final double width,height;
    private Rectangle2D bounds;
    private Dot[] polygonToFill;
    private double initInterval;
    private int initIntervalToDraw;
    private final List<Straight> straights = new ArrayList<>();
    private final List<String> unequals = new ArrayList<>();
    private Pair<Dot, Dot> antiNormalVector;
    private String ordinateName = "y";
    private String abscissaName = "x";

    public CanvasGraphDrawer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.x0 = width / 5;
        this.y0 = height * 4 / 5;
        initInterval = 20;
        setGCProperties();
        countBounds();
        countFillArea();
        draw();
    }
    private void setGCProperties(){
        gc.setFill(Color.rgb(117, 227, 104, 0.5));
        gc.setFont(new Font("Verdana",14));
    }


    private void countBounds() {
        Dot bl = convertCoordsCanvasToGraph(new Dot(0, height));
        bounds = new Rectangle2D(bl.x(), bl.y(), width / initInterval, height / initInterval);
        initIntervalToDraw = (int) ((bounds.getMaxX() - bounds.getMinX()) / 10);
    }

    private void drawCoordinateSystem() {
        gc.setLineWidth(4);
        drawLine(new Straight(1,0,0));
        drawLine(new Straight(0,1,0));
        gc.setLineWidth(1);

        gc.strokeText(abscissaName, width - 15, y0 - 5);
        gc.strokeText(ordinateName, x0 + 5, 15);
        double l = 16 / initInterval;
        for (int i = initIntervalToDraw; i < bounds.getMaxY() || i < bounds.getMaxX() || -i > bounds.getMinX() || -i > bounds.getMinY(); i += initIntervalToDraw) {
            if (i < bounds.getMaxX()) {
                drawLine(new Dot(i, bounds.getMinY()), new Dot(i, bounds.getMaxY()));
                drawText("" + i, new Dot(i+l/10, -l));
            }
            if (-i > bounds.getMinX()) {
                drawLine(new Dot(-i, bounds.getMinY()), new Dot(-i, bounds.getMaxY()));
                drawText("" + (-i), new Dot(-i+l/10, -l));
            }
            if (i < bounds.getMaxY()) {
                drawLine(new Dot(bounds.getMinX(), i), new Dot(bounds.getMaxX(), i));
                drawText("" + i, new Dot(-l, i+l/10));
            }
            if (i > bounds.getMinY()) {
                drawLine(new Dot(bounds.getMinX(), -i), new Dot(bounds.getMaxX(), -i));
                drawText("" + (-i), new Dot(-l, -i+l/10));
            }
        }
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
        unequals.clear();
        antiNormalVector = null;
        abscissaName = "x";
        ordinateName = "y";
    }
    private void fillArea(){

        double[] x = new double[polygonToFill.length];
        double[] y = new double[polygonToFill.length];
        for(int i=0;i<polygonToFill.length;++i){
            Dot converted = convertCoordsGraphToCanvas(polygonToFill[i]);
            x[i] = converted.x();
            y[i] = converted.y();
        }
        gc.fillPolygon(x,y,polygonToFill.length);
    }
    private void countFillArea(){
        Rectangle2D defaultArea = new Rectangle2D(0,0, bounds.getWidth(), bounds.getHeight());
        polygonToFill = ConvexHull.countConvexHull(straights,defaultArea);
    }

    private void drawTask() {
        for (Straight straight : straights) {
            drawLine(straight);
        }
        drawAntiNormalVector();
    }
    private void drawAntiNormalVector(){
        if (antiNormalVector == null) return;
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        drawLine(antiNormalVector.getKey(), antiNormalVector.getValue());
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        drawText("-n",antiNormalVector.getKey());
    }

    public void setTask(Task<?> task) {
        removeTask();
        SimplexTable<?> table = getlast(task.getSteps());
        Matrix<?> matrix = table.getCloneMatrix();
        abscissaName = "x" + (matrix.getOrder()[matrix.columns - 1] + 1);
        ordinateName = "x" + (matrix.getOrder()[matrix.columns - 2] + 1);
        for (int i = 0; i < matrix.rows; ++i) {
            double a, b, c;
            a = -matrix.get(i, matrix.columns - 1).doubleValue();
            b = -matrix.get(i, matrix.columns - 2).doubleValue();
            c = matrix.getExt(i).doubleValue();
            Straight straight = new Straight(a, b, c);
            straights.add(straight);
        }
        unequals.addAll(table.getUnequals());
        countFillArea();
        double x = task.getTargetFunction()[matrix.getOrder()[matrix.columns - 1]].doubleValue();
        double y = task.getTargetFunction()[matrix.getOrder()[matrix.columns - 2]].doubleValue();
        double length = Math.sqrt(x * x + y * y);
        Dot d1 = new Dot(-x / length, -y / length);
        Dot d2 = new Dot(0, 0);
        antiNormalVector = pair(d1, d2);
        draw();
    }

    public void increaseInitInterval() {
        initInterval *= 2;
        if (canvas.getWidth() / initInterval < 10) {
            initInterval /= 2;
            return;
        }
        countBounds();
        countFillArea();
        draw();
    }

    public void decreaseInitInterval() {
        initInterval /= 2;
        if (canvas.getWidth() / initInterval > 1000000000) {
            initInterval *= 2;
            return;
        }
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
    public List<String> getUnequals(){
        return unequals;
    }
}
