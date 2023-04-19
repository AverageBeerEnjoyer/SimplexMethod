package ru.ac.uniyar.katkov.simplexmethod.math;

import javafx.geometry.Rectangle2D;
import ru.ac.uniyar.katkov.simplexmethod.controllers.graphics.Dot;
import ru.ac.uniyar.katkov.simplexmethod.controllers.graphics.Straight;

import java.util.ArrayList;
import java.util.List;

public class ConvexHull {
    public static Dot[] countConvexHull(List<Straight> limits, Rectangle2D bounds) {
        Dot[] polygon = new Dot[]{
                new Dot(bounds.getMinX(), bounds.getMinY()),
                new Dot(bounds.getMinX(), bounds.getMaxY()),
                new Dot(bounds.getMaxX(), bounds.getMaxY()),
                new Dot(bounds.getMaxX(), bounds.getMinY())
        };
        for (Straight straight : limits) {
            int[] values = new int[polygon.length];
            List<Dot> newDots = new ArrayList<>();
            for (int i = 0; i < polygon.length; ++i) {
                values[i] = Double.compare(straight.leftValue(polygon[i]), 0);
            }
            for (int i = 0; i < polygon.length; ++i) {
                if (values[i] == -1) {
                    if (values[(i - 1) % values.length] != -1) {
                        Dot cross = new Straight(polygon[i], polygon[(i - 1) % values.length]).cross(straight);
                        newDots.add(cross);
                    }
                    if (values[(i + 1) % values.length] != -1) {
                        Dot cross = new Straight(polygon[i], polygon[(i + 1) % values.length]).cross(straight);
                        newDots.add(cross);
                    }
                } else {
                    newDots.add(polygon[i]);
                }
            }
            polygon = newDots.toArray(new Dot[0]);
        }
        return polygon;
    }
}
