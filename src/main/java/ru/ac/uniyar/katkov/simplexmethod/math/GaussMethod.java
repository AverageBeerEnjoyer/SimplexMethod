package ru.ac.uniyar.katkov.simplexmethod.math;

import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Arithmetic;
import ru.ac.uniyar.katkov.simplexmethod.math.numbers.Num;

import static ru.ac.uniyar.katkov.simplexmethod.math.numbers.OrdinaryFraction.*;

public class GaussMethod<T extends Num<T>> {
    public void solve(Matrix<T> matrix, int[] order) {
        if (order.length != matrix.rows) {
            solve(matrix);
            return;
        }
        for (int i = 0; i < order.length; ++i) {
            if (i != order[i]) {
                matrix.swapColumns(i, order[i]);
            }
        }
        solve(matrix);
    }

    public void solveWithColSwaps(Matrix<T> matrix){
        solve(matrix);
        while(matrix.hasZeroOnMainDiagonal()){
            makeDiagonal(matrix);
            solve(matrix);
        }
    }
    public void makeDiagonal(Matrix<T> matrix){
        for(int i=0;i< matrix.rows;++i){
            if(matrix.get(i,i).equals(OF(1,1))) continue;
            int col = findNotZeroCol(matrix,i);
            matrix.swapColumns(i,col);
        }
    }

    public  void solve(Matrix<T> matrix) {
        for (int i = 0; i < Math.min(matrix.rows, matrix.columns); ++i) {
            if (findFirstNotZero(matrix,i)) {
                continue;
            }
            normalizeRow(matrix, i, i);
            excludeVarFromColumn(matrix, i, i);
        }
    }

    private boolean findFirstNotZero(Matrix<T> matrix, int i) {
        if (matrix.get(i, i).isZero()) {
            for (int j = i + 1; j < matrix.rows; ++j) {
                if (!matrix.get(j, i).isZero()) {
                    matrix.swapRows(j, i);
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void normalizeRow(Matrix<T> matrix,int row, int column) {
        Arithmetic<T> ametic = Arithmetic.getArithmeticOfType(matrix.get(0,0));
        T coef = ametic.flip(matrix.get(row, column));
        matrix.multiplyRow(row, coef);
    }

    private void excludeVarFromColumn(Matrix<T> matrix, int row, int column) {
        Arithmetic<T> ametic = Arithmetic.getArithmeticOfType(matrix.get(0,0));
        for (int i = 0; i < matrix.rows; ++i) {
            if (i == row) continue;
            T coef = ametic.divide(matrix.get(i, column), ametic.revert(matrix.get(row, column)));
            matrix.addRowtoRow(i, row, coef);
        }
    }
    private int findNotZeroCol(Matrix<T> matrix, int row){
        for(int i = row;i< matrix.columns;++i){
            if(!matrix.get(row,i).equals(ZERO)) return i;
        }
        return row;
    }
}
