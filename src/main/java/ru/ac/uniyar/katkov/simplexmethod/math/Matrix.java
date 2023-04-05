package ru.ac.uniyar.katkov.simplexmethod.math;

import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;


public class Matrix<T extends Num<T>> implements Cloneable {
    private final Arithmetic<T> ametic;
    public final int rows, columns;
    private final int[] order;
    private final T[][] numbers;
    private final T[] extension;

    public Matrix(T[][] numbers, T[] extension, int[] order) {
        if (numbers.length < 1 || numbers[0].length < 1) throw new IllegalArgumentException("empty matrix");
        this.ametic = Arithmetic.getArithmeticOfType(extension[0]);
        this.numbers = numbers;
        this.rows = numbers.length;
        this.columns = numbers[0].length;
        this.extension = extension;
        this.order = order;
    }

    public Matrix(T[][] numbers, T[] extension) {
        if (numbers.length < 1 || numbers[0].length < 1) throw new IllegalArgumentException("empty matrix");
        this.ametic = Arithmetic.getArithmeticOfType(extension[0]);
        this.numbers = numbers;
        this.rows = numbers.length;
        this.columns = numbers[0].length;
        this.extension = extension;
        order = new int[columns];
        for (int i = 0; i < columns; ++i) {
            order[i] = i;
        }
    }



    public void multiplyRow(int rowNum, T c) {
        for (int i = 0; i < columns; ++i) {
            numbers[rowNum][i] = ametic.multiply(numbers[rowNum][i], c);
        }
        extension[rowNum] = ametic.multiply(extension[rowNum], c);
    }

    public void addRowtoRow(int targetRow, int additionalRow, T multiplyCoef) {
        for (int i = 0; i < columns; ++i) {
            T a = numbers[targetRow][i];
            T b = numbers[additionalRow][i];
            numbers[targetRow][i] = ametic.plus(a, ametic.multiply(b, multiplyCoef));
        }
        T a = extension[targetRow];
        T b = extension[additionalRow];
        extension[targetRow] = ametic.plus(a, ametic.multiply(b, multiplyCoef));
    }

    public void swapRows(int row1, int row2) {
        T x;
        for (int i = 0; i < columns; ++i) {
            x = numbers[row1][i];
            numbers[row1][i] = numbers[row2][i];
            numbers[row2][i] = x;
        }
        x = extension[row1];
        extension[row1] = extension[row2];
        extension[row2] = x;
    }

    public void swapColumns(int col1, int col2) {
        T x;
        for (int i = 0; i < rows; ++i) {
            x = numbers[i][col1];
            numbers[i][col1] = numbers[i][col2];
            numbers[i][col2] = x;
        }
        int c = order[col1];
        order[col1] = order[col2];
        order[col2] = c;
    }

    public boolean isDiagonal() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < rows; ++j) {
                if (j == i) {
                    if (numbers[i][j].isZero()) return false;
                } else {
                    if (!numbers[i][j].isZero()) return false;
                }
            }
        }
        return true;
    }

    public void prepareToABM() {
        for (int i = 0; i < rows; ++i) {
            if(extension[i].compareTo(ametic.zero())<0){
                multiplyRow(i,ametic.parse("-1"));
            }
        }
    }

    public boolean hasZeroOnMainDiagonal() {
        for (int i = 0; i < rows; ++i) {
            if (numbers[i][i].isZero()) return true;
        }
        return false;
    }

    public T[][] getNumbers() {
        return numbers;
    }

    public T[] getExtension() {
        return extension;
    }

    public T get(int row, int column) {
        try {
            return numbers[row][column];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public T getExt(int row) {
        try {
            return extension[row];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int[] getOrder() {
        return order;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                sb.append(get(i, j));
                sb.append("\t");
            }
            sb.append("|\t");
            sb.append(getExt(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Matrix<T> clone() {
        try {
            return (Matrix<T>) super.clone();
        } catch (CloneNotSupportedException | ClassCastException e) {
            throw new AssertionError();
        }
    }
}
