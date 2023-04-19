package ru.ac.uniyar.katkov.simplexmethod.controllers.graphics;

import javafx.geometry.Rectangle2D;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Doubl;

import static java.lang.Math.sqrt;
import static ru.ac.uniyar.katkov.simplexmethod.Utils.pair;

public class Straight {

    private final double A, B, C;

    public Straight(double a, double b, double c) {
        if (a == 0 && b == 0) throw new IllegalArgumentException("Wrong straight equation");
        double norm = sqrt(a * a + b * b);
        this.A = a / norm;
        this.B = b / norm;
        this.C = c / norm;
    }

    public Straight(Dot dot1, Dot dot2) {
        double x1 = dot1.x(),
                y1 = dot1.y(),
                x2 = dot2.x(),
                y2 = dot2.y();
        if (x1 == x2 && y1 == y2) throw new IllegalArgumentException("Dot 1 is equal dot 2.");

        if (x1 == x2) {
            this.A = 1;
            this.B = 0;
            this.C = -x1;
            return;
        }
        if (y1 == y2) {
            this.A = 0;
            this.B = 1;
            this.C = -y1;
            return;
        }
        double a, b, c, norm;
        a = 1 / (x2 - x1);
        b = -1 / (y2 - y1);
        c = y1 / (y2 - y1) - x1 / (x2 - x1);
        norm = sqrt(a * a + b * b);
        this.A = a / norm;
        this.B = b / norm;
        this.C = c / norm;
    }

    public double x(double y) {
        return (-C - y * B) / A;
    }

    public double y(double x) {
        return (-C - x * A) / B;
    }

    public Pair<Dot, Dot> recount(Rectangle2D a) {
        XYChart.Series<Number, Number> cur = new XYChart.Series<>();
        if (A == 0) {
            double tmp = -C / B;
            if (a.getMinY() <= tmp && tmp <= a.getMaxY()) {
                return pair(new Dot(a.getMinX(), tmp), new Dot(a.getMaxX(), tmp));
            } else return null;
        }
        if (B == 0) {
            double tmp = -C / A;
            if (a.getMinX() <= tmp && tmp <= a.getMaxX()) {
                return pair(new Dot(tmp, a.getMinY()), new Dot(tmp, a.getMaxY()));
            } else return null;
        }
        return pair(new Dot(a.getMinX(), y(a.getMinX())), new Dot(a.getMaxX(), y(a.getMaxX())));
    }

    public double leftValue(Dot dot) {
        return A * dot.x() + B * dot.y() + C;
    }

    public Dot cross(Straight straight) {
        double s1A = A,
                s1B = B,
                s1C = C,
                s2A = straight.getA(),
                s2B = straight.getB(),
                s2C = straight.getC();
        if (Math.abs(s1A + s2A) < Doubl.eps && Math.abs(s1B + s2B) < Doubl.eps) {
            s2A *= -1;
            s2B *= -1;
            s2C *= -1;
        }
        if (Math.abs(s1A - s2A) < Doubl.eps && Math.abs(s1B - s2B) < Doubl.eps) {
            return null;
        }
        if (Math.abs(s1A) > Doubl.eps) {
            double y = (s2A * s1C / s1A - s2C) / (s2B - s2A * s1B / s1A),
                    x = (-s1C - s1B * y) / s1A;
            return new Dot(x, y);
        } else {
            double x = (s2B * s1C / s1B - s2C) / (s2A - s2B * s1A / s1B),
                    y = (-s1C - s1A * x) / s1B;
            return new Dot(x, y);
        }
    }

    @Override
    public String toString() {
        String res = "";
        if (A != 0)
            res += (String.format("%.3f", A) + "*X");
        if (B != 0) {
            if (!res.equals(""))
                if (B > 0)
                    res += "+";
            res += (String.format("%.3f", B) + "*Y");
        }
        if (C != 0) {
            if (C > 0)
                res += "+";
            res += String.format("%.3f", C);
        }
        res += "=0";
        return res;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    public double getC() {
        return C;
    }
}
