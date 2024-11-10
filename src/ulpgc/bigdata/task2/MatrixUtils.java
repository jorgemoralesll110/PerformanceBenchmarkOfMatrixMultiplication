package ulpgc.bigdata.task2;

public class MatrixUtils {
    public static double[][] generateRandomMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random() * 10;
            }
        }
        return matrix;
    }

    public static double[][] generateSparseMatrix(int rows, int cols, double sparsity) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.random() > sparsity) {
                    matrix[i][j] = Math.random() * 10;
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }

    public static double[][] standardMultiply(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;
        double[][] result = new double[rowsA][colsB];

        int blockSize = 64;

        for (int i = 0; i < rowsA; i += blockSize) {
            for (int j = 0; j < colsB; j += blockSize) {
                for (int k = 0; k < colsA; k += blockSize) {
                    for (int i2 = i; i2 < Math.min(i + blockSize, rowsA); i2++) {
                        for (int j2 = j; j2 < Math.min(j + blockSize, colsB); j2++) {
                            double sum = 0;
                            for (int k2 = k; k2 < Math.min(k + blockSize, colsA); k2++) {
                                sum += matrixA[i2][k2] * matrixB[k2][j2];
                            }
                            result[i2][j2] += sum;
                        }
                    }
                }
            }
        }
        return result;
    }



}
