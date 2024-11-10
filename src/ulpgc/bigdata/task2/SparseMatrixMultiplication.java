package ulpgc.bigdata.task2;

import java.util.HashMap;
import java.util.Map;

public class SparseMatrixMultiplication {
    public static double[][] multiply(double[][] A, double[][] B) {
        int rowsA = A.length;
        int colsB = B[0].length;

        Map<Integer, Map<Integer, Double>> sparseA = convertToSparse(A);
        Map<Integer, Map<Integer, Double>> sparseB = convertToSparse(B);
        double[][] result = new double[rowsA][colsB];

        for (int i : sparseA.keySet()) {
            for (int k : sparseA.get(i).keySet()) {
                if (sparseB.containsKey(k)) {
                    for (int j : sparseB.get(k).keySet()) {
                        result[i][j] += sparseA.get(i).get(k) * sparseB.get(k).get(j);
                    }
                }
            }
        }
        return result;
    }

    private static Map<Integer, Map<Integer, Double>> convertToSparse(double[][] matrix) {
        Map<Integer, Map<Integer, Double>> sparseMatrix = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    sparseMatrix.computeIfAbsent(i, k -> new HashMap<>()).put(j, matrix[i][j]);
                }
            }
        }
        return sparseMatrix;
    }
}
