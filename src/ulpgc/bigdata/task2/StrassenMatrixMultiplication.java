package ulpgc.bigdata.task2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class StrassenMatrixMultiplication {
    private static final int TRESHOLD = 64;

    public static double[][] multiply(double[][] matrixA, double[][] matrixB){
        int n = matrixA.length;
        if (n <= TRESHOLD){
            return MatrixUtils.standardMultiply(matrixA, matrixB);
        }

        int newSize = n / 2;
        double[][] A11 = new double[newSize][newSize];
        double[][] A12 = new double[newSize][newSize];
        double[][] A21 = new double[newSize][newSize];
        double[][] A22 = new double[newSize][newSize];
        double [][] B11 = new double[newSize][newSize];
        double [][] B12 = new double[newSize][newSize];
        double [][] B21 = new double[newSize][newSize];
        double [][] B22 = new double[newSize][newSize];

        split(matrixA, A11, 0, 0);
        split(matrixA, A12, 0, newSize);
        split(matrixA, A21, newSize, 0);
        split(matrixA, A22, newSize, newSize);
        split(matrixB, B11, 0, 0);
        split(matrixB, B12, 0, newSize);
        split(matrixB, B21, newSize, 0);
        split(matrixB, B22, newSize, newSize);

        ForkJoinPool pool = new ForkJoinPool();

        double[][] M1 = pool.invoke(new MatrixTask(add(A11, A22), add(B11, B22)));
        double[][] M2 = pool.invoke(new MatrixTask(add(A21, A22), B11));
        double[][] M3 = pool.invoke(new MatrixTask(A11, subtract(B12, B22)));
        double[][] M4 = pool.invoke(new MatrixTask(A22, subtract(B21, B11)));
        double[][] M5 = pool.invoke(new MatrixTask(add(A11, A12), B22));
        double[][] M6 = pool.invoke(new MatrixTask(subtract(A21, A11), add(B11, B12)));
        double[][] M7 = pool.invoke(new MatrixTask(subtract(A12, A22), add(B21, B22)));

        double[][] C11 = add(subtract(add(M1, M4), M5), M7);
        double[][] C12 = add(M3, M5);
        double[][] C21 = add(M2, M4);
        double[][] C22 = add(subtract(add(M1, M3), M2), M6);

        double[][] matrixC = new double[n][n];
        join(C11, matrixC, 0, 0);
        join(C12, matrixC, 0, newSize);
        join(C21, matrixC, newSize, 0);
        join(C22, matrixC, newSize, newSize);

        return matrixC;
    }
    private static class MatrixTask extends RecursiveTask<double[][]> {
        private final double[][] matrixA;
        private final double[][] matrixB;

        public MatrixTask(double[][] matrixA, double[][] matrixB){
            this.matrixA = matrixA;
            this.matrixB = matrixB;
        }

        @Override
        protected double[][] compute(){
            return StrassenMatrixMultiplication.multiply(matrixA, matrixB);
        }
    }

    private static void split(double[][] parent, double[][] child, int row, int col){
        for (int i = 0; i < child.length; i++) {
            System.arraycopy(parent[row + i], col, child[i], 0, child.length);
        }
    }

    private static void join(double[][] child, double[][] parent, int row, int col){
        for (int i = 0; i < child.length; i++) {
            System.arraycopy(child[i], 0, parent[row + i], col, child.length);
        }
    }

    private static double[][] add(double[][] matrixA, double[][] matrixB){
        int n = matrixA.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return result;
    }

    private static double[][] subtract(double[][] matrixA, double[][] matrixB){
        int n = matrixA.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                result[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }
        return result;
    }
}


